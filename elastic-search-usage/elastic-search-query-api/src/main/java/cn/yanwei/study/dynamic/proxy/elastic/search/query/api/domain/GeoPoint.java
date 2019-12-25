package cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain;

import org.springframework.data.geo.Point;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 16:08
 */
public class GeoPoint {
    private double lat;
    private double lon;

    private GeoPoint() {
        //required by mapper to instantiate object
    }

    public GeoPoint(double latitude, double longitude) {
        this.lat = latitude;
        this.lon = longitude;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public static GeoPoint fromPoint(Point point) {
        return new GeoPoint(point.getX(), point.getY());
    }

    public static Point toPoint(GeoPoint point) {
        return new Point(point.getLat(), point.getLon());
    }
}
