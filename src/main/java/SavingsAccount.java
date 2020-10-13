import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SavingsAccount {
    int balance;
    int preferredWaiting;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    void withdraw(boolean preferred, int amount) throws InterruptedException {
        lock.lock ();
        try {
            if(preferred) {
                preferredWaiting++;
                while(preferredWaiting > 1) {
                    condition.await();
                }
                --preferredWaiting;
            } else {
                while(preferredWaiting > 0) {
                    condition.await();
                }
            }
            balance -= amount;
        }
        finally {
            lock.unlock();
            condition.signalAll();
        }
    }
    void deposit(int amount) {
        lock.lock();
        try {
            balance += amount;
        }
        finally {
            lock.unlock();
        }
    }
}
