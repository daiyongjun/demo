package org.nlpcn.es4sql.query;


import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.client.Client;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nlpcn.es4sql.domain.Delete;
import org.nlpcn.es4sql.domain.Query;
import org.nlpcn.es4sql.domain.Where;
import org.nlpcn.es4sql.exception.SqlParseException;

import org.nlpcn.es4sql.query.maker.QueryMaker;

public class DeleteQueryAction extends QueryAction {
	public DeleteQueryAction(Client client, Query query) {
		super(client, query);
	}

	@Override
	public SearchSourceBuilder explain() throws SqlParseException {
		return null;
	}

//	private final Delete delete;
//	private DeleteByQueryRequestBuilder request;
//
//	public DeleteQueryAction(Client client, Delete delete) {
//		super(client, delete);
//		this.delete = delete;
//	}
//
//	@Override
//	public SqlElasticDeleteByQueryRequestBuilder explain() throws SqlParseException {
//		this.request = new DeleteByQueryRequestBuilder(client, DeleteByQueryAction.INSTANCE);
//
//		setIndicesAndTypes();
//		setWhere(delete.getWhere());
//        SqlElasticDeleteByQueryRequestBuilder deleteByQueryRequestBuilder = new SqlElasticDeleteByQueryRequestBuilder(request);
//		return deleteByQueryRequestBuilder;
//	}
//
//	@Override
//	public SearchSourceBuilder explain1() throws SqlParseException {
//		return null;
//	}
//
//
//	/**
//	 * Set indices and types to the delete by query request.
//	 */
//	private void setIndicesAndTypes() {
//
//        DeleteByQueryRequest innerRequest = request.request();
//        innerRequest.indices(query.getIndexArr());
//        String[] typeArr = query.getTypeArr();
//        if (typeArr!=null){
//            innerRequest.getSearchRequest().types(typeArr);
//        }
////		String[] typeArr = query.getTypeArr();
////		if (typeArr != null) {
////            request.set(typeArr);
////		}
//	}
//
//
//	/**
//	 * Create filters based on
//	 * the Where clause.
//	 *
//	 * @param where the 'WHERE' part of the SQL query.
//	 * @throws SqlParseException
//	 */
//	private void setWhere(Where where) throws SqlParseException {
//		if (where != null) {
//			QueryBuilder whereQuery = QueryMaker.explan(where);
//			request.filter(whereQuery);
//		} else {
//			request.filter(QueryBuilders.matchAllQuery());
//		}
//	}

}
