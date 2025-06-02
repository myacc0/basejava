package ru.javawebinar.basejava;

public class DeadLockDemo {
    private static final Lock lock1 = new Lock("lock1");
    private static final Lock lock2 = new Lock("lock2");

    public static void main(String[] args) {
        Thread worker1 = new Thread(() -> executeWithLocks(lock1, lock2), "worker1");
        Thread worker2 = new Thread(() -> executeWithLocks(lock2, lock1), "worker2");

        worker1.start();
        worker2.start();
    }

    private static void executeWithLocks(Object firstLock, Object secondLock) {
        synchronized (firstLock) {
            System.out.println(Thread.currentThread().getName() + " got " + firstLock + "...");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + " trying to get " + secondLock + "...");
            synchronized (secondLock) {
                System.out.println(Thread.currentThread().getName() + " got " + secondLock + "...");
            }
        }
    }

    static class Lock {
        private String name;

        public Lock(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
