package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.Hand;

public class EndTurnResponse extends AbstractResponse
{
    /** Choix automatique de la carte */
    private Boolean autoChoice;

    /** Choix de la carte */
    private Integer card;

    /** Main mise à jour */
    private Hand hand;

    public Hand getHand()
    {
        return this.hand;
    }

    public void setHand(Hand hand)
    {
        this.hand = hand;
    }

    public Integer getCard()
    {
        return this.card;
    }

    public void setCard(Integer card)
    {
        this.card = card;
    }

    public Boolean getAutoChoice()
    {
        return this.autoChoice;
    }

    public void setAutoChoice(Boolean autoChoice)
    {
        this.autoChoice = autoChoice;
    }
}
