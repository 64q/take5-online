package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.Card;

public class CardChoiceResponse extends AbstractResponse
{
    /** Carte prise */
    private Card pickedCard;

    public Card getPickedCard()
    {
        return this.pickedCard;
    }

    public void setPickedCard(Card pickedCard)
    {
        this.pickedCard = pickedCard;
    }
}
