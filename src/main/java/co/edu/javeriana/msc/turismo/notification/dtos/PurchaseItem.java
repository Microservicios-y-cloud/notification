package co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos;

public record PurchaseItem(
        Double subtotal,
        Integer quantity,
        SuperService service
) {
}
