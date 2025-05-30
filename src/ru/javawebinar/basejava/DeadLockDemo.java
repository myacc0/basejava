package ru.javawebinar.basejava;

public class DeadLockDemo {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {

        Thread worker1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " get lock1...");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(Thread.currentThread().getName() + " trying to get lock2...");
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " get lock2...");
                }
            }
        }, "worker1");

        Thread worker2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " get lock2...");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(Thread.currentThread().getName() + " trying to get lock1...");
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " get lock1...");
                }
            }
        }, "worker2");


        worker1.start();
        worker2.start();
    }
}
