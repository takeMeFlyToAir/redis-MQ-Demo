/**
 * 使用redis的blpop命令实现消息队列
 * 优点：
 */
public class ConsumerByBRPopLPush extends Thread {
    @Override
    public void run() {
        JedisUtil jedisUtil = JedisUtil.getInstance();
        JedisUtil.Lists lists = jedisUtil.new Lists();
        while (true) {
            //启动备份队列监控，使用守护进程
            DameonThreadForBackupList dameonThreadForBackupList = new DameonThreadForBackupList();
            dameonThreadForBackupList.setDaemon(true);
            dameonThreadForBackupList.start();
            System.out.println("从redis取数开始");
            String value = lists.brpoplpush(RedisListCons.MESSAGE_LIST_KEY, RedisListCons.MESSAGE_LIST_KEY_BACK_UP);
            System.out.println("key = " + RedisListCons.MESSAGE_LIST_KEY + ",value = " + value);
            //模拟业务处理逻辑
            doSomeThing(value);
        }
    }

    private void doSomeThing(String value) {
        try {
            System.out.println("业务处理");
            /**
             * 模拟出错，当消息值是error的时候，我们模拟业务 逻辑出错，
             * 如果出错，那么orderBackUp消息队列的值就不会被移除
             */
            if (value.equals("error")) {
                System.out.println(1 / 0);
            }
            /**
             * 业务正常处理完毕，从orderBackUp消息队列中删除数据
             */
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Lists lists = jedisUtil.new Lists();
            lists.lrem(RedisListCons.MESSAGE_LIST_KEY_BACK_UP, 1, value);
        } catch (Exception e) {
            //业务出错
            e.printStackTrace();
        }
    }

    private class DameonThreadForBackupList extends Thread {
        @Override
        public void run() {
            JedisUtil jedisUtil = JedisUtil.getInstance();
            JedisUtil.Lists lists = jedisUtil.new Lists();
            /**
             * 守护进程监控备份队列是否有数据，并且此数据存在的时间较长，这里我们需要设置一个超时时间，
             * 假设超过此时间范围，就默认此消息没被正常处理，应该对这个消息做善后处理，至于超时时间的
             * 判断我建议可用内存记录时间戳即可;
             * 善后处理方式：
             * （1）发送通知
             * （2）将数据重新塞进原消息队列
             * 这里因为是模拟数据，为了避免和上面的逻辑造成死循环，我不把数据塞进原消息队列了，只是从
             * 实时输出备份队列中的数据
             *
             * 因为上面的业务逻辑处理需要时间，所以移除操作会稍后，即备份队列会有短暂的数据存储，但是
             * 如果有超过超时时间范围的消息，即可认为是业务逻辑处理失败，需要做善后处理
             */
            while (true) {
                long length = lists.llen(RedisListCons.MESSAGE_LIST_KEY_BACK_UP);
                System.out.println("备份消息队列中的数据有多少个:==========" + length);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
