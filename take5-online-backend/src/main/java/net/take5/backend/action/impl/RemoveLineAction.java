package net.take5.backend.action.impl;

import java.io.IOException;
import java.util.List;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.action.AbstractAction;
import net.take5.backend.context.ServerState;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.RemoveLineParams;
import net.take5.commons.pojo.output.common.Card;
import net.take5.commons.pojo.output.common.ErrorCode;
import net.take5.commons.pojo.output.common.GameBoard;
import net.take5.commons.pojo.output.common.LobbyState;
import net.take5.commons.pojo.output.common.OutputAction;
import net.take5.commons.pojo.output.common.State;
import net.take5.commons.pojo.output.common.User;
import net.take5.commons.pojo.output.response.RemoveLineResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("REMOVE_LINE")
public class RemoveLineAction extends AbstractAction<RemoveLineParams, RemoveLineResponse>
{
    /** Etat du serveur */
    @Autowired
    private ServerState serverState;

    @Override
    public void init()
    {
        response = new RemoveLineResponse();
        response.setAction(OutputAction.REMOVE_LINE);
    }

    @Override
    public void execute(Session session, Message<RemoveLineParams> message) throws IOException, EncodeException
    {
        Integer lineIndex = message.getParams().getLine();
        User user = serverState.getUser(session);

        List<Card> cardsList = user.getCurrentLobby().getGameBoard().getBoard().get(lineIndex);

        // ajout des cartes dans la cagnotte de l'utilisateur
        user.getHand().getTakenCards().addAll(cardsList);
        // vidage de la ligne
        user.getCurrentLobby().getGameBoard().getBoard().get(lineIndex).clear();

        response.setState(State.OK);
        response.setLine(lineIndex);
        response.setUser(user);

        notifyUsersInLobby(user);
    }

    /**
     * Notifie les autres utilisateurs du lobby
     * 
     * @param user
     *            utilisateur envoyant le choix de colonne
     */
    private void notifyUsersInLobby(User user)
    {
        for (User userInLobby : user.getCurrentLobby().getUsers()) {
            if (!userInLobby.equals(user)) {
                userInLobby.getSession().getAsyncRemote().sendObject(response);
            }
        }
    }

    @Override
    public Boolean validate(Session session, Message<RemoveLineParams> message)
    {
        Boolean isValid = true;
        Integer lineIndex = message.getParams().getLine();
        User user = serverState.getUser(session);

        GameBoard gameBoard = user.getCurrentLobby().getGameBoard();

        if (lineIndex < 0 || lineIndex >= gameBoard.getBoard().size()) {
            response.setState(State.KO);
            response.setCode(ErrorCode.INVALID_COLUMN);

            isValid = false;
        }

        if (isValid && user.getCurrentLobby().getState() != LobbyState.CHOICE) {
            response.setState(State.KO);
            response.setCode(ErrorCode.INVALID_STATE);

            isValid = false;
        }

        // TODO valider aussi que le remove choice émane du joueur devant
        // enlever la ligne

        return isValid;
    }

}
