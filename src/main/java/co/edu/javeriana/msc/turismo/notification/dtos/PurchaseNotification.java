package co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos;

import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;

import java.io.Serializable;
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
        Double amount
) implements Serializable {
}
