package net.take5.commons.pojo.input.params;

import net.take5.commons.pojo.input.AbstractParams;

public class LoginParams extends AbstractParams
{
    /** Nom d'utilisateur */
    private String username;

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}
