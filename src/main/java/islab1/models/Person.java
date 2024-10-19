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
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)  // Поле может быть null
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)  // Поле может быть null
    private Color hairColor;

    @ManyToOne(optional = false)  // Поле не может быть null
    @JoinColumn(name = "location_id")
    private Location location;

    // Поле может быть null, значение должно быть больше 0
    @Min(value = 1, message = "Height must be greater than 0")
    @Column(nullable = true)
    private Long height;

    // Длина строки должна быть от 10 до 33 символов
    @Size(min = 10, max = 33, message = "Passport ID must be between 10 and 33 characters")
    @Column(length = 33, nullable = true)
    private String passportID;

    public void setCreator(User creator) throws ConvertionException {
        if (creator == null) {
            throw new ConvertionException("Creator cannot be null.");
        }
        this.creator = creator;
    }

    public void setHeight(Long height) throws ConvertionException {
        if (height != null && height <= 0) {
            throw new ConvertionException("Height must be greater than 0.");
        }
        this.height = height;
    }

    public void setLocation(Location location) throws ConvertionException {
        if (location == null) {
            throw new ConvertionException("Location cannot be null.");
        }
        this.location = location;
    }

    public void setPassportID(String passportID) throws ConvertionException {
        if (passportID != null && (passportID.length() > 33 || passportID.length() < 10)) {
            throw new ConvertionException("Passport ID must be between 10 and 33 characters.");
        }
        this.passportID = passportID;
    }
}
