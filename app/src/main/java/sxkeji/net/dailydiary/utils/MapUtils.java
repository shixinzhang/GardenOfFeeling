package sxkeji.net.dailydiary.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加是否为Null判断的Map类，简化请求服务器前参数检查
 * Created by zhangshixin on 2015/11/26.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class MapUtils {
    private Map<String,String> map;
    private MapUtils(Map<String, String> map){
        this.map = map;
    }
    public static MapUtils builder(Map<String,String> map){
        return new MapUtils(map);
    }
    public Map<String, String> checkEmptyMap(Map<String, String> map) {
        HashMap<String, String> resultMap = new HashMap<>();
        for (String key :
                map.keySet()) {
            String vaule = map.get(key);
            if (!TextUtils.isEmpty(vaule)) {
                resultMap.put(key, vaule);
            }
        }
        return resultMap;
    }
    public MapUtils put(String key,String value){
        if(!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) && map != null){
            map.put(key,value);
        }
        return this;
    }
}
