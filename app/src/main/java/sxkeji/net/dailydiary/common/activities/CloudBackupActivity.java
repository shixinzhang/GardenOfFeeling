package sxkeji.net.dailydiary.common.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.ArticleDao;
import net.sxkeji.dailydiary.R;
import sxkeji.net.dailydiary.Todo;
import sxkeji.net.dailydiary.TodoDao;
import sxkeji.net.dailydiary.common.BaseActivity;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.http.HttpClient;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.storage.SharedPreferencesUtils;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.StringUtils;
import sxkeji.net.dailydiary.utils.UIUtils;

/**
 * 云备份
 * Created by zhangshixin on 4/14/2016.
 */
public class CloudBackupActivity extends BaseActivity {
    private final String TAG = "CloudBackupActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_cloud_number)
    TextView tvCloudNumber;
    @Bind(R.id.tv_local_number)
    TextView tvLocalNumber;
    @Bind(R.id.tv_upload)
    Button tvUpload;
    @Bind(R.id.tv_download)
    Button tvDownload;
    @Bind(R.id.tv_last_sync_time)
    TextView tvLastSyncTime;
    @Bind(R.id.tv_cloud_article_number)
    TextView tvCloudArticleNumber;
    @Bind(R.id.tv_cloud_todo_number)
    TextView tvCloudTodoNumber;
    @Bind(R.id.tv_local_article_number)
    TextView tvLocalArticleNumber;
    @Bind(R.id.tv_local_todo_number)
    TextView tvLocalTodoNumber;

    private String userNumber;
    private int cloudNumber; //云端备份数量,文章与待做事项之和
    private int cloudArticleNumber, cloudTodoNumber;
    private int localNumber; //本地数量,文章与待做事项之和
    private int localArticleNumber, localTodoNumber;
    private Date nowDate;
    private long lastSyncTime;
    private ArticleDao articleDao;
    private TodoDao todoDao;
    private List<Article> localArticles;
    private List<Todo> localTodos;
    private final String UPLOAD_TODO = "upload_todo";   //上传过云端的toDo,有了ObjectId
    private final String UPDATE_TODO = "update_todo";   //要更新的todo
    private final String UPLOAD_ARTICLE = "upload_article";
    private final String UPDATE_ARTICLE = "update_article";   //要更新的article
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_backup);
        ButterKnife.bind(this);
//        AVOSCloud.initialize(this, Constant.LEANCLOUD_APPID, Constant.LEANCLOUD_KEY);         //如果把同步放到单独的进程，需要初始化AVOSCloud
        initData();
        initViews();
        setListeners();
    }

    private void setListeners() {
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload2Server();
            }
        });
        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download2Local();
            }
        });
    }

    /**
     * 获取同步信息
     */
    private void initData() {
        nowDate = new Date();
        userNumber = (String) SharedPreferencesUtils.get(this, Constant.ACCOUNT_USER_NUMBER, "");
        lastSyncTime = (long) SharedPreferencesUtils.get(this, Constant.LAST_SYNC_TIME, nowDate.getTime());
        queryCloudNumber();
        queryLocalNumber();
    }

    /**
     * 查询本地数据
     */
    private void queryLocalNumber() {
        articleDao = BaseApplication.getDaoSession().getArticleDao();
        todoDao = BaseApplication.getDaoSession().getTodoDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: 4/28/2016 本地查找时也得加个号码匹配条件
                queryLocalArticleNumber();
                queryLocalTodoNumber();
                tvLocalNumber.setText(localNumber + "篇");
            }
        }).start();

    }

    /**
     * 查询云端数据
     */
    private void queryCloudNumber() {
        //查询文章数量
        queryCloudArticleNumber(false);

        //查询todo数量
        queryCloudTodoNumber(false);
    }

    private void queryCloudTodoNumber(final boolean save2Local) {
        startProgress();
        AVQuery<AVObject> todoQuery = new AVQuery<>(Constant.LEANCLOUD_TABLE_TODO);
        todoQuery.whereEqualTo(Constant.LEANCLOUD_TABLE_USERNUMBER, userNumber);
        todoQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list != null) {
                        if (save2Local) {
                            showToast("下载成功");
                            saveTodo2DB(list);
                        } else {
                            cloudTodoNumber = list.size();
                            cloudNumber += cloudTodoNumber;
                            tvCloudTodoNumber.setText(cloudTodoNumber + "篇");
                            tvCloudNumber.setText(cloudNumber + "篇");
                            LogUtils.e(TAG, "query success , cloudTodoNumber is " + cloudTodoNumber);
                            dismissProgress();
                        }
                    } else {
                        LogUtils.e(TAG, "query success ,cloudTodoNumber is null ;size is " + list.size());
                        dismissProgress();
                    }
                } else {
                    int errorCode = e.getCode();
                    showToast("连接服务器失败 " + e.getMessage() + errorCode);
                    LogUtils.e(TAG, "query number from server failed " + e.getMessage());
                    dismissProgress();
                }
            }
        });
    }

    /**
     * 将下载的todo保存到本地
     *
     * @param list
     */
    private void saveTodo2DB(List<AVObject> list) {
        for (AVObject avObject : list) {
            Todo tempTodo = new Todo();
            tempTodo.setDate(avObject.getDate(Constant.LEANCLOUD_TODO_PROPERTY_DATE));
            tempTodo.setContent(avObject.getString(Constant.LEANCLOUD_TODO_PROPERTY_TITLE));
            tempTodo.setColor(avObject.getInt(Constant.LEANCLOUD_TODO_PROPERTY_COLOR));
            tempTodo.setHasReminder(avObject.getBoolean(Constant.LEANCLOUD_TODO_PROPERTY_REMINDER));
            tempTodo.setShowOnLockScreen(avObject.getBoolean(Constant.LEANCLOUD_TODO_PROPERTY_SHOW_ON_SCREEN));
            tempTodo.setIsFinished(avObject.getBoolean(Constant.LEANCLOUD_TODO_PROPERTY_ISFINISHED));
            tempTodo.setObjectId(avObject.getObjectId());
            todoDao.insertOrReplace(tempTodo);
        }
        dismissProgress();
        localNumber = localArticleNumber;
        queryLocalTodoNumber();
        updateSyncTime();
        LogUtils.e(TAG, "saveTodo2DB size " + list.size());
    }

    private void queryCloudArticleNumber(final boolean save2Local) {
        startProgress();
        AVQuery<AVObject> query = new AVQuery<>(Constant.LEANCLOUD_TABLE_DIARY);
        query.whereEqualTo(Constant.LEANCLOUD_TABLE_USERNUMBER, userNumber);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list != null) {
                        cloudArticleNumber = list.size();
                        if (save2Local) {
                            showToast("下载成功");
                            saveArticle2DB(list);

                        } else {
                            cloudNumber += cloudArticleNumber;
                            tvCloudArticleNumber.setText(cloudArticleNumber + "篇");
                            tvCloudNumber.setText(cloudNumber + "篇");
                            LogUtils.e(TAG, "query success , cloudArticleNumber is " + cloudArticleNumber);
                            dismissProgress();
                        }
                    } else {
                        LogUtils.e(TAG, "query success , cloudArticleNumber is null ;size is " + list.size());
                        dismissProgress();
                    }
                } else {
                    showToast("连接服务器失败 " + e.getMessage());
                    LogUtils.e(TAG, "query number from server failed " + e.getMessage());
                    dismissProgress();
                }
            }
        });
    }

    /**
     * 下载的文章数据保存到本地
     *
     * @param list
     */
    private void saveArticle2DB(List<AVObject> list) {
        for (AVObject avObject : list) {
            Article tempArticle = new Article();
            String date = avObject.getString(Constant.LEANCLOUD_ARTICLE_PROPERTY_DATE);
            if (TextUtils.isEmpty(date)) {
                date = "";
            }
            tempArticle.setDate(date);
            tempArticle.setAddress(avObject.getString(Constant.LEANCLOUD_ARTICLE_PROPERTY_ADDRESS));
            tempArticle.setWeather(avObject.getString(Constant.LEANCLOUD_ARTICLE_PROPERTY_WEATHER));
            tempArticle.setTitle(avObject.getString(Constant.LEANCLOUD_ARTICLE_PROPERTY_TITLE));
            tempArticle.setContent(avObject.getString(Constant.LEANCLOUD_ARTICLE_PROPERTY_CONTENT));
            tempArticle.setType(avObject.getInt(Constant.LEANCLOUD_ARTICLE_PROPERTY_TYPE));
            tempArticle.setImg_path(avObject.getString(Constant.LEANCLOUD_ARTICLE_PROPERTY_IMGPATH));
            tempArticle.setObjectId(avObject.getObjectId());
            articleDao.insertOrReplace(tempArticle);
        }
        dismissProgress();

        localNumber = localTodoNumber;
        queryLocalArticleNumber();
        updateSyncTime();
        LogUtils.e(TAG, "saveArticle2DB size " + list.size());
    }

    private void queryLocalTodoNumber() {
        Query<Todo> todoQuery = todoDao.queryBuilder().build();
        localTodos = todoQuery.list();
        localTodoNumber = localTodos.size();
        localNumber += localTodoNumber;
        tvLocalNumber.setText(localNumber + "篇");
        tvLocalTodoNumber.setText(localTodoNumber + "篇");
    }

    private void queryLocalArticleNumber() {
        Query<Article> query = articleDao.queryBuilder().where(ArticleDao.Properties.Type.notEq(Constant.TYPE_DRAFT)).build();
        localArticles = query.list();
        localArticleNumber = localArticles.size();
        localNumber += localArticleNumber;
        tvLocalNumber.setText(localNumber + "篇");
        tvLocalArticleNumber.setText(localArticleNumber + "篇");
    }

    /**
     * 上传到服务器
     */
    private void upload2Server() {
        if (localArticleNumber > cloudArticleNumber) {     //本地文章数量多于云端，上传文章
            for (Article article : localArticles) {
                uploadArticle2LeanCloud(this, article);
            }
            cloudNumber = cloudTodoNumber;
            queryCloudArticleNumber(false);
        }
        if (localTodoNumber > cloudTodoNumber) {     //本地Todo数量多于云端，上传Todo
            for (Todo todo : localTodos) {
                uploadTodo2Cloud(this, todo);
            }
            cloudNumber = cloudArticleNumber;
            queryCloudTodoNumber(false);
        }
        if (localArticleNumber == 0 && localTodoNumber == 0) {
            showToast("您还没有写任何文字");
            return;
        }
        if (localArticleNumber <= cloudArticleNumber && localTodoNumber <= cloudTodoNumber) {
            showToast("您的全部内容在云端已有备份");
        }

    }

    /**
     * 下载到本地
     */
    private void download2Local() {
        if (localArticleNumber < cloudArticleNumber) {     //本地文章数量少于云端，下载文章
            queryCloudArticleNumber(true);
        }
        if (localTodoNumber < cloudTodoNumber) {     //本地Todo数量少于云端，下载To do
            queryCloudTodoNumber(true);
        }
        if (cloudArticleNumber == 0 && cloudTodoNumber == 0) {
            showToast("云端没有任何内容");
            return;
        }
        if (localArticleNumber >= cloudArticleNumber && localTodoNumber >= cloudTodoNumber) {
            showToast("没有新内容可以下载");
        }

    }


    /**
     * 上传Article到LeanCloud
     *
     * @param article
     */
    public void uploadArticle2LeanCloud(final Context context, final Article article) {
        String userNumber = (String) SharedPreferencesUtils.get(context, Constant.ACCOUNT_USER_NUMBER, "");
        if (TextUtils.isEmpty(userNumber)) {
            LogUtils.e("upload2LeanCloud", "userNumber is null , upload failed!");
            return;
        }
        startProgress();
        String objectId = article.getObjectId();

        final AVObject uploadArticle = new AVObject(Constant.LEANCLOUD_TABLE_DIARY);
        uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_DATE, article.getDate());
        uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_ADDRESS, article.getAddress());
        uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_WEATHER, article.getWeather());
        uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_TITLE, article.getTitle());
        uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_CONTENT, article.getContent());
        uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_TYPE, article.getType());
        uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_IMGPATH, article.getImg_path());
        uploadArticle.put(Constant.LEANCLOUD_TABLE_USERNUMBER, userNumber);

        if (TextUtils.isEmpty(objectId)) {          //新的，上传
            uploadArticle.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        UIUtils.showToastSafe(context, "上传云端成功");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(UPLOAD_ARTICLE, uploadArticle);
                        bundle.putSerializable(UPDATE_ARTICLE, article);
                        Message msg = new Message();
                        msg.setData(bundle);
                        msg.what = 2;
                        handler.sendMessage(msg);

                    } else {
                        UIUtils.showToastSafe(context, "上传云端失败" + e.getMessage());
                    }
                    dismissProgress();
                }
            });
        } else {                        //旧的，更新
            uploadArticle.put(Constant.LEANCLOUD_ARTICLE_PROPERTY_OBJECTID, objectId);
            uploadArticle.refreshInBackground(new RefreshCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        UIUtils.showToastSafe(context, "上传云端成功");
                    } else {
                        UIUtils.showToastSafe(context, "上传云端失败" + e.getMessage());
                    }
                    dismissProgress();
                }
            });
        }
    }

    /**
     * 上传ToDo到云端、然后更新此ToDo的objectId状态，已经上传过
     *
     * @param context
     * @param todo
     */
    public void uploadTodo2Cloud(final Context context, final Todo todo) {
        String userNumber = (String) SharedPreferencesUtils.get(context, Constant.ACCOUNT_USER_NUMBER, "");
        if (TextUtils.isEmpty(userNumber)) {
            LogUtils.e("uploadTodo2Cloud", "userNumber is null , upload failed!");
            return;
        }
        final String objectId = todo.getObjectId();

        final AVObject uploadTodo = new AVObject(Constant.LEANCLOUD_TABLE_TODO);

        uploadTodo.put(Constant.LEANCLOUD_TABLE_USERNUMBER, userNumber);
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_DATE, todo.getDate());
        //TODO:到底要不要内容呢？还是只一个标题就好了,在"一起改进"里问一下
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_TITLE, todo.getContent());
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_COLOR, todo.getColor());
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_ISFINISHED, false);
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_REMINDER, todo.getHasReminder());
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_SHOW_ON_SCREEN, todo.getShowOnLockScreen());
        if (TextUtils.isEmpty(objectId)) {              //上传
            uploadTodo.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        UIUtils.showToastSafe(context, "上传云端成功");
                        //之前没上传过，上传后需要更新本地objectId，下次上传就不传了
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(UPLOAD_TODO, uploadTodo);
                        bundle.putSerializable(UPDATE_TODO, todo);
                        Message msg = new Message();
                        msg.setData(bundle);
                        msg.what = 1;
                        handler.sendMessage(msg);

                    } else {
                        UIUtils.showToastSafe(context, "上传云端失败" + e.getMessage());
                    }

                    dismissProgress();
                }
            });
        } else {             //更新
            uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_OBJECTID, objectId);
            uploadTodo.refreshInBackground(new RefreshCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        LogUtils.e(TAG, "update todo success " + avObject.getObjectId());
                    } else {
                        LogUtils.e(TAG, "update todo failed " + e.getMessage());
                    }
                    dismissProgress();
                }
            });
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:     //更新todo
                    AVObject upLoadObject = msg.getData().getParcelable(UPLOAD_TODO);
                    Todo todo = (Todo) msg.getData().getSerializable(UPDATE_TODO);
                    todo.setObjectId(upLoadObject.getObjectId());
                    BaseApplication.getDaoSession().getTodoDao().update(todo);
                    updateSyncTime();
                    LogUtils.e(TAG, "Update todo " + upLoadObject.getObjectId());
                    break;
                case 2:
                    AVObject upLoadObject2 = msg.getData().getParcelable(UPLOAD_ARTICLE);
                    Article article = (Article) msg.getData().getSerializable(UPDATE_ARTICLE);
                    article.setObjectId(upLoadObject2.getObjectId());
                    BaseApplication.getDaoSession().getArticleDao().update(article);
                    updateSyncTime();
                    LogUtils.e(TAG, "Update article " + upLoadObject2.getObjectId());
                    break;
            }
        }
    };

    /**
     * 更新上次同步时间
     */
    private void updateSyncTime() {
        Date date = new Date();
        tvLastSyncTime.setText(StringUtils.dateToString(date));
        SharedPreferencesUtils.put(this, Constant.LAST_SYNC_TIME, date.getTime());
    }

    private void startProgress() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("正在努力进行中...");
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    private void showToast(String str) {
        UIUtils.showToastSafe(CloudBackupActivity.this, str);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        Date lastDate = new Date(lastSyncTime);
        tvLastSyncTime.setText(StringUtils.dateToString(lastDate));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cloud_back_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_setting:
                startActivity(new Intent(CloudBackupActivity.this, SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        android.os.Process.killProcess(Process.myPid());      //单独在一个进程时推出时要关闭进程，暂时放弃在单独的进程
    }
}
