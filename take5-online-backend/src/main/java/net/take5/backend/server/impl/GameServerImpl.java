package net.take5.backend.server.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.action.Action;
import net.take5.backend.context.ServerState;
import net.take5.backend.server.GameServer;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.Lobby;
import net.take5.commons.pojo.output.User;
import net.take5.engine.service.Take5Engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Dispatcher de message reçu depuis la WebSocket
 * 
 * @author Quentin
 */
@Component
public class GameServerImpl implements GameServer
{
    @Autowired
    private Take5Engine gameEngine;

    /** Etat du serveur courant */
    @Autowired
    private ServerState serverState;

    /** Contexte de l'application */
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void dispatch(Session session, Message message) throws IOException, EncodeException
    {
        AbstractResponse response = ((Action) applicationContext.getBean(message.getAction().name())).run(session,
                message);

        // envoi dans la socket de la réponse à la demande
        session.getBasicRemote().sendObject(response);
    }

    @Override
    public Map<Session, User> getUsers()
    {
        return serverState.getUsers();
    }

    @Override
    public Queue<Lobby> getLobbies()
    {
        return this.serverState.getLobbies();
    }

    @Override
    public void remove(Session session)
    {
        serverState.remove(session);
    }
}
