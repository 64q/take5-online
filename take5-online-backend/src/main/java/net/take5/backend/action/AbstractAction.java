package net.take5.backend.action;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.commons.exception.ResponseNotInitializedException;
import net.take5.commons.pojo.input.AbstractParams;
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
 * @param <P>
 *            type de paramètres reçus de l'action
 * @param <R>
 *            type de réponse à apporter à la requête en entrée
 */
@Component
public abstract class AbstractAction<P extends AbstractParams, R extends AbstractResponse> implements Action<P, R>
{
    /** Réponse à l'action */
    protected R response;

    @Override
    public R run(Session session, Message<P> message) throws IOException, EncodeException
    {
        initialize();

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

    /**
     * Initialise l'action à effectuer
     */
    public abstract void initialize();

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
    public abstract void execute(Session session, Message<P> message) throws IOException, EncodeException;

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
    public abstract Boolean validate(Session session, Message<P> message);
}
