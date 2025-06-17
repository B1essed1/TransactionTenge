package uz.tenge.transactiontenge.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import uz.tenge.transactiontenge.entity.Transaction;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction,Long> {
}
