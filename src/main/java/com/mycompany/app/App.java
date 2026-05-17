/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

package com.mycompany.app;

class BankAccount {
    private int balance = 100;

    // Synchronized to prevent race conditions
    public synchronized void withdraw(String user, int amount) {
        System.out.println(user + " is trying to withdraw $" + amount);
        if (balance >= amount) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            balance -= amount;
            System.out.println(user + " completed withdrawal. Remaining balance: $" + balance);
        } else {
            System.out.println(user + " tried to withdraw $" + amount + " but insufficient funds. Balance: $" + balance);
        }
    }

    public synchronized void deposit(String user, int amount) {
        System.out.println(user + " is depositing $" + amount);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        balance += amount;
        System.out.println(user + " completed deposit. New balance: $" + balance);
    }

    public synchronized int getBalance() {
        return balance;
    }
}

class User implements Runnable {
    private BankAccount account;
    private String userName;

    public User(BankAccount account, String userName) {
        this.account = account;
        this.userName = userName;
    }

    public void run() {
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                account.deposit(userName, 50);
            } else {
                account.withdraw(userName, 60);
            }
        }
    }
}

public class App {
    public static void main(String[] args) throws InterruptedException {
        BankAccount sharedAccount = new BankAccount();

        Thread user1 = new Thread(new User(sharedAccount, "Alice"));
        Thread user2 = new Thread(new User(sharedAccount, "Bob"));

        user1.start();
        user2.start();

        user1.join();
        user2.join();

        System.out.println("Final balance: $" + sharedAccount.getBalance());
    }
}