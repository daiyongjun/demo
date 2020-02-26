package cn.yanwei.study.elastic.search.query.sql.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents abstract query. every query
 * has indexes, types, and where clause.
 * @author yanwei
 */
public abstract class BaseQuery {

	private Where where = null;
	private List<From> from = new ArrayList<>();


	public Where getWhere() {
		return this.where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public List<From> getFrom() {
		return from;
	}
}
