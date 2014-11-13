package net.take5.backend.server;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.commons.pojo.input.AbstractParams;
import net.take5.commons.pojo.input.Message;

import org.springframework.stereotype.Service;

/**
 * Serveur de jeu, implémente un dispatcher de messages reçus et permet de
 * supprimer une session du serveur
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
     *            session envoyant le message
     * @param message
     *            message reçu
     * @throws IOException
     * @throws EncodeException
     */
    public void dispatch(Session session, Message<AbstractParams> message) throws IOException, EncodeException;

    /**
     * Supprime la session du serveur de jeu.
     * 
     * @param session
     *            session à supprimer
     */
    public void remove(Session session);
}
