package com.qinweizhao.account.config;
import com.qinweizhao.account.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MQConfig {



    //@Bean Binding，Queue，Exchange

    /**
     * 容器中的 Binding，Queue，Exchange 都会自动创建（RabbitMQ没有的情况）
     * RabbitMQ 只要有。@Bean声明属性发生变化也不会覆盖
     * @return
     */

    @Bean
    public Queue accountQueue() {
        Queue queue = new Queue(RabbitConstant.ACCOUNT_QUEUE, true, false, false);
        return queue;
    }

    @Bean
    public Exchange accountEventExchange() {
        //String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
       return new TopicExchange(RabbitConstant.ACCOUNT_EXCHANGE,true,false);
    }

    @Bean
    public Binding accountBinding() {
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			Map<String, Object> arguments
        return new Binding(RabbitConstant.ACCOUNT_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitConstant.ACCOUNT_EXCHANGE,
                RabbitConstant.ACCOUNT_KEY,
                null);
    }

}
