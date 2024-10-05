package co.edu.javeriana.msc.turismo.payment_microservice.payment.enums;

public enum PaymentStatus {
    ACEPTADA(1),
    RECHAZADA(2);

    private final int codigo;

    PaymentStatus(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}