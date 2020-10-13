import barriers.MyLock;

public interface ReadWriteLock {
    MyLock ReadLock();
    MyLock WriteLock();
}
