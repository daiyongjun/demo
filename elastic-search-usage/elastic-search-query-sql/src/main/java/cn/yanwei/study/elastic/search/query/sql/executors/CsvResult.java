package cn.yanwei.study.elastic.search.query.sql.executors;

import lombok.Data;

import java.util.List;


/**
 * 返回CSV的结果集合
 *
 * @author yanwei
 */
@Data
public class CsvResult {
    private final List<String> headers;
    private final List<String> lines;

    CsvResult(List<String> headers, List<String> lines) {
        this.headers = headers;
        this.lines = lines;
    }
}
