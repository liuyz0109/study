Fanout是路由广播的形式,将消息发给绑定它的全部队列,
即便设置了bindingkey,也会被忽略，所有绑定的队列都会接收到消息。