package uz.tenge.transactiontenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.tenge.transactiontenge.data.*;
import uz.tenge.transactiontenge.entity.Operation;
import uz.tenge.transactiontenge.entity.Transaction;
import uz.tenge.transactiontenge.enums.OPERATION_STATUS;
import uz.tenge.transactiontenge.enums.OPERATION_TYPE;
import uz.tenge.transactiontenge.enums.TRANSACTION_STATUS;
import uz.tenge.transactiontenge.repository.OperationRepository;
import uz.tenge.transactiontenge.repository.TransactionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    Map<OPERATION_TYPE, Function<Operation, Mono<OperationResponse>>> methodMap = new HashMap<>();

    {
        methodMap.put(OPERATION_TYPE.H2H, this::humo2Humo);
    }

    public OperationService(OperationRepository operationRepository, TransactionRepository transactionRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.operationRepository = operationRepository;
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<OperationResponse> transactionStart(TransactionRequest request) {
        // todo there is a card validations
        OPERATION_TYPE operationType = getOperationType(request);
        Operation operation = Operation.of(request, operationType);
        return operationRepository.save(operation)
                .flatMap(operation1 -> methodMap.get(operationType).apply(operation1));
    }

    private OPERATION_TYPE getOperationType(TransactionRequest request) {
        // todo there is a operation type validations sender and receiver card type

        return OPERATION_TYPE.H2H;
    }

    // Humo 2HUmo operatsiyasi uchun metod
    private Mono<OperationResponse> humo2Humo(Operation request) {
        Transaction transaction = Transaction.create(request);
        return transactionRepository.save(transaction)
                .flatMap(savedTransaction -> {
                    send(TOPICS.HUMO2HUMO, H2HKafkaPayload.create(savedTransaction)); // Send Kafka message
                    savedTransaction.setStatus(TRANSACTION_STATUS.PENDING); // Change status
                    return transactionRepository.save(savedTransaction); // Save again with new status
                })
                .flatMap(updatedTransaction -> {

                    request.setStatus(OPERATION_STATUS.PENDING);
                    return operationRepository.save(request);
                })
                .map(this::toOperationResponse);
    }


    // Humo 2 Humo da success bolgan holatlar uchun listener
    @KafkaListener(topics = TOPICS.HUMO2HUMO_RES, groupId = "my-group")
    public void listen(H2HResponse message) {

        // todo some work with humo itself
        transactionRepository.findByRrn(message.getRrn())
                .flatMap(transaction -> {
                    if (message.getStatus().equals("OK")) {
                        return saveTransaction(TRANSACTION_STATUS.SUCCESS, transaction);
                    }
                    return saveTransaction(TRANSACTION_STATUS.ERROR, transaction);
                }).subscribe();

    }


    private Mono<Void> saveTransaction(TRANSACTION_STATUS status, Transaction transaction) {
        transaction.setStatus(status);
        return transactionRepository.save(transaction)
                .flatMap(savedTransaction -> saveOperation(status, savedTransaction));
    }

    private Mono<Void> saveOperation(TRANSACTION_STATUS status, Transaction transaction) {
        return operationRepository.findByOperationId(transaction.getOperationId())
                .flatMap(operation -> {
                    operation.setStatus(status == TRANSACTION_STATUS.SUCCESS ? OPERATION_STATUS.SUCCESS : OPERATION_STATUS.ERROR);
                    return operationRepository.save(operation);
                }).then();
    }

    public <T> void send(String topic, T payload) {
        kafkaTemplate.send(topic, payload);
    }


    private OperationResponse toOperationResponse(Operation operation) {
        OperationResponse response = new OperationResponse();
        response.setOperationId(operation.getOperationId());
        response.setSender(operation.getSender());
        response.setReceiver(operation.getRecipient());
        response.setAmount(operation.getAmount());
        return response;
    }
}
