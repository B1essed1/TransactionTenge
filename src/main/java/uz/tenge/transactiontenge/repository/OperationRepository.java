package uz.tenge.transactiontenge.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import uz.tenge.transactiontenge.entity.Operation;

@Repository
public interface OperationRepository extends ReactiveCrudRepository<Operation,Long> {

    Mono<Operation> findByOperationId(String oid);
}
