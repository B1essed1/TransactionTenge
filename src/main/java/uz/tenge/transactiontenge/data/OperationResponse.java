package uz.tenge.transactiontenge.data;

import lombok.Getter;
import lombok.Setter;
import uz.tenge.transactiontenge.entity.Operation;

@Getter
@Setter
public class OperationResponse {
    private String operationId;
    private String sender;
    private String receiver;
    private Long amount;

}
