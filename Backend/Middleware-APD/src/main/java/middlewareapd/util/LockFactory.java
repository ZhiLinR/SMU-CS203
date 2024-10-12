// working LockFactory
package middlewareapd.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.*;


/*
 * LockFactory is intended to be used to create & access locks across
 * threads. The locks are named (via a String) and accessed by that
 * name. Internally, there is a map from lockName to lock object.
 *
 * There are 2 types of locks: ReentrantLock and ReentrantReadWriteLock,
 * accessible via getLock and getRWLock respectively.
 */
public class LockFactory {

  /*
    Note that we expect concurrent access to the LockFactory itself!  This means that
    adding new locks and acquiring existing locks must be ... locked!  We do this
    by using the java.util.ConcurrentHashMap which guarantees thread-safe access to the
    underlying map.
   */
  // ------------------------------------------------------------------------------------------
  private static final ConcurrentHashMap<String, ReentrantLock> LOCK_MAP = new ConcurrentHashMap<>();    // for keeping ReentrantLocks

  // get lock (Returns a ReentrantLock)
  // factory singleton method 
  public static Lock getLock(String lockName) {
    Lock lock = LOCK_MAP.putIfAbsent(lockName, new ReentrantLock());
    if (lock == null) {
      lock = LOCK_MAP.get(lockName);
    }
    return lock;
  }

  // ------------------------------------------------------------------------------------------
  private static final ConcurrentHashMap<String, ReentrantReadWriteLock> RWLOCK_MAP = new ConcurrentHashMap<>();  // for keeping ReentrantReadWriteLocks

  // get Read Write lock (Returns a ReentrantReadWriteLock)
  // factory singleton method 
  public static ReadWriteLock getRWLock(String lockName) {
    ReadWriteLock lock = RWLOCK_MAP.putIfAbsent(lockName, new ReentrantReadWriteLock());
    if (lock == null) {
      lock = RWLOCK_MAP.get(lockName);
    }
    return lock;
  }

  // for testing
  public static void main(String[] args) {
    //Test the Factory
    Lock lock = LockFactory.getRWLock("RWL a").readLock();
    assert (lock != null);

    lock = LockFactory.getRWLock("RWL b").writeLock();
    assert (lock != null);

    lock = LockFactory.getLock("RL a");
    assert (lock != null);

  }
}
