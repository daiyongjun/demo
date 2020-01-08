package cn.yanwei.study.dynamic.proxy.elastic.search.query.api.annotation;

import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.constant.SortType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * show the search result is order by the field
 * can only be used on class field
 * Created by yemengying on 15/11/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sort {

        SortType type() default SortType.ASC;//order type ,default ASC

}
