package QuickPay.demo.service;

import QuickPay.demo.model.Account;
import QuickPay.demo.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account){
        return accountRepository.save(account);
    }

    public Account updateAccount(Account account){
        Account savedAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Account By Id %s", account.getId())
                ));

        savedAccount.setUserName(account.getUserName());
        savedAccount.setPassword(account.getPassword());
        savedAccount.setEmail(account.getEmail());
        savedAccount.setBalance(account.getBalance());

        accountRepository.save(savedAccount);
        return savedAccount;
    }

    public List<Account> getAllAccount(){
        return accountRepository.findAll();
    }

    public Account getAccountById(String id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Account By Id %s", id)
                ));

    }

    public void deleteAccount(String id){
        accountRepository.deleteById(id);
    }


    public Account changeBalance(String accountId, Double balance){
        Account savedAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Account By Id %s", accountId)
                ));

        savedAccount.setBalance(savedAccount.getBalance() + balance);
        accountRepository.save(savedAccount);
        return savedAccount;
    }
}
