package islab1.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Ticket;

@Repository
public interface  TicketRepo extends JpaRepository<Ticket, Long>{
    
}
