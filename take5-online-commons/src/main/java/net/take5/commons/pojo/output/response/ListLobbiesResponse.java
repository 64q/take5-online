package net.take5.commons.pojo.output.response;

import java.util.List;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.Lobby;

public class ListLobbiesResponse extends AbstractResponse
{
    /** Liste des lobbies */
    private List<Lobby> lobbies;

    public List<Lobby> getLobbies()
    {
        return this.lobbies;
    }

    public void setLobbies(List<Lobby> lobbies)
    {
        this.lobbies = lobbies;
    }

}
