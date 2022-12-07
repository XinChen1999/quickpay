package QuickPay.demo.repository;

import QuickPay.demo.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String>{
    @Query("{'accountId': ?0}")
    List<Transaction> findByAccountId(String accountId);

    @Query("{'accountId': ?0, 'paymentType': ?1, 'status': ?2, 'cardType': ?3}")
    List<Transaction> findReceivablesById(String id, String paymentType, String status, String cardType);

    @Query("{'accountId': ?0, 'status': ?1}")
    List<Transaction> findTransactionsByAccountIdAndStatus(String id, String status);

//    @Query("{'accountId': ?0, 'status': ?1, 'dateCreated': {$gte: ?2, $lse: ?3}}")
//    List<Transaction> findTransactionsByDateCreatedBetween(String id, String status, Integer startDate, Integer endDate);

}
