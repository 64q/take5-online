package net.take5.backend.action.impl;

import java.util.Locale;

import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.LoginParams;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.LoginResponse;
import net.take5.commons.pojo.output.response.UserJoinServerResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/**
 * Implémentation de l'action pour la connexion de l'utilisateur
 * 
 * @author Quentin
 */
@Component("LOGIN")
public class LoginAction extends AbstractAction<LoginParams, LoginResponse> implements MessageSourceAware
{
    /** Etat courant du serveur */
    @Autowired
    private ServerState serverState;

    private MessageSource messageSource;

    @Override
    public void execute(Session session, Message<LoginParams> message)
    {
        String username = message.getParams().getUsername();
        User user = new User();

        user.setUsername(username);
        user.setSession(session);
        user.setWonGames(0L);
        user.setLostGames(0L);

        // notifie les autres utilisateurs déjà connectés
        notifyOtherUsers(user);

        // ajout à la liste des utilisateurs connectés
        serverState.getUsers().put(session, user);

        response.setState(State.OK);
        response.setUsername(user.getUsername());
    }

    private void notifyOtherUsers(User newUser)
    {
        UserJoinServerResponse notification = new UserJoinServerResponse();

        notification.setState(State.OK);
        notification.setAction(OutputAction.USER_JOIN_SERVER);
        notification.setUser(newUser);

        for (User user : serverState.getUsers().values()) {
            user.getSession().getAsyncRemote().sendObject(notification);
        }
    }

    @Override
    public Boolean validate(Session session, Message<LoginParams> message)
    {
        Boolean isValid = true;
        String username = message.getParams().getUsername();

        // vérifie que le pseudo n'est pas vide
        if (isValid) {
            if (StringUtils.isBlank(username)) {
                response.setReason(messageSource.getMessage(MessageKey.ERROR_LOGIN_USERNAME_EMPTY, null,
                        Locale.getDefault()));
                response.setCode(ErrorCode.USERNAME_EMPTY);

                isValid = false;
            }
        }

        // vérifie que le pseudo n'est pas déjà pris
        if (isValid) {
            for (User user : serverState.getUsers().values()) {
                if (user.getUsername().equals(username)) {
                    response.setReason(messageSource.getMessage(MessageKey.ERROR_LOGIN_USERNAME_ALREADY_TAKEN,
                            new String[] { username }, Locale.getDefault()));
                    response.setCode(ErrorCode.USERNAME_ALREADY_TAKEN);
                    isValid = false;
                }
            }
        }

        if (!isValid) {
            response.setState(State.KO);
        }

        return isValid;
    }

    @Override
    public void initialize()
    {
        response = new LoginResponse();
        response.setAction(OutputAction.LOGIN);
    }

    @Override
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

}
