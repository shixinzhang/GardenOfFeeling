package sxkeji.net.dailydiary.common.activitys;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.ArticleDao;
import sxkeji.net.dailydiary.DaoMaster;
import sxkeji.net.dailydiary.DaoSession;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.StringUtils;

/**
 * 写文字页面
 * Created by zhangshixin on 3/17/2016.
 */
public class WriteArticleActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_delete)
    ImageView btnDelete;
    @Bind(R.id.tv_not_show_forever)
    TextView tvNotShowForever;
    @Bind(R.id.layout_select_img)
    RelativeLayout layoutSelectImg;
    @Bind(R.id.et_content)
    EditText etContent;

    private static final String TAG = "WriteArticleActivity";
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article);
        ButterKnife.bind(this);
        initViews();
        setListeners();
        loadData();
    }

    private void loadData() {
    }





    private void setListeners() {
        layoutSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackToast("选图片");
            }
        });

        /**
         * 本文不要配图--配图消失
         */
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator alpha = ObjectAnimator.ofFloat(layoutSelectImg, "alpha", 1f, 0f).setDuration(1000);
                alpha.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layoutSelectImg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                alpha.start();
            }
        });

        tvNotShowForever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackToast("不再显示");
            }
        });
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_action_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackToast("取消编辑");
            }
        });
    }

    private void showSnackToast(String str) {
        if (str != null) {
            Snackbar.make(toolbar, str, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_undo:
                break;
            case R.id.item_menu_markdown:
                break;
            case R.id.item_menu_done:
                addArticle();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加新文章
     */
    private void addArticle() {
        String content = etContent.getText().toString();
        String date = StringUtils.getToday();
        String title = date;
        Article article = new Article(null, date, null, null, title, content);
        BaseApplication.getDaoSession().getArticleDao().insert(article);
        LogUtils.e(TAG, "Insert new article, id : " + article.getId());
        showSnackToast("保存成功");
    }
}
