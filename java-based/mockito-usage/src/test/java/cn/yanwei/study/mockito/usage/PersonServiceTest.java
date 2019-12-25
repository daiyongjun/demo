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
