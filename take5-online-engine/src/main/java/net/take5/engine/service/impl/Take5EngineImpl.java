package net.take5.engine.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import net.take5.commons.pojo.output.common.Card;
import net.take5.commons.pojo.output.common.GameBoard;
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

        Stack<Card> cards = new Stack<Card>();

        // remplissage du paquet
        for (int i = 1; i <= 104; i++) {
            cards.add(getCardFromValue(i));
        }

        drawCards(lobby, cards);
        initGameBoard(lobby, cards);
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
    protected void drawCards(Lobby lobby, Stack<Card> cards)
    {
        // mélange du paquet
        Collections.shuffle(cards);

        // distribution en round robin
        for (int i = 0; i < 10; i++) {
            for (User user : lobby.getUsers()) {
                user.getHand().getCards().add(cards.pop());
            }
        }
    }

    protected static Card getCardFromValue(Integer value)
    {
        Card card = new Card();
        card.setValue(value);

        // règle div 10 = 3 têtes
        if (value % 10 == 0) {
            card.setOxHeads(3);
        } else if (value % 5 == 0) {
            card.setOxHeads(2);
        } else if (value % 11 == 0) {
            card.setOxHeads(5);
        } else {
            card.setOxHeads(1);
        }

        // règle spéciale 55
        if (value == 55) {
            card.setOxHeads(7);
        }

        return card;
    }

    /**
     * Initialise le plateau de jeu
     * 
     * @param lobby
     *            lobby à traiter
     * @param cards
     *            cartes restantes à mettre en jeu
     */
    protected void initGameBoard(Lobby lobby, Stack<Card> cards)
    {
        GameBoard gameBoard = new GameBoard();
        List<List<Card>> board = gameBoard.getBoard();

        for (List<Card> cols : board) {
            cols.add(cards.pop());
        }

        lobby.setGameBoard(gameBoard);
    }
}
