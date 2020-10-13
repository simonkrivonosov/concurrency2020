import barriers.MyLock;

public class SimpleReadWriteLock implements ReadWriteLock {

    MyLock readLock, writeLock;
    private boolean hasWriter;
    private int readers;

    public SimpleReadWriteLock() {
        readLock = new ReadMyLock();
        writeLock = new WriteMyLock();
    }

    @Override
    public MyLock ReadLock() {
        return readLock;
    }

    @Override
    public MyLock WriteLock() {
        return writeLock;
    }

    public class ReadMyLock implements MyLock {
        @Override
        public void lock() {
            synchronized (SimpleReadWriteLock.this) {
                while (hasWriter) {
                    try {
                        SimpleReadWriteLock.this.wait();
                    } catch (InterruptedException ignore) {
                    }
                    ++readers;
                }
            }
        }

        @Override
        public void unlock() {
            synchronized (SimpleReadWriteLock.this) {
                --readers;
                if (readers == 0) {
                    SimpleReadWriteLock.this.notifyAll();
                }
            }
        }
    }

    public class WriteMyLock implements MyLock {
        @Override
        public void lock() {
            synchronized (SimpleReadWriteLock.this) {
                while (hasWriter || readers > 0) {
                    try {
                        SimpleReadWriteLock.this.wait();
                    } catch (InterruptedException ignore) {
                    }
                }

                hasWriter = true;
            }
        }

        @Override
        public void unlock() {
            synchronized (SimpleReadWriteLock.this) {
                hasWriter = false;
                SimpleReadWriteLock.this.notifyAll();
            }
        }
    }

}
