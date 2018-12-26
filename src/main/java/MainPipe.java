import redis.clients.jedis.Pipeline;

/**
 * Created by zhaozhirong on 2018/12/13.
 */
public class MainPipe {
    public static void main(String[] args) {

        long start = 0;
        long end = 0;
        long count = 100000;
        //生产者
        JedisUtil jedisUtil= JedisUtil.getInstance();
        JedisUtil.Lists lists=jedisUtil.new Lists();
        start = System.currentTimeMillis();
        for (int i =0 ; i < count; i++){
            lists.lpush("goodsClass",String.valueOf(i));
        }
        end = System.currentTimeMillis();
        System.out.println("lpush  used [" + (end-start)/1000 + "] seconds ..");
        Pipeline pipeline  = JedisUtil.getInstance().getJedis().pipelined();
        start = System.currentTimeMillis();
        for (int i =0 ; i < count; i++){
            pipeline.lpush("goods",String.valueOf(i));
        }
        pipeline.sync();
        end = System.currentTimeMillis();
        System.out.println("lpush with pipeline used [" + (end-start)/1000 + "] seconds ..");

    }
}
