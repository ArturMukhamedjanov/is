package islab1.models.json;

import javax.validation.constraints.NotNull;

import islab1.models.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventJson {
    @NotNull
    private String name;
    @NotNull
    private Integer minAge;
    private EventType eventType;
}
