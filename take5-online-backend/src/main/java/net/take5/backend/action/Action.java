package net.take5.backend.action;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.commons.pojo.input.AbstractParams;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.AbstractResponse;

/**
 * Interface d'action définissant un comportement à déclencher lors de la
 * réception d'un {@link Message} de la part d'un utilisateur
 * 
 * @author Quentin
 * 
 * @param <P>
 *            type de paramètre transmis par l'action reçue
 * @param <R>
 *            type de réponse à apporter au message en entrée
 */
public interface Action<P extends AbstractParams, R extends AbstractResponse>
{
    /**
     * Exécute une action vis à vis d'un message reçu en paramètre
     * 
     * @param session
     *            session courante
     * @param message
     *            message à traiter
     * @return réponse au message en entrée
     * @throws IOException
     * @throws EncodeException
     */
    public R run(Session session, Message<P> message) throws IOException, EncodeException;
}
