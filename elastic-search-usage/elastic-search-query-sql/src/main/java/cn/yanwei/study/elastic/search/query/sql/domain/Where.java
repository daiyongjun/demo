package cn.yanwei.study.elastic.search.query.sql.domain;

import java.util.LinkedList;

public class Where implements Cloneable {

    public enum CONN {
        AND, OR;

        public CONN negative() {
            return this == AND ? OR : AND;
        }
    }

    public static Where newInstance() {
        return new Where(CONN.AND);
    }

    private LinkedList<Where> wheres = new LinkedList<>();

    protected CONN conn;

    public Where(String connStr) {
        this.conn = CONN.valueOf(connStr.toUpperCase());
    }

    public Where(CONN conn) {
        this.conn = conn;
    }

    public void addWhere(Where where) {
        wheres.add(where);
    }

    public CONN getConn() {
        return this.conn;
    }

    public void setConn(CONN conn) {
        this.conn = conn;
    }

    public LinkedList<Where> getWheres() {
        return wheres;
    }

    private Long startStamp;

    public void setStartStamp(Long stamp) {
        if (this.startStamp == null) {
            this.startStamp = stamp;
        } else if (stamp < this.startStamp) {
            this.startStamp = stamp;
        }
    }

    public Long getStartStamp() {
        return this.startStamp;
    }

    private Long endStamp;

    public void setEndStamp(Long stamp) {
        if (this.endStamp == null) {
            this.endStamp = stamp;
        } else if (stamp > this.endStamp) {
            this.endStamp = stamp;
        }
    }

    public Long getEndStamp() {
        return this.endStamp;
    }

    @Override
    public String toString() {
        if (wheres.size() > 0) {
            String whereStr = wheres.toString();
            return this.conn + " ( " + whereStr.substring(1, whereStr.length() - 1) + " ) ";
        } else {
            return "";
        }

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Where clonedWhere = new Where(this.getConn());
        for (Where innerWhere : this.getWheres()) {
            clonedWhere.addWhere((Where) innerWhere.clone());
        }
        return clonedWhere;
    }
}
