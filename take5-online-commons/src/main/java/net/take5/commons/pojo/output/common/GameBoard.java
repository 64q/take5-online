package net.take5.commons.pojo.output.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Plateau de jeu
 * 
 * @author Quentin
 */
public class GameBoard
{
    /** Plateau du jeu représenté par une matrice (généralement 4 x 6) */
    private List<List<Card>> board;

    public GameBoard() {
        board = new ArrayList<List<Card>>(4);

        for (int i = 0; i < 4; i++) {
            board.add(new ArrayList<Card>());
        }
    }

    public List<List<Card>> getBoard()
    {
        return this.board;
    }

    public void setBoard(List<List<Card>> board)
    {
        this.board = board;
    }

}
