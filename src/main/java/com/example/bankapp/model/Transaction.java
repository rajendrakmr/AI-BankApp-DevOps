package com.example.bankapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a financial transaction.
 */
@Entity
@Table(name = "transactions")
public final class Transaction {

    /** Precision for monetary values. */
    private static final int AMOUNT_PRECISION = 19;

    /** Scale for monetary values. */
    private static final int AMOUNT_SCALE = 2;

    /** Transaction ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Transaction amount. */
    @Column(nullable = false, precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE)
    private BigDecimal amount;

    /** Transaction type (DEPOSIT/WITHDRAW). */
    @Column(nullable = false)
    private String type;

    /** Transaction timestamp. */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /** Associated account. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    /** Default constructor required by JPA. */
    public Transaction() {
    }

    /**
     * Creates a transaction.
     *
     * @param transactionAmount transaction amount
     * @param transactionType transaction type
     * @param transactionTimestamp timestamp
     * @param transactionAccount account
     */
    public Transaction(
        final BigDecimal transactionAmount,
        final String transactionType,
        final LocalDateTime transactionTimestamp,
        final Account transactionAccount
    ) {
        this.amount = transactionAmount;
        this.type = transactionType;
        this.timestamp = transactionTimestamp;
        this.account = transactionAccount;
    }

    /** @return transaction id */
    public Long getId() {
        return id;
    }

    /** @param newId transaction id */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return amount */
    public BigDecimal getAmount() {
        return amount;
    }

    /** @param newAmount amount */
    public void setAmount(final BigDecimal newAmount) {
        this.amount = newAmount;
    }

    /** @return transaction type */
    public String getType() {
        return type;
    }

    /** @param newType transaction type */
    public void setType(final String newType) {
        this.type = newType;
    }

    /** @return timestamp */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /** @param newTimestamp timestamp */
    public void setTimestamp(final LocalDateTime newTimestamp) {
        this.timestamp = newTimestamp;
    }

    /** @return account */
    public Account getAccount() {
        return account;
    }

    /** @param newAccount account */
    public void setAccount(final Account newAccount) {
        this.account = newAccount;
    }
}
