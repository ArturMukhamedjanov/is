package islab1.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Venue;

@Repository
public interface VenueRepo extends JpaRepository<Venue, Long>{
    
}
