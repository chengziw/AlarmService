package com.miss.common.mq.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.miss.common.mq.manage.MqManage;
import com.miss.common.mq.bean.ConsumerConfig;
import com.miss.common.mq.bean.ProducerConfig;
import com.miss.common.mq.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@ComponentScan("com.miss.common.mq")
public class MqConfiguration {

    /**
     * ip
     */
    private static String host;

    /**
     * 端口
     */
    private static int port;

    /**
     * 用户名
     */
    private static String userName;

    /**
     * 密码
     */
    private static String password;

    /**
     * 虚拟host
     */
    private static String virtualHost;

    static {
        //加载 mq.properties文件
        ResourceBundle bundle = ResourceBundle.getBundle("mq");

        //初始化数据
        host = bundle.getString("rabbitmq.host");
        port = Integer.valueOf(bundle.getString("rabbitmq.port"));
        userName = bundle.getString("rabbitmq.userName");
        password = bundle.getString("rabbitmq.password");
        virtualHost = bundle.getString("rabbitmq.virtualHost");

    }

    @Bean
    public MqManage defaultMqManage() {
        //配置bean
        MqConfig config = new MqConfig(host, port, userName, password, virtualHost);
        queuesInit();
        //默认管理器
        MqManage mqManage = MqManage.getInstance(config);

        //生产者队列绑定
        mqManage.bindProducers();
        return mqManage;
    }

    private void queuesInit() {
        try {
            JSONObject configs = JsonUtils.readQueuesFromClassPath("queues.json");
            System.out.println(">>>>>>>>>>>>>>>>>加载mq配置》》》》》》》》》》》》》》》》》》" + JSON.toJSONString(configs));
            JSONArray producers = configs.getJSONArray("producers");
            if (producers != null) {
                List<ProducerConfig> producerConfigs = producers.toJavaList(ProducerConfig.class);
                QueuesPool.setProducerConfig(producerConfigs.stream().collect(Collectors.toMap(producer -> (producer.getExchangeName() + "." + producer.getRoutingKey()), producer -> producer)));
            }

            JSONArray consumers = configs.getJSONArray("consumers");
            if (consumers != null) {
                List<ConsumerConfig> consumerConfigs = consumers.toJavaList(ConsumerConfig.class);
                QueuesPool.setConsumerConfig(consumerConfigs.stream().collect(Collectors.toMap(ConsumerConfig::getConsumerTag, queue -> queue)));
            }
        } catch (Exception e) {
            log.error("common-mq",null,e,"【初始化】 未获取到queues.json数据");
        }
    }
}
