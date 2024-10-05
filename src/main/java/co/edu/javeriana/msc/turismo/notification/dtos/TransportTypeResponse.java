package co.edu.javeriana.msc.turismo.notification.dtos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transportTypes")
public record TransportTypeResponse(
        @Id
        Long transportTypeId,
        String name
) {
}