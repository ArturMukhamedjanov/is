package islab1.models;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ticket")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id;  // Значение генерируется автоматически, уникальное

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    @NotNull(message = "Creator cannot be null.")
    private User creator;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be null or an empty string.")
    private String name;  // Поле не может быть null, строка не может быть пустой

    @ManyToOne(optional = false)
    @JoinColumn(name = "coordinates_id")
    @NotNull(message = "Coordinates cannot be null.")
    private Coordinates coordinates;  // Поле не может быть null

    @Column(nullable = false, updatable = false)
    @NotNull(message = "Creation date cannot be null.")
    private ZonedDateTime creationDate;  // Поле не может быть null, генерируется автоматически

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    @NotNull(message = "Person cannot be null.")
    private Person person;  // Поле не может быть null

    @ManyToOne(optional = true)
    @JoinColumn(name = "event_id")
    private Event event;  // Поле может быть null

    @Positive(message = "Price must be greater than 0.")
    @Column(nullable = false)
    @NotNull(message = "Price cannot be null.")
    private Double price;

    @Enumerated(EnumType.STRING)
    private TicketType type;  // Поле может быть null

    @Min(value = 1, message = "Discount must be greater than 0.")
    @Max(value = 100, message = "Discount must be less than or equal to 100.")
    @Column(nullable = true)
    private Long discount;

    @Positive(message = "Number must be greater than 0.")
    @Column(nullable = true)
    private Double number;

    @NotBlank(message = "Comment cannot be null or an empty string.")
    @Column(nullable = false)
    private String comment;  // Поле не может быть null

    @NotNull(message = "Refundable cannot be null.")
    @Column(nullable = false)
    private Boolean refundable;  // Поле не может быть null

    @ManyToOne(optional = false)
    @JoinColumn(name = "venue_id")
    @NotNull(message = "Venue cannot be null.")
    private Venue venue;  // Поле не может быть null

    // Метод для автоматической установки creationDate при создании записи
    @PrePersist
    protected void onCreate() {
        this.creationDate = ZonedDateTime.now();
    }

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

    public void setName(String name) throws ConvertionException {
        if (name == null || name.trim().isEmpty()) {
            throw new ConvertionException("Name cannot be null or an empty string.");
        }
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) throws ConvertionException {
        if (coordinates == null) {
            throw new ConvertionException("Coordinates cannot be null.");
        }
        this.coordinates = coordinates;
    }

    public void setPrice(Double price) throws ConvertionException {
        if (price == null || price <= 0) {
            throw new ConvertionException("Price must be greater than 0.");
        }
        this.price = price;
    }

    public void setDiscount(Long discount) throws ConvertionException {
        if (discount != null && (discount <= 0 || discount > 100)) {
            throw new ConvertionException("Discount must be greater than 0 and less than or equal to 100.");
        }
        this.discount = discount;
    }

    public void setNumber(Double number) throws ConvertionException {
        if (number != null && number <= 0) {
            throw new ConvertionException("Number must be greater than 0.");
        }
        this.number = number;
    }

    public void setComment(String comment) throws ConvertionException {
        if (comment == null || comment.trim().isEmpty()) {
            throw new ConvertionException("Comment cannot be null or an empty string.");
        }
        this.comment = comment;
    }

    public void setRefundable(Boolean refundable) throws ConvertionException {
        if (refundable == null) {
            throw new ConvertionException("Refundable cannot be null.");
        }
        this.refundable = refundable;
    }

    public void setVenue(Venue venue) throws ConvertionException {
        if (venue == null) {
            throw new ConvertionException("Venue cannot be null.");
        }
        this.venue = venue;
    }
}