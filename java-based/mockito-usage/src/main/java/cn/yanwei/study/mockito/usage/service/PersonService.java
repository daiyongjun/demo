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