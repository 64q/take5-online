package net.take5.engine.service.impl;

import net.take5.commons.pojo.output.Lobby;
import net.take5.commons.pojo.output.LobbyState;
import net.take5.commons.pojo.output.User;
import net.take5.engine.service.Take5Engine;

import org.springframework.stereotype.Component;

@Component
public class Take5EngineImpl implements Take5Engine
{
    @Override
    public Lobby createLobby(String name, User creator)
    {
        Lobby lobby = new Lobby();

        lobby.setName(name);
        lobby.setOwner(creator);

        // ajout du créateur
        lobby.getUsers().add(creator);

        // re-settage du lobby sur l'utilisateur créateur
        creator.setCurrentLobby(lobby);

        return lobby;
    }

    @Override
    public void joinLobby(User user, Lobby lobby)
    {
        lobby.getUsers().add(user);
        user.setCurrentLobby(lobby);
    }

    @Override
    public void newGame(Lobby lobby)
    {
        // mise en route de la partie en changeant l'état à en cours
        lobby.setState(LobbyState.RUNNING);

        // TODO initialiser le plateau de jeu
    }

    @Override
    public void quitLobby(User user, Lobby lobby)
    {
        lobby.getUsers().remove(user);
        user.setCurrentLobby(null);
    }
}
