package net.take5.backend.scheduler.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.take5.backend.context.ServerState;
import net.take5.backend.scheduler.AsyncExecutor;
import net.take5.commons.pojo.output.common.Card;
import net.take5.commons.pojo.output.common.Hand;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.LobbyState;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.EndGameResponse;
import net.take5.commons.pojo.output.response.EndTurnResponse;
import net.take5.commons.pojo.output.response.RemoveColumnResponse;
import net.take5.commons.pojo.output.response.RemoveLineResponse;
import net.take5.engine.service.Take5Engine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class AsyncExecutorImpl implements AsyncExecutor {
	/** Logger */
	private static final Logger LOG = Logger.getLogger(AsyncExecutorImpl.class);

	/** Moteur de jeu */
	@Autowired
	private Take5Engine gameEngine;

	/** Etat du serveur */
	@Autowired
	private ServerState serverState;

	@Override
	@Async
	public void performEndTurn(Lobby lobby, Long timeout) {
		if (timeout == null) {
			timeout = 300L;
		}

		boolean finishedGame = false;

		// attente de XX secondes avant la résolution
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			LOG.error(
					"L'attente pour jouer une carte pour le lobby "
							+ lobby.getUid() + " n'a pas pu être menée a bien",
					e);
		}

		LOG.debug("Temps d'attente de 30 secondes terminé, résolution du tour");

		// résolution des pickedCards non affectées
		gameEngine.resolvePickedCards(lobby);

		SortedMap<Card, User> selectedCards = new TreeMap<Card, User>();

		for (User user : lobby.getUsers()) {
			selectedCards.put(user.getHand().getPickedCard(), user);
		}

		// le parcours des cartes est effectué dans l'ordre croissant selon la
		// valeur
		Iterator<Map.Entry<Card, User>> it = selectedCards.entrySet()
				.iterator();
		Map.Entry<Card, User> entry = it.next();

		if (gameEngine.determineRemoveLine(lobby, entry.getKey())) {
			lobby.setState(LobbyState.CHOICE);

			RemoveColumnResponse notification = new RemoveColumnResponse();

			Hand savedHand = entry.getValue().getHand();

			notification.setState(State.OK);
			notification.setAction(OutputAction.REMOVE_LINE_CHOICE);
			notification.setUser(entry.getValue());

			notification.getUser().setHand(null);

			// envoi de la notification à tous les utilisateurs du lobby
			for (User user : lobby.getUsers()) {
				user.getSession().getAsyncRemote().sendObject(notification);
			}

			entry.getValue().setHand(savedHand);

			// attente de 30 secondes avant la suppression de colonne
			// automatique
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				LOG.error("L'attente pour retier une colonne pour le lobby "
						+ lobby.getUid() + " n'a pas pu être menée a bien", e);
			}

			if (gameEngine.resolveRemoveLine(lobby, entry.getValue())) {
				performAutomaticRemoval(lobby, entry.getValue());
			}
		}

		gameEngine.resolveTurn(lobby);

		// après résolution du tour on envoit à chaque joueur sa main mise à
		// jour et le plateau mis à jour
		EndTurnResponse response = new EndTurnResponse();

		response.setState(State.OK);
		response.setAction(OutputAction.END_TURN);
		response.setGameBoard(lobby.getGameBoard());

		for (User user : lobby.getUsers()) {
			response.setHand(user.getHand());
			user.getSession().getAsyncRemote().sendObject(response);

			// reset de la main
			user.getHand().setPickedAuto(false);
			user.getHand().setPickedCard(null);

			if (CollectionUtils.isEmpty(user.getHand().getCards())) {
				finishedGame = true;
			}
		}

		LOG.debug("Fin de l'appel asynchrone à la résolution du tour");

		// evaluation de l'état actuel de la partie, si cette dernière ne
		// contient plus d'utilisateur, on arrête tout
		if (CollectionUtils.isEmpty(lobby.getUsers())) {
			finishedGame = true;
			LOG.info("Le lobby de la partie " + lobby.getName()
					+ " ne contient plus d'utilisateurs, la partie s'arrête");
		}

		if (finishedGame) {
			// la partie est terminée, on envoie la notification de fin de
			// partie aux joueurs du lobby
			performEndGame(lobby);
		} else {
			// relance du perform turn car la partie n'est pas encore finie
			performEndTurn(lobby, timeout);
		}
	}

	/**
	 * Réalise la suppression de colonne automatiquement
	 * 
	 * @param lobby
	 *            lobby à traiter
	 * @param user
	 *            utilisateur devant enlever la colonne
	 */
	protected void performAutomaticRemoval(Lobby lobby, User user) {
		RemoveLineResponse choiceResponse = new RemoveLineResponse();

		choiceResponse.setState(State.OK);
		choiceResponse.setAction(OutputAction.REMOVE_LINE);
		choiceResponse.setUser(user);
		choiceResponse.setLine(0);

		for (User userInLobby : lobby.getUsers()) {
			userInLobby.getSession().getAsyncRemote()
					.sendObject(choiceResponse);
		}
	}

	/**
	 * Fin de partie, envoi d'une notification aux joueurs du lobby
	 * 
	 * @param lobby
	 *            lobby à traiter
	 */
	protected void performEndGame(Lobby lobby) {
		// détermination du gagnant
		User winner = null;
		Integer winnerSum = Integer.MAX_VALUE;

		for (User user : lobby.getUsers()) {
			Integer sum = 0;

			for (Card card : user.getHand().getTakenCards()) {
				sum = sum + card.getOxHeads();
			}

			if (winnerSum.compareTo(sum) > 0) {
				winnerSum = sum;
				winner = user;
			}
		}

		EndGameResponse response = new EndGameResponse();

		response.setAction(OutputAction.END_GAME);
		response.setState(State.OK);
		response.setWinner(winner);
		response.setLobby(lobby);

		for (User user : lobby.getUsers()) {
			if (user.equals(winner)) {
				user.setWonGames(user.getWonGames() + 1);
			} else {
				user.setLostGames(user.getLostGames() + 1);
			}
		}

		for (User user : lobby.getUsers()) {
			user.getSession().getAsyncRemote().sendObject(response);
		}

		// destruction de la partie dans le serveur
		// serverState.getLobbies().remove(lobby);

		LOG.info("La partie " + lobby.getName() + " est désormais terminée");
	}
}
