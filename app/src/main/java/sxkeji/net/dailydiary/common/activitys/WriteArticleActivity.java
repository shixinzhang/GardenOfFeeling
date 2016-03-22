package sxkeji.net.dailydiary.common.activitys;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.MediaUtils;
import sxkeji.net.dailydiary.utils.NetWorkUtils;
import sxkeji.net.dailydiary.utils.StringUtils;
import sxkeji.net.dailydiary.utils.SystemUtils;
import sxkeji.net.dailydiary.widgets.ExtendMediaPicker;

/**
 * 写文字页面
 * Created by zhangshixin on 3/17/2016.
 */
public class WriteArticleActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_no_img_header)
    TextView tvNoImgHeader;
    @Bind(R.id.layout_select_img)
    RelativeLayout layoutSelectImg;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.img_select)
    ImageView imgSelect;

    private static final String TAG = "WriteArticleActivity";
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    private Cursor cursor;
    private ExtendMediaPicker mediaPicker;

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
                mediaPicker.showPickerView(true, layoutSelectImg);
            }
        });
        layoutSelectImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSnackToast("保存图片到 我的收藏 ");
                return true;
            }
        });


        /**
         * 本文不要配图--配图消失
         */
        tvNoImgHeader.setOnClickListener(new View.OnClickListener() {
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

    }


    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackToast("自动保存");
            }
        });

        collapsingToolbarLayout.setTitle("写文字");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        mediaPicker = new ExtendMediaPicker(this, imgSelect);
        getImgFromNet();
    }

    private void getImgFromNet() {
        if(layoutSelectImg.getVisibility() != View.VISIBLE){
            layoutSelectImg.setVisibility(View.VISIBLE);
            LogUtils.e(TAG, "Visibility changed! : " + layoutSelectImg.getVisibility());
        }

        if(!NetWorkUtils.isNetworkAvailable(WriteArticleActivity.this)){
            showSnackToast(getResources().getString(R.string.network_no_can_not_load_img));
            return;
        }


        Drawable drawable = imgSelect.getDrawable();
        Picasso.with(WriteArticleActivity.this).load(Constant.URL_IMG)
                .error(R.mipmap.background_menu_account_info_colorful)
                .placeholder(drawable)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .config(Bitmap.Config.RGB_565)
                .into(imgSelect);

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
            case R.id.item_menu_refresh_img:
                getImgFromNet();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {

            return;
        }

        LogUtils.e(TAG, "Img select : onActivityResult");
        switch (requestCode) {
            case ExtendMediaPicker.REQUEST_CODE_PICK_IMAGE:
                String path = MediaUtils.getPath(WriteArticleActivity.this, data.getData());
//                cropImageUri(Uri.fromFile(new File(path)), 300, 300,
//                        REQUEST_CODE_CROP_PHOTO);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imgSelect.setImageBitmap(bitmap);
                break;
            case ExtendMediaPicker.REQUEST_CODE_TAKE_PHOTO:

//                cropImageUri(imageUri, 300, 300, REQUEST_CODE_CROP_PHOTO);
                break;
            case ExtendMediaPicker.REQUEST_CODE_CROP_PHOTO:
//                String imagePath = tempFile.getAbsolutePath();
//                LogUtils.i("crop" + imagePath);
//
//                if (mMediaPickerListener != null) {
//
//
//                    mMediaPickerListener.onSelectedMediaChanged(imagePath);
//                }
                break;
        }


    }
}
