package islab1.models.DTO;

import java.time.ZonedDateTime;

import islab1.models.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {
    private long id;
    private String name;
    private CoordinatesDTO coordinates;
    private ZonedDateTime creationDate;
    private PersonDTO person;
    private EventDTO event;
    private Double price;
    private TicketType type;
    private Long discount;
    private Double number;
    private String comment;
    private Boolean refundable;
    private VenueDTO venue;
}
