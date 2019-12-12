package cn.yanwei.study.junit.operation.base;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 中的重要的 API
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 15:57
 */
class TestJunit1 {
    @Test
    void testAdd() {
        //test data
        int num = 5;
        String temp = null;
        String str = "Junit is working fine";
        //check for equality
        assertEquals("Junit is working fine", str);
        //check for false condition
        assertFalse(num > 6);
        //check for true condition
        assertTrue(num <= 6);
        //check for not null value
        assertNotNull(str);
        //check for null value
        assertNull(temp);
        fail();
    }
}
