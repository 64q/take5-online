package net.take5.backend.scheduler;

import net.take5.commons.pojo.output.Lobby;

import org.springframework.stereotype.Service;

/**
 * Service appelé de manière asynchrone à la fin du temps de choix d'une carte
 * 
 * @author Quentin
 */
@Service
public interface AsyncExecutor
{
    /**
     * Effectue la résolution de fin de tour
     * 
     * @param lobby
     *            lobby à traiter
     * @param timeout
     *            temps à définir avant de déclencher le traitement (en
     *            millisecondes)
     */
    public void performEndTurn(Lobby lobby, Long timeout);
}
