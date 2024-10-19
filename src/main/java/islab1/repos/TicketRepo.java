package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Ticket;
import islab1.models.auth.User;

@Repository
public interface  TicketRepo extends JpaRepository<Ticket, Long>{

    List<Ticket> getTicketsByCreator(User user);
    
}
