package cn.yanwei.study.elastic.search.query.api;

import cn.yanwei.study.elastic.search.query.api.domain.CriteriaQueryProcessor;
import cn.yanwei.study.elastic.search.query.api.models.Criteria;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;


/**
 * 基本测试类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 16:20
 */
class SearchTest {
    private CriteriaQueryProcessor processor = new CriteriaQueryProcessor();

    /**
     * 测试基础语法规则
     */
    @Test
    void testGrammarRule() {
        //term
        Criteria term = new Criteria("new_tile").is("测试规则");
        String query = processor.createQueryFromCriteria(term).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'term':{'new_tile':{'value':'测试规则','boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");
        //terms
        Criteria terms = new Criteria("new_tile").in((Object[]) new String[]{"测试规则", "测试规则2"});
        query = processor.createQueryFromCriteria(terms).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'terms':{'new_tile':['测试规则','测试规则2'],'boost':1.0}}],'adjust_pure_negative':true,'boost':1.0}}");
        //exclude_terms
        Criteria exclude_terms = new Criteria("new_tile").in((Object[]) new String[]{"测试规则", "测试规则2"}).not();
        query = processor.createQueryFromCriteria(exclude_terms).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must_not':[{'terms':{'new_tile':['测试规则','测试规则2'],'boost':1.0}}],'adjust_pure_negative':true,'boost':1.0}}");

        //greater_than
        Criteria greater_than = new Criteria("news_posttime").greaterThan("2019-06-01T00:00:36");
        query = processor.createQueryFromCriteria(greater_than).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'range':{'news_posttime':{'from':'2019-06-01T00:00:36','to':null,'include_lower':false,'include_upper':true,'boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");


        //less_than
        Criteria less_than = new Criteria("news_posttime").lessThan("2019-12-21T14:02:36");
        query = processor.createQueryFromCriteria(less_than).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'range':{'news_posttime':{'from':null,'to':'2019-12-21T14:02:36','include_lower':true,'include_upper':false,'boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //greater_than_equal
        Criteria greater_than_equal = new Criteria("news_posttime").greaterThanEqual("2019-06-01T00:00:36");
        query = processor.createQueryFromCriteria(greater_than_equal).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'range':{'news_posttime':{'from':'2019-06-01T00:00:36','to':null,'include_lower':true,'include_upper':true,'boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //less_than_equal
        Criteria less_than_equal = new Criteria("news_posttime").lessThanEqual("2019-12-21T14:02:36");
        query = processor.createQueryFromCriteria(less_than_equal).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'range':{'news_posttime':{'from':null,'to':'2019-12-21T14:02:36','include_lower':true,'include_upper':true,'boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //between
        Criteria between = new Criteria("news_posttime").between("2019-06-01T00:00:36", "2019-12-21T14:02:36");
        query = processor.createQueryFromCriteria(between).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'range':{'news_posttime':{'from':'2019-06-01T00:00:36','to':'2019-12-21T14:02:36','include_lower':true,'include_upper':true,'boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //match
        Criteria match = new Criteria("news_content").match("测试");
        query = processor.createQueryFromCriteria(match).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'match':{'news_content':{'query':'测试','operator':'OR','prefix_length':0,'max_expansions':50,'fuzzy_transpositions':true,'lenient':false,'zero_terms_query':'NONE','auto_generate_synonyms_phrase_query':true,'boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //match_phrase
        Criteria match_phrase = new Criteria("news_content").phrase("测试");
        query = processor.createQueryFromCriteria(match_phrase).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'match_phrase':{'news_content':{'query':'测试','slop':0,'zero_terms_query':'NONE','boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //starts_with
        Criteria starts_with = new Criteria("news_content").startsWith("测试");
        query = processor.createQueryFromCriteria(starts_with).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'wildcard':{'news_content':{'wildcard':'测试*','boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //ends_with
        Criteria ends_with = new Criteria("news_content").endsWith("测试");
        query = processor.createQueryFromCriteria(ends_with).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'wildcard':{'news_content':{'wildcard':'*测试','boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //contains
        Criteria contains = new Criteria("news_content").contains("测试");
        query = processor.createQueryFromCriteria(contains).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'wildcard':{'news_content':{'wildcard':'*测试*','boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");

        //fuzzy
        Criteria fuzzy = new Criteria("news_content").fuzzy("测试");
        query = processor.createQueryFromCriteria(fuzzy).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'fuzzy':{'news_content':{'value':'测试','fuzziness':'AUTO','prefix_length':0,'max_expansions':50,'transpositions':false,'boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");
    }

    /**
     * 测试简单逻辑关系的使用【filter暂时并不支持】
     */
    @Test
    void testLogicRelation() {
        //must
        Criteria must = new Criteria("new_tile").is("测试规则");
        String query = processor.createQueryFromCriteria(must).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'term':{'new_tile':{'value':'测试规则','boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");
        //should
        Criteria should = new Criteria("new_tile").is("测试规则").or();
        query = processor.createQueryFromCriteria(should).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'should':[{'term':{'new_tile':{'value':'测试规则','boost':1.0}}}],'adjust_pure_negative':true,'minimum_should_match':'1','boost':1.0}}");
        //must not
        Criteria must_not = new Criteria("new_tile").is("测试规则").not();
        query = processor.createQueryFromCriteria(must_not).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must_not':[{'term':{'new_tile':{'value':'测试规则','boost':1.0}}}],'adjust_pure_negative':true,'boost':1.0}}");
    }

    /**
     * 测试简单逻辑关系的使用【filter暂时并不支持和must not】
     */
    @Test
    void testComplexLogicRelation() {
        //must[grammar...]
        Criteria must = new Criteria().and("news_title").phrase("测试1").and("news_content").phrase("测试2");
        String query = processor.createQueryFromCriteria(must).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'match_phrase':{'news_title':{'query':'测试1','slop':0,'boost':1.0}}},{'match_phrase':{'news_content':{'query':'测试2','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'boost':1.0}}");

        //should[grammar...]
        Criteria should = new Criteria().or("news_title").phrase("测试1").or("news_content").phrase("测试2");
        query = processor.createQueryFromCriteria(should).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'should':[{'match_phrase':{'news_title':{'query':'测试1','slop':0,'boost':1.0}}},{'match_phrase':{'news_content':{'query':'测试2','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'minimum_should_match':'1','boost':1.0}}");

        //must[should[grammar...],should[grammar...]]
        Criteria c_must = new Criteria().and(should).and(should);
        query = processor.createQueryFromCriteria(c_must).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'bool':{'should':[{'match_phrase':{'news_title':{'query':'测试1','slop':0,'boost':1.0}}},{'match_phrase':{'news_content':{'query':'测试2','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'minimum_should_match':'1','boost':1.0}},{'bool':{'should':[{'match_phrase':{'news_title':{'query':'测试1','slop':0,'boost':1.0}}},{'match_phrase':{'news_content':{'query':'测试2','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'minimum_should_match':'1','boost':1.0}}],'disable_coord':false,'adjust_pure_negative':true,'boost':1.0}}");

        //should[must[grammar...],must[grammar...]]
        Criteria c_should = new Criteria().or(must).or(must);
        query = processor.createQueryFromCriteria(c_should).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'should':[{'bool':{'must':[{'match_phrase':{'news_title':{'query':'测试1','slop':0,'boost':1.0}}},{'match_phrase':{'news_content':{'query':'测试2','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'boost':1.0}},{'bool':{'must':[{'match_phrase':{'news_title':{'query':'测试1','slop':0,'boost':1.0}}},{'match_phrase':{'news_content':{'query':'测试2','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'boost':1.0}}],'disable_coord':false,'adjust_pure_negative':true,'minimum_should_match':'1','boost':1.0}}");


        //实战 我们去video中查找相关的vivo和oppo的相关的文档
        //title中
        Criteria title = new Criteria().and("news_title").phrase("vivo").and("news_title").phrase("oppo");
        //content中
        Criteria content = new Criteria().and("news_content").phrase("vivo").and("news_content").phrase("oppo");
        Criteria criteria = new Criteria().or(title).or(content).and("news_posttime").between("2019-01-01T00:00:00", "2020-01-11T00:00:00");
        query = processor.createQueryFromCriteria(criteria).toString();
        System.out.println(query);
        assertEquals(StringUtil.messageString(query), "{'bool':{'must':[{'range':{'news_posttime':{'from':'2019-01-01T00:00:00','to':'2020-01-11T00:00:00','include_lower':true,'include_upper':true,'boost':1.0}}}],'should':[{'bool':{'must':[{'match_phrase':{'news_title':{'query':'vivo','slop':0,'boost':1.0}}},{'match_phrase':{'news_title':{'query':'oppo','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'boost':1.0}},{'bool':{'must':[{'match_phrase':{'news_content':{'query':'vivo','slop':0,'boost':1.0}}},{'match_phrase':{'news_content':{'query':'oppo','slop':0,'boost':1.0}}}],'disable_coord':false,'adjust_pure_negative':true,'boost':1.0}}],'disable_coord':false,'adjust_pure_negative':true,'minimum_should_match':'1','boost':1.0}}");
    }
}
