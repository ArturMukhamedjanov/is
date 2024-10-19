package islab1.models.DTO;

import java.time.ZonedDateTime;

import islab1.models.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {
    private long id;
    private Long creatorId;
    private String name;
    private Long coordinatesId;
    private ZonedDateTime creationDate;
    private Long personId;
    private Long eventId;
    private Double price;
    private TicketType type;
    private Long discount;
    private Double number;
    private String comment;
    private Boolean refundable;
    private Long venueId;
}
