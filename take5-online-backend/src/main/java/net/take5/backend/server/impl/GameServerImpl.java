package net.take5.backend.server.impl;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.action.Action;
import net.take5.backend.context.ServerState;
import net.take5.backend.server.GameServer;
import net.take5.commons.pojo.input.AbstractParams;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.AbstractResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Serveur de jeu, implémente un dispatcher de messages reçus et permet de
 * supprimer une session du serveur
 * 
 * @author Quentin
 */
@Component
public class GameServerImpl implements GameServer
{
    /** Etat du serveur courant */
    @Autowired
    private ServerState serverState;

    /** Contexte de l'application */
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void dispatch(Session session, Message<AbstractParams> message) throws IOException, EncodeException
    {
        @SuppressWarnings("unchecked")
        AbstractResponse response = ((Action<AbstractParams>) applicationContext.getBean(message.getAction().name()))
                .run(session, message);

        // envoi dans la socket de la réponse à la demande
        session.getBasicRemote().sendObject(response);
    }

    @Override
    public void remove(Session session)
    {
        serverState.remove(session);
    }
}
