package barriers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test {
    static void foo() {
        System.out.println("foo()");
    }
    static void bar() {
        System.out.println("bar()");
    }

    public static void main(String[] args) throws InterruptedException {
//        final TTASBarrier barrier1 = new TTASBarrier(10);
//        for (int i = 0; i < 10; ++i) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                foo();
//                barrier1.await();
//                bar();
//            }).start();
//        }
        final TTASBarrier barrier1 = new TTASBarrier(10);
        final ArrayBarrier barrier2 = new ArrayBarrier(10);
        List<Thread> threads = new ArrayList<>();
        long begin = System.nanoTime();
        testBarrier(barrier1, threads);
        try {
            for(Thread t : threads) {
               t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println((double) (end - begin));
    }

    private static void testBarrier(Barrier barrier, List<Thread> threads) {

        for (int i = 0; i < 10; ++i) {
            int finalI = i;
            threads.add(new Thread(() -> {
                Thread.currentThread().setName(String.valueOf(finalI));
                foo();
                barrier.await();
                bar();
            }));
            threads.get(i).start();
        }
    }

    private static double getTime(Barrier barrier, List<Thread> threads) {
        for (int i = 0; i < 2; i++) { //прогрев JVM
            testBarrier(barrier, threads);
        }
        int count = 2; //первоначальное кол-во повтора выполнения testMethod

        while (true) {
            long begin = System.nanoTime();

            for (int i = 0; i < count; i++)
                testBarrier(barrier, threads);

            long end = System.nanoTime();

            return (double) (end - begin) / count;
        }
    }
}
