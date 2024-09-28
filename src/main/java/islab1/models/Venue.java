package islab1.models;

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

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "INTEGER CHECK (capacity > 0)")  // Поле не может быть null, значение больше 0
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VenueType type;

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

    public void setCapacity(Integer capacity) throws ConvertionException {
        if (capacity == null || capacity <= 0) {
            throw new ConvertionException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
    }

    public void setType(VenueType type) throws ConvertionException {
        if (type == null) {
            throw new ConvertionException("Type cannot be null.");
        }
        this.type = type;
    }
}