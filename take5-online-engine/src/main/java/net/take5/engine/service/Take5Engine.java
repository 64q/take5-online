package net.take5.engine.service;

import net.take5.commons.pojo.output.common.Card;
import net.take5.commons.pojo.output.common.Lobby;
import net.take5.commons.pojo.output.common.User;

import org.springframework.stereotype.Service;

/**
 * Interface du moteur de jeu
 * 
 * @author Quentin
 */
@Service
public interface Take5Engine
{
    /**
     * Permet de créer un lobby
     * 
     * @param name
     *            nom du lobby
     * @param creator
     *            créateur du lobby
     * @return un lobby
     */
    Lobby createLobby(String name, User creator);

    /**
     * permet de joindre un lobby
     * 
     * @param user
     *            utilisateur rejoignant le lobby
     * @param lobby
     *            lobby à rejoindre
     */
    void joinLobby(User user, Lobby lobby);

    /**
     * Initialise une nouvelle partie depuis un lobby
     * 
     * @param lobby
     *            lobby à initialiser
     */
    void newGame(Lobby lobby);

    /**
     * Retire l'utilisateur du lobby
     * 
     * @param user
     *            l'utilisateur à retirer
     * @param lobby
     *            le lobby à traiter
     */
    void quitLobby(User user, Lobby lobby);

    /**
     * Résout l'issue d'un tour
     * 
     * @param lobby
     *            lobby à traiter
     */
    void resolveTurn(Lobby lobby);

    /**
     * Méthode déterminant si un joueur doit enlever une colonne
     * 
     * @param lobby
     *            lobby à traiter
     * @param card
     *            la carte à insérer
     * @return vrai si la plus petite carte est insérable
     */
    Boolean determineRemoveColumn(Lobby lobby, Card card);

    /**
     * Résout les picked cards non attribuées durant le timer
     * 
     * @param lobby
     *            lobby à traiter
     */
    void resolvePickedCards(Lobby lobby);

    /**
     * Résout la suppression d'une colonne si nécessaire
     * 
     * @param lobby
     *            lobby à traiter
     * @param user
     *            utilisateur cible de la suppression de colonne
     * @param retourne
     *            vrai si la suppression a été faite automatiquement
     */
    Boolean resolveRemoveColumn(Lobby lobby, User user);
}
