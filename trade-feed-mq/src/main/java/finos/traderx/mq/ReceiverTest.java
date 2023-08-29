package finos.traderx.mq;

import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverTest {
    
	@JmsListener(destination = "/test")
	public void processMessage(String content) {
		LoggerFactory.getLogger(ReceiverTest.class).info("Incoming Message: "+content);
	}
} 