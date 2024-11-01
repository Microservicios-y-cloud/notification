package co.edu.javeriana.msc.turismo.notification;

import co.edu.javeriana.msc.turismo.notification.dtos.PurchaseNotification;
import co.edu.javeriana.msc.turismo.notification.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.notification.enums.Status;
import co.edu.javeriana.msc.turismo.notification.mail.EmailService;
import jakarta.ws.rs.ProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@Configuration
@AllArgsConstructor
public class MessageQueueConsumer {
    private final EmailService emailService;
    @Bean
    Consumer<Message<PurchaseNotification>> receiveNotification() {
        return message -> {
            try{
                log.info("Received message: {}", message);
                log.info("Payload: {}", message.getPayload());
                log.info("Customer received: {}", message.getPayload().purchaser().getEmail());

                if (message.getPayload().paymentStatus().equals(PaymentStatus.RECHAZADA)){
                    try {
                        emailService.sentPaymentFailEmail(
                                message.getPayload().purchaser().getEmail(),
                                message.getPayload().purchaser().getFirstName() + " " + message.getPayload().purchaser().getLastName(),
                                message.getPayload().amount(),
                                message.getPayload().id());
                    } catch (Exception e) {
                        log.error("Error sending email", e);
                        throw new ProcessingException("Error sending email", e);
                    }
                }
                if (message.getPayload().paymentStatus().equals(PaymentStatus.ACEPTADA)){
                    try {
                        emailService.sentPaymentSuccessEmail(
                                message.getPayload().purchaser().getEmail(),
                                message.getPayload().purchaser().getFirstName() + " " + message.getPayload().purchaser().getLastName(),
                                message.getPayload().amount(),
                                message.getPayload().id());
                    } catch (Exception e) {
                        log.error("Error sending email", e);
                        throw new ProcessingException("Error sending email", e);
                    }
                }
            } catch (Exception e) {
                log.error("Error processing message: {}", e.getMessage());
                throw new ProcessingException("Error processing PurchaseNotification message. Message received but could not be processed.", e);
            }
        };
    }
}
