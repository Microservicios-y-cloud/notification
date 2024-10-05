package co.edu.javeriana.msc.turismo.notification.dtos;

import co.edu.javeriana.msc.turismo.notification.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.notification.enums.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record PurchaseNotification(
        String id,
        LocalDateTime creationDate,
        LocalDateTime lastUpdate,
        Customer purchaser,
        Status orderStatus,
        PaymentStatus paymentStatus,
        List<PurchaseItem> purchaseItems,
        BigDecimal amount
) implements Serializable {
}
