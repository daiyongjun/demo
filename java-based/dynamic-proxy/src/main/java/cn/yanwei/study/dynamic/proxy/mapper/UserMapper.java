package cn.yanwei.study.dynamic.proxy.mapper;

import cn.yanwei.study.dynamic.proxy.model.User;

/**
 * 用户mapper的基础类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/19 15:29
 */
public interface UserMapper {
    User getUserById(Integer id);
}
