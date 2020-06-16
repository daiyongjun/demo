package cn.yanwei.study.demo.hadoop.kafka.usage.producer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口服务
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/6/11 15:45
 */
@RestController
@RequestMapping("/kafka")
public class SendController {

    private final Producer producer;

    public SendController(Producer producer) {
        this.producer = producer;
    }

    @RequestMapping(value = "/send")
    public String send() {
        producer.send();
        return "{\"code\":0}";
    }
}
