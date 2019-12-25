package cn.yanwei.study.dynamic.proxy.springboot.mybatis.mapper;

import cn.yanwei.study.dynamic.proxy.springboot.mybatis.base.MyMapper;
import cn.yanwei.study.dynamic.proxy.springboot.mybatis.model.Operate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 操作类
 *
 * @author daiyongjun
 * @version 1.0
 * @date 2018/11/23 17:52
 */
@Mapper
@Component
public interface OperateMapper extends MyMapper<Operate> {
}
