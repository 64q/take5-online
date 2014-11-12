package net.take5.engine.service;

import net.take5.commons.pojo.output.Lobby;
import net.take5.commons.pojo.output.User;

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
}
