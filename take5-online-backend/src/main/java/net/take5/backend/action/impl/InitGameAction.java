package net.take5.backend.action.impl;

import java.io.IOException;
import java.util.Locale;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.backend.scheduler.AsyncExecutor;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.NoParams;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.InitGameResponse;
import net.take5.engine.service.Take5Engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component("INIT_GAME")
public class InitGameAction extends AbstractAction<NoParams, InitGameResponse> implements MessageSourceAware
{
    /** Etat du serveur */
    @Autowired
    private ServerState serverState;

    /** Message Source */
    private MessageSource messageSource;

    /** Exécuteur asynchrone d'actions */
    @Autowired
    private AsyncExecutor asyncExecutor;

    @Autowired
    private Take5Engine gameEngine;

    @Override
    public void initialize()
    {
        response = new InitGameResponse();
        response.setAction(OutputAction.INIT_GAME);
        response.setState(State.OK);
    }

    @Override
    public void execute(Session session, Message<NoParams> message) throws IOException, EncodeException
    {
        User user = serverState.getUser(session);
        Lobby lobby = user.getCurrentLobby();

        gameEngine.newGame(lobby);

        // Lancement de l'appel asynchrone de la résolution du tour
        // attention, l'appel est déclenché ensuite toutes les 30 secondes
        asyncExecutor.performEndTurn(lobby, 30000L);

        response.setGameBoard(lobby.getGameBoard());

        // Notification envoyée aux autres participants que la partie a démarrée
        notifyInitGame(lobby);

        response.setHand(user.getHand());
    }

    /**
     * Envoie une notification de début de partie aux autres participants
     * 
     * @param lobby
     *            lobby à traiter
     * @throws EncodeException
     * @throws IOException
     */
    private void notifyInitGame(Lobby lobby)
    {
        for (User user : lobby.getUsers()) {
            if (!user.equals(lobby.getOwner())) {
                // envoi de la main générée pour cet utilisateur
                response.setHand(user.getHand());

                user.getSession().getAsyncRemote().sendObject(response);
            }
        }
    }

    @Override
    public Boolean validate(Session session, Message<NoParams> message)
    {
        Boolean isValid = true;

        User user = serverState.getUser(session);

        // vérification que l'utilisateur est bien connecté
        if (isValid && user == null) {
            response.setReason(messageSource.getMessage(MessageKey.ERROR_USER_NOT_LOGGED, null, Locale.getDefault()));
            response.setCode(ErrorCode.NOT_LOGGED);

            isValid = false;
        }

        if (isValid) {
            Lobby lobby = user.getCurrentLobby();

            if (lobby == null) {
                response.setReason(messageSource.getMessage(MessageKey.ERROR_NOT_IN_LOBBY, null, Locale.getDefault()));
                response.setCode(ErrorCode.NOT_IN_LOBBY);

                isValid = false;
            }

            // vérifie que le démarrage est bien effectué par le créateur
            if (isValid && !lobby.getOwner().equals(user)) {
                response.setReason(messageSource.getMessage(MessageKey.ERROR_CANNOT_INIT_GAME, null,
                        Locale.getDefault()));
                response.setCode(ErrorCode.CANNOT_INIT_GAME);

                isValid = false;
            }
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
