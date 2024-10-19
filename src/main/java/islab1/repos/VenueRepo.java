package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Venue;
import islab1.models.auth.User;

@Repository
public interface VenueRepo extends JpaRepository<Venue, Long>{

    List<Venue> getVenuesByCreator(User user);
    
}
