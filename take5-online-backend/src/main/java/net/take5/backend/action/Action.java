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
 * @param <T>
 *            type de paramètre transmis par l'action reçue
 */
public interface Action<T extends AbstractParams>
{
    /**
     * 
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
    public AbstractResponse run(Session session, Message<T> message) throws IOException, EncodeException;
}
