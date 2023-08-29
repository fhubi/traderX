package finos.traderx.messaging.mq;

import java.net.URI;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.core.JmsTemplate;

import finos.traderx.messaging.PubSubException;
import finos.traderx.messaging.Publisher;

import kotlin.NotImplementedError;

/**
 * Simple socketIO Publisher, which uses 3 commands - 'subscribe',
 * 'unsubscribe', and 'publish' followed by payload
 * Publish events consist of an envelope and an internal payload.
 * 
 */

public abstract class ActiveMQJSONPublisher<T> implements Publisher<T> {
    private static ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private final JmsTemplate jmsTemplate;

    @Autowired
    public ActiveMQJSONPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    boolean connected = false;

    @Override
    public boolean isConnected() {
        return connected;
    }

    String topic = "/default";

    public void setTopic(String t) {
        topic = t;
    }

    @Override
    public void publish(T message) throws PubSubException {
        publish(topic, message);
    }

    @Override
    public void publish(String topic, T message) throws PubSubException {
        try {
            ActiveMQEnvelope<T> envelope = new ActiveMQEnvelope<T>(topic, message);
            String msgString = objectMapper.writerFor(ActiveMQEnvelope.class).writeValueAsString(envelope);
            JSONObject obj = new JSONObject(msgString);
            log.debug("PUBLISH->" + obj);
            jmsTemplate.convertAndSend("/test",obj.toString());
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override
    public void disconnect() throws PubSubException {
        throw new PubSubException("Not implemented for JMS",new NotImplementedError());
    }

    @Override
    public void connect() throws PubSubException {
        throw new PubSubException("Not implemented for JMS",new NotImplementedError());
    }

    
}