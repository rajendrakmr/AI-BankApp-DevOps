package com.example.bankapp.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a bank account.
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

    @Column(nullable = false,
            precision = BALANCE_PRECISION,
            scale = BALANCE_SCALE)
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "account",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public Account() {
    }

    public Account(final String userName, final String userPassword) {
        this.username = userName;
        this.password = userPassword;
        this.balance = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long newId) {
        this.id = newId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String newUsername) {
        this.username = newUsername;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(final String newPassword) {
        this.password = newPassword;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal newBalance) {
        this.balance = newBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(final List<Transaction> newTransactions) {
        this.transactions = newTransactions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
