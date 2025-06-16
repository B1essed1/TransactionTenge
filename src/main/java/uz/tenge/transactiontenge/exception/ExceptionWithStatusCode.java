
package uz.tenge.transactiontenge.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ExceptionWithStatusCode extends RuntimeException{
    private Integer httpStatusCode;
    private String messageKey;
    private Objects[] args;

    public ExceptionWithStatusCode(Integer status, String messageKey){
        this.httpStatusCode =status;
        this.messageKey = messageKey;
    }

    public ExceptionWithStatusCode( String messageKey){
        this.httpStatusCode =400;
        this.messageKey = messageKey;
    }

    public ExceptionWithStatusCode(Integer status, String messageKey,Throwable cause){
        super(cause);
        this.httpStatusCode =status;
        this.messageKey = messageKey;
    }



    public ExceptionWithStatusCode(Integer httpStatusCode, String messageKey, Objects[] args) {
        this.httpStatusCode = httpStatusCode;
        this.messageKey = messageKey;
        this.args = args;
    }

    public ExceptionWithStatusCode(Throwable cause, Integer httpStatusCode, String messageKey, Objects[] args) {
        super(cause);
        this.httpStatusCode = httpStatusCode;
        this.messageKey = messageKey;
        this.args = args;
    }

    public void andExpectMessage(String s) {
        
    }
}