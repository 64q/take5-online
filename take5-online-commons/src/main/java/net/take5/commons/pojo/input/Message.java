package net.take5.commons.pojo.input;

import java.util.Map;

/**
 * Message générique reçu par les clients
 * 
 * @author Quentin
 * 
 */
public class Message
{
    /** InputAction reçue */
    protected InputAction action;

    /** Paramètres */
    protected Map<String, Object> params;

    public InputAction getAction()
    {
        return this.action;
    }

    public void setAction(InputAction action)
    {
        this.action = action;
    }

    public Map<String, Object> getParams()
    {
        return this.params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

}
