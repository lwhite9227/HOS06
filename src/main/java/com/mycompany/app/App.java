/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

package com.mycompany.app;

class BankAccount {
    private int balance = 100;

    // Synchronized to prevent race conditions
    public synchronized void withdraw(String user, int amount) {
        if (balance >= amount) {
            System.out.println(user + " is trying to withdraw $" + amount);
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            balance -= amount;
            System.out.println(user + " completed withdrawal. Remaining balance: $" + balance);
        } else {
            System.out.println(user + " tried to withdraw $" + amount + " but insufficient funds. Balance: $" + balance);
        }
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
        for (int i = 0; i < 2; i++) {  // Each user tries to withdraw twice
            account.withdraw(userName, 60);
        }
    }
}

public class App {
    public static void main(String[] args) {
        BankAccount sharedAccount = new BankAccount();

        Thread user1 = new Thread(new User(sharedAccount, "Alice"));
        Thread user2 = new Thread(new User(sharedAccount, "Bob"));

        user1.start();
        user2.start();
    }
}