package islab1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.EventMapper;
import islab1.models.DTO.EventDTO;
import islab1.models.Event;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.EventRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;
    private final EventRepo eventRepo;

    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    public List<Event> getEventsByUser(User user) {
        return eventRepo.getEventsByCreator(user);
    }

    public Event getEventById(Long id) {
        return eventRepo.getById(id);
    }

    public Optional<Event> getEventByIdOpt(Long id) {
        return eventRepo.findById(id);
    }

    public boolean existById(Long id) {
        return eventRepo.existsById(id);
    }

    public Event createEvent(EventDTO eventDTO) throws ConvertionException {
        Event event = eventMapper.toEntity(eventDTO);
        return eventRepo.save(event);
    }

    public Event updateEvent(Long id, EventDTO eventDTO) throws ConvertionException, EntityNotFoundException {
        Event newEvent = eventMapper.toEntity(eventDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no event with id " + id);
        }
        Event event = getEventById(id);
        
        newEvent.setId(id);
        newEvent.setCreator(event.getCreator());
        eventRepo.save(newEvent);
        return newEvent;
    }

    public void deleteEvent(Long id) {
        try{
            eventRepo.deleteById(id);
        }catch(Exception e){
            throw e;
        }
    }

    public boolean checkAccess(User user, Long eventId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Event event = getEventById(eventId);
        return event != null && event.getCreator().equals(user);
    }

    public List<EventDTO> convertEventsToDTOs(List<Event> events) {
        return events.stream()
            .map(eventMapper::toDto)
            .collect(Collectors.toList());
    }

    public EventDTO convertEventToDTO(Event event) {
        return eventMapper.toDto(event);
    }

    public Event saveEvent(Event event) {
        return eventRepo.save(event);
    }
}
