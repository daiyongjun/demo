package cn.yanwei.study.elastic.search.query.sql.executors;


/**
 * 自定义解析结果异常
 *
 * @author yanwei
 */
class CsvExtractorException extends Exception {
    CsvExtractorException(String message) {
        super(message);
    }
}