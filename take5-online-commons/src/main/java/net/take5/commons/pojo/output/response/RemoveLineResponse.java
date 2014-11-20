package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.User;

public class RemoveLineResponse extends AbstractResponse
{
    /** Utilisateur devant retirer une colonne */
    private User user;

    /** Index de la ligne retirée */
    private Integer line;

    public Integer getLine()
    {
        return this.line;
    }

    public void setLine(Integer column)
    {
        this.line = column;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
