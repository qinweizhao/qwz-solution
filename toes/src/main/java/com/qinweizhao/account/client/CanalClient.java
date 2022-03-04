package com.qinweizhao.account.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.qinweizhao.account.constant.RabbitConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/4/1 11:32
 * </p>
 */

@Component
public class CanalClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void run() {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.56.10",
                11111), "example", "", "");
        int batchSize = 1000;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }
                // 提交确认
                connector.ack(batchId);
                // 处理失败, 回滚数据
                // connector.rollback(batchId);
            }

        } finally {
            connector.disconnect();
        }
    }

    private void printEntry(List<Entry> entryList) {
        for (Entry entry : entryList) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    String account = printColumn(rowData.getAfterColumnsList());
                    System.out.println(account);

                    //TODO 一定将消息发出去
                    try {
                        rabbitTemplate.convertAndSend(RabbitConstant.ACCOUNT_EXCHANGE, RabbitConstant.ACCOUNT_KEY, account);
                    } catch (Exception e) {
                        //TODO 将没法送成功的消息进行重试发送。
                    }

                } else if (eventType == EventType.UPDATE) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------&gt; before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------&gt; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static String printColumn(List<Column> columns) {
        Map<String, String> map = new HashMap<>();
        for (Column column : columns) {
            map.put(column.getName(), column.getValue());
        }
        String s = JSON.toJSONString(map);
        return s;
    }

}