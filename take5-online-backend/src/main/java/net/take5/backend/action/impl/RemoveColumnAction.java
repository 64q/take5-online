package net.take5.backend.action.impl;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.RemoveColumnParams;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.GameBoard;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.RemoveColumnChoiceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("REMOVE_COLUMN")
public class RemoveColumnAction extends AbstractAction<RemoveColumnParams, RemoveColumnChoiceResponse>
{
    /** Etat du serveur */
    @Autowired
    private ServerState serverState;

    @Override
    public void init()
    {
        response = new RemoveColumnChoiceResponse();
        response.setAction(OutputAction.REMOVE_COLUMN_CHOICE);
    }

    @Override
    public void execute(Session session, Message<RemoveColumnParams> message) throws IOException, EncodeException
    {
        Integer columnIndex = message.getParams().getColumn();
        User user = serverState.getUser(session);

        response.setState(State.OK);
        response.setColumn(columnIndex);
        response.setUser(user);

        for (User userInLobby : user.getCurrentLobby().getUsers()) {
            userInLobby.getSession().getAsyncRemote().sendObject(response);
        }
    }

    @Override
    public Boolean validate(Session session, Message<RemoveColumnParams> message)
    {
        Boolean isValid = true;
        Integer columnIndex = message.getParams().getColumn();
        User user = serverState.getUser(session);

        GameBoard gameBoard = user.getCurrentLobby().getGameBoard();

        if (columnIndex < 0 || columnIndex >= gameBoard.getBoard().size()) {
            response.setState(State.KO);
            response.setCode(ErrorCode.INVALID_COLUMN);

            isValid = false;
        }

        return isValid;
    }

}
