package QuickPay.demo.controller;

import QuickPay.demo.model.Account;
import QuickPay.demo.service.AccountService;
import QuickPay.demo.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity addAccount(@RequestBody Account account){
        accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body("Your account has been created");
    }

    @PutMapping
    public ResponseEntity updateAccount(@RequestBody Account account){
        accountService.updateAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccount());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity getAccountById(@PathVariable String id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccount(@PathVariable String id){
        accountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // get all transactions of the account
    @GetMapping("/transactions/all/{id}")
    public ResponseEntity getAllTransactions(@PathVariable String id){
        return ResponseEntity.ok("All your transactions:" + "\n" +
                transactionService.getAllTransactionsByAccountId(id));
    }

    // get all completed transactions of the account
    @GetMapping("/transactions/part/{id}")
    public ResponseEntity getPartTransactions(@PathVariable String id){
        return ResponseEntity.ok("Your completed transactions:" +  "\n" +
                transactionService.getPartTransactionsByAccountId(id));
    }

    // get receivables
    @GetMapping("/receivables/{id}")
    public ResponseEntity getReceivables(@PathVariable String id){
        return ResponseEntity.ok("Your account receivables are " + transactionService.getReceivables(id));
    }

    // get revenues in a certain period
    @GetMapping("/revenue/{id}/{startDate}/{endDate}")
    public ResponseEntity getRevenue(@PathVariable String id, @PathVariable Integer startDate,
                                     @PathVariable Integer endDate){
        return ResponseEntity.ok("You made " + transactionService.getRevenue(id, startDate, endDate) +
                " from " + startDate + " to " + endDate);
    }

    // get the account total balance
    @GetMapping("/balance/{id}")
    public ResponseEntity getBalance(@PathVariable String id){
        return ResponseEntity.ok("Your total balance is " + transactionService.getRevenue(id, 0, 99999999));
    }


}
