package co.edu.javeriana.msc.turismo.notification;

import co.edu.javeriana.msc.turismo.notification.dtos.MyMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class MessageQueueConsumer {
    @Bean
    Consumer<Message<MyMessageDTO>> receiveMessage() {
        return message -> {
            log.info("Received message: {}", message);
            log.info("Payload: {}", message.getPayload());
        // Process the message here
        };
    }
}
