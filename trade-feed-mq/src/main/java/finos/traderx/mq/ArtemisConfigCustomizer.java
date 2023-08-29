package finos.traderx.mq;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtemisConfigCustomizer  implements ArtemisConfigurationCustomizer {
   @Override
   public void customize(org.apache.activemq.artemis.core.config.Configuration configuration) {
    try{
      configuration.addAcceptorConfiguration("remote", "tcp://0.0.0.0:61616");
    } catch (Exception e){
        e.printStackTrace();
    }
   }
} 