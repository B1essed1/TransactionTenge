package uz.tenge.transactiontenge.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.tenge.transactiontenge.data.H2HKafkaPayload;
import uz.tenge.transactiontenge.data.TOPICS;
import uz.tenge.transactiontenge.data.TransactionRequest;
import uz.tenge.transactiontenge.entity.Operation;
import uz.tenge.transactiontenge.entity.Transaction;
import uz.tenge.transactiontenge.enums.OPERATION_TYPE;
import uz.tenge.transactiontenge.enums.TRANSACTION_STATUS;
import uz.tenge.transactiontenge.repository.OperationRepository;
import uz.tenge.transactiontenge.repository.TransactionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class OperationService {

    private final OperationRepository  operationRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate;



    Map<OPERATION_TYPE, Function<Operation, Operation>> methodMap = new HashMap<>();
    {
        methodMap.put(OPERATION_TYPE.H2H, this::humo2Humo);
    }

    public OperationService(OperationRepository operationRepository, TransactionRepository transactionRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.operationRepository = operationRepository;
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void transactionStart(TransactionRequest request) {
        // todo there is a card validations
        OPERATION_TYPE operationType = getOperationType(request);
        Operation operation = Operation.of(request,operationType);
        return operationRepository.save(operation)
                .map(operation1 -> methodMap.get(operationType).apply(operation))
                .map(oper)


    }

    private OPERATION_TYPE getOperationType(TransactionRequest request) {
        // todo there is a operation type validations sender and receiver card type

        return OPERATION_TYPE.H2H;
    }

    private Mono<Operation> humo2Humo(Operation request) {
        Transaction transaction =  Transaction.create(request);
        transactionRepository.save(transaction)
                .map(transaction1 -> {
                    send(TOPICS.HUMO2HUMO, H2HKafkaPayload.create(transaction));
                    transaction1.setStatus(TRANSACTION_STATUS.PENDING);
                    return transactionRepository.save(transaction1);
                })
                .thenReturn();


        transactionRepository.save(transaction);
    }

    public <T> void send(String topic, T payload) {
        kafkaTemplate.send(topic, payload);
    }
}
