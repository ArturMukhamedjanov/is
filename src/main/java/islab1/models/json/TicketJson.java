package islab1.models.json;

import islab1.models.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Optional;

@Getter
@Setter
@Builder
public class TicketJson {


    @NotBlank(message = "Name cannot be null or an empty string.")
    private String name;

    private Optional<CoordinatesJson> coordinates;
    private Optional<Long> coordinatesId;

    private Optional<PersonJson> person;
    private Optional<Long> personId;

    private Optional<EventJson> event;
    private Optional<Long> eventId;

    @Positive(message = "Price must be greater than 0.")
    @NotNull(message = "Price cannot be null.")
    private Double price;

    @NotNull(message = "Type cannot be null.")
    private TicketType type;  // Поле может быть null

    @Min(value = 1, message = "Discount must be greater than 0.")
    @Max(value = 100, message = "Discount must be less than or equal to 100.")
    private Long discount;

    @Positive(message = "Number must be greater than 0.")
    private Double number;

    @NotBlank(message = "Comment cannot be null or an empty string.")
    private String comment;  // Поле не может быть null

    @NotNull(message = "Refundable cannot be null.")
    private Boolean refundable;  // Поле не может быть null

    private Optional<VenueJson> venue;  // Поле не может быть null
    private Optional<Long> venueId;


}
