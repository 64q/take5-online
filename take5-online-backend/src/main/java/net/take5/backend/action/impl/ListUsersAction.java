package net.take5.backend.action.impl;

import java.util.ArrayList;

import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.NoParams;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.ListUsersResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("LIST_USERS")
public class ListUsersAction extends AbstractAction<NoParams, ListUsersResponse>
{
    /** Etat du serveur */
    @Autowired
    private ServerState serverState;

    @Override
    public void initialize()
    {
        response = new ListUsersResponse();
        response.setAction(OutputAction.LIST_USERS);
    }

    @Override
    public void execute(Session session, Message<NoParams> message)
    {
        response.setState(State.OK);
        response.setUsers(new ArrayList<User>(serverState.getUsers().values()));
    }

    @Override
    public Boolean validate(Session session, Message<NoParams> message)
    {
        return true;
    }

}
