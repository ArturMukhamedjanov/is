package islab1.models.DTO;

import java.time.ZonedDateTime;

import islab1.models.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {
    private long id;
    private long creatorId;
    private String name;
    private long coordinatesId;
    private ZonedDateTime creationDate;
    private long personId;
    private long eventId;
    private Double price;
    private TicketType type;
    private Long discount;
    private Double number;
    private String comment;
    private Boolean refundable;
    private long venueId;
}
