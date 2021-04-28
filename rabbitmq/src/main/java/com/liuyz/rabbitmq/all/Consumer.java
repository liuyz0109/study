package com.liuyz.rabbitmq.all;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description 消费者-direct
 */
public class Consumer {

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //创建连接工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            //配置连接参数
            //ip
            connectionFactory.setHost("192.168.141.168");
            //端口
            connectionFactory.setPort(5672);
            //用户名
            connectionFactory.setUsername("liuyz");
            //密码
            connectionFactory.setPassword("123456");
            //访问节点
            connectionFactory.setVirtualHost("/");
            Connection connection = null;
            Channel channel = null;
            try {
                //创建连接
                //连接名称
                connection = connectionFactory.newConnection("生产者");
                //通过连接获取通道
                channel = connection.createChannel();
                //通过创建交换机，声明队列，绑定关系，路由Key，发送消息，接收消息
                //获取当前线程的名称作为队列名
                final String queueName = Thread.currentThread().getName();
                channel.basicConsume(queueName, true, new DeliverCallback() {
                    @Override
                    public void handle(String s, Delivery delivery) throws IOException {
                        System.out.println(queueName + "收到消息：" + new String(delivery.getBody(), "UTF-8"));
                    }
                }, new CancelCallback() {
                    @Override
                    public void handle(String s) throws IOException {
                        System.out.println("接收消息失败");
                    }
                });
                System.out.println(queueName + "开始接收消息！！！");
                //使系统保持运行状态
                System.in.read();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //关闭通道
                if (channel != null && channel.isOpen()) {
                    try {
                        channel.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //关闭连接
                if (connection != null && connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public static void main(String[] args) {
        new Thread(runnable,"queue5").start();
        new Thread(runnable,"queue6").start();
        new Thread(runnable,"queue7").start();
    }

}
