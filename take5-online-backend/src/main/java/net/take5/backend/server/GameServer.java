package net.take5.backend.server;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.context.ConnectedUsers;
import net.take5.commons.pojo.input.Message;

import org.springframework.stereotype.Service;

/**
 * Dispatcher de messages reçu depuis la WebSocket
 * 
 * @author Quentin
 */
@Service
public interface GameServer
{
    /**
     * Dispatche et traite le message reçu d'une WebSocket
     * 
     * @param session
     * @param message
     * @throws IOException
     * @throws EncodeException
     */
    public void dispatch(Session session, Message message) throws IOException, EncodeException;

    /**
     * Récupère la liste des utilisateurs courants
     * 
     * @return
     */
    public ConnectedUsers getUsers();

    /**
     * Supprime la session du serveur de jeu.
     * 
     * @param session
     *            session à supprimer
     */
    public void remove(Session session);
}
