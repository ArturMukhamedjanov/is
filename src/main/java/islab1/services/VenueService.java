package islab1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.VenueMapper;
import islab1.models.DTO.VenueDTO;
import islab1.models.Venue;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.VenueRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueMapper venueMapper;
    private final VenueRepo venueRepo;

    public List<Venue> getAllVenues() {
        return venueRepo.findAll();
    }

    public List<Venue> getVenuesByUser(User user) {
        return venueRepo.getVenuesByCreator(user);
    }

    public Venue getVenueById(Long id) {
        return venueRepo.getById(id);
    }

    public boolean existById(Long id) {
        return venueRepo.existsById(id);
    }

    public Venue createVenue(VenueDTO venueDTO) throws ConvertionException {
        Venue venue = venueMapper.toEntity(venueDTO);
        return venueRepo.save(venue);
    }

    public Venue updateVenue(Long id, VenueDTO venueDTO) throws ConvertionException, EntityNotFoundException {
        Venue newVenue = venueMapper.toEntity(venueDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no venue with id " + id);
        }
        Venue venue = getVenueById(id);

        newVenue.setId(id);
        newVenue.setCreator(venue.getCreator());
        venueRepo.save(newVenue);
        return newVenue;
    }

    public void deleteVenue(Long id) {
        try{
            venueRepo.deleteById(id);
        }catch (Exception e){
            throw e;
        }
    }

    public boolean checkAccess(User user, Long venueId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Venue venue = getVenueById(venueId);
        return venue != null && venue.getCreator().equals(user);
    }

    public List<VenueDTO> convertVenuesToDTOs(List<Venue> venues) {
        return venues.stream()
            .map(venueMapper::toDto)
            .collect(Collectors.toList());
    }

    public VenueDTO convertVenueToDTO(Venue venue) {
        return venueMapper.toDto(venue);
    }
}
