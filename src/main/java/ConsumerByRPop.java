import java.util.List;

/**
 * 使用redis的blpop命令实现消息队列
 * 优点：
 */
public class ConsumerByRPop extends Thread {
    @Override
    public void run() {
        JedisUtil jedisUtil= JedisUtil.getInstance();
        JedisUtil.Lists lists=jedisUtil.new Lists();
        while (true){
            System.out.println("从redis取数开始");
            String value = lists.rpop(RedisListCons.MESSAGE_LIST_KEY);
            if(value != null){
                System.out.println("key = "+RedisListCons.MESSAGE_LIST_KEY+",value = "+value);
            }else{
                System.out.println("队列不存在数据");
            }
        }
    }
}
