package islab1.models.json;

import islab1.models.Color;
import islab1.models.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Optional;

@Getter
@Setter
@Builder
public class PersonJson {

    private Color eyeColor;

    private Color hairColor;

    private Optional<LocationJson> location;

    private Optional<Long> locationId;

    @Min(value = 1, message = "Height must be greater than 0")
    private Long height;

    @Size(min = 10, max = 33, message = "Passport ID must be between 10 and 33 characters")
    private String passportID;
}
