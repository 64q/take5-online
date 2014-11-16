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

    /** Indique si la carte prise à été déterminée automatiquement */
    private Boolean pickedAuto;

    /** Cartes prises par le joueur */
    private List<Card> takenCards;

    public Hand() {
        pickedCard = null;
        cards = new ArrayList<Card>(10);
    }

    public List<Card> getTakenCards()
    {
        if (takenCards == null) {
            takenCards = new ArrayList<Card>();
        }

        return this.takenCards;
    }

    public void setTakenCards(List<Card> takenCards)
    {
        this.takenCards = takenCards;
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

    public Boolean getPickedAuto()
    {
        return this.pickedAuto;
    }

    public void setPickedAuto(Boolean pickedAuto)
    {
        this.pickedAuto = pickedAuto;
    }
}
