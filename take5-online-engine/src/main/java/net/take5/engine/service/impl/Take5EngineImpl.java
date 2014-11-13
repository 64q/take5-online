package net.take5.engine.service.impl;

import java.util.Collections;
import java.util.Stack;

import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.LobbyState;
import net.take5.commons.pojo.output.common.User;
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

        drawCards(lobby);
    }

    @Override
    public void quitLobby(User user, Lobby lobby)
    {
        lobby.getUsers().remove(user);
        user.setCurrentLobby(null);
    }

    /**
     * Mélange le paquet de cartes pour le lobby donné
     * 
     * @param lobby
     * @return
     */
    protected void drawCards(Lobby lobby)
    {
        Stack<Integer> cards = new Stack<Integer>();

        // remplissage du paquet
        for (int i = 1; i <= 104; i++) {
            cards.add(Integer.valueOf(i));
        }

        // mélange du paquet
        Collections.shuffle(cards);

        // distribution en round robin
        for (int i = 0; i < 10; i++) {
            for (User user : lobby.getUsers()) {
                user.getHand().getCards().add(cards.pop());
            }
        }
    }
}
