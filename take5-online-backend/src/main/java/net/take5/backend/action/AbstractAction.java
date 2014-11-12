package net.take5.backend.action;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.commons.exception.ResponseNotInitializedException;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.AbstractResponse;

import org.springframework.stereotype.Component;

/**
 * Implémentation <em>abstraite</em> de l'action à mener lors de la réception
 * d'un {@link Message} utilisateur. Définit ici trois méthodes abstraites à
 * implémenter.
 * 
 * @author Quentin
 * 
 * @param <T>
 */
@Component
public abstract class AbstractAction<T extends AbstractResponse> implements Action
{
    /** Réponse à l'action */
    protected T response;

    @Override
    public T run(Session session, Message message) throws IOException, EncodeException
    {
        init();

        // la validation du message en entrée est un pré-requis à l'exécution de
        // la méthode principale de l'action
        if (validate(session, message)) {
            execute(session, message);
        }

        // dans le cas où la réponse n'a pas été initialisée, on jette une
        // exception particulière remontée dans la WebSocket
        if (response == null) {
            throw new ResponseNotInitializedException();
        }

        return response;
    }

    /** Initialise l'action à effectuer */
    abstract public void init();

    /**
     * Méthode effectuant une action après validation
     * 
     * @param session
     *            session courante WebSocket
     * @param message
     *            message à traiter
     * @return réponse au message
     * @throws IOException
     * @throws EncodeException
     */
    abstract public void execute(Session session, Message message) throws IOException, EncodeException;

    /**
     * La méthode de validation doit à la fois vérifier le message en entrée et
     * si l'action voulue est concrètement réalisable dans le contexte actuel
     * 
     * @param session
     *            session courante WebSocket
     * @param message
     *            message à analyser
     * @return vrai si l'action est exécutable
     */
    abstract public Boolean validate(Session session, Message message);
}
