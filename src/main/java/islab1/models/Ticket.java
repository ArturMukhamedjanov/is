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
import javax.persistence.Table;

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
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime creationDate = ZonedDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    private TicketType type;

    @Column
    private Long discount;

    @Column
    private Double number;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Boolean refundable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;
}