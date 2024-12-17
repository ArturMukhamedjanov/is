package islab1.services;

import lombok.Builder;

@Builder
public record TransactionOperationResponse(Integer objects, String filename) {

}
