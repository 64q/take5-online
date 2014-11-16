package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.GameBoard;
import net.take5.commons.pojo.output.common.Hand;

public class EndTurnResponse extends AbstractResponse
{
    /** Main mise à jour */
    private Hand hand;

    /** Plateau de jeu mise à jour */
    private GameBoard gameBoard;

    public GameBoard getGameBoard()
    {
        return this.gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
    }

    public Hand getHand()
    {
        return this.hand;
    }

    public void setHand(Hand hand)
    {
        this.hand = hand;
    }
}
