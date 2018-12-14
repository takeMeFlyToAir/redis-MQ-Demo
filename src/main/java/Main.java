/**
 * Created by zhaozhirong on 2018/12/13.
 */
public class Main {
    public static void main(String[] args) {

        //生产者
        JedisUtil jedisUtil= JedisUtil.getInstance();
        JedisUtil.Lists lists=jedisUtil.new Lists();
        lists.lpush("order","1");
        lists.lpush("order","2");
        lists.lpush("order","3");

        /**
         * 使用rpop实现消息队列
         * 缺点：
         * 1、rpop没有阻塞功能，需要无限轮询查看list队列是否有数据存在，造成资源浪费
         * 2、假如客户端取到数据以后，客户端处理过程中，应用服务器宕机、系统崩溃等会造成消息丢失，因为消息已经
         * 从队列取出，但是没消费成功，会造成资源丢失
         */
//        ConsumerByRPop consumerByLPop = new ConsumerByRPop();
//        consumerByLPop.start();

        /**
         * 使用brpop实现消息队列
         * 缺点：
         * 1、假如客户端取到数据以后，客户端处理过程中，应用服务器宕机、系统崩溃等会造成消息丢失，因为消息已经
         * 从队列取出，但是没消费成功，会造成资源丢失
         *
         * 与rpop对比有以下优点：
         * 优点：
         * 1、brpop有阻塞功能，如果队列中没有消息存在，会阻塞，直到队列中有数据，能够避免资源浪费
         */
//        ConsumerByBRPop consumerByBLPop = new ConsumerByBRPop();
//        consumerByBLPop.start();

        /**
         * 使用brpoplpush实现消息队列
         * 与brpop对比有以下优点：
         * 优点：
         * RPOPLPUSH (或者其阻塞版本的 BRPOPLPUSH） 提供了一种方法来避免这个问题：
         * 消费者端取到消息的同时把该消息放入一个正在处理中的列表。 当消息被处理了之后，
         * 该命令会使用 LREM 命令来移除正在处理中列表中的对应消息。另外，
         * 可以添加一个客户端来监控这个正在处理中列表，如果有某些消息已经在这个列表中存在很长时间了
         * （即超过一定的处理时限）， 那么这个客户端会把这些超时消息重新加入到队列中
         */
        ConsumerByBRPopLPush consumerByBRPopLPush = new ConsumerByBRPopLPush();
        consumerByBRPopLPush.start();
    }
}
