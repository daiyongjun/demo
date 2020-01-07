package cn.yanwei.study.dynamic.proxy.elastic.search.query.api.model;

import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.annotation.Bool;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.annotation.DocumentType;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.annotation.EsField;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.annotation.Index;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.constant.MatchType;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.constant.EsSearchType;
import lombok.Data;

/**
 * Created by yemengying on 16/4/23.
 */
@Index("index")
@DocumentType("document")
@Data
public class EsModel {

    //针对id字段进行TERM查询 匹配要求是MUST
    @EsField("id")
    @Bool(value = MatchType.MUST, type = EsSearchType.TERM)
    private Integer id;

    //针对user_name字段进行TERM查询 匹配要求是MUST_NOT
    @EsField("user_name")
    @Bool(value = MatchType.MUST, type = EsSearchType.TERM)
    private String name;

    //针对user_phone字段进行MATCH查询 匹配要求是MUST_NOT
    @EsField("user_phone")
    @Bool(value = MatchType.MUST_NOT, type = EsSearchType.MATCH)
    private String phone;

    @Override
    public String toString() {
        return "EsModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
