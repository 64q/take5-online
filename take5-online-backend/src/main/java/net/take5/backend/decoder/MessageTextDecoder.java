package net.take5.backend.decoder;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import net.take5.commons.pojo.input.AbstractParams;
import net.take5.commons.pojo.input.InputAction;
import net.take5.commons.pojo.input.Message;
import net.take5.commons.pojo.input.params.CardChoiceParams;
import net.take5.commons.pojo.input.params.CreateLobbyParams;
import net.take5.commons.pojo.input.params.JoinLobbyParams;
import net.take5.commons.pojo.input.params.LoginParams;
import net.take5.commons.pojo.input.params.QuitLobbyParams;
import net.take5.commons.pojo.input.params.RemoveLineParams;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class MessageTextDecoder implements Decoder.Text<Message<AbstractParams>>
{
    /** Logger */
    private static final Logger LOG = Logger.getLogger(MessageTextDecoder.class);

    /** Mapper JSON */
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // enregistrement des types de paramètres attentus dans la requête
        mapper.registerSubtypes(new NamedType(LoginParams.class, InputAction.LOGIN.name()), new NamedType(
                CreateLobbyParams.class, InputAction.CREATE_LOBBY.name()), new NamedType(JoinLobbyParams.class,
                InputAction.JOIN_LOBBY.name()), new NamedType(QuitLobbyParams.class, InputAction.QUIT_LOBBY.name()),
                new NamedType(CardChoiceParams.class, InputAction.CARD_CHOICE.name()), new NamedType(
                        RemoveLineParams.class, InputAction.REMOVE_LINE.name()));
    }

    @Override
    public void init(EndpointConfig config)
    {

    }

    @Override
    public void destroy()
    {

    }

    @Override
    public Message<AbstractParams> decode(String s) throws DecodeException
    {
        try {
            @SuppressWarnings("unchecked")
            Message<AbstractParams> message = mapper.readValue(s, Message.class);
            return message;
        } catch (IOException e) {
            LOG.error("Erreur de déserialisation", e);
            throw new DecodeException(s, "Erreur lors de la déserialisation", e);
        }
    }

    @Override
    public boolean willDecode(String s)
    {
        boolean isValid = true;

        try {
            mapper.readValue(s, Message.class);
        } catch (IOException e) {
            LOG.error("Erreur lors du décodage, l'objet n'est pas valide", e);

            isValid = false;
        }

        return isValid;
    }

}
