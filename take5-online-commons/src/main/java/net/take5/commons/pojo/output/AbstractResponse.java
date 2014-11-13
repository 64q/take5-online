package net.take5.commons.pojo.output;

import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;

/**
 * Réponse JSON de la WebSocket
 * 
 * @author Quentin
 */
abstract public class AbstractResponse
{
    /** InputAction de sortie */
    private OutputAction action;

    /** Etat de la réponse formulée par le serveur */
    private State state;

    /** Code d'erreur éventuel */
    private ErrorCode code;

    /** Raison du KO (message d'erreur) */
    private String reason;

    public State getState()
    {
        return this.state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public String getReason()
    {
        return this.reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public OutputAction getAction()
    {
        return this.action;
    }

    public void setAction(OutputAction action)
    {
        this.action = action;
    }

    public ErrorCode getCode()
    {
        return this.code;
    }

    public void setCode(ErrorCode code)
    {
        this.code = code;
    }
}
