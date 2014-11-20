package net.take5.engine.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

import net.take5.commons.pojo.output.common.Card;
import net.take5.commons.pojo.output.common.GameBoard;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.LobbyState;
import net.take5.commons.pojo.output.common.User;
import net.take5.engine.service.Take5Engine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Take5EngineImpl implements Take5Engine
{
    /** Logger */
    private static final Logger LOG = Logger.getLogger(Take5EngineImpl.class);

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
        if (value.equals(55)) {
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

    @Override
    public void resolveTurn(Lobby lobby)
    {
        SortedMap<Card, User> selectedCards = new TreeMap<Card, User>();

        for (User user : lobby.getUsers()) {
            selectedCards.put(user.getHand().getPickedCard(), user);
        }

        // le parcours des cartes est effectué dans l'ordre croissant selon la
        // valeur de la carte
        Iterator<Map.Entry<Card, User>> it = selectedCards.entrySet().iterator();

        while (it.hasNext()) {
            List<Card> selectedCol = null;
            Map.Entry<Card, User> entry = it.next();

            for (List<Card> col : lobby.getGameBoard().getBoard()) {
                // dans le cas où la colonne est vide, celle ci est forcément
                // sélectionnée pour la carte à placer
                if (col.size() == 0) {
                    selectedCol = col;
                } else if (entry.getKey().getValue().intValue() > col.get(col.size() - 1).getValue().intValue()
                        && selectedCol == null) {
                    selectedCol = col;
                } else {
                    // lorsque la diff avec la col sélectionnée est inférieure,
                    // on réaffecte à la colonne courante
                    if (entry.getKey().getValue().intValue() > col.get(col.size() - 1).getValue().intValue()
                            && entry.getKey().getValue().intValue() - col.get(col.size() - 1).getValue().intValue() < entry
                                    .getKey().getValue().intValue()
                                    - selectedCol.get(selectedCol.size() - 1).getValue().intValue()) {
                        selectedCol = col;
                    }
                }

            }

            if (selectedCol.size() >= 5) {
                LOG.info("L'utilisateur " + entry.getValue() + " prend une ligne");
                // le joueur prend la ligne !
                entry.getValue().getHand().getTakenCards().addAll(selectedCol);
                selectedCol.clear();
            }

            selectedCol.add(entry.getKey());

            // suppression de la carte courante de la main du joueur
            entry.getValue().getHand().getCards().remove(entry.getKey());
        }
    }

    @Override
    public Boolean determineRemoveLine(Lobby lobby, Card card)
    {
        Boolean notInsertable = true;
        List<List<Card>> board = lobby.getGameBoard().getBoard();

        for (List<Card> col : board) {
            if (col.get(col.size() - 1).compareTo(card) <= 0) {
                notInsertable = false;
            }
        }

        if (notInsertable) {
            LOG.info("La carte " + card + " n'est pas insérable, une colonne doit être retirée");
        }

        return notInsertable;
    }

    @Override
    public void resolvePickedCards(Lobby lobby)
    {
        for (User user : lobby.getUsers()) {
            if (user.getHand().getPickedCard() == null) {
                Card selectedCard = user.getHand().getCards().get(0);

                user.getHand().setPickedCard(selectedCard);
                user.getHand().setPickedAuto(true);

                LOG.info("Choix automatique d'une carte " + selectedCard + " pour le joueur " + user);
            }
        }
    }

    @Override
    public Boolean resolveRemoveLine(Lobby lobby, User user)
    {
        Boolean notEmpty = false;
        GameBoard board = lobby.getGameBoard();

        for (List<Card> cards : board.getBoard()) {
            if (CollectionUtils.isEmpty(cards)) {
                notEmpty = true;

                LOG.debug("Une ligne a été supprimée en amont");
            }
        }

        // si jamais une des ligne n'a pas été vidée, on prend la première
        if (!notEmpty) {
            user.getHand().getTakenCards().addAll(board.getBoard().get(0));
            board.getBoard().get(0).clear();

            LOG.debug("Une ligne va être supprimée automatiquement");
        }

        return !notEmpty;
    }
}
