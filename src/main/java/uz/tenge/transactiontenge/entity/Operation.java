package uz.tenge.transactiontenge.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import uz.tenge.transactiontenge.data.TransactionRequest;
import uz.tenge.transactiontenge.enums.OPERATION_STATUS;
import uz.tenge.transactiontenge.enums.OPERATION_TYPE;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("operation")
@Getter
@Setter
public class Operation {
    private Long id;
    private OPERATION_TYPE type;
    private Long amount;
    private LocalDateTime createdAt;
    private OPERATION_STATUS status;
    private String operationId;
    private String sender;
    private String recipient;

    public static Operation of(TransactionRequest request, OPERATION_TYPE type){
        Operation operation = new Operation();
        operation.setType(type);
        operation.setOperationId(UUID.randomUUID().toString());
        operation.setAmount(request.getAmount());
        operation.setCreatedAt(LocalDateTime.now());
        operation.setStatus(OPERATION_STATUS.NEW);
        operation.setSender(request.getSender());
        operation.setRecipient(request.getRecipient());
        return operation;
    }
}
