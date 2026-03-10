package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for account operations such as
 * registration, deposits, withdrawals, transfers,
 * and transaction history retrieval.
 */
@Service
public final class AccountService implements UserDetailsService {

    /** Account repository. */
    private final AccountRepository accountRepository;

    /** Transaction repository. */
    private final TransactionRepository transactionRepository;

    /** Password encoder. */
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates the service.
     *
     * @param accountRepo account repository
     * @param transactionRepo transaction repository
     * @param encoder password encoder
     */
    public AccountService(
            final AccountRepository accountRepo,
            final TransactionRepository transactionRepo,
            final PasswordEncoder encoder) {

        this.accountRepository = accountRepo;
        this.transactionRepository = transactionRepo;
        this.passwordEncoder = encoder;
    }

    /**
     * Loads a user by username for authentication.
     *
     * @param username username
     * @return user details
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        return accountRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );
    }

    /**
     * Registers a new account.
     *
     * @param username username
     * @param password password
     * @return true if registration successful
     */
    public boolean registerAccount(
            final String username,
            final String password) {

        if (accountRepository.findByUsername(username).isPresent()) {
            return false;
        }

        final Account account =
                new Account(username, passwordEncoder.encode(password));

        accountRepository.save(account);
        return true;
    }

    /**
     * Deposits money into an account.
     *
     * @param account account
     * @param amount amount
     */
    @Transactional
    public void deposit(
            final Account account,
            final BigDecimal amount) {

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        final Transaction transaction =
                new Transaction(
                        amount,
                        "Deposit",
                        LocalDateTime.now(),
                        account
                );

        transactionRepository.save(transaction);
    }

    /**
     * Withdraws money from an account.
     *
     * @param account account
     * @param amount amount
     * @return true if withdrawal successful
     */
    @Transactional
    public boolean withdraw(
            final Account account,
            final BigDecimal amount) {

        if (account.getBalance().compareTo(amount) < 0) {
            return false;
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        final Transaction transaction =
                new Transaction(
                        amount,
                        "Withdrawal",
                        LocalDateTime.now(),
                        account
                );

        transactionRepository.save(transaction);

        return true;
    }

    /**
     * Transfers money between accounts.
     *
     * @param from source account
     * @param toUsername destination username
     * @param amount transfer amount
     * @return error message or null if successful
     */
    @Transactional
    public String transferAmount(
            final Account from,
            final String toUsername,
            final BigDecimal amount) {

        if (from.getUsername().equals(toUsername)) {
            return "Cannot transfer to yourself.";
        }

        if (from.getBalance().compareTo(amount) < 0) {
            return "Insufficient funds.";
        }

        final Account to =
                accountRepository.findByUsername(toUsername).orElse(null);

        if (to == null) {
            return "Recipient not found.";
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        final LocalDateTime now = LocalDateTime.now();

        transactionRepository.save(
                new Transaction(amount, "Transfer Out", now, from)
        );

        transactionRepository.save(
                new Transaction(amount, "Transfer In", now, to)
        );

        return null;
    }

    /**
     * Returns transaction history.
     *
     * @param account account
     * @return list of transactions
     */
    public List<Transaction> getTransactionHistory(final Account account) {

        return transactionRepository
                .findByAccountIdOrderByTimestampDesc(account.getId());
    }
}
