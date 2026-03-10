package com.example.bankapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a bank account in the system.
 */
@Entity
@Table(name = "accounts")
public final class Account implements UserDetails {

    /** Balance precision constant. */
    private static final int BALANCE_PRECISION = 19;

    /** Balance scale constant. */
    private static final int BALANCE_SCALE = 2;

    /** Account ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Account username. */
    @Column(unique = true, nullable = false)
    private String username;

    /** Account password. */
    @Column(nullable = false)
    private String password;

    /** Account balance. */
    @Column(nullable = false, precision = BALANCE_PRECISION, scale = BALANCE_SCALE)
    private BigDecimal balance = BigDecimal.ZERO;

    /** Transactions belonging to this account. */
    @OneToMany(
        mappedBy = "account",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Transaction> transactions = new ArrayList<>();

    /** Default constructor. */
    public Account() {
    }

    /**
     * Creates an account.
     *
     * @param userName username
     * @param userPassword password
     */
    public Account(final String userName, final String userPassword) {
        this.username = userName;
        this.password = userPassword;
        this.balance = BigDecimal.ZERO;
    }

    /** @return account id */
    public Long getId() {
        return id;
    }

    /** @param newId account id */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return username */
    @Override
    public String getUsername() {
        return username;
    }

    /** @param newUsername username */
    public void setUsername(final String newUsername) {
        this.username = newUsername;
    }

    /** @return password */
    @Override
    public String getPassword() {
        return password;
    }

    /** @param newPassword password */
    public void setPassword(final String newPassword) {
        this.password = newPassword;
    }

    /** @return account balance */
    public BigDecimal getBalance() {
        return balance;
    }

    /** @param newBalance balance */
    public void setBalance(final BigDecimal newBalance) {
        this.balance = newBalance;
    }

    /** @return transactions */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** @param newTransactions transactions */
    public void setTransactions(final List<Transaction> newTransactions) {
        this.transactions = newTransactions;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
