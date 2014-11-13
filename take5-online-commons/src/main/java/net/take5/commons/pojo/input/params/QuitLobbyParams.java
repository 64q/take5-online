package net.take5.commons.pojo.input.params;

import net.take5.commons.pojo.input.AbstractParams;

public class QuitLobbyParams extends AbstractParams
{
    /** Uid du lobby */
    private String uid;

    public String getUid()
    {
        return this.uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

}
