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
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.UserQuitServerResponse;

import org.apache.log4j.Logger;
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
    /** Logger */
    private static final Logger LOG = Logger.getLogger(GameServerImpl.class);

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
        AbstractResponse response = ((Action<AbstractParams, AbstractResponse>) applicationContext.getBean(message
                .getAction().name())).run(session, message);

        // envoi dans la socket de la réponse à la demande
        session.getBasicRemote().sendObject(response);
    }

    @Override
    public void remove(Session session)
    {
        // notification aux autres utilisateurs que le joueur est parti
        User oldUser = serverState.getUser(session);

        UserQuitServerResponse notification = new UserQuitServerResponse();

        notification.setState(State.OK);
        notification.setAction(OutputAction.USER_QUIT_SERVER);

        serverState.remove(session);

        if (oldUser != null) {
            notification.setUser(oldUser);

            LOG.info("L'utilisateur " + oldUser.getUsername() + " a quitté le serveur");
        }

        for (User user : serverState.getUsers().values()) {
            user.getSession().getAsyncRemote().sendObject(notification);
        }
    }
}
