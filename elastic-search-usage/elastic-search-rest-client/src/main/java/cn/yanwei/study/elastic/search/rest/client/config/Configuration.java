package cn.yanwei.study.elastic.search.rest.client.config;

import lombok.Data;

/**
 * 配置参数
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/10/19 14:49
 */
@Data
public class Configuration {
    private String username;
    private String password;
    private String host;
}
