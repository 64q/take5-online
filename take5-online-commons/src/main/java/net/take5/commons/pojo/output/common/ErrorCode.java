package net.take5.commons.pojo.output.common;

/**
 * Codes d'erreur de l'application
 * 
 * @author Quentin
 */
public enum ErrorCode
{
    /** Non connecté */
    NOT_LOGGED,
    /** Username déjà pris */
    USERNAME_ALREADY_TAKEN, USERNAME_EMPTY, LOBBY_NAME_EMPTY, LOBBY_NOT_FOUND, ALREADY_IN_LOBBY, LOBBY_NOT_WAITING, NOT_IN_LOBBY, CANNOT_INIT_GAME, INVALID_CARD, INVALID_COLUMN, INVALID_STATE;
}
