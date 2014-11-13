package net.take5.backend.scheduler.impl;

import java.io.IOException;

import javax.websocket.EncodeException;

import net.take5.backend.scheduler.AsyncExecutor;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.EndTurnResponse;
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

        for (User user : lobby.getUsers()) {
            EndTurnResponse response = new EndTurnResponse();

            Integer card = 10;

            response.setState(State.OK);
            response.setAction(OutputAction.END_TURN);
            response.setCard(card);

            if (user.getHand().getPickedCard() == null) {
                response.setAutoChoice(true);
            } else {
                response.setAutoChoice(false);
            }

            response.setHand(user.getHand());

            try {
                user.getSession().getBasicRemote().sendObject(response);
            } catch (IOException | EncodeException e) {
                LOG.error("Erreur lors de la sérialisation de la réponse au choix de la carte", e);
            }
        }

        LOG.debug("Fin de l'appel asynchrone à la résolution du tour");
    }

    @Override
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
}
