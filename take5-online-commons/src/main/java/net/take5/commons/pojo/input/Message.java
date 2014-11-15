package net.take5.commons.pojo.input;

import net.take5.commons.pojo.input.params.NoParams;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Message générique reçu par les clients
 * 
 * @author Quentin
 */
public class Message<T extends AbstractParams>
{
    /** InputAction reçue */
    protected InputAction action;

    /** Paramètres */
    @JsonTypeInfo(defaultImpl = NoParams.class, use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "action")
    protected T params;

    public InputAction getAction()
    {
        return this.action;
    }

    public void setAction(InputAction action)
    {
        this.action = action;
    }

    public T getParams()
    {
        return this.params;
    }

    public void setParams(T params)
    {
        this.params = params;
    }

}
