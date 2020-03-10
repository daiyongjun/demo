package cn.yanwei.study.dynamic.proxy.springboot.mybatis;

import cn.yanwei.study.dynamic.proxy.springboot.mybatis.mapper.OperateMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ApplicationTests {
    @Autowired
    OperateMapper operateMapper;

    @Test
    void contextLoads() {
        assertEquals(operateMapper.selectAll().toString(), "select * from operate");
    }
}
