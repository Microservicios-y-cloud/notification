package co.edu.javeriana.msc.turismo.service_rating_microservice.queue.dtos;

import co.edu.javeriana.msc.turismo.service_rating_microservice.enums.ServiceType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Document(collection = "services")
public record SuperService(
        @Id
        @NotNull(message = "The id of the service is required")
        Long id,
        @NotNull(message = "The id of the supplier is required")
        String createdBy,
        @NotNull(message = "The destination is required")
        LocationResponse destination,
        @NotNull(message = "The name of the service is required")
        String name,
        @NotNull(message = "The description of the service is required")
        String description,
        @NotNull(message = "The unit price of the service is required")
        BigDecimal unitValue,
        @NotNull(message = "The start date of the service is required")
        LocalDateTime startDate,
        @NotNull(message = "The end date of the service is required")
        LocalDateTime endDate,
        @NotNull
        ServiceType serviceType,
        FoodTypeResponse foodType,
        AccommodationTypeResponse accommodationType,
        Integer capacity,
        TransportTypeResponse transportationType,
        LocationResponse origin,
        String company
) {

}
