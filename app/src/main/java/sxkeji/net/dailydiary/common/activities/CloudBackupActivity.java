package sxkeji.net.dailydiary.common.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.ArticleDao;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.BaseActivity;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.http.HttpClient;
import sxkeji.net.dailydiary.http.LoadingPage;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.utils.LogUtils;
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

    private int cloudNumber; //云端备份数量
    private int localNumber; //本地数量
    private ArticleDao articleDao;
    private List<Article> localArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_backup);
        ButterKnife.bind(this);
        getDataFrom();
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
    }

    /**
     * 获取同步信息
     */
    private void getDataFrom() {

        queryCloudNumber();
        queryLocalNumber();
    }

    /**
     * 查询本地数据
     */
    private void queryLocalNumber() {
        articleDao = BaseApplication.getDaoSession().getArticleDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Query<Article> query = articleDao.queryBuilder().where(ArticleDao.Properties.Type.notEq(Constant.TYPE_DRAFT) ).orderDesc(ArticleDao.Properties.Date).build();
                localArticles = query.list();
                localNumber = localArticles.size();
                tvLocalNumber.setText(localNumber + "篇");
            }
        }).start();

    }

    /**
     * 查询云端数据
     */
    private void queryCloudNumber() {
        AVQuery<AVObject> query = new AVQuery<>(Constant.LEANCLOUD_TABLE_DIARY);
        query.whereEqualTo(Constant.LEANCLOUD_TABLE_USERNUMBER, "18789440700");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list != null) {
                        cloudNumber = list.size();
                        tvCloudNumber.setText(cloudNumber + "篇");
                        LogUtils.e(TAG, "query success , size is " + cloudNumber);
                    } else {
                        LogUtils.e(TAG, "query success , list is null ;size is " + list.size());
                    }
                } else {
                    LogUtils.e(TAG, "query number from server failed " + e.getMessage());
                }
            }
        });
    }

    /**
     * 上传到服务器
     */
    private void upload2Server(){
        if (localArticles != null){
            if (localNumber > cloudNumber){
                for (Article article : localArticles) {
                    HttpClient.uploadArticle2LeanCloud(CloudBackupActivity.this,article);
                }
                queryLocalNumber();
            }else {
                showToast("您的全部文章在云端已有备份");
            }
        }else {
            showToast("您还没有创建文章，无法上传");
        }
    }

    private void showToast(String str){
        UIUtils.showToastSafe(CloudBackupActivity.this,str);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cloud_back_help, menu);
        return true;
    }
}
