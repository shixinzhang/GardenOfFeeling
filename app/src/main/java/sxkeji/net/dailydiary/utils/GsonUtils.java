package sxkeji.net.dailydiary.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Json解析工具类
 * Created by zhangshixin on 2015/11/26.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class GsonUtils {

    private static Gson mGson = new Gson();

    public static <T> T json2Bean(String json, Class<T> clazz) {
        try {
            return mGson.fromJson(json, clazz);
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return null;
        }

    }

    public static <T>List<T> toList(String json,Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try{
            list = mGson.fromJson(json,new TypeToken<List<T>>(){}.getType());
        }catch (Exception e){
            LogUtils.e(e.toString());
        }
        return list;
    }

    /**
     * fromJson
     * @param json json
     * @param c c
     * @param <T> <T>
     * @return T
     */
    public static <T> T fromJson(String json, Class<T> c){
        return mGson.fromJson(json, c);
    }

    /**
     * toJson
     * @param src src
     * @return String
     */
    public static String toJson(Object src) {
        return mGson.toJson(src);
    }
}
