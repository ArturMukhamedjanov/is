package islab1.mappers;

import islab1.models.DTO.TransactionInfoDTO;
import islab1.models.TransactionInfo;
import org.springframework.stereotype.Component;

@Component
public class TransactionInfoMapper {

    public TransactionInfoDTO mapToDto(TransactionInfo transaction) {
        return TransactionInfoDTO.builder()
                .id(transaction.getId())
                .userId(transaction.getCreator().getId())
                .addedObjects(transaction.getAddedObjects())
                .successful(transaction.getSuccessful())
                .build();
    }

}
