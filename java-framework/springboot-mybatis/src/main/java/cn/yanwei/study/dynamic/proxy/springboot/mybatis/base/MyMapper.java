package cn.yanwei.study.dynamic.proxy.springboot.mybatis.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Mapper基础类
 *
 * @author daiyongjun
 * @version 1.0
 * @date 2019/1/2 9:39
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
