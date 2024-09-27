package islab1.models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {
    private long id;
    private long creatorId;
    private long x;
    private double y;
    private int z;
    private String name;
}
