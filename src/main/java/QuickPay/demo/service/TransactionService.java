package QuickPay.demo.service;

import QuickPay.demo.repository.TransactionRepository;
import QuickPay.demo.model.Transaction;
import org.springframework.stereotype.Service;






import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Transaction addTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Transaction transaction){
        Transaction savedTransaction = transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Transaction By Id %s", transaction.getId())
                ));

        savedTransaction.setAccountId(transaction.getAccountId());
        savedTransaction.setBalance(transaction.getBalance());
        savedTransaction.setCardNumber(transaction.getCardNumber());
        savedTransaction.setLocation(transaction.getLocation());
        savedTransaction.setStatus(transaction.getStatus());
        savedTransaction.setDateCreated(transaction.getDateCreated());
        savedTransaction.setPaymentType(transaction.getPaymentType());
        savedTransaction.setCardType(transaction.getCardType());

        transactionRepository.save(savedTransaction);
        return savedTransaction;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(String id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Transaction By Id %s", id)
                ));
    }

    public List<Transaction> getAllTransactionsByAccountId(String accountId){
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transaction> getPartTransactionsByAccountId(String accountId){
        return transactionRepository.findTransactionsByAccountIdAndStatus(accountId, "completed");
    }

    /**
     * Process a refund to a transaction, add a new transaction in the database to represent the refund
     * @param id transaction id
     * @param balance refund amount
     */
    public Transaction processRefund(String id, Double balance){
        Transaction savedTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Transaction By Id %s", id)
                ));
        savedTransaction.setPaymentType("refund");
        savedTransaction.setBalance(balance);
        savedTransaction.setStatus("completed");
        savedTransaction.setId("-" + savedTransaction.getId());
        addTransaction(savedTransaction);
        return savedTransaction;
    }

    public void deleteTransaction(String id){
        transactionRepository.deleteById(id);
    }

    /**
     * Cancel a refund
     * @param id transaction id
     */
    public Optional<Transaction> cancelRefund(String id){
        Transaction savedTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Transaction By Id %s", id)
                ));

        if (Objects.equals(savedTransaction.getPaymentType(), "refund")){
            savedTransaction.setStatus("canceled");
            updateTransaction(savedTransaction);
            return Optional.of(savedTransaction);
        }
        return Optional.empty();
    }

    /**
     * Check account receivables for all customers who have pending credit card charges
     * @param id account id
     * @return receivables
     */
    public Double getReceivables(String id){
        List<Transaction> receivables = transactionRepository.
                findReceivablesById(id, "charge", "pending", "credit");
        return receivables.stream().mapToDouble(Transaction::getBalance).sum();
    }

    /**
     * Check the revenue the account made in a certain time period
     *
     * @param id the account ID
     * @param startDate start date
     * @param endDate end date
     * @return how much revenue the account make in a certain time
     */
    public Double getRevenue(String id, Integer startDate, Integer endDate){
        List<Transaction> transactions = transactionRepository.
                findTransactionsByAccountIdAndStatus(id, "completed");

        List<Transaction> chargesAndRefunds = transactions.stream().
                filter(c -> c.getDateCreated() > startDate && c.getDateCreated() < endDate).
                collect(Collectors.toList());

        Double charges = chargesAndRefunds.stream().filter(c -> Objects.equals(c.getPaymentType(), "charge")).
                mapToDouble(Transaction::getBalance).sum();

        Double refunds = chargesAndRefunds.stream().filter(c -> Objects.equals(c.getPaymentType(), "refund")).
                mapToDouble(Transaction::getBalance).sum();

        return charges - refunds;
    }

    /**
     * Get the status of a transaction
     * @param id transaction ID
     * @return the status of a transaction
     */
    public String getStatus(String id){
        Transaction savedTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Transaction By Id %s", id)
                ));
        return savedTransaction.getStatus();
    }

    /**
     * Get account ID of a certain transaction
     * @param id transaction ID
     * @return account ID
     */
    public String getAccountIdByTransactionId(String id){
        Transaction savedTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Transaction By Id %s", id)
                ));

        return savedTransaction.getAccountId();
    }

}
