package domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * <p>
 * Created by xiaoyu on 2016/10/17.
 */
public class GeoHash {

    public static final int MAX_PRECISION = 50;
    public static final double EARTH_RADIUS = 6372797.560856;
    private static final long FIRST_BIT_FLAGGED = 0x8000000000000L;
    public static Map<BigDecimal, Integer> PRECISION_MAP = new TreeMap<BigDecimal, Integer>();

    static {
        final BigDecimal EARTH_RADIUS = new BigDecimal(6372797.560856);

        for (int angle = 1; angle <= MAX_PRECISION / 2; angle++) {
            BigDecimal bigDecimal = new BigDecimal(2)
                    .multiply(new BigDecimal(Math.PI))
                    .multiply(EARTH_RADIUS)
                    .divide(new BigDecimal(2).pow(angle), ROUND_HALF_UP);
            PRECISION_MAP.put(bigDecimal, 2 * angle);
        }
    }

    private long bits = 0;
    private Coordinate coordinate;

    public GeoHash(double lon, double lat) {
        this.coordinate = new Coordinate(lon, lat);
        this.calculateHashCode(this.getCoordinate().getLon(), this.getCoordinate().getLat(), MAX_PRECISION);
    }




    private void calculateHashCode(double lon, double lat, int precision) {
        ArrayBlockingQueue lonQueue = this.lonToQueue(lon, precision);
        ArrayBlockingQueue latQueue = this.latToQueue(lat, precision);
        for (int i = 0; i < precision / 2; i++) {
            bits <<= 1;
            bits |= (Integer) lonQueue.poll();
            bits <<= 1;
            bits |= (Integer) latQueue.poll();
        }
    }

    private ArrayBlockingQueue lonToQueue(double lon, int precision) {
        double[] longitudeRange = new double[]{-180.0, 180.0};

        ArrayBlockingQueue lonQueue = new ArrayBlockingQueue(precision / 2);
        for (int count = 0; count < precision / 2; count++) {
            try {
                this.divideRange(lonQueue, longitudeRange, lon);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return lonQueue;

    }

    private ArrayBlockingQueue latToQueue(double lat, int precision) {
        double[] latitudeRange = new double[]{-90.0, 90.0};
        ArrayBlockingQueue<Byte> latQueue = new ArrayBlockingQueue(precision / 2);
        for (int count = 0; count < precision / 2; count++) {
            try {
                this.divideRange(latQueue, latitudeRange, lat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return latQueue;
    }


    private void divideRange(ArrayBlockingQueue queue, double[] range, double num) throws InterruptedException {
        double mid = (range[0] + range[1]) / 2;
        if (num > mid) {
            range[0] = mid;
            queue.put(1);
        } else {
            range[1] = mid;
            queue.put(0);
        }
    }


    public String toBinaryString() {
        return Long.toBinaryString(bits);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public long getLongValue() {
        return bits;
    }





}
