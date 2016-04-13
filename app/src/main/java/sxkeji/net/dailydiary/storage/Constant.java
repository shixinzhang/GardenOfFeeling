package sxkeji.net.dailydiary.storage;

/**
 * 一些常量
 * Created by zhangshixin on 2015/12/2.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class Constant {
    //常量
    public static String appName = "garden_of_feeling";
    public static String showGuide = "show_guide";
    public static int SPLASH_DELAYED_TIME = 3000;
    public static String ARTICLE_BEAN = "article_bean";
    public static String ARTICLE_TYPE = "article_type";

    //文档类型
    public static final int TYPE_MARKDOWN = 0 ;
    public static final int TYPE_NORMAL = 1 ;
    public static final int TYPE_REMINDER = 2;

    //缓存地址
    public static final String BASE_CACHE_FILE_PATH = "sdcard/sxkeji/";
    public static final String IMG_CACHE_PATH = BASE_CACHE_FILE_PATH + "images/";
    public static final String ARTICLE_CACHE_PATH = BASE_CACHE_FILE_PATH + "articles/";
    public static final String REMINDER_CACHE_PATH = BASE_CACHE_FILE_PATH + "reminds/";

    //接口地址
    //文字配图网络地址，返回的就是图片
    public static final String URL_IMG = "http://tu.ihuan.me/tu/api/me_all_pic/";
    ////豆瓣电影top250 https://api.douban.com/v2/movie/top250?start=0&count=10
    public static final String URL_DOUBAN_MOVIE_TOP250 = "https://api.douban.com/v2/movie/";
}
