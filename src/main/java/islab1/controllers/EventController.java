package islab1.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.services.AuthenticationService;
import islab1.exceptions.ConvertionException;
import islab1.models.DTO.EventDTO;
import islab1.models.Event;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.EventService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/events")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventController {

    private final EventService eventService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        User user = authenticationService.getCurrentUser();
        List<Event> events = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            events = eventService.getAllEvents();
        } else {
            events = eventService.getEventsByUser(user);
        }
        List<EventDTO> eventDTOs = eventService.convertEventsToDTOs(events);
        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!eventService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Event not found").body(null);
        }
        if (!eventService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Event event = eventService.getEventById(id);
        EventDTO eventDTO = eventService.convertEventToDTO(event);
        return ResponseEntity.ok(eventDTO);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        User user = authenticationService.getCurrentUser();
        eventDTO.setCreatorId(user.getId());
        Event event;
        try {
            event = eventService.createEvent(eventDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        eventDTO = eventService.convertEventToDTO(event);
        return ResponseEntity.ok(eventDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        User user = authenticationService.getCurrentUser();
        if (!eventService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Event not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !eventService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Event event;
        try {
            event = eventService.updateEvent(id, eventDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        eventDTO = eventService.convertEventToDTO(event);
        return ResponseEntity.ok(eventDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!eventService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Event not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !eventService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try {
            eventService.deleteEvent(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete entity because it is used in other entity").body(null);
        }
        return ResponseEntity.ok().build();
    }
}
