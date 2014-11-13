package net.take5.commons.pojo.output.response;

import java.util.List;

import net.take5.commons.pojo.output.AbstractResponse;
import net.take5.commons.pojo.output.common.User;

/**
 * Réponse liste des utilisateurs
 * 
 * @author Quentin
 */
public class ListUsersResponse extends AbstractResponse
{
    /** Liste des utilisateurs */
    private List<User> users;

    public List<User> getUsers()
    {
        return this.users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

}
