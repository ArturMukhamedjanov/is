package islab1.models.json;

import islab1.models.VenueType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VenueJson {
    private String name;

    private Integer capacity;

    private VenueType type;
}
