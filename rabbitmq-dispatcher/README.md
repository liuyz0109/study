派单服务

通过可靠消费解决分布式事务

1、使用try+catch+手动ack+死信
配置中必须开启手动ack
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: manual # 开启手动ack，默认为none，让程序控制消息的重发、删除、转移
          
2、也可以使用重试机制
配置中必须配置重试机制参数
spring:
  rabbitmq:
    listener:
      simple:
        retry:
          enabled: true # 开启重试配置，默认为false
          max-attempts: 5 # 重试次数，默认为3
          initial-interval: 2000ms # 重试间隔时间