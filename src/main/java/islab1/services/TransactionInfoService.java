package islab1.services;

import islab1.auth.services.AuthenticationService;
import islab1.mappers.*;
import islab1.models.*;
import islab1.models.DTO.TransactionInfoDTO;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.models.json.*;
import islab1.repos.TransactionInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionInfoService {

    private final AuthenticationService authenticationService;

    private final TransactionInfoMapper transactionInfoMapper;

    private final TransactionInfoRepo transactionInfoRepo;

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    private final CoordinatesService coordinatesService;
    private final CoordinatesMapper coordinatesMapper;

    private final EventService eventService;
    private final EventMapper eventMapper;

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    private final PersonService personService;
    private final PersonMapper personMapper;

    private final VenueService venueService;
    private final VenueMapper venueMapper;

    private final MinioService minioService;

    public TransactionInfo saveTransactionInfo(TransactionInfo transactionInfo) {
        return transactionInfoRepo.save(transactionInfo);
    }

    public List<TransactionInfo> getAllTransactionInfos() {
        return transactionInfoRepo.findAll();
    }

    public Optional<TransactionInfo> getTransactionInfoById(Long id) {
        return transactionInfoRepo.findById(id);
    }

    public void deleteTransactionInfoById(Long id) {
        transactionInfoRepo.deleteById(id);
    }

    public boolean existsById(Long id) {
        return transactionInfoRepo.existsById(id);
    }

    public List<TransactionInfo> getTransactionInfosByCreator(User user) {
        return transactionInfoRepo.getTransactionInfosByCreator(user);
    }

    public List<TransactionInfoDTO> convertTransactionInfoToDTOs(List<TransactionInfo> transactionInfos) {
        return transactionInfos.stream().map(transactionInfoMapper::mapToDto).collect(Collectors.toList());
    }

    public boolean checkAccess(User user, Long transactionId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Optional<TransactionInfo> transaction = getTransactionInfoById(transactionId);
        return transaction.map(t -> t.getCreator().equals(user)).orElse(false);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionOperationResponse executeTransaction(MultipartFile file, JsonContainer jsonContainer) throws Exception{
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            minioService.uploadFile(fileName, file);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
        Integer totalUploadedObjects = 0;
        try {
            User user = authenticationService.getCurrentUser();
            totalUploadedObjects += processCoordinates(jsonContainer.getCoordinates(), user);
            totalUploadedObjects += processEvents(jsonContainer.getEvents(), user);
            totalUploadedObjects += processLocations(jsonContainer.getLocations(), user);
            totalUploadedObjects += processPersons(jsonContainer.getPersons(), user);
            totalUploadedObjects += processVenues(jsonContainer.getVenues(), user);
            totalUploadedObjects += processTickets(jsonContainer.getTickets(), user);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
        return TransactionOperationResponse.builder()
            .objects(totalUploadedObjects)
            .filename(fileName)
            .build();
    }

    private Coordinates processCoordinates(CoordinatesJson coordinatesJson, User user) throws Exception {
        Coordinates coordinates = coordinatesMapper.fromJson(coordinatesJson);
        coordinates.setCreator(user);
        return coordinatesService.saveCoordinates(coordinates);
    }

    private Integer processCoordinates(List<CoordinatesJson> coordinatesJsonList, User user) throws Exception {
        if(coordinatesJsonList == null){
            return 0;
        }
        Integer processedCount = 0;
        for (CoordinatesJson coordinatesJson : coordinatesJsonList) {
            processCoordinates(coordinatesJson, user);
            processedCount++;
        }
        return processedCount;
    }

    private Event processEvent(EventJson eventJson, User user) throws Exception {
        Event event = eventMapper.fromJson(eventJson);
        event.setCreator(user);
        return eventService.saveEvent(event);
    }

    private Integer processEvents(List<EventJson> eventJsonList, User user) throws Exception {
        if(eventJsonList == null){
            return 0;
        }
        Integer processedCount = 0;
        for (EventJson eventJson : eventJsonList) {
            processEvent(eventJson, user);
            processedCount++;
        }
        return processedCount;
    }

    private Location processLocation(LocationJson locationJson, User user) throws Exception {
        Location location = locationMapper.fromJson(locationJson);
        location.setCreator(user);
        return locationService.saveLocation(location);
    }

    private Integer processLocations(List<LocationJson> locationJsonList, User user) throws Exception {
        if(locationJsonList == null){
            return 0;
        }
        Integer processedCount = 0;
        for (LocationJson locationJson : locationJsonList) {
            processLocation(locationJson, user);
            processedCount++;
        }
        return processedCount;
    }

    private Venue processVenue(VenueJson venueJson, User user) throws Exception {
        Venue venue = venueMapper.fromJson(venueJson);
        venue.setCreator(user);
        return venueService.saveVenue(venue);
    }

    private Integer processVenues(List<VenueJson> venueJsonList, User user) throws Exception {
        if(venueJsonList == null){
            return 0;
        }
        Integer processedCount = 0;
        for (VenueJson venueJson : venueJsonList) {
            processVenue(venueJson, user);
            processedCount++;
        }
        return processedCount;
    }

    private Integer processPersons(List<PersonJson> personJsonList, User user) throws Exception {
        if(personJsonList == null){
            return 0;
        }
        Integer processedCount = 0;
        for (PersonJson personJson : personJsonList) {
            Person person = personMapper.fromJson(personJson);
            person.setCreator(user);
            if (personJson.getLocation().isPresent()) {
                Location location = processLocation(personJson.getLocation().get(), user);
                person.setLocation(location);
                processedCount++;
            } else if (personJson.getLocationId().isEmpty() || !locationService.existById(personJson.getLocationId().get())) {
                throw new Exception("Location not found");
            } else {
                Long locationId = personJson.getLocationId().get();
                Location location = locationService.getLocationById(locationId);
                if(location.getCreator() != user){
                    throw new Exception("cant create person with location from other user!");
                }
                person.setLocation(location);
            }
            personService.savePerson(person);
            processedCount++;
        }
        return processedCount;
    }

    private Integer processTickets(List<TicketJson> ticketJsonList, User user) throws Exception {
        if(ticketJsonList == null){
            return 0;
        }
        Integer processedCount = 0;
        for (TicketJson ticketJson : ticketJsonList) {
            Ticket ticket = ticketMapper.fromJson(ticketJson);
            ticket.setCreator(user);
            if (ticketJson.getCoordinates().isPresent()) {
                Coordinates coordinates = processCoordinates(ticketJson.getCoordinates().get(), user);
                ticket.setCoordinates(coordinates);
                processedCount++;
            } else if (ticketJson.getCoordinatesId().isPresent()) {
                if(!coordinatesService.checkAccess(user, ticketJson.getCoordinatesId().get())){
                    throw new Exception("Cant create ticket with coordinates that dont belongs to you");
                }
                Long coordinatesId = ticketJson.getCoordinatesId().get();
                Coordinates coordinates = coordinatesService.getCoordinatesByIdOpt(coordinatesId)
                        .orElseThrow(() -> new Exception("Coordinates with ID " + coordinatesId + " not found."));
                if(coordinates.getCreator() != user){
                    throw new Exception("Cant create ticket with coordinates that dont belongs to you");
                }
                ticket.setCoordinates(coordinates);
            } else {
                throw new Exception("Coordinates or Coordinates ID must be provided for ticket.");
            }

            // Обрабатываем Person
            if (ticketJson.getPerson().isPresent()) {
                // Если задаются данные Person в JSON
                Person person = personMapper.fromJson(ticketJson.getPerson().get());
                person.setCreator(user);
                Location location = null;
                if(ticketJson.getPerson().get().getLocation().isPresent()){
                    location = processLocation(ticketJson.getPerson().get().getLocation().get(), user);
                } else if (ticketJson.getPerson().get().getLocationId().isPresent()){
                    location = locationService.getLocationByIdOpt(ticketJson.getPerson().get().getLocationId().get())
                        .orElseThrow(() -> new Exception("Location with ID " + ticketJson.getPerson().get().getLocationId().get() + " not found."));
                    if(location.getCreator() != user){
                        throw new Exception("cant create person with location from other user!");
                    }
                }
                if(location == null){
                    throw new Exception("Unable to get location in person");
                }
                person.setLocation(location);
                person = personService.savePerson(person); // Сохраняем или обновляем Person
                ticket.setPerson(person);
                processedCount++;
            } else if (ticketJson.getPersonId().isPresent()) {
                // Если существует только personId
                Long personId = ticketJson.getPersonId().get();
                Person person = personService.getPersonByIdOpt(personId)
                        .orElseThrow(() -> new Exception("Person with ID " + personId + " not found."));
                if(person.getCreator() != user){
                    throw new Exception("Cant create ticket with person that dont belongs to you");
                }
                ticket.setPerson(person);
            } else {
                throw new Exception("Person or Person ID must be provided for ticket.");
            }

            if (ticketJson.getEvent().isPresent()) {
                Event event = processEvent(ticketJson.getEvent().get(), user);
                ticket.setEvent(event);
                processedCount++;
            } else if (ticketJson.getEventId().isPresent()) {
                Long eventId = ticketJson.getEventId().get();
                Event event = eventService.getEventByIdOpt(eventId)
                        .orElseThrow(() -> new Exception("Event with ID " + eventId + " not found."));
                if(event.getCreator() != user){
                    throw new Exception("Cant create ticket with event that dont belongs to you");
                }
                ticket.setEvent(event);
            } else {
                throw new Exception("Event or Event ID must be provided for ticket.");
            }

            if (ticketJson.getVenue().isPresent()) {
                Venue venue = processVenue(ticketJson.getVenue().get(), user);
                ticket.setVenue(venue);
                processedCount++;
            } else if (ticketJson.getVenueId().isPresent()) {
                // Если существует только venueId
                Long venueId = ticketJson.getVenueId().get();
                Venue venue = venueService.getVenueByIdOpt(venueId)
                        .orElseThrow(() -> new Exception("Venue with ID " + venueId + " not found."));
                if(venue.getCreator() != user){
                    throw new Exception("Cant create ticket with venue that dont belongs to you");
                }
                ticket.setVenue(venue);
            } else {
                throw new Exception("Venue or Venue ID must be provided for ticket.");
            }
            ticketService.saveTicket(ticket);
            processedCount++;
        }

        return processedCount;
    }
}
