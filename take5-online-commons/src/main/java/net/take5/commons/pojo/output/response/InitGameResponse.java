package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.Hand;

public class InitGameResponse extends AbstractResponse
{
    /** Main de l'utilisateur */
    private Hand hand;

    public Hand getHand()
    {
        return this.hand;
    }

    public void setHand(Hand hand)
    {
        this.hand = hand;
    }

}
