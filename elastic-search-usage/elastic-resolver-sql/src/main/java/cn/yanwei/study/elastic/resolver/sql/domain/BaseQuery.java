package cn.yanwei.study.elastic.resolver.sql.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * query基础类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/2/14 10:04
 */
@Data
public abstract class BaseQuery {
    private Where where = null;
    private final List<From> from = new ArrayList<>();
    private int offset;
    private int rowCount = -1;
}
