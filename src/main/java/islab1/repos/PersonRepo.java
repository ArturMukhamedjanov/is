package islab1.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long>{
    
}
