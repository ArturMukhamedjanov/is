package islab1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.TicketMapper;
import islab1.models.DTO.TicketDTO;
import islab1.models.Ticket;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.TicketRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketMapper ticketMapper;
    private final TicketRepo ticketRepo;

    public List<Ticket> getAllTickets() {
        return ticketRepo.findAll();
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepo.getTicketsByCreator(user);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepo.getById(id);
    }

    public Optional<Ticket> getTicketByIdOpt(Long id){
        return ticketRepo.findById(id);
    }

    public boolean existById(Long id) {
        return ticketRepo.existsById(id);
    }

    public Ticket createTicket(TicketDTO ticketDTO) throws ConvertionException {
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        return ticketRepo.save(ticket);
    }

    public Ticket updateTicket(Long id, TicketDTO ticketDTO) throws ConvertionException, EntityNotFoundException {
        Ticket newTicket = ticketMapper.toEntity(ticketDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no ticket with id " + id);
        }
        Ticket ticket = getTicketById(id);
        
        newTicket.setId(id);
        newTicket.setCreator(ticket.getCreator());
        ticketRepo.save(newTicket);
        return newTicket;
    }

    public void deleteTicket(Long id) {
        ticketRepo.deleteById(id);
    }

    public boolean checkAccess(User user, Long ticketId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Ticket ticket = getTicketById(ticketId);
        return ticket != null && ticket.getCreator().equals(user);
    }

    public List<TicketDTO> convertTicketsToDTOs(List<Ticket> tickets) {
        return tickets.stream()
            .map(ticketMapper::toDto)
            .collect(Collectors.toList());
    }

    public Ticket saveTicket(Ticket ticket){
        return ticketRepo.save(ticket);
    }

    public TicketDTO convertTicketToDTO(Ticket ticket) {
        return ticketMapper.toDto(ticket);
    }
}
