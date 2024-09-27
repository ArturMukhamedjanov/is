package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.TicketDTO;
import islab1.models.Ticket;
import islab1.models.auth.User;
import islab1.models.Coordinates;
import islab1.models.Person;
import islab1.models.Event;
import islab1.models.Venue;
import islab1.repos.UserRepo;
import islab1.repos.CoordinatesRepo;
import islab1.repos.PersonRepo;
import islab1.repos.EventRepo;
import islab1.repos.VenueRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TicketMapper {

    private final UserRepo userRepo;
    private final CoordinatesRepo coordinatesRepo;
    private final PersonRepo personRepo;
    private final EventRepo eventRepo;
    private final VenueRepo venueRepo;

    public Ticket toEntity(TicketDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            Coordinates coordinates = coordinatesRepo.getReferenceById(dto.getCoordinatesId());
            Person person = personRepo.getReferenceById(dto.getPersonId());
            Event event = eventRepo.getReferenceById(dto.getEventId());
            Venue venue = venueRepo.getReferenceById(dto.getVenueId());

            if (creator == null || coordinates == null || person == null || venue == null || event == null) {
                throw new ConvertionException("Required entity not found");
            }

            Ticket ticket = new Ticket();
            ticket.setId(dto.getId());
            ticket.setCreator(creator);
            ticket.setName(dto.getName());
            ticket.setCoordinates(coordinates);
            ticket.setCreationDate(dto.getCreationDate());
            ticket.setPerson(person);
            ticket.setEvent(event);
            ticket.setPrice(dto.getPrice());
            ticket.setType(dto.getType());
            ticket.setDiscount(dto.getDiscount());
            ticket.setNumber(dto.getNumber());
            ticket.setComment(dto.getComment());
            ticket.setRefundable(dto.getRefundable());
            ticket.setVenue(venue);
            return ticket;
        } catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public TicketDTO toDto(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setCreatorId(ticket.getCreator().getId());
        dto.setName(ticket.getName());
        dto.setCoordinatesId(ticket.getCoordinates().getId());
        dto.setCreationDate(ticket.getCreationDate());
        dto.setPersonId(ticket.getPerson().getId());
        dto.setEventId(ticket.getEvent().getId());
        dto.setPrice(ticket.getPrice());
        dto.setType(ticket.getType());
        dto.setDiscount(ticket.getDiscount());
        dto.setNumber(ticket.getNumber());
        dto.setComment(ticket.getComment());
        dto.setRefundable(ticket.getRefundable());
        dto.setVenueId(ticket.getVenue().getId());
        return dto;
    }
}
