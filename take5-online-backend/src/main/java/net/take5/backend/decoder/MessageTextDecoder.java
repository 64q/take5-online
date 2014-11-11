package net.take5.backend.decoder;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import net.take5.commons.pojo.input.Message;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageTextDecoder implements Decoder.Text<Message>
{
    /** Logger */
    private static final Logger LOG = Logger.getLogger(MessageTextDecoder.class);

    /** Mapper JSON */
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @Override
    public void init(EndpointConfig config)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Message decode(String s) throws DecodeException
    {
        Message message = null;

        try {
            message = mapper.readValue(s, Message.class);
        } catch (IOException e) {
            LOG.error("Erreur de déserialisation", e);
            throw new DecodeException(s, "Erreur lors de la déserialisation", e);
        }

        return message;
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
