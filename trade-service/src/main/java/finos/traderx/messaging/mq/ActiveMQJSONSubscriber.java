package finos.traderx.messaging.mq;

import java.net.URI;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;


import finos.traderx.messaging.Envelope;
import finos.traderx.messaging.PubSubException;
import finos.traderx.messaging.Subscriber;

import kotlin.NotImplementedError;

/**
 * Simple socketIO Subscriber, which uses 3 commands - 'subscribe',
 * 'unsubscribe', and 'publish' followed by payload
 * Publish events consist of an envelope and an internal payload.
 */
public abstract class ActiveMQJSONSubscriber<T> implements Subscriber<T>, InitializingBean {
    private static ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public ActiveMQJSONSubscriber(Class<T> typeClass) {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(ActiveMQEnvelope.class, typeClass );
        this.envelopeType = type;
        this.objectType = typeClass;
    }

    final JavaType envelopeType;
    final Class<T> objectType;

    org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass().getName());

    boolean connected = false;

    @Override
    public boolean isConnected() {
        return connected;
    }

    String socketAddress = "http://localhost:3000";

    public void setSocketAddress(String addr) {
        socketAddress = addr;
    }

    private String defaultTopic = "/default";
    public void setDefaultTopic(String topic) {
        defaultTopic = topic;
    }

    public abstract void onMessage(Envelope<?> envelope, T message);

    @Override
    public void subscribe(String topic) throws PubSubException {
        log.info("Subscribing to " + topic);
       // socket.emit("subscribe", topic);
    }

    @Override
    public void unsubscribe(String topic) throws PubSubException {
      //  socket.emit("unsubscribe", "topic");
    }

    @Override
    public void disconnect() throws PubSubException {
       throw new PubSubException("Not implemented for JMS",new NotImplementedError());
    }

    @Override
    public void connect() throws PubSubException {
            throw new PubSubException("Not implemented for JMS",new NotImplementedError());
    }
 
    @JmsListener(destination = "/*")
	public void processMessage(String content) {
                    try{
                    JSONObject json = new JSONObject(content);
                    log.info("Raw Payload " + content.toString());
                
                    if(! objectType.getSimpleName().equals(json.get("type"))){
                        log.info("System Message>>>>> " + content.toString());
                    } else {
                        ActiveMQEnvelope<T> envelope = (ActiveMQEnvelope<T>) objectMapper.readValue(json.toString(),  envelopeType);
                        log.info("Incoming Payload: " + envelope.getPayload());
                        ActiveMQJSONSubscriber.this.onMessage(envelope, envelope.getPayload());
                    }
                } catch (Exception e){
                    e.printStackTrace();;
                }
                   
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        subscribe(defaultTopic);
    }
}