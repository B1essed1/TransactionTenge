package uz.tenge.transactiontenge.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import uz.tenge.transactiontenge.enums.PROCESSING_TYPE;
import uz.tenge.transactiontenge.enums.TRANSACTION_STATUS;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("transaction")
@Getter
@Setter
public class Transaction {
    @Id
    private Long id;
    private String transactionId;
    private Long amount;
    private LocalDateTime createdAt;
    private String operationId;
    private TRANSACTION_STATUS status;
    private PROCESSING_TYPE  processingType;
    private Long userId;
    private String sender;
    private String rrn;
    private String recipient;

    public  static  Transaction create(Operation request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setOperationId(request.getOperationId());
        transaction.setSender(request.getSender());
        transaction.setRecipient(request.getRecipient());
        transaction.setStatus(TRANSACTION_STATUS.PENDING);
        transaction.setRrn(UUID.randomUUID().toString());
        transaction.setTransactionId(UUID.randomUUID().toString());
        return transaction;
    }
}
