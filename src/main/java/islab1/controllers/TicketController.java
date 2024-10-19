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
import islab1.models.DTO.TicketDTO;
import islab1.models.Ticket;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.CoordinatesService;
import islab1.services.EventService;
import islab1.services.PersonService;
import islab1.services.TicketService;
import islab1.services.VenueService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TicketController {

    private final TicketService ticketService;
    private final AuthenticationService authenticationService;
    private final CoordinatesService coordinatesService;
    private final PersonService personService;
    private final EventService eventService;
    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        User user = authenticationService.getCurrentUser();
        List<Ticket> tickets = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            tickets = ticketService.getAllTickets();
        } else {
            tickets = ticketService.getTicketsByUser(user);
        }
        List<TicketDTO> ticketDTOs = ticketService.convertTicketsToDTOs(tickets);
        return ResponseEntity.ok(ticketDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!ticketService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Ticket with that id does not exist").body(null);
        }
        if (!ticketService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Ticket ticket = ticketService.getTicketById(id);
        TicketDTO ticketDTO = ticketService.convertTicketToDTO(ticket);
        return ResponseEntity.ok(ticketDTO);
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        User user = authenticationService.getCurrentUser();
        ResponseEntity<TicketDTO> validationResults = validateLinkedObjects(user, ticketDTO);
        if (validationResults != null) {
            return validationResults;
        }
        ticketDTO.setCreatorId(user.getId());
        Ticket ticket;
        try {
            ticket = ticketService.createTicket(ticketDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        ticketDTO = ticketService.convertTicketToDTO(ticket);
        return ResponseEntity.ok(ticketDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        User user = authenticationService.getCurrentUser();
        ResponseEntity<TicketDTO> validationResults = validateLinkedObjects(user, ticketDTO);
        if (validationResults != null) {
            return validationResults;
        }
        if (!ticketService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Ticket with that id does not exist").body(null);
        }
        if (user.getRole() != Role.ADMIN && !ticketService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Ticket ticket;
        try {
            ticket = ticketService.updateTicket(id, ticketDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        ticketDTO = ticketService.convertTicketToDTO(ticket);
        return ResponseEntity.ok(ticketDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!ticketService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Ticket with that id does not exist").body(null);
        }
        if (user.getRole() != Role.ADMIN && !ticketService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try {
            ticketService.deleteTicket(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete ticket, it is linked to other entities").body(null);
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<TicketDTO> validateLinkedObjects(User user, TicketDTO ticketDTO) {
        if (!coordinatesService.existById(ticketDTO.getCoordinatesId())) {
            return ResponseEntity.status(400).header("ErrMessage", "Coordinates with that id do not exist").body(null);
        }
        if (coordinatesService.getCoordinatesById(ticketDTO.getCoordinatesId()).getCreator() != user) {
            return ResponseEntity.status(400).header("ErrMessage", "Cannot link object with coordinates not created by you").body(null);
        }
        
        if (!personService.existById(ticketDTO.getPersonId())) {
            return ResponseEntity.status(400).header("ErrMessage", "Person with that id does not exist").body(null);
        }
        if (personService.getPersonById(ticketDTO.getPersonId()).getCreator() != user) {
            return ResponseEntity.status(400).header("ErrMessage", "Cannot link object with person not created by you").body(null);
        }

        if(ticketDTO.getEventId() != null){
            if (!eventService.existById(ticketDTO.getEventId())) {
                return ResponseEntity.status(400).header("ErrMessage", "Event with that id does not exist").body(null);
            }
            if (eventService.getEventById(ticketDTO.getEventId()).getCreator() != user) {
                return ResponseEntity.status(400).header("ErrMessage", "Cannot link object with event not created by you").body(null);
            }
        }
        if (!venueService.existById(ticketDTO.getVenueId())) {
            return ResponseEntity.status(400).header("ErrMessage", "Venue with that id does not exist").body(null);
        }
        if (venueService.getVenueById(ticketDTO.getVenueId()).getCreator() != user) {
            return ResponseEntity.status(400).header("ErrMessage", "Cannot link object with venue not created by you").body(null);
        }

        return null;
    }
}
