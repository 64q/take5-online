package net.take5.backend.action.impl;

import java.util.Locale;

import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.JoinLobbyParams;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.LobbyState;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.JoinLobbyResponse;
import net.take5.commons.pojo.output.response.UserJoinLobbyResponse;
import net.take5.engine.service.Take5Engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component("JOIN_LOBBY")
public class JoinLobbyAction extends AbstractAction<JoinLobbyParams, JoinLobbyResponse> implements MessageSourceAware
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
    public void execute(Session session, Message<JoinLobbyParams> message)
    {
        String lobbyUid = message.getParams().getUid();

        User user = serverState.getUser(session);
        Lobby lobby = serverState.getLobby(lobbyUid);

        // notification envoyée aux autres membres du lobby
        notifyLobbyUserJoinLobby(user, lobby);

        gameEngine.joinLobby(user, lobby);

        response.setState(State.OK);
        response.setLobby(lobby);
    }

    /**
     * Envoie une notification aux autres joueurs qu'un utilisateur a join
     * 
     * @param lobby
     *            le lobby a traiter
     */
    private void notifyLobbyUserJoinLobby(User user, Lobby lobby)
    {
        UserJoinLobbyResponse response = new UserJoinLobbyResponse();

        response.setState(State.OK);
        response.setAction(OutputAction.USER_JOIN_LOBBY);
        response.setUser(user);

        for (User userInLobby : lobby.getUsers()) {
            userInLobby.getSession().getAsyncRemote().sendObject(response);
        }
    }

    @Override
    public Boolean validate(Session session, Message<JoinLobbyParams> message)
    {
        Boolean isValid = true;
        String lobbyUid = message.getParams().getUid();

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
