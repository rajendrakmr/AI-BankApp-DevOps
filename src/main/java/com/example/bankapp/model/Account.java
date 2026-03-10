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
 * Entity representing a bank account in the system.
 * Implements UserDetails for Spring Security authentication.
 */
@Entity
@Table(name = "accounts")
public final class Account implements UserDetails {

    private static final int BALANCE_PRECISION = 19;
    private static final int BALANCE_SCALE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(
        nullable = false,
        precision = BALANCE_PRECISION,
        scale = BALANCE_SCALE
    )
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(
        mappedBy = "account",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Transaction> transactions = new ArrayList<>();

    /** Default constructor for JPA. */
    public Account() {
    }

    /**
     * Constructor for creating a new account.
     *
     * @param username account username
     * @param password account password
     */
    public Account(final String username, final String password) {
        this.username = username;
        this.password = password;
        this.balance = BigDecimal.ZERO;
    }

    /** @return account id */
    public Long getId() {
        return id;
    }

    /** @param newId new account id */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return username */
    @Override
    public String getUsername() {
        return username;
    }

    /** @param newUsername username to set */
    public void setUsername(final String newUsername) {
        this.username = newUsername;
    }

    /** @return password */
    @Override
    public String getPassword() {
        return password;
    }

    /** @param newPassword password to set */
    public void setPassword(final String newPassword) {
        this.password = newPassword;
    }

    /** @return account balance */
    public BigDecimal getBalance() {
        return balance;
    }

    /** @param newBalance balance to set */
    public void setBalance(final BigDecimal newBalance) {
        this.balance = newBalance;
    }

    /** @return list of transactions */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** @param newTransactions transaction list */
    public void setTransactions(final List<Transaction> newTransactions) {
        this.transactions = newTransactions;
    }

    /** @return user authorities */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /** @return account expiration status */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** @return account locked status */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** @return credentials expiration status */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** @return enabled status */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
