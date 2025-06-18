package uz.tenge.transactiontenge.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import uz.tenge.transactiontenge.data.OperationResponse;
import uz.tenge.transactiontenge.data.TransactionRequest;
import uz.tenge.transactiontenge.service.OperationService;

@RestController
@RequestMapping("api/v1/operation")
public class OperationController {
    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("start")
    Mono<OperationResponse> startTransaction(@RequestBody TransactionRequest request) {
        return operationService.transactionStart(request);
    }
}
