package cn.yanwei.study.demo.hadoop.kafka.usage.producer;

import lombok.Data;

import java.util.Date;

/**
 * 消息信息
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/6/11 15:47
 */
@Data
class Message {
    private String id;
    private String msg;
    private Date sendTime;
}
