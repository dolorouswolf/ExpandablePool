import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

/**
 * Created by LM on 2017/7/14.
 */
public class DaemonTask extends TimerTask {
    private BlockingQueue pool;
    private int minIdleSize;

    public DaemonTask(BlockingQueue pool, int minIdleSize) {
        this.pool = pool;
        this.minIdleSize = minIdleSize;
    }

    @Override
    public void run() {
        if (pool == null)
            return;
        while (pool.size() < minIdleSize) {
            try {
//                Thread.currentThread().sleep(200); //模拟先拿到连接再放入Pool
                pool.put(new Connect());
//                System.out.println(pool.size() + " [daemon put]");
                System.out.println(pool.size());
            } catch (InterruptedException e) {
                System.out.println("[daemon put connect failed]");
                break;
            }
        }
    }
}
