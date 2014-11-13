package net.take5.commons.pojo.output.common;

import java.util.List;

/**
 * Plateau de jeu
 * 
 * @author Quentin
 */
public class GameBoard
{
    /** Plateau du jeu représenté par une matrice (généralement 4 x 6) */
    private List<List<Integer>> board;

    public List<List<Integer>> getBoard()
    {
        return this.board;
    }

    public void setBoard(List<List<Integer>> board)
    {
        this.board = board;
    }

}
