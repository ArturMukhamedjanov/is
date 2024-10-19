package islab1.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import islab1.exceptions.ConvertionException;
import islab1.models.auth.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coordinates")
@Getter
@Setter
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    //@Column(nullable = false, columnDefinition = "DOUBLE CHECK (x <= 182)")
    @Column(nullable = false)
    @Min(value = 0, message = "X must be greater than or equal to 0.")
    @Max(value = 182, message = "X must be less than or equal to 182.")
    private Double x;

    //@Column(nullable = false, columnDefinition = "BIGINT CHECK (y <= 329)")
    @Column(nullable = false)
    @Min(value = 0, message = "Y must be greater than or equal to 0.")
    @Max(value = 329, message = "Y must be less than or equal to 329.")
    private Long y;

    public void setCreator(User creator) throws ConvertionException{
        if(creator == null){
            throw new ConvertionException("Creator not found");
        }
        this.creator = creator;
    }

    public void setX(Double x) throws ConvertionException {
        if (x != null && x > 182) {
            throw new ConvertionException("X must be less than or equal to 182.");
        }
        this.x = x;
    }

    public void setY(Long y) throws ConvertionException {
        if (y != null && y > 329) {
            throw new ConvertionException("Y must be less than or equal to 329.");
        }
        this.y = y;
    }
}
