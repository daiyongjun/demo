package cn.yanwei.study.demo.hadoop.kafka.usage.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 消费者模块
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/6/11 15:19
 */
@Component
public class Consumer {
    @KafkaListener(id = "test-consumer", topics = {"test_offline_task"},groupId = "test_offline_task")
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.out.println(Thread.currentThread().getName() + "---->" + record + "---->" + message);
        }
    }
}
