package com.example.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Bank application.
 */
@SpringBootApplication
public final class BankappApplication {

    /**
     * Private constructor to prevent instantiation.
     */
    private BankappApplication() {
    }

    /**
     * Application main method.
     *
     * @param args application arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(BankappApplication.class, args);
    }

}
