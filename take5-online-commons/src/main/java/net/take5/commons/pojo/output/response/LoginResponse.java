package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;

public class LoginResponse extends AbstractResponse
{
    /** Renvoi du pseudo utilisateur */
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
