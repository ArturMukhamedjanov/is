package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Event;
import islab1.models.auth.User;

@Repository
public interface EventRepo extends JpaRepository<Event, Long>{

    List<Event> getEventsByCreator(User user);
    
}
