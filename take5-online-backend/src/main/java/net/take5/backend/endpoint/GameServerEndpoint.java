package net.take5.backend.endpoint;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.take5.backend.decoder.MessageTextDecoder;
import net.take5.backend.encoder.ResponseTextEncoder;
import net.take5.backend.server.GameServer;
import net.take5.backend.server.impl.GameServerImpl;
import net.take5.commons.pojo.input.Message;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

@ServerEndpoint(value = "/game", encoders = { ResponseTextEncoder.class }, decoders = { MessageTextDecoder.class }, configurator = SpringConfigurator.class)
public class GameServerEndpoint
{
    /** Logger */
    private static final Logger LOG = Logger.getLogger(GameServerEndpoint.class);

    /** Dispatcher de messages de la WebSocket */
    private GameServer dispatcher = new GameServerImpl();

    @Autowired
    public GameServerEndpoint(GameServer dispatcher) {
        this.dispatcher = dispatcher;
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException
    {
        LOG.info("Réception d'un message de type '" + message.getAction().name() + "'");

        dispatcher.dispatch(session, message);
    }

    @OnOpen
    public void openConnection(Session session)
    {
        LOG.info("Connexion d'un nouveau client : " + session.getId());
    }

    @OnClose
    public void closedConnection(Session session)
    {
        dispatcher.remove(session);
    }

    @OnError
    public void error(Session session, Throwable t)
    {
        LOG.error("Erreur remontée sur la WebSocket", t);

        dispatcher.remove(session);
    }
}