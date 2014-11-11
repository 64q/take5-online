package net.take5.commons.pojo.output;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Informations pour un Lobby
 * 
 * @author Quentin
 */
public class Lobby
{
    /** UUID du lobby */
    private UUID uid;

    /** Nom de la partie */
    private String name;

    /** Propriétaire du lobby */
    private User owner;

    /** Liste des utilisateurs dans le lobby */
    private Set<User> users;

    public Lobby() {
        uid = UUID.randomUUID();
        users = new HashSet<User>();
    }

    public User getOwner()
    {
        return this.owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    public Set<User> getUsers()
    {
        return this.users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }

    public UUID getUid()
    {
        return uid;
    }

    public void setUid(UUID uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
