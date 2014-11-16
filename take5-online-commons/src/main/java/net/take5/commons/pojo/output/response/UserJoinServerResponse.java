package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.User;

public class UserJoinServerResponse extends AbstractResponse
{
    /** Utilisateur ayant rejoint le serveur */
    private User user;

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

}
