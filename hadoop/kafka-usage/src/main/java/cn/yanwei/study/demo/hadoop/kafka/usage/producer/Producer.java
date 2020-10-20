package cn.yanwei.study.demo.hadoop.kafka.usage.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/6/11 15:46
 */
@Component
public class Producer {
    private final KafkaTemplate kafkaTemplate;
    private static Gson gson = new GsonBuilder().create();

    @Autowired
    public Producer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    void send() {
        Message message = new Message();
        message.setId("test_" + System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        kafkaTemplate.send("test_offline_task", gson.toJson(message));
    }
}
