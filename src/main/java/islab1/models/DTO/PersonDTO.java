package islab1.models.DTO;

import islab1.models.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    private long id;
    private Color eyeColor;
    private Color hairColor;
    private LocationDTO location;
    private Long height;
    private String passportID;

    // Getters and setters
}
