package net.take5.commons.pojo.input;

import net.take5.commons.pojo.input.params.CreateLobbyParams;
import net.take5.commons.pojo.input.params.JoinLobbyParams;
import net.take5.commons.pojo.input.params.LoginParams;
import net.take5.commons.pojo.input.params.NoParams;
import net.take5.commons.pojo.input.params.QuitLobbyParams;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Message générique reçu par les clients
 * 
 * @author Quentin
 * 
 */
public class Message<T extends AbstractParams>
{
    /** InputAction reçue */
    protected InputAction action;

    /** Paramètres */
    @JsonTypeInfo(defaultImpl = NoParams.class, use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "action")
    @JsonSubTypes({ @JsonSubTypes.Type(value = LoginParams.class, name = "LOGIN"),
            @JsonSubTypes.Type(value = CreateLobbyParams.class, name = "CREATE_LOBBY"),
            @JsonSubTypes.Type(value = JoinLobbyParams.class, name = "JOIN_LOBBY"),
            @JsonSubTypes.Type(value = QuitLobbyParams.class, name = "QUIT_LOBBY") })
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
