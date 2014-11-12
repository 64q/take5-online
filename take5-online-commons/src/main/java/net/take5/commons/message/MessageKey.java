package net.take5.commons.message;

/**
 * Constantes de messsages
 * 
 * @author Quentin
 */
public interface MessageKey
{
    /** Nom d'une partie */
    String ENGINE_LOBBY_NAME = "engine.lobby.name";

    /** Erreur : non connecté */
    String ERROR_USER_NOT_LOGGED = "error.user.not_logged";

    /** Username vide */
    String ERROR_LOGIN_USERNAME_EMPTY = "error.login.username_empty";

    /** Username déjà pris */
    String ERROR_LOGIN_USERNAME_ALREADY_TAKEN = "error.login.username_already_taken";

    /** Nom de lobby vide */
    String ERROR_LOBBY_NAME_EMPTY = "error.lobby.name_empty";

    /** Lobby introuvable */
    String ERROR_LOBBY_NOT_FOUND = "error.lobby.not_found";

    /** Déjà dans un lobby */
    String ERROR_ALREADY_IN_LOBBY = "error.lobby.already_in_lobby";

    /** Pas en attente de joueurs */
    String ERROR_LOBBY_NOT_WAITING = "error.lobby.not_waiting";

    /**
     * Erreur survenant lorsque l'on est pas dans un lobby au moment de l'action
     */
    String ERROR_NOT_IN_LOBBY = "error.lobby.not_in_lobby";

    /** Impossible d'initialiser la game (pas le créateur) */
    String ERROR_CANNOT_INIT_GAME = "error.lobby.cannot_init_game";

    /** Suppression du lobby de l'utilisateur */
    String NOTIFICATION_REMOVED_FROM_LOBBY = "message.notification.removed_from_lobby";
}
