package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.User;

public class RemoveColumnChoiceResponse extends AbstractResponse
{
    /** Utilisateur devant retirer une colonne */
    private User user;

    /** Index de la colonne retirée */
    private Integer column;

    public Integer getColumn()
    {
        return this.column;
    }

    public void setColumn(Integer column)
    {
        this.column = column;
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
