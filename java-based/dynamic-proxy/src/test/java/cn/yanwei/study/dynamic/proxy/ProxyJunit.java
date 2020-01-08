package cn.yanwei.study.dynamic.proxy;

import cn.yanwei.study.dynamic.proxy.mapper.UserMapper;
import cn.yanwei.study.dynamic.proxy.model.User;
import cn.yanwei.study.dynamic.proxy.proxy.MapperProxy;
import org.junit.jupiter.api.Test;

/**
 * 动态代理测试类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/19 15:36
 */
class ProxyJunit {
    @Test
    void proxyJunit() {
        MapperProxy proxy = new MapperProxy();
        UserMapper mapper = proxy.newInstance(UserMapper.class);
        User user = mapper.getUserById(1001);

        System.out.println("ID:" + user.getId());
        System.out.println("Name:" + user.getName());
        System.out.println("Age:" + user.getAge());

        System.out.println(mapper.toString());
    }
}
