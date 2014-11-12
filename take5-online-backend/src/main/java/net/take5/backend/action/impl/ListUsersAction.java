package net.take5.backend.action.impl;

import java.util.ArrayList;

import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.OutputAction;
import net.take5.commons.pojo.output.State;
import net.take5.commons.pojo.output.User;
import net.take5.commons.pojo.output.response.ListUsersResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("LIST_USERS")
public class ListUsersAction extends AbstractAction<ListUsersResponse>
{
    /** Etat du serveur */
    @Autowired
    private ServerState serverState;

    @Override
    public void init()
    {
        response = new ListUsersResponse();
        response.setAction(OutputAction.LIST_USERS);
    }

    @Override
    public void execute(Session session, Message message)
    {
        response.setState(State.OK);
        response.setUsers(new ArrayList<User>(serverState.getUsers().values()));
    }

    @Override
    public Boolean validate(Session session, Message message)
    {
        return true;
    }

}
