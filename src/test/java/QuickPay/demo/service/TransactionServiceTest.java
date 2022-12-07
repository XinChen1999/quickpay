package QuickPay.demo.service;

import QuickPay.demo.model.Account;
import QuickPay.demo.model.Transaction;
import QuickPay.demo.repository.AccountRepository;
import QuickPay.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setStatus("OK");
        transaction.setBalance(100.0);
        transaction.setPaymentType("CreditCard");
        transaction.setAccountId("123");
        transaction.setCardNumber("123");
        transaction.setCardType("CreditCard");
        transaction.setDateCreated(2022);
        transaction.setLocation("Seattle");
        transaction.setId("1");
    }

    @Test
    void addTransaction() {
        given(transactionRepository.save(transaction)).willReturn(transaction);

        Transaction savedTransaction = transactionService.addTransaction(transaction);

        Assertions.assertNotNull(savedTransaction);
        Assertions.assertEquals("1", savedTransaction.getId());
    }

    @Test
    void updateTransaction() {
        given(transactionRepository.findById("1")).willReturn(Optional.of(transaction));
        transaction.setStatus("Cancelled");
        transaction.setLocation("Florida");
        transaction.setDateCreated(2023);
        transaction.setAccountId("999");
        transaction.setBalance(10.0);
        transaction.setCardNumber("111");
        transaction.setPaymentType("Cash");
        transaction.setCardType("Debit");

        Transaction savedTransaction = transactionService.updateTransaction(transaction);

        Assertions.assertEquals("Cancelled", savedTransaction.getStatus());
        Assertions.assertEquals("Florida", savedTransaction.getLocation());
        Assertions.assertEquals(2023, savedTransaction.getDateCreated());
        Assertions.assertEquals("999", savedTransaction.getAccountId());
        Assertions.assertEquals(10.0, savedTransaction.getBalance());
        Assertions.assertEquals("111", savedTransaction.getCardNumber());
        Assertions.assertEquals("Cash", savedTransaction.getPaymentType());
        Assertions.assertEquals("Debit", savedTransaction.getCardType());
    }

    @Test
    void getAllTransactions() {
        given(transactionRepository.findAll()).willReturn(List.of(transaction));

        List<Transaction> list = transactionService.getAllTransactions();

        Assertions.assertEquals(1, list.size());
    }

    @Test
    void getTransactionById() {
        given(transactionRepository.findById("1")).willReturn(Optional.of(transaction));

        Transaction savedTransaction = transactionService.getTransactionById("1");

        Assertions.assertEquals("123", savedTransaction.getAccountId());
    }

    @Test
    void getAllTransactionsByAccountId() {
        given(transactionRepository.findByAccountId("123")).willReturn(List.of(transaction));

        List<Transaction> list = transactionService.getAllTransactionsByAccountId("123");

        Assertions.assertEquals(1, list.size());
    }

    @Test
    void getPartTransactionsByAccountId() {
        given(transactionRepository.findTransactionsByAccountIdAndStatus("123", "completed")).willReturn(List.of(transaction));

        List<Transaction> list = transactionService.getPartTransactionsByAccountId("123");

        Assertions.assertEquals(1, list.size());
    }

    @Test
    void processRefund() {
        given(transactionRepository.findById("1")).willReturn(Optional.of(transaction));
        given(transactionRepository.save(transaction)).willReturn(transaction);

        Transaction savedTransaction = transactionService.processRefund("1", 10.0);

        Assertions.assertEquals("refund", savedTransaction.getPaymentType());
        Assertions.assertEquals(10.0, savedTransaction.getBalance());
        Assertions.assertEquals("completed", savedTransaction.getStatus());
        Assertions.assertEquals("-1", savedTransaction.getId());
    }

    @Test
    void deleteTransaction() {
        willDoNothing().given(transactionRepository).deleteById("1");

        transactionService.deleteTransaction("1");

        verify(transactionRepository, times(1)).deleteById("1");
    }

    @Test
    void cancelRefund() {
        transaction.setPaymentType("refund");
        given(transactionRepository.findById("1")).willReturn(Optional.of(transaction));

        Optional<Transaction> savedTransaction = transactionService.cancelRefund("1");

        Assertions.assertTrue(savedTransaction.isPresent());
        Assertions.assertEquals("refund", savedTransaction.get().getPaymentType());
        Assertions.assertEquals("canceled", savedTransaction.get().getStatus());
    }

    @Test
    void getReceivables() {
        given(transactionRepository.findReceivablesById("1","charge", "pending", "credit")).willReturn(List.of(transaction));

        double balance = transactionService.getReceivables("1");

        Assertions.assertEquals(100.0, balance);
    }

    @Test
    void getRevenue() {
        Transaction t1 = new Transaction();
        t1.setPaymentType("charge");
        t1.setBalance(100.0);
        t1.setStatus("completed");
        t1.setDateCreated(10);
        t1.setAccountId("123");
        Transaction t2 = new Transaction();
        t2.setPaymentType("refund");
        t2.setBalance(10.0);
        t2.setStatus("completed");
        t2.setDateCreated(11);
        t2.setAccountId("123");
        given(transactionRepository.findTransactionsByAccountIdAndStatus("123", "completed")).willReturn(List.of(t1,t2));

        double balance = transactionService.getRevenue("123", 1, 15);

        Assertions.assertEquals(90.0, balance);
    }

    @Test
    void getStatus() {
        given(transactionRepository.findById("1")).willReturn(Optional.of(transaction));

        String status = transactionService.getStatus("1");

        Assertions.assertEquals("OK", status);
    }

    @Test
    void getAccountIdByTransactionId() {
        given(transactionRepository.findById("1")).willReturn(Optional.of(transaction));

        String accountId = transactionService.getAccountIdByTransactionId("1");

        Assertions.assertEquals("123", accountId);
    }
}