package uz.tenge.transactiontenge.data;

import lombok.Getter;
import lombok.Setter;
import uz.tenge.transactiontenge.entity.Transaction;

import java.io.Serializable;

@Getter
@Setter
public class H2HKafkaPayload implements Serializable {
    private String sender;
    private String receiver;
    private String rrn;
    private Long amount ;

    public static H2HKafkaPayload create(Transaction transaction) {
        H2HKafkaPayload payload = new H2HKafkaPayload();
        payload.setSender(transaction.getSender());
        payload.setReceiver(transaction.getRecipient());
        payload.setRrn(transaction.getRrn());
        payload.setAmount(transaction.getAmount());
        return payload;
    }
}
