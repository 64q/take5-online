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
    private Card pickedCard;

    /** Cartes courantes */
    private List<Card> cards;

    public Hand() {
        pickedCard = null;
        cards = new ArrayList<Card>(10);
    }

    public Card getPickedCard()
    {
        return this.pickedCard;
    }

    public void setPickedCard(Card pickedCard)
    {
        this.pickedCard = pickedCard;
    }

    public List<Card> getCards()
    {
        return this.cards;
    }

    public void setCards(List<Card> cards)
    {
        this.cards = cards;
    }

}
