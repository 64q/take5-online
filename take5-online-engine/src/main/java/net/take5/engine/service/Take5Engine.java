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
     * @param creator
     *            créateur du lobby
     * @return un lobby
     */
    Lobby createLobby(User creator);
}
