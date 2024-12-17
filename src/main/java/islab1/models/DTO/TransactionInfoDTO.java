package islab1.models.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TransactionInfoDTO {
    private Long id;
    private Long userId;
    private Integer addedObjects;
    private Boolean successful;
    private String filename;
}
