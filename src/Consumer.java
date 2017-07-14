/**
 * Created by LM on 2017/7/14.
 */
public class Consumer extends Thread{

    ExpandablePool pool;

    public Consumer(ExpandablePool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        Connect connect = null;
        try {
            connect = pool.poll();
            connect.use();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (connect != null)
            pool.offer(connect);
    }
}
