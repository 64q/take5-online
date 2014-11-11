package net.take5.commons.pojo.output;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Représente un utilisateur
 * 
 * @author Quentin
 */
public class User
{
    /** Nom d'utilisateur */
    private String username;

    /** Nombre de parties gagnées */
    private Long wonGames;

    /** Nombre de parties perdues */
    private Long lostGames;

    /** Lobby courant de l'utilisateur */
    private Lobby currentLobby;

    @JsonIgnore
    public Lobby getCurrentLobby()
    {
        return this.currentLobby;
    }

    public void setCurrentLobby(Lobby currentLobby)
    {
        this.currentLobby = currentLobby;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Long getWonGames()
    {
        return this.wonGames;
    }

    public void setWonGames(Long won)
    {
        this.wonGames = won;
    }

    public Long getLostGames()
    {
        return this.lostGames;
    }

    public void setLostGames(Long lost)
    {
        this.lostGames = lost;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (this.username == null) {
            if (other.username != null)
                return false;
        } else if (!this.username.equals(other.username))
            return false;
        return true;
    }
}
