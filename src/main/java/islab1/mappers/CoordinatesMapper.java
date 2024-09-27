package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.CoordinatesDTO;
import islab1.models.Coordinates;
import islab1.models.auth.User;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CoordinatesMapper {

    private final UserRepo userRepo;

    public Coordinates toEntity(CoordinatesDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            if (creator == null) {
                throw new ConvertionException("Creator was not found");
            }
            if (dto.getX()  > 182){
                throw new ConvertionException("Field X cant be mpre then 182");
            }
            if (dto.getY()  > 329){
                throw new ConvertionException("Field Y cant be mpre then 329");
            }
            Coordinates coordinates = new Coordinates();
            coordinates.setId(dto.getId());
            coordinates.setCreator(creator);
            coordinates.setX(dto.getX());
            coordinates.setY(dto.getY());
            return coordinates;
        } catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public CoordinatesDTO toDto(Coordinates coordinates) {
        CoordinatesDTO dto = new CoordinatesDTO();
        dto.setId(coordinates.getId());
        dto.setCreatorId(coordinates.getCreator().getId());
        dto.setX(coordinates.getX());
        dto.setY(coordinates.getY());
        return dto;
    }
}
