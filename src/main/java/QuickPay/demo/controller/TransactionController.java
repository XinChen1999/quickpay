package QuickPay.demo.controller;


import QuickPay.demo.service.AccountService;
import QuickPay.demo.service.TransactionService;
import QuickPay.demo.model.Transaction;
import QuickPay.demo.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String VALIDATE_CARD = "https://c3jkkrjnzlvl5lxof74vldwug40pxsqo.lambda-url.us-west-2.on.aws/";

    private final String CARD_AMOUNT = "https://223didiouo3hh4krxhm4n4gv7y0pfzxk.lambda-url.us-west-2.on.aws/";

    private final AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService){
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity addTransaction(@RequestBody Transaction transaction){
        String cardNumber = transaction.getCardNumber();
        Double amount = transaction.getBalance();

        //use external api to validate credit card number and debit card funds
        switch (transaction.getCardType()){
            case "credit":
                Map<String, String> response = restTemplate.postForObject(VALIDATE_CARD,
                        Map.of("card_number", cardNumber), Map.class);
                assert response != null;
                if (! response.get("success").equals("true")){
                    return ResponseEntity.ok("Transaction denied: the credit card number is invalid");
                }
                break;
            case "debit":
                Map<String, String> response2 = restTemplate.postForObject(CARD_AMOUNT,
                        Map.of("card_number", cardNumber, "amt", amount), Map.class);
                assert response2 != null;
                if (! response2.get("success").equals("true")) {
                    return ResponseEntity.ok("Transaction denied: Insufficient funds in the debit card");
                }
                break;
        }

        transactionService.addTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body("The payment has been initiated");
    }

    @PutMapping
    public ResponseEntity updateTransaction(@RequestBody Transaction transaction){
        transactionService.updateTransaction(transaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity getTransactionById(@PathVariable String id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTransaction(@PathVariable String id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/status/{id}")
    public ResponseEntity getTransactionStatus(@PathVariable String id){
        String status = transactionService.getStatus(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The transaction is " + status);
    }

    //process a fully or partially refund
    @PostMapping({"/refund/{id}/{balance}"})
    public ResponseEntity processRefund(@PathVariable String id, @PathVariable Double balance){
        transactionService.processRefund(id, balance);

        return ResponseEntity.status(HttpStatus.CREATED).body(balance + "has been refunded");
    }

    // cancel a refund
    @PutMapping({"/refund/cancel/{id}"})
    public ResponseEntity cancelRefund(@PathVariable String id){
        transactionService.cancelRefund(id);
        return ResponseEntity.ok().body("The refund is canceled");
    }
}
