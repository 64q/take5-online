package net.take5.backend.action.impl;

import java.util.Locale;

import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.ErrorCode;
import net.take5.commons.pojo.output.Lobby;
import net.take5.commons.pojo.output.LobbyState;
import net.take5.commons.pojo.output.OutputAction;
import net.take5.commons.pojo.output.State;
import net.take5.commons.pojo.output.User;
import net.take5.commons.pojo.output.response.JoinLobbyResponse;
import net.take5.engine.service.Take5Engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component("JOIN_LOBBY")
public class JoinLobbyAction extends AbstractAction<JoinLobbyResponse> implements MessageSourceAware
{
    /** Etat du serveur */
    @Autowired
    private ServerState serverState;

    /** Moteur de jeu */
    @Autowired
    private Take5Engine gameEngine;

    /** Message source */
    private MessageSource messageSource;

    @Override
    public void init()
    {
        response = new JoinLobbyResponse();
        response.setAction(OutputAction.JOIN_LOBBY);
    }

    @Override
    public void execute(Session session, Message message)
    {
        String lobbyUid = (String) message.getParams().get("lobby");

        User user = serverState.getUser(session);
        Lobby lobby = serverState.getLobby(lobbyUid);

        gameEngine.joinLobby(user, lobby);

        response.setState(State.OK);
        response.setLobby(lobby);
    }

    @Override
    public Boolean validate(Session session, Message message)
    {
        Boolean isValid = true;
        String lobbyUid = (String) message.getParams().get("lobby");

        // validation que le lobby existe
        if (isValid) {
            if (!serverState.lobbyExists(lobbyUid)) {
                response.setReason(messageSource.getMessage(MessageKey.ERROR_LOBBY_NOT_FOUND, null, Locale.getDefault()));
                response.setCode(ErrorCode.LOBBY_NOT_FOUND);

                isValid = false;
            }
        }

        // validation que l'utilisateur est connecté
        if (isValid && !serverState.userExists(session)) {
            response.setReason(messageSource.getMessage(MessageKey.ERROR_USER_NOT_LOGGED, null, Locale.getDefault()));
            response.setCode(ErrorCode.NOT_LOGGED);

            isValid = false;
        }

        // validation que le lobby est en état d'attente
        if (isValid && serverState.getLobby(lobbyUid).getState() != LobbyState.WAITING) {
            response.setReason(messageSource.getMessage(MessageKey.ERROR_LOBBY_NOT_WAITING, null, Locale.getDefault()));
            response.setCode(ErrorCode.LOBBY_NOT_WAITING);

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
