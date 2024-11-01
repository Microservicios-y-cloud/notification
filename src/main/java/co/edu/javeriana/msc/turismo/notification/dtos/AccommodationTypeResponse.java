package co.edu.javeriana.msc.turismo.notification.dtos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accommodationTypes")
public record AccommodationTypeResponse(
        @Id
        Long accommodationTypeId,
        String name
) {
}