package cn.yanwei.study.springboot.mybatis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 创建实体类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/7/29 9:31
 */
@Data
public class User {
    /**
     * 指定主键使用数据库ID自增策略
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String passWord;
}

