## 1.引入依赖

```xml
<dependency>
     <groupId>com.miss</groupId>
     <artifactId>common-mq</artifactId>
     <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 2.配置

a)resources目录下创建mq.properties文件

```xml
rabbitmq.host=127.0.0.1
rabbitmq.port=5672
rabbitmq.userName=sysdebug
rabbitmq.password=123456
rabbitmq.virtualHost=/jt
```

b)resources目录下创建queues.json 文件

```json
{
  "consumers": [
    {
      "exchangeName": "ex-sdk2",
      "queueName": "ex-test-mul",
      "confirmType": "0",
      "consumerTag": "test",
      "basicOrNack": "0"
    }
  ],
  "producers": [
    {
      "exchangeName": "ex-sdk2",
      "queueName": "ex-test-mul",
      "routingKey": "key1"
    },
    {
      "exchangeName": "ex-sdk2",
      "queueName": "ex-test-mul",
      "routingKey": "key2"
    },
    {
      "exchangeName": "ex-sdk2",
      "queueName": "ex-test-mul",
      "routingKey": "key3"
    }
  ]
}
```

备注：

consumers：消费者配置

| 名称         | 备注                                               |
| ------------ | -------------------------------------------------- |
| exchangeName | 交换机名称                                         |
| queueName    | 队列名称                                           |
| confirmType  | 0：自动确认 1：手动确认                            |
| consumerTag  | 消费者标签                                         |
| basicOrNack  | 手动确认 异常后消息处理，0：返回队列 1：确认消费掉 |

producers：生产者配置

| 名称         | 备注       |
| ------------ | ---------- |
| exchangeName | 交换机名称 |
| queueName    | 队列名称   |
| routingKey   | 路由key    |



## 3.生产者开发示例

```java
@Autowired
private IMqManage mqManage;

ISender sender = mqManage.getSender();
sender.send("exchangeName","routingKey",msg);
```



## 4.消费者开发示例

### 4.1 消费业务开发



```java
package com.sky.common.mq.test.timer;

@Component
//tag 和queues.json中的consumerTag 要一致
@MqConsumer(tag = "test")
public class MsgHandler extends AbstractConsumer {
    @Override
    protected boolean onReceive(String msg) {
        log.info("**********************接收到消息："+msg);
        return true;
    }
}
```



### 4.2 消费控制

```
//引入控制器
@Autowired
private IMqManage mqManage;

//停止消费
mqManage.stopConsumer("test");//消费者标签 

//恢复消费
mqManage.resumeConsumer("test");//消费者标签
```



