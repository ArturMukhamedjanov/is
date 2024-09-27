package islab1.models.DTO;

import islab1.models.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {
    private long id;
    private long creatorId;
    private String name;
    private Integer minAge;
    private EventType eventType;
}
