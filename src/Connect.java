import java.util.Random;

/**
 * Created by LM on 2017/7/14.
 */
public class Connect {
    private boolean isFirst = true;
    private int firstCost = 200;
    private int normalCost = 50;
    private int failCost = 10000;
    private double failPercent = 1.0/8.0;

    public void use() throws InterruptedException {
        if (isFirst) {
            isFirst = false;
            Thread.currentThread().sleep(firstCost); //建立连接花费时间
        }
        Random random = new Random();
        int c = random.nextInt(10000);
        if (c > failPercent * 10000) { // 有7/8的概率可以正常访问
            Thread.currentThread().sleep(normalCost);
        }
        else { // 1/8的概率不能正常访问，重试10s后该连接不可用，下次使用该连接还需重新建立连接
            Thread.currentThread().sleep(failCost);
            isFirst = true;
        }
    }

    public void close() {

    }
}
