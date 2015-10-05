package net.take5.backend.action.impl;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.QuitLobbyParams;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.NotificationResponse;
import net.take5.commons.pojo.output.response.QuitLobbyResponse;
import net.take5.commons.pojo.output.response.UserJoinLobbyResponse;
import net.take5.engine.service.Take5Engine;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component("QUIT_LOBBY")
public class QuitLobbyAction extends
		AbstractAction<QuitLobbyParams, QuitLobbyResponse> implements
		MessageSourceAware {
	/** Logger */
	private static final Logger LOG = Logger.getLogger(QuitLobbyAction.class);

	/** Etat du serveur */
	@Autowired
	private ServerState serverState;

	/** Moteur de jeu */
	@Autowired
	private Take5Engine gameEngine;

	/** Message source */
	private MessageSource messageSource;

	@Override
	public void initialize() {
		response = new QuitLobbyResponse();
		response.setAction(OutputAction.QUIT_LOBBY);
	}

	@Override
	public void execute(Session session, Message<QuitLobbyParams> message) {
		String lobbyUid = message.getParams().getUid();

		User user = serverState.getUser(session);
		Lobby lobby = serverState.getLobby(lobbyUid);

		gameEngine.quitLobby(user, lobby);

		// envoi d'une notification que l'utilisateur est parti du lobby
		notifyLobbyUserQuitLobby(user, lobby);

		// si le créateur quitte le lobby, ce dernier est supprimé
		if (lobby.getOwner().equals(user)) {
			notifyQuitLobby(lobby.getUsers());

			for (User userInLobby : lobby.getUsers()) {
				gameEngine.quitLobby(userInLobby, lobby);
			}

			serverState.getLobbies().remove(lobby);
		}

		// si le lobby est vide, ce dernier doit être supprimé
		if (lobby.isEmpty()) {
			serverState.getLobbies().remove(lobby);
		}

		response.setState(State.OK);
	}

	/**
	 * Notifie aux autres utilisateurs que le lobby a été supprimé
	 * 
	 * @param users
	 *            liste des utilisateurs à prévenir
	 */
	private void notifyQuitLobby(Set<User> users) {
		for (User user : users) {
			NotificationResponse notification = new NotificationResponse();

			notification.setState(State.OK);
			notification.setAction(OutputAction.DESTROY_LOBBY);
			notification.setNotification(messageSource.getMessage(
					MessageKey.NOTIFICATION_REMOVED_FROM_LOBBY, null,
					Locale.getDefault()));

			try {
				user.getSession().getBasicRemote().sendObject(notification);
			} catch (IOException | EncodeException e) {
				LOG.error(
						"Erreur lors de la sérialisation du message d'envoi de notification de suppression du lobby",
						e);
			}
		}
	}

	/**
	 * Envoie une notification aux autres joueurs qu'un utilisateur a quitté
	 * 
	 * @param lobby
	 *            le lobby a traiter
	 */
	private void notifyLobbyUserQuitLobby(User user, Lobby lobby) {
		UserJoinLobbyResponse response = new UserJoinLobbyResponse();

		response.setState(State.OK);
		response.setAction(OutputAction.USER_QUIT_LOBBY);
		response.setUser(user);

		for (User userInLobby : lobby.getUsers()) {
			userInLobby.getSession().getAsyncRemote().sendObject(response);
		}
	}

	@Override
	public Boolean validate(Session session, Message<QuitLobbyParams> message) {
		Boolean isValid = true;
		String lobbyUid = message.getParams().getUid();

		// validation que le lobby existe
		if (isValid) {
			if (!serverState.lobbyExists(lobbyUid)) {
				response.setReason(messageSource.getMessage(
						MessageKey.ERROR_LOBBY_NOT_FOUND, null,
						Locale.getDefault()));
				response.setCode(ErrorCode.LOBBY_NOT_FOUND);

				isValid = false;
			}
		}

		// validation que l'utilisateur est connecté
		if (isValid && !serverState.userExists(session)) {
			response.setReason(messageSource.getMessage(
					MessageKey.ERROR_USER_NOT_LOGGED, null, Locale.getDefault()));
			response.setCode(ErrorCode.NOT_LOGGED);

			isValid = false;
		}

		// validation que l'utilisateur est bien dans un lobby
		if (isValid && serverState.getUser(session).getCurrentLobby() == null) {
			response.setReason(messageSource.getMessage(
					MessageKey.ERROR_NOT_IN_LOBBY, null, Locale.getDefault()));
			response.setCode(ErrorCode.NOT_IN_LOBBY);

			isValid = false;
		}

		// validation que l'utilisateur est bien dans le lobby ciblé
		if (isValid) {
			User user = serverState.getUser(session);
			Lobby lobby = serverState.getLobby(lobbyUid);

			if (!lobby.getUsers().contains(user)) {
				response.setReason(messageSource.getMessage(
						MessageKey.ERROR_NOT_IN_LOBBY, null,
						Locale.getDefault()));
				response.setCode(ErrorCode.NOT_IN_LOBBY);

				isValid = false;
			}
		}

		if (!isValid) {
			response.setState(State.KO);
		}

		return isValid;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
