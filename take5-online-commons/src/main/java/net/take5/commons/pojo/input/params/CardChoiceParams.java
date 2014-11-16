package net.take5.commons.pojo.input.params;

import net.take5.commons.pojo.input.AbstractParams;

public class CardChoiceParams extends AbstractParams
{
    /** Index de la carte choisie dans la main */
    private Integer card;

    public Integer getCard()
    {
        return this.card;
    }

    public void setCard(Integer card)
    {
        this.card = card;
    }
}
