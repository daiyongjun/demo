package cn.yanwei.study.demo.hadoop.kafka.usage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;

/**
 * kafka初始化配置
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/7/28 17:09
 */
@Configuration
public class KafkaInitialConfiguration {
    //异常处理器
    @Bean
    public ConsumerAwareListenerErrorHandler myConsumerAwareErrorHandler() {
        return (message, exception, consumer) -> {
            System.out.println("--- 发生消费异常 ---");
            System.out.println(message.getPayload());
            System.out.println(exception);
            return null;
        };
    }
}
