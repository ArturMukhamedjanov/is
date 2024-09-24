package islab1.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Event;

@Repository
public interface EventRepo extends JpaRepository<Event, Long>{
    
}
