package finos.traderx.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.EnableJms;


@SpringBootApplication
@EnableJms
public class MQApplication {
	static final Logger log=LoggerFactory.getLogger(MQApplication.class);

	public static void main(String[] args)throws Throwable {
		ApplicationContext ctx= SpringApplication.run(MQApplication.class, args);
		Thread.sleep(1000*25);
		log.info("About to send");
		ctx.getBeansOfType(SendTest.class).values().iterator().next().send();
		log.info("Just Sent");
	}

}
