package net.take5.commons.pojo.input.params;

import net.take5.commons.pojo.input.AbstractParams;

public class CreateLobbyParams extends AbstractParams
{
    /** Nom du lobby */
    private String name;

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
