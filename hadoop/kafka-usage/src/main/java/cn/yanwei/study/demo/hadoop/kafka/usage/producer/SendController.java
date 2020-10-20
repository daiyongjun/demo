package cn.yanwei.study.demo.hadoop.kafka.usage.producer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api("数据发送接口")
@RequestMapping("/kafka")
public class SendController {

    private final Producer producer;

    @Autowired
    public SendController(Producer producer) {
        this.producer = producer;
    }

    @ApiOperation("发送数据")
    @GetMapping(value = "/send")
    public String send() {
        producer.send();
        return "{\"code\":0}";
    }
}
