package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.GameBoard;
import net.take5.commons.pojo.output.common.Hand;

public class InitGameResponse extends AbstractResponse
{
    /** Main de l'utilisateur */
    private Hand hand;

    /** Plateau de jeu */
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
