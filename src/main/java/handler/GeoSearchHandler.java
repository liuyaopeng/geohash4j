package handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public abstract class GeoSearchHandler {

    public abstract List<Map<String, Object>> getResult(Map<String,Object> map);

    public List<Map<String, Object>> search(double lon, double lat, double distance,Map<String,Object> map) {
        GeoSearch geoSearch = new GeoSearch();
        String key = geoSearch.getSearchKey(lon, lat, distance);
        map.put("key",key);
        List<Map<String, Object>> resultTore = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> result = this.getResult(map);
        for (Map<String, Object> stringObjectMap : result) {
            Double lonResult = (Double) stringObjectMap.get("lon");
            Double latResult = (Double) stringObjectMap.get("lat");
            Double disResult = geoSearch.getDistance(lon, lat, lonResult, latResult);
            if (disResult <= distance) {
                stringObjectMap.put("dis", disResult);
                resultTore.add(stringObjectMap);
            }
        }
        return resultTore;
    }
}
