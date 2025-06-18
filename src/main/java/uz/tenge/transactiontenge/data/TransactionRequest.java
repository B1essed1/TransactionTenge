package uz.tenge.transactiontenge.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionRequest implements Serializable {
    private String sender;
    private String recipient;
    private Long amount;
}
