package com.qinweizhao.account.listener;

import com.qinweizhao.account.constant.RabbitConstant;
import com.qinweizhao.account.service.EsService;
import com.qinweizhao.account.utils.Result;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/4/1 10:59
 * </p>
 */

@RabbitListener(queues = RabbitConstant.ACCOUNT_QUEUE)
@Component
public class EventListener {


    @Autowired
    EsService esService;

    @RabbitHandler
    public void listener(Message message, Channel channel, String account) {

        //将数据写入es

        Result result = esService.dbToEs(account);

        if (result.getStatus()==200){
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e) {
                System.out.println("出错了");
            }
        }



//        System.out.println("Account====="+account);
//        System.out.println("Message====="+message);
//        System.out.println("Channel====="+channel);
//        System.out.println("CanalEntry====="+columns);
    }

}
