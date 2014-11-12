package net.take5.backend.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.Session;

import net.take5.commons.pojo.output.Lobby;
import net.take5.commons.pojo.output.User;

import org.springframework.stereotype.Component;

/**
 * Classe maintenant un état du jeu, implémenté comme singleton
 * 
 * @author Quentin
 */
@Component
public class ServerState
{
    /** Liste des utilisateurs actuellement connectés */
    private Map<Session, User> users;

    /** Liste des lobbies courants */
    private Queue<Lobby> lobbies;

    public ServerState() {
        users = new HashMap<Session, User>();
        lobbies = new ConcurrentLinkedQueue<Lobby>();
    }

    public User getUser(Session session)
    {
        return users.get(session);
    }

    public Map<Session, User> getUsers()
    {
        return this.users;
    }

    public void setUsers(Map<Session, User> users)
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

    public boolean userExists(Session session)
    {
        return this.users.containsKey(session);
    }

    /**
     * Méthode permettant de supprimer la session du serveur de jeu
     * 
     * @param session
     *            session à supprimer
     */
    public void remove(Session session)
    {
        User user = getUsers().get(session);

        if (user != null) {
            Lobby lobby = user.getCurrentLobby();

            if (lobby != null) {
                lobby.getUsers().remove(user);

                if (lobby.getOwner().equals(user)) {
                    // si l'utilisateur courant est propriétaire du lobby, alors
                    // on supprime aussi le lobby
                    getLobbies().remove(lobby);
                }
            }

            // suppression de l'utilisateur du serveur
            getUsers().remove(session);
        }
    }

    /**
     * Vérifie qu'un lobby existe par rapport à son Uid
     * 
     * @param lobbyUid
     *            uid du lobby
     * @return vrai si le lobby existe
     */
    public boolean lobbyExists(String lobbyUid)
    {
        return getLobby(lobbyUid) != null;
    }

    /**
     * Récupère un lobby depuis son uid
     * 
     * @param lobbyUid
     *            uid du lobby
     * @return le lobby associé
     */
    public Lobby getLobby(String lobbyUid)
    {
        Lobby found = null;

        for (Lobby lobby : lobbies) {
            if (lobby.getUid().equals(UUID.fromString(lobbyUid))) {
                found = lobby;
            }
        }

        return found;
    }

}
