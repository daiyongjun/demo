## 日常使用单元测试
> [W3Cschool：JUnit](https://www.w3cschool.cn/junit/x6o71hv6.html "JUnit") 

> [Githup：Maven module](https://github.com/zhaoyunxing92/spring-boot-learn-box/blob/master/spring-boot-elasticsearch/pom.xml "maven module")

> [Junit：Junit5 guide](https://junit.org/junit5/docs/current/user-guide/#overview "Junit5 guide")

## 测试 JUnit 建立
```
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * 测试核心类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 14:05
 */
class TestJunit {
    @Test
    void testAdd() {
        String str = "Junit is working fine";
        assertEquals("Junit is working fine", str);
    }
}
```

## JUnit - 基本用法
- 创建一个名为 MessageUtil.java 的类
- 创建一个名为 TestJunit.java 的测试类
- 向测试类中添加名为 testPrintMessage() 的方法。
- 向方法中添加 Annotaion @Test。
- 执行测试条件并且应用 Junit 的 assertEquals API 来检查。

```
package cn.yanwei.study.junit.operation.utils;

/**
 * This class prints the given message on console.
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 15:12
 */
public class MessageUtil {
    private String message;

    /**
     * Constructor
     *
     * @param message to be printed
     */
    public MessageUtil(String message) {
        this.message = message;
    }


    /**
     * prints the message
     */
    public String printMessage() {
        System.out.println(message);
        return message;
    }
}
```

```
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
        assertEquals(message, messageUtil.printMessage());
    }
}
```
在idea中运行，并返回结果。
```
Hello World
```
给message重新定义值，其他不变。
```
    @Test
    void testPrintMessage() {
        message = "New World";
        assertEquals(message, messageUtil.printMessage());
    }
```
在idea中运行，并返回结果。
```
Hello World

org.opentest4j.AssertionFailedError: 
Expected :New World
Actual   :Hello World
<Click to see difference>
```

## JUnit - API
#### JUnit 中的重要的 API
JUnit 中的最重要的程序包是 junit.framework 它包含了所有的核心类。我们主要使用Assert，assert 方法的集合,基本使用中有使用。

序号| 类的名称 |类的功能 
---|---|---
1 | void assertEquals(boolean expected, boolean actual) | 检查两个变量或者等式是否平衡
2| void assertFalse(boolean condition) | 检查条件是假的
3| void assertNotNull(Object object) | 检查对象不是空的
4 |	void assertNull(Object object) | 检查对象是空的
5 |	void assertTrue(boolean condition) | 检查条件为真
6 |	void fail() | 在没有报告的情况下使测试不通过

下面让我们在例子中来测试一下上面提到的一些方法。
```
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
        //the failure
        fail();
    }
}

```

## JUnit - 编写测试
#### JUnit - 编写测试
 
