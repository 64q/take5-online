package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.Lobby;

public class JoinLobbyResponse extends AbstractResponse
{
    /** Lobby rejoint */
    private Lobby lobby;

    public Lobby getLobby()
    {
        return this.lobby;
    }

    public void setLobby(Lobby lobby)
    {
        this.lobby = lobby;
    }

}
