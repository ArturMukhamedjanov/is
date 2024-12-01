package islab1.models;

import islab1.models.auth.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "transaction_info")
@Getter
@Setter
public class TransactionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;  // Значение генерируется автоматически, уникальное

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    @NotNull(message = "Creator cannot be null.")
    private User creator;

    @Min(value = 1, message = "Height must be greater than 0")
    @Column(nullable = true)
    private Integer addedObjects;

    @Column(nullable = false)
    private Boolean successful;
}
