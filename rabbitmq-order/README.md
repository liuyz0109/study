订单服务

通过可靠生产解决分布式事务
使用冗余表、rabbitmq的确认机制

配置中必须开启确认机制类型
spring:
  rabbitmq:
    publisher-confirm-type: correlated