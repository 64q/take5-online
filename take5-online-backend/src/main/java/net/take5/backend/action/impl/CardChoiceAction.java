package net.take5.backend.action.impl;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.CardChoiceParams;
import net.take5.commons.pojo.output.common.Card;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.CardChoiceResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CARD_CHOICE")
public class CardChoiceAction extends AbstractAction<CardChoiceParams, CardChoiceResponse>
{
    /** Logger */
    private static final Logger LOG = Logger.getLogger(CardChoiceAction.class);

    /** Etat du serveur */
    @Autowired
    private ServerState serverState;

    @Override
    public void initialize()
    {
        response = new CardChoiceResponse();
        response.setAction(OutputAction.CARD_CHOICE);
    }

    @Override
    public void execute(Session session, Message<CardChoiceParams> message) throws IOException, EncodeException
    {
        Integer cardIndex = message.getParams().getCard();
        User user = serverState.getUser(session);
        Card pickedCard = user.getHand().getCards().get(cardIndex);

        LOG.info("L'utilisateur " + user.getUsername() + " a choisi la carte " + pickedCard);

        user.getHand().setPickedAuto(false);
        user.getHand().setPickedCard(pickedCard);

        response.setPickedCard(pickedCard);
    }

    @Override
    public Boolean validate(Session session, Message<CardChoiceParams> message)
    {
        Boolean isValid = true;

        User user = serverState.getUser(session);
        Integer cardIndex = message.getParams().getCard();

        if (cardIndex == null) {
            response.setState(State.KO);
            response.setCode(ErrorCode.INVALID_CARD);
            isValid = false;
        }

        if (isValid && (cardIndex < 0 || cardIndex >= user.getHand().getCards().size())) {
            response.setState(State.KO);
            response.setCode(ErrorCode.INVALID_CARD);
            isValid = false;
        }

        return isValid;
    }

}
