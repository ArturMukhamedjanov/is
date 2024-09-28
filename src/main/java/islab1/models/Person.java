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

    @Column(nullable = true, columnDefinition = "BIGINT CHECK (height > 0)")  // Поле может быть null, значение должно быть больше 0
    private Long height;

    @Column(length = 33, columnDefinition = "VARCHAR(33) CHECK (LENGTH(passport_id) >= 10)")  // Поле может быть null, длина строки должна быть от 10 до 33 символов
    private String passportID;

    public void setCreator(User creator) throws ConvertionException{
        if(creator == null){
            throw new ConvertionException("Height cant be less then 0");
        }
        this.creator = creator;
    }

    public void setHeight(Long height) throws ConvertionException{
        if(height != null && height < 0){
            throw new ConvertionException("Height cant be less then 0");
        }
        this.height = height;
    }

    public void setLocation(Location location) throws ConvertionException{
        if(location == null){
            throw new ConvertionException("Location cant be null");
        }
        this.location = location;
    }

    public void setPassportID(String passportID) throws ConvertionException{
        if(passportID != null && (passportID.length() > 33 || passportID.length() < 10)){
            throw new ConvertionException("passportID should be from 10 to 33 symbols");
        }
        this.passportID = passportID;
    }
}