package cn.yanwei.study.dynamic.proxy.springboot.mybatis.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * POJO类
 *
 * @author daiyongjun
 * @version 1.0
 * @date 2018/11/23 17:11
 */
@Data
@Accessors(chain = true)
public class Operate {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 任务状态  1：未执行 2：已经执行 3：执行成功 4：执行失败 5：过期文件已删除
     */
    private Integer status;

    /**
     * 类型:1.通用文章下载，2全网提及量查询,3.微博账号查询,4指定网站下载
     */
    private String type;

}
