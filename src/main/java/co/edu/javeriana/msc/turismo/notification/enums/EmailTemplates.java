package co.edu.javeriana.msc.turismo.notification.enums;


import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

@Getter
public enum EmailTemplates {
    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment successfully processed"),
    ORDER_CONFIRMATION("order-confirmation.html", "Order successfully processed"),
    PAYMENT_REJECTED("payment-rejected.html", "Payment rejected");

    private final String template;
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
