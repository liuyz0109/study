TTL设置有两种方式：
给队列设置x-message-ttl参数，时间必须为int类型。

给消息设置额外参数。
MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setExpiration("5000");
        message.getMessageProperties().setContentEncoding("UTF-8");
        return message;
    }
};
rabbitTemplate.convertAndSend(exchangeName, routingKey, orderid,messagePostProcessor);

如果两种方式都设置了，是以TTL的时间较短的一个设置为准。

如果设置队列为TTL过期队列的话，里面的消息过期后是可以放入死信队列的；
如果单设置消息为TTL过期消息的话，消息过期就会被直接删除，不会放入死信队列。

首先设置一个TTL过期队列，在队列的参数中绑定死信队列，
那么在消息过期后就会将消息发送至死信交换机，再由死信交换机发送消息到死信队列。

@Bean
public Queue ttlQueueDirect() {
    Map<String, Object> args = new HashMap<>();
    //传入TTL参数
    //时间必须为int类型
    args.put("x-message-ttl", 5000);
    //绑定死信队列
    args.put("x-dead-letter-exchange", "ttl_dead_direct_exchange");
    //绑定死信队列key
    args.put("x-dead-letter-routing-key", "ttlDead");
    return new Queue("ttl.direct.queue", true,false,false,args);
}

其它参数：
1、最大存储数量
//设置最大存储信息数量
args.put("x-max-length", 5);

当信息超过设置的5条时，会将最前面的参数发送到死信队列中。（后进后出）

2、最大存储数量和过期时间
//时间必须为int类型
args.put("x-message-ttl", 5000);
//设置最大存储信息数量
args.put("x-max-length", 5);

当信息超过设置的5条时，会将最前面的参数发送到死信队列中，在过期时间到达时，会将剩下的信息发送至死信队列。


可以通过以上的配置来达到延迟队列的操作，
例如：用户下订单，30分钟内为进行支付，30分钟之后对这个订单进行保存或者销毁等操作。