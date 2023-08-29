package finos.traderx.tradeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import finos.traderx.messaging.Publisher;
import finos.traderx.messaging.mq.ActiveMQJSONPublisher;
import finos.traderx.tradeservice.model.TradeOrder;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class PubSubConfig {
    @Value("${trade.feed.address}")
    private String tradeFeedAddress;

     private final JmsTemplate jmsTemplate;

    @Autowired
    public PubSubConfig(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    @Bean 
    public Publisher<TradeOrder> tradePublisher() {
        ActiveMQJSONPublisher<TradeOrder> publisher = new ActiveMQJSONPublisher<TradeOrder>(jmsTemplate){};
         publisher.setTopic("/trades");
        return publisher;
    }

}
