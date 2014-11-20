package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.User;

/**
 * Objet de notification de fin de partie
 * 
 * @author Quentin
 */
public class EndGameResponse extends AbstractResponse
{
    /** Gagnant de la partie */
    private User winner;

    /** Lobby mis à jour avec les utilisateurs */
    private Lobby lobby;

    public Lobby getLobby()
    {
        return this.lobby;
    }

    public void setLobby(Lobby lobby)
    {
        this.lobby = lobby;
    }

    public User getWinner()
    {
        return this.winner;
    }

    public void setWinner(User winner)
    {
        this.winner = winner;
    }

}
