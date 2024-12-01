package islab1.models.json;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationJson {
    private long x;

    private double y;

    private int z;

    private String name;
}
