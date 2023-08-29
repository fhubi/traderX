package finos.traderx.mq;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendTest {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public SendTest(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(){
        jmsTemplate.convertAndSend("/test", "Hello World "+new Date().toString());
    }
}
