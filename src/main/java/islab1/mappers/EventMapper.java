package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.EventDTO;
import islab1.models.Event;
import islab1.models.auth.User;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EventMapper {

    private final UserRepo userRepo;

    public Event toEntity(EventDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            if (creator == null) {
                throw new ConvertionException("Creator was not found");
            }
            Event event = new Event();
            event.setId(dto.getId());
            event.setCreator(creator);
            event.setName(dto.getName());
            event.setMinAge(dto.getMinAge());
            event.setEventType(dto.getEventType());
            return event;
        } catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public EventDTO toDto(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setCreatorId(event.getCreator().getId());
        dto.setName(event.getName());
        dto.setMinAge(event.getMinAge());
        dto.setEventType(event.getEventType());
        return dto;
    }
}