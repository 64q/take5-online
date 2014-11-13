package net.take5.commons.pojo.output.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Main de l'utilisateur
 * 
 * @author Quentin
 */
public class Hand
{
    /** Carte sélectionnée */
    private Integer pickedCard;

    /** Cartes courantes */
    private List<Integer> cards;

    public Hand() {
        pickedCard = null;
        cards = new ArrayList<Integer>(10);
    }

    public Integer getPickedCard()
    {
        return this.pickedCard;
    }

    public void setPickedCard(Integer pickedCard)
    {
        this.pickedCard = pickedCard;
    }

    public List<Integer> getCards()
    {
        return this.cards;
    }

    public void setCards(List<Integer> cards)
    {
        this.cards = cards;
    }

}
