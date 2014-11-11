package net.take5.backend.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import net.take5.commons.pojo.output.User;

/**
 * Classe représentant les utilisateurs connectés au serveur de jeu
 * 
 * @author Quentin
 */
public class ConnectedUsers
{
    /** Liste des utilisateurs connectés */
    private final Map<Session, User> usersMap;

    public ConnectedUsers() {
        usersMap = new ConcurrentHashMap<Session, User>();
    }

    public void put(Session session, User user)
    {
        usersMap.put(session, user);
    }

    public void remove(Session session)
    {
        usersMap.remove(session);
    }

    public Map<Session, User> getUsersMap()
    {
        return this.usersMap;
    }

    public User get(Session session)
    {
        return usersMap.get(session);
    }
}
