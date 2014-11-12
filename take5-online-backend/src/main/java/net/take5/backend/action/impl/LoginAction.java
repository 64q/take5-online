package net.take5.backend.action.impl;

import java.util.Locale;
import java.util.Map;

import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.ErrorCode;
import net.take5.commons.pojo.output.OutputAction;
import net.take5.commons.pojo.output.State;
import net.take5.commons.pojo.output.User;
import net.take5.commons.pojo.output.response.LoginResponse;

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
public class LoginAction extends AbstractAction<LoginResponse> implements MessageSourceAware
{
    /** Etat courant du serveur */
    @Autowired
    private ServerState serverState;

    private MessageSource messageSource;

    @Override
    public void execute(Session session, Message message)
    {
        String username = (String) message.getParams().get("username");
        User user = new User();

        user.setUsername(username);
        user.setSession(session);
        user.setWonGames(0L);
        user.setLostGames(0L);

        // ajout à la liste des utilisateurs connectés
        serverState.getUsers().put(session, user);

        response.setState(State.OK);
        response.setUsername(user.getUsername());
    }

    @Override
    public Boolean validate(Session session, Message message)
    {
        Boolean isValid = true;
        Map<String, Object> params = message.getParams();
        String username = (String) params.get("username");

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
    public void init()
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
