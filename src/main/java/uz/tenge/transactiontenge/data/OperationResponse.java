package uz.tenge.transactiontenge.data;

import lombok.Getter;
import lombok.Setter;
import uz.tenge.transactiontenge.entity.Operation;

import java.io.Serializable;

@Getter
@Setter
public class OperationResponse implements Serializable {
    private String operationId;
    private String sender;
    private String receiver;
    private Long amount;

}
