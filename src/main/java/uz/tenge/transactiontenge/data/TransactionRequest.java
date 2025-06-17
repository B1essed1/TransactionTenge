package uz.tenge.transactiontenge.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {
    private String sender;
    private String recipient;
    private Long amount;
}
