import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by LM on 2017/7/14.
 */
public class ExpandablePool {
    private BlockingQueue<Connect> pool = new LinkedBlockingQueue<>();
    private int maxIdleSize = 0;
    private int minIdleSize = 0;
    Timer timer;
    long dropSize = 0;

    public ExpandablePool(int maxPoolSize) {
        init(maxPoolSize);
    }

    public void init(int maxPoolSize) {
        for (int i = 0; i < maxPoolSize; i ++) {
            try {
                this.pool.put(new Connect());
            } catch (InterruptedException e) {
                System.out.println("put connect failed when init!");
            }
        }
//        System.out.println("pool init finished, size "+pool.size());
    }

    public void setMaxIdleSize(int maxIdleSize) {
        this.maxIdleSize = maxIdleSize;
    }

    public void setMinIdleSize(int minIdleSize) {
        this.minIdleSize = minIdleSize;
    }

    public void setDaemonInterval(int interval) {
        if (interval == 0 || maxIdleSize == 0 || minIdleSize == 0)
            return;
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new DaemonTask(pool, minIdleSize), 0, interval);
    }

    public Connect poll() {
        Connect connect = null;
        try {
            connect = this.pool.poll(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("get connect failed!");
        }
        if (connect == null)
            throw new RuntimeException("Pool Busy!");
//        System.out.println(this.pool.size() + " [poll]");
        System.out.println(pool.size());
        return connect;
    }

    public void offer(Connect connect) {
        if (this.pool.size() < maxIdleSize) {
            this.pool.offer(connect);
//            System.out.println(this.pool.size() + " [offer]");
            System.out.println(pool.size());
        } else {
            ++dropSize;
            connect.close();
//            System.out.println(dropSize + " [drop]");
        }
    }

    public void clear() {
        synchronized (pool) {
            if (timer != null)
                timer.cancel();
            pool.clear();
        }
        System.out.println("pool clear, drop "+dropSize);
    }
}
