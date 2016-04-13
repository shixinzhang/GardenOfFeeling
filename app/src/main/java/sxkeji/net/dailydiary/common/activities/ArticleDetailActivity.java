package sxkeji.net.dailydiary.common.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.sxkeji.markdownlib.MarkdownView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.storage.Constant;

/**
 * 文章详情Activity
 * Created by zhangshixin on 4/13/2016.
 */
public class ArticleDetailActivity extends AppCompatActivity {
    private final String TAG = "ArticleDetailActivity";
    @Bind(R.id.img_select)
    ImageView imgSelect;
    @Bind(R.id.layout_select_img)
    RelativeLayout layoutSelectImg;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.view_markdown)
    MarkdownView viewMarkdown;
    @Bind(R.id.fab_preview_save)
    FloatingActionButton fabPreviewSave;
    private Article mArticle;
    private int mArticleType;
    private String mArticleDate;
    private String mArticleContent;
    private String mArticleImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        initData();
        initViews();
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
        collapsingToolbarLayout.setTitle(mArticleDate);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarTitle);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        viewMarkdown.loadMarkdown(mArticleContent);
        if (!TextUtils.isEmpty(mArticleImg)) {
            BaseApplication.getPicassoSingleton().load(new File(mArticleImg))
                    .error(R.mipmap.background_menu_account_info_colorful)
                    .skipMemoryCache()
                    .placeholder(R.mipmap.background_menu_account_info_colorful)
                    .resize(800, 450)
                    .centerInside()
                    .config(Bitmap.Config.RGB_565).into(imgSelect);
        } else {
            int color = Color.parseColor("#03A9F4");
            imgSelect.setImageDrawable(new ColorDrawable(color));
        }
    }

    private void initData() {
        mArticle = (Article) getIntent().getSerializableExtra(Constant.ARTICLE_BEAN);
        if (mArticle == null) {
            return;
        }
        Log.e(TAG, "article title" + mArticle.getTitle() + " / type " + mArticle.getType());
        mArticleType = mArticle.getType();
        mArticleDate = mArticle.getDate();
        mArticleContent = mArticle.getContent();
        mArticleImg = mArticle.getImg_path();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_detail, menu);
        return true;
    }
}
