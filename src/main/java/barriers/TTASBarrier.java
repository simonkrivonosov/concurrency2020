package barriers;

public class TTASBarrier implements Barrier {

    private final int maxThreadCount;
    private volatile int currentThreadCount;
    private MyLock lock = new TTASLock();

    public TTASBarrier(int maxThreadCount)  {
        this.maxThreadCount = maxThreadCount;
        this.currentThreadCount = 0;
    }

    public void await() {
        lock.lock();
        this.currentThreadCount++;
        lock.unlock();
        while (this.currentThreadCount < this.maxThreadCount) {
        }
    }

}
