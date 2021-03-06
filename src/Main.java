public class Main {

    public static void main(String[] args) throws InterruptedException {
        ExpandablePool pool = new ExpandablePool(200);
        pool.setMinIdleSize(100);
        pool.setMaxIdleSize(200);
        pool.setDaemonInterval(10);

        for (int i = 0; i < 1800000; i++) { //访问60s
            Consumer consumer = new Consumer(pool);
            consumer.start();
            Consumer consumer2 = new Consumer(pool);
            consumer2.start();
            Thread.currentThread().sleep(1); //2000 TPS
        }
        Thread.currentThread().sleep(15000);
        pool.clear();
    }

    /**
     *
     */
}
