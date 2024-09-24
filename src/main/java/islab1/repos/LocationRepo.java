package islab1.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Location;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long>{
    
}
