package cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain;

import org.springframework.data.geo.Box;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 16:47
 */
public class GeoBox {
    private GeoPoint topLeft;
    private GeoPoint bottomRight;

    public GeoBox(GeoPoint topLeft, GeoPoint bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public GeoPoint getTopLeft() {
        return topLeft;
    }

    public GeoPoint getBottomRight() {
        return bottomRight;
    }

    public static GeoBox fromBox(Box box) {
        GeoPoint topLeft = GeoPoint.fromPoint(box.getFirst());
        GeoPoint bottomRight = GeoPoint.fromPoint(box.getSecond());

        return new GeoBox(topLeft, bottomRight);
    }
}
