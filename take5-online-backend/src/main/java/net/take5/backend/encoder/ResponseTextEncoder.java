package net.take5.backend.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import net.take5.commons.pojo.output.AbstractResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseTextEncoder implements Encoder.Text<AbstractResponse>
{
    /** Mapper JSON */
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // N'inclus pas les NULL
        mapper.setSerializationInclusion(Include.NON_NULL);
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
    public String encode(AbstractResponse response) throws EncodeException
    {
        try {
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new EncodeException(response, "Erreur à la sérialisation", e);
        }
    }

}
