package net.take5.backend.server.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import net.take5.backend.context.ConnectedUsers;
import net.take5.backend.server.GameServer;
import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.input.InputAction;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.ErrorCode;
import net.take5.commons.pojo.output.Lobby;
import net.take5.commons.pojo.output.OutputAction;
import net.take5.commons.pojo.output.State;
import net.take5.commons.pojo.output.User;
import net.take5.commons.pojo.output.response.CreateLobbyResponse;
import net.take5.commons.pojo.output.response.ListLobbiesResponse;
import net.take5.commons.pojo.output.response.ListUsersResponse;
import net.take5.commons.pojo.output.response.LoginResponse;
import net.take5.engine.service.Take5Engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/**
 * Dispatcher de message reçu depuis la WebSocket
 * 
 * @author Quentin
 */
@Component
public class GameServerImpl implements GameServer, MessageSourceAware
{
    @Autowired
    private Take5Engine gameEngine;

    /** Queue contenant les sessions utilisateur */
    private ConnectedUsers users = new ConnectedUsers();

    /** Queue contenant les lobby courants */
    private Queue<Lobby> lobbies = new ConcurrentLinkedQueue<Lobby>();

    /** Messages stockées */
    private MessageSource messageSource;

    @Override
    public void dispatch(Session session, Message message) throws IOException, EncodeException
    {
        AbstractResponse response = null;

        if (message.getAction() == InputAction.LOGIN) {
            response = dispatchLogin(session, message);
        } else if (message.getAction() == InputAction.LIST_USERS) {
            response = dispatchListUsers(session, message);
        } else if (message.getAction() == InputAction.CREATE_LOBBY) {
            response = dispatchCreateLobby(session, message);
        } else if (message.getAction() == InputAction.LIST_LOBBIES) {
            response = dispatchListLobbies(session, message);
        } else {
            // si le message reçu n'est pas compréhensible, on lance un
            // exception et la socket sera fermée
            throw new RuntimeException("Message envoyé indéfini");
        }

        // envoi dans la socket de la réponse à la demande
        session.getBasicRemote().sendObject(response);
    }

    private AbstractResponse dispatchListLobbies(Session session, Message message)
    {
        List<Lobby> list = new ArrayList<Lobby>(lobbies);

        ListLobbiesResponse response = new ListLobbiesResponse();

        response.setAction(OutputAction.LIST_LOBBIES);
        response.setState(State.OK);
        response.setLobbies(list);

        return response;
    }

    private AbstractResponse dispatchCreateLobby(Session session, Message message)
    {
        // récupération de l'utilisateur connecté
        User user = getUsers().get(session);

        // création de la réponse
        CreateLobbyResponse response = new CreateLobbyResponse();
        response.setAction(OutputAction.CREATE_LOBBY);

        if (user == null) {
            // si l'utilisateur n'est pas connecté, on renvoie un KO
            return createNotLoggedResponse(response);
        }

        // création du lobby
        Lobby lobby = gameEngine.createLobby(user);

        response.setState(State.OK);
        response.setLobby(lobby);

        // ajout à la liste des lobbies
        lobbies.add(lobby);

        return response;
    }

    /**
     * Traite la connexion au jeu
     * 
     * @param session
     * @param message
     * @throws IOException
     * @throws EncodeException
     */
    private AbstractResponse dispatchLogin(Session session, Message message)
    {
        User user = new User();
        user.setUsername((String) message.getParams().get("username"));

        // ajout à la liste des utilisateurs connectés
        getUsers().put(session, user);

        LoginResponse response = new LoginResponse();

        response.setState(State.OK);
        response.setAction(OutputAction.LOGIN);
        response.setUsername(user.getUsername());

        return response;
    }

    /**
     * Traite la récupération des utilisateurs connectés
     * 
     * @param session
     * @param message
     * @throws IOException
     * @throws EncodeException
     */
    private AbstractResponse dispatchListUsers(Session session, Message message)
    {
        ListUsersResponse response = new ListUsersResponse();

        response.setAction(OutputAction.LIST_USERS);
        response.setState(State.OK);
        response.setUsers(new ArrayList<User>(getUsers().getUsersMap().values()));

        return response;
    }

    @Override
    public ConnectedUsers getUsers()
    {
        return users;
    }

    public void setUsers(ConnectedUsers users)
    {
        this.users = users;
    }

    public Queue<Lobby> getLobbies()
    {
        return this.lobbies;
    }

    public void setLobbies(Queue<Lobby> lobbies)
    {
        this.lobbies = lobbies;
    }

    /**
     * Retourne une réponse en cas d'erreur : non connecté
     * 
     * @return une {@link AbstractResponse}
     */
    private AbstractResponse createNotLoggedResponse(AbstractResponse response)
    {
        response.setState(State.KO);
        response.setReason(messageSource.getMessage(MessageKey.ERROR_USER_NOT_LOGGED, null, Locale.getDefault()));
        response.setCode(ErrorCode.NOT_LOGGED);

        return response;
    }

    @Override
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    @Override
    public void remove(Session session)
    {
        User user = getUsers().get(session);

        if (user != null) {
            Lobby lobby = user.getCurrentLobby();

            if (lobby != null) {
                lobby.getUsers().remove(user);

                if (lobby.getOwner().equals(user)) {
                    // si l'utilisateur courant est propriétaire du lobby, alors
                    // on
                    // supprime aussi le lobby
                    lobbies.remove(lobby);
                }
            }

            // suppression de l'utilisateur du serveur
            getUsers().remove(session);
        }

    }
}
