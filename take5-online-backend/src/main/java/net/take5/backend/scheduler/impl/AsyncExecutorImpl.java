package net.take5.backend.scheduler.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.take5.backend.scheduler.AsyncExecutor;
import net.take5.commons.pojo.output.common.Card;
import net.take5.commons.pojo.output.common.Hand;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.LobbyState;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.EndTurnResponse;
import net.take5.commons.pojo.output.response.RemoveColumnChoiceResponse;
import net.take5.commons.pojo.output.response.RemoveColumnResponse;
import net.take5.engine.service.Take5Engine;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class AsyncExecutorImpl implements AsyncExecutor, MessageSourceAware
{
    /** Logger */
    private static final Logger LOG = Logger.getLogger(AsyncExecutorImpl.class);

    /** Moteur de jeu */
    @Autowired
    private Take5Engine gameEngine;

    /** Source de messages */
    @Autowired
    private MessageSource messageSource;

    @Override
    @Async
    public void performEndTurn(Lobby lobby, Long timeout)
    {
        if (timeout == null) {
            timeout = 30000L;
        }

        // attente de XX secondes avant la résolution
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            LOG.error("L'attente pour jouer une carte pour le lobby " + lobby.getUid()
                    + " n'a pas pu être menée a bien", e);
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
        Iterator<Map.Entry<Card, User>> it = selectedCards.entrySet().iterator();
        Map.Entry<Card, User> entry = it.next();

        if (gameEngine.determineRemoveColumn(lobby, entry.getKey())) {
            lobby.setState(LobbyState.CHOICE);

            RemoveColumnResponse notification = new RemoveColumnResponse();

            Hand savedHand = entry.getValue().getHand();

            notification.setState(State.OK);
            notification.setAction(OutputAction.REMOVE_COLUMN);
            notification.setUser(entry.getValue());

            notification.getUser().setHand(null);

            for (User user : lobby.getUsers()) {
                user.getSession().getAsyncRemote().sendObject(notification);
            }

            entry.getValue().setHand(savedHand);

            // attente de 30 secondes avant la suppression de colonne
            // automatique
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                LOG.error("L'attente pour retier une colonne pour le lobby " + lobby.getUid()
                        + " n'a pas pu être menée a bien", e);
            }

            if (gameEngine.resolveRemoveColumn(lobby, entry.getValue())) {
                RemoveColumnChoiceResponse choiceResponse = new RemoveColumnChoiceResponse();

                choiceResponse.setState(State.OK);
                choiceResponse.setAction(OutputAction.REMOVE_COLUMN_CHOICE);
                choiceResponse.setUser(entry.getValue());
                choiceResponse.setColumn(0);

                for (User user : lobby.getUsers()) {
                    user.getSession().getAsyncRemote().sendObject(choiceResponse);
                }
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
        }

        LOG.debug("Fin de l'appel asynchrone à la résolution du tour");

        // relance du perform turn
        performEndTurn(lobby, 30000L);
    }

    @Override
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
}
