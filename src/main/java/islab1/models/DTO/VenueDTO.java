package islab1.models.DTO;

import islab1.models.VenueType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueDTO {
    private long id;
    private long creatorId;
    private String name;
    private Integer capacity;
    private VenueType type;
}
