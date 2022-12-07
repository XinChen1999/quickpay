package QuickPay.demo.service;

import QuickPay.demo.model.Account;
import QuickPay.demo.repository.AccountRepository;
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
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId("1");
        account.setEmail("123@gmail.com");
        account.setUserName("username");
        account.setBalance(100.0);
        account.setPassword("password");
    }

    @Test
    void givenAccount_whenAddAccount_thenReturnSavedAccount() {
        given(accountRepository.save(account)).willReturn(account);
        Account savedAccount = accountService.addAccount(account);
        Assertions.assertNotNull(savedAccount);
    }

    @Test
    void givenAccount_whenUpdateAccount_thenReturnAccount() {
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
        account.setEmail("456@gmail.com");
        account.setPassword("789");

        Account updatedAccount = accountService.updateAccount(account);

        Assertions.assertEquals("456@gmail.com", updatedAccount.getEmail());
        Assertions.assertEquals("789", updatedAccount.getPassword());
    }

    @Test
    void givenAccountList_whenGetAllAccounts_thenReturnAccountList() {
        Account account1 = new Account();
        account1.setId("2");
        account1.setUserName("Jack");

        given(accountRepository.findAll()).willReturn(List.of(account, account1));

        List<Account> list = accountService.getAllAccount();

        Assertions.assertEquals(2, list.size());
    }

    @Test
    void givenAccountId_whenGetAccountById_thenReturnAccount() {
        given(accountRepository.findById("1")).willReturn(Optional.of(account));

        Account savedAccount = accountService.getAccountById("1");

        Assertions.assertEquals("password", savedAccount.getPassword());
    }

    @Test
    void deleteAccount() {
        willDoNothing().given(accountRepository).deleteById("1");

        accountService.deleteAccount("1");

        verify(accountRepository, times(1)).deleteById("1");
    }

    @Test
    void changeBalance() {
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));

        Account savedAccount = accountService.changeBalance("1",100.0);

        Assertions.assertEquals(200.0, savedAccount.getBalance());
    }
}