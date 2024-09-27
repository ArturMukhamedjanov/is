package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.VenueDTO;
import islab1.models.Venue;
import islab1.models.auth.User;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class VenueMapper {

    private final UserRepo userRepo;
    
    public Venue toEntity(VenueDTO dto) throws ConvertionException {
        try{
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            if(creator == null){
                throw new ConvertionException("Creator was not found");
            }
            Venue venue = new Venue();
            venue.setId(dto.getId());
            venue.setCreator(creator);
            venue.setName(dto.getName());
            venue.setCapacity(dto.getCapacity());
            venue.setType(dto.getType());
            return venue;
        }catch(EntityNotFoundException e){
            throw new ConvertionException(e.getMessage());
        }
    }

    public VenueDTO toDto(Venue venue) {
        VenueDTO dto = new VenueDTO();
        dto.setId(venue.getId());
        dto.setCreatorId(venue.getCreator().getId()); 
        dto.setName(venue.getName());
        dto.setCapacity(venue.getCapacity());
        dto.setType(venue.getType());
        return dto;
    }

}
