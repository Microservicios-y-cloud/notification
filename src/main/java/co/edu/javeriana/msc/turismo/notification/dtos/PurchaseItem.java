package co.edu.javeriana.msc.turismo.notification.dtos;

public record PurchaseItem(
        Double subtotal,
        Integer quantity,
        SuperService service
) {
}
