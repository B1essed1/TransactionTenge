package uz.tenge.transactiontenge.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class H2HResponse implements Serializable {
    private String rrn;
    private String status;
    private Long amount;

    public static H2HResponse create(String rrn) {
        var response = new H2HResponse();
        response.setRrn(rrn);
        response.setStatus("OK");
        response.setAmount(123L);
        return response;
    }
}
