package net.take5.commons.pojo.input.params;

import net.take5.commons.pojo.input.AbstractParams;

public class JoinLobbyParams extends AbstractParams
{
    /** Lobby uid */
    private String uid;

    public String getUid()
    {
        return this.uid;
    }

    public void setUid(String lobby)
    {
        this.uid = lobby;
    }

}
