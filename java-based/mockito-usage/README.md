>[博客：单元测试系列：Mock工具之Mockito实战](https://www.cnblogs.com/zishi/p/6780719.html "单元测试系列：Mock工具之Mockito实战")

>[博客：JUnit 5 Jupiter API](https://www.ibm.com/developerworks/cn/java/j-introducing-junit5-part1-jupiter-api/index.html "JUnit 5 Jupiter API")


#### 概述
在使用过程中了解Junit和JUnit Jupiter的区别，
JUnit Jupiter 是使用 JUnit 5 编写测试内容的 API。
JUnit 5 是一个项目名称（和版本），其 3 个主要模块关注不同的方面：JUnit Jupiter、JUnit Platform 和 JUnit Vintage。


#### mock的使用
##### maven
```
<properties>
	<mock.version>2.7.19</mock.version>
</properties>s

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>RELEASE</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>${lombok.version}</version>
    <scope>test</scope>
</dependency>
```
##### domain代码
```
package cn.yanwei.study.mockito.usage.dao;

import cn.yanwei.study.mockito.usage.modules.Person;

/**
 * 人员处理类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/25 9:56
 */
public interface PersonDao {
    /**
     * 指定信息获取人员信息
     *
     * @param id 人员id
     * @return person
     */
    Person getPerson(int id);

    /**
     * 更新人员信息
     *
     * @param person 待更新的 person对象
     * @return boolean
     */
    boolean update(Person person);
}
```
```
package cn.yanwei.study.mockito.usage.modules;

import lombok.Data;

/**
 * POJO类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/25 9:54
 */
@Data
public class Person {
    private final int id;
    private final String name;
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
```
```
package cn.yanwei.study.mockito.usage.service;

import cn.yanwei.study.mockito.usage.dao.PersonDao;
import cn.yanwei.study.mockito.usage.modules.Person;

/**
 * 服务处理类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/25 9:58
 */
public class PersonService {
    private final PersonDao personDao;
    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }
    public boolean update(int id, String name) {
        Person person = personDao.getPerson(id);
        if (person == null)
        { return false; }
        Person personUpdate = new Person(person.getId(), name);
        return personDao.update(personUpdate);
    }
}
```
##### mockito usage
mock基础使用：定义对象的包括接口的实例化，并使用mock自带的API进行定义接口或者对象的方法的行为
```
package cn.yanwei.study.mockito.usage;

import cn.yanwei.study.mockito.usage.dao.PersonDao;
import cn.yanwei.study.mockito.usage.modules.Person;
import cn.yanwei.study.mockito.usage.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * 服务层测试类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/25 9:59
 */
class PersonServiceTest {
    private PersonDao mockDao;
    private PersonService personService;

    @BeforeEach
    void setUp() {
        //模拟PersonDao对象
        mockDao = mock(PersonDao.class);
        when(mockDao.getPerson(1)).thenReturn(new Person(1, "Person1"));
        when(mockDao.update(isA(Person.class))).thenReturn(true);
        personService = new PersonService(mockDao);
    }

    @Test
    void testUpdate() {
        boolean result = personService.update(1, "new name");
        assertTrue(result);
        //验证是否执行过一次getPerson(1)
        verify(mockDao, times(1)).getPerson(eq(1));
        //验证是否执行过一次update
        verify(mockDao, times(1)).update(isA(Person.class));
    }
}
```