package islab1.models.DTO;

import islab1.models.VenueType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueDTO {
    private Long id;
    private Integer creatorId;
    private String name;
    private Integer capacity;
    private VenueType type;
}
