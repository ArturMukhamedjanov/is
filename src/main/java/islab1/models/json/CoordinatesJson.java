package islab1.models.json;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
public class CoordinatesJson {

    @Min(value = 0, message = "X must be greater than or equal to 0.")
    @Max(value = 182, message = "X must be less than or equal to 182.")
    private Double x;

    @Min(value = 0, message = "Y must be greater than or equal to 0.")
    @Max(value = 329, message = "Y must be less than or equal to 329.")
    private Long y;
}

