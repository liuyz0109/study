package com.liuyz.rabbitmq.all;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description 生产者-direct模式-完全由代码进行控制交换机、队列、交换机和队列的关系
 */
public class Producer {

    public static void main(String[] args) {
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
            //路由key
            String routingKey = "email";
            //交换机类型
            String type = "direct";
            //交换机名称
            String exchangeName = "direct-all-exchange";
            //消息内容
            String msg = "Hello Rabbitmq!!!";

            //创建交换机
            /*
             * param1 交换机名称
             * param2 交换机类型
             * param3 是否持久化
             */
            channel.exchangeDeclare(exchangeName, type, true);

            //声明队列
            /*
             * param1 队列名称
             * param2 是否持久化，持久化即存盘
             * param3 是否有排他性，独占独立
             * param4 是否自动删除，随着最后一个消息被消费，是否删除该队列
             * param5 携带的参数
             */
            channel.queueDeclare("queue5",true,false,false,null);
            channel.queueDeclare("queue6",true,false,false,null);
            channel.queueDeclare("queue7",true,false,false,null);

            //绑定关系
            /*
             * param1 队列名称
             * param2 交换机名称
             * param3 路由key
             */
            channel.queueBind("queue5",exchangeName,"email");
            channel.queueBind("queue6",exchangeName,"weichar");
            channel.queueBind("queue7",exchangeName,"email");

            //发送消息给队列queue
            /*
             * param1 交换机名称
             * param2 路由key
             * param3 携带参数
             * param4 消息内容
             */
            channel.basicPublish(exchangeName, routingKey,null, msg.getBytes());
            System.out.println("消息发送成功！！！");
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

}
