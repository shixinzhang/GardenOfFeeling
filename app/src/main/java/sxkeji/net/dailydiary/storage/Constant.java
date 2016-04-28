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
    public static String appName = "365_diary";
    public static String showGuide = "show_guide";
    public static int SPLASH_DELAYED_TIME = 3000;
    public static String ARTICLE_BEAN = "article_bean";
    public static String ARTICLE_TYPE = "article_type";
    public static String OPEN_EYE_DATA = "open_eye_dialy";
    public static final String GESTURE_PASSWORD = "GesturePassword";
    public static final String THEME = "theme";
    public static final String THEME_LIGHT = "theme_light";
    public static final String THEME_DARK = "theme_dark";

    //账户状态相关
    public static final String ACCOUNT_IS_LOGIN = "account_login";
    public static final String ACCOUNT_USER_NUMBER = "account_number";

    //可以用户设置的状态
    public static final String SETTING_TODO_ORDER = "todo_order";
    public static final String SETTING_TODO_AUTO_SYNC = "todo_auto_sync";       //保存后自动上传

    //Activity间跳转传递的数据
    public static final String EXTRA_FROM = "start_from";
    public static final String EXTRA_TO = "jump_to";
    public static final String ACTIVITY_CLOUD_BACK = "activity_cloud_back";
    public static final String ACTIVITY_LOCAL_EXPROT = "activity_local_export";
    public static final String ACTIVITY_SETTING = "activity_setting";
    public static final String PLAY_TITLE = "play_title";
    public static final String PLAY_URL = "play_url";

    //LeanCloud ID 和 Key
    public static String LEANCLOUD_APPID = "p5u1dmlmaznldib4hu0goexf4cg5kwwed7to6pdhsm9x8dpd";
    public static String LEANCLOUD_KEY = "3qd9nuefo4fyzknx3cwk15mvgwoctym1l3ohb1ndhk5xfl3y";
    //LeanCloud 表名
    public static String LEANCLOUD_TABLE_DIARY = "articles_365";
    public static String LEANCLOUD_TABLE_TODO = "todo_365";
    public static String LEANCLOUD_TABLE_USERNUMBER = "user_number";

    public static final String OPEN_EYE_UID = "57e17df8bc634de8b5d427bd27a126b9f1ba1a56";
    //    vc=74&vn=1.10.1&deviceModel=m1%20note&first_channel=eyepetizer_meizu_market&last_channel=eyepetizer_meizu_market%20ZHOU
    public static final String OPEN_EYE_VC = "74";
    public static final String OPEN_EYE_VN = "1.10.1";
    public static final String OPEN_EYE_DEVICEMODEL = "m1%20note";
    public static final String OPEN_EYE_FIRST_CHANNEL = "eyepetizer_meizu_market";
    public static final String OPEN_EYE_LAST_CHANNEL = "eyepetizer_meizu_market%20ZHOU";

    //文档类型
    public static final int TYPE_MARKDOWN = 0 ; //markdown
    public static final int TYPE_NORMAL = 1 ;
    public static final int TYPE_REMINDER = 2;  //todo
    public static final int TYPE_DRAFT = 3 ; //草稿

    //缓存地址
    public static final String BASE_CACHE_FILE_PATH = "sdcard/sxkeji/";
    public static final String IMG_CACHE_PATH = BASE_CACHE_FILE_PATH + "images/";
    public static final String ARTICLE_CACHE_PATH = BASE_CACHE_FILE_PATH + "articles/";
    public static final String REMINDER_CACHE_PATH = BASE_CACHE_FILE_PATH + "reminds/";

    //接口地址
    //文字配图网络地址，返回的就是图片
    public static final String URL_IMG = "http://tu.ihuan.me/tu/api/me_all_pic/";
    //豆瓣电影top250 https://api.douban.com/v2/movie/top250?start=0&count=10
    public static final String URL_DOUBAN_MOVIE_TOP250 = "https://api.douban.com/v2/movie/";
    //开眼每周API http://baobab.wandoujia.com/api/v3/ranklist?num=100&strategy=weekly&udid=57e17df8bc634de8b5d427bd27a126b9f1ba1a56&vc=74&vn=1.10.1&deviceModel=m1%20note&first_channel=eyepetizer_meizu_market&last_channel=eyepetizer_meizu_market%20ZONG
    public static final String URL_OPEN_EYE_WEEKLY = "http://baobab.wandoujia.com/api/v3/ranklist";
     //开眼每日API  http://baobab.wandoujia.com/api/v2/feed?num=2&udid=57e17df8bc634de8b5d427bd27a126b9f1ba1a56&vc=74&vn=1.10.1&deviceModel=m1%20note&first_channel=eyepetizer_meizu_market&last_channel=eyepetizer_meizu_market%20ZHOU
    public static final String URL_OPEN_EYE_DIALY = " http://baobab.wandoujia.com/api/v2/feed";
}
