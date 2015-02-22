package net.take5.backend.action.impl;

import java.util.Locale;

import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.CreateLobbyParams;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.CreateLobbyResponse;
import net.take5.engine.service.Take5Engine;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component("CREATE_LOBBY")
public class CreateLobbyAction extends AbstractAction<CreateLobbyParams, CreateLobbyResponse> implements
        MessageSourceAware
{
    /** Message source */
    private MessageSource messageSource;

    /** Server state */
    @Autowired
    private ServerState serverState;

    /** Moteur de jeu */
    @Autowired
    private Take5Engine gameEngine;

    @Override
    public void initialize()
    {
        response = new CreateLobbyResponse();
        response.setAction(OutputAction.CREATE_LOBBY);
    }

    @Override
    public void execute(Session session, Message<CreateLobbyParams> message)
    {
        String name = message.getParams().getName();
        // récupération de l'utilisateur connecté
        User user = serverState.getUsers().get(session);

        // création du lobby
        Lobby lobby = gameEngine.createLobby(name, user);

        response.setState(State.OK);
        response.setLobby(lobby);

        // ajout à la liste des lobbies
        serverState.getLobbies().add(lobby);
    }

    @Override
    public Boolean validate(Session session, Message<CreateLobbyParams> message)
    {
        Boolean isValid = true;
        String name = message.getParams().getName();

        // validation que le nom de lobby n'est pas vide
        if (isValid && StringUtils.isBlank(name)) {
            response.setReason(messageSource.getMessage(MessageKey.ERROR_LOBBY_NAME_EMPTY, null, Locale.getDefault()));
            response.setCode(ErrorCode.LOBBY_NAME_EMPTY);

            isValid = false;
        }

        // validation que l'utilisateur est bien connecté
        if (isValid && !serverState.userExists(session)) {
            response.setReason(messageSource.getMessage(MessageKey.ERROR_USER_NOT_LOGGED, null, Locale.getDefault()));
            response.setCode(ErrorCode.NOT_LOGGED);

            isValid = false;
        }

        // validation que l'utilisateur n'est pas déjà dans un lobby
        if (isValid && serverState.getUser(session).getCurrentLobby() != null) {
            response.setReason(messageSource.getMessage(MessageKey.ERROR_ALREADY_IN_LOBBY, null, Locale.getDefault()));
            response.setCode(ErrorCode.ALREADY_IN_LOBBY);

            isValid = false;
        }

        if (!isValid) {
            response.setState(State.KO);
        }

        return isValid;
    }

    @Override
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

}
