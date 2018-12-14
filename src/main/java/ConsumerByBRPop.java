import java.util.List;

/**
 * 使用redis的blpop命令实现消息队列
 * 优点：
 */
public class ConsumerByBRPop extends Thread {
    @Override
    public void run() {
        JedisUtil jedisUtil= JedisUtil.getInstance();
        JedisUtil.Lists lists=jedisUtil.new Lists();
        while (true){
            System.out.println("从redis取数开始");
            List<String> values = lists.brpop(RedisListCons.MESSAGE_LIST_KEY);
            System.out.println("key = "+RedisListCons.MESSAGE_LIST_KEY+",value = "+values.get(1));
        }
    }
}
