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
