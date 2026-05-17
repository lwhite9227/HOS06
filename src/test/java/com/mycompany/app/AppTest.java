package com.mycompany.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class AppTest {
    public AppTest() {
    }

    @Test
    public void testApp() {
        assertTrue(true);
    }

    @Test
    public void testMore() {
        assertTrue(true);
    }

    @Test
    public void testDepositAndWithdraw() throws InterruptedException {
        BankAccount sharedAccount = new BankAccount();

        Thread user1 = new Thread(new User(sharedAccount, "Alice"));
        Thread user2 = new Thread(new User(sharedAccount, "Bob"));

        user1.start();
        user2.start();

        user1.join();
        user2.join();

        assertEquals(80, sharedAccount.getBalance());
    }
}
