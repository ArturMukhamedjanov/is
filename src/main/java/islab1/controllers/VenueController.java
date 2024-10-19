package islab1.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.services.AuthenticationService;
import islab1.exceptions.ConvertionException;
import islab1.models.DTO.VenueDTO;
import islab1.models.Venue;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.VenueService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/venues")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VenueController {

    private final VenueService venueService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        User user = authenticationService.getCurrentUser();
        List<Venue> venues = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            venues = venueService.getAllVenues();
        } else {
            venues = venueService.getVenuesByUser(user);
        }
        List<VenueDTO> venueDTOs = venueService.convertVenuesToDTOs(venues);
        return ResponseEntity.ok(venueDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!venueService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Venue with that id does not exist").body(null);
        }
        if (!venueService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Venue venue = venueService.getVenueById(id);
        VenueDTO venueDTO = venueService.convertVenueToDTO(venue);
        return ResponseEntity.ok(venueDTO);
    }

    @PostMapping
    public ResponseEntity<VenueDTO> createVenue(@RequestBody VenueDTO venueDTO) {
        User user = authenticationService.getCurrentUser();
        venueDTO.setCreatorId(user.getId());
        Venue venue;
        try {
            venue = venueService.createVenue(venueDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        venueDTO = venueService.convertVenueToDTO(venue);
        return ResponseEntity.ok(venueDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueDTO> updateVenue(@PathVariable Long id, @RequestBody VenueDTO venueDTO) {
        User user = authenticationService.getCurrentUser();
        if (!venueService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Venue with that id does not exist").body(null);
        }
        if (user.getRole() != Role.ADMIN && !venueService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Venue venue;
        try {
            venue = venueService.updateVenue(id, venueDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        venueDTO = venueService.convertVenueToDTO(venue);
        return ResponseEntity.ok(venueDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!venueService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Venue with that id does not exist").body(null);
        }
        if (user.getRole() != Role.ADMIN && !venueService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try {
            venueService.deleteVenue(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete entity because it is used in other entity").body(null);
        }
        return ResponseEntity.ok().build();
    }
}
