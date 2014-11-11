package net.take5.engine.service.impl;

import java.util.Locale;

import net.take5.commons.message.MessageKey;
import net.take5.commons.pojo.output.Lobby;
import net.take5.commons.pojo.output.User;
import net.take5.engine.service.Take5Engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component
public class Take5EngineImpl implements Take5Engine, MessageSourceAware
{
    @Autowired
    private MessageSource messageSource;

    @Override
    public Lobby createLobby(User creator)
    {
        Lobby lobby = new Lobby();

        lobby.setOwner(creator);
        lobby.getUsers().add(creator);
        lobby.setName(messageSource.getMessage(MessageKey.ENGINE_LOBBY_NAME, new String[] { creator.getUsername() },
                Locale.getDefault()));

        creator.setCurrentLobby(lobby);

        return lobby;
    }

    @Override
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

}
