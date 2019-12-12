package cn.yanwei.study.junit.operation.base;

import cn.yanwei.study.junit.operation.utils.MessageUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit - 基本用法
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 14:05
 */
class TestJunit {

    private String message = "Hello World";
    private MessageUtil messageUtil = new MessageUtil(message);

    @Test
    void testAdd() {
        String str = "Junit is working fine";
        assertEquals("Junit is working fine", str);
    }

    @Test
    void testPrintMessage() {
        message = "New World";
        assertEquals(message, messageUtil.printMessage());
    }
}