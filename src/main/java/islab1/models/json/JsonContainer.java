package islab1.models.json;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JsonContainer {

    private List<CoordinatesJson> coordinates;
    private List<EventJson> events;
    private List<LocationJson> locations;
    private List<PersonJson> persons;
    private List<TicketJson> tickets;
    private List<VenueJson> venues;
}
