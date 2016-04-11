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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import net.sxkeji.markdownlib.MarkdownView;

import java.io.IOException;

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
import sxkeji.net.dailydiary.utils.ViewUtils;
import sxkeji.net.dailydiary.widgets.ExtendMediaPicker;

/**
 * 写文字页面
 * Created by zhangshixin on 3/17/2016.
 */
public class WriteArticleActivity extends AppCompatActivity {
    private static final String TAG = "WriteArticleActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_no_img_header)
    TextView tvNoImgHeader;
    @Bind(R.id.layout_select_img)
    RelativeLayout layoutSelectImg;
    //    @Bind(R.id.et_content)
//    EditText etContent;
    @Bind(R.id.img_select)
    ImageView imgSelect;

    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    //    @Bind(R.id.ll_write_normal)
//    LinearLayout llWriteNormal;
//    @Bind(R.id.view_markdown)
//    net.sxkeji.markdownlib.MarkdownView viewMarkdown;
    @Bind(R.id.et_markdown)
    EditText etMarkdown;
    @Bind(R.id.ll_write_markdown)
    LinearLayout llWriteMarkdown;
    @Bind(R.id.fab_preview_save)
    FloatingActionButton fabPreviewSave;
    private Cursor cursor;
    private ExtendMediaPicker mediaPicker;
    private String editContent;

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
        updateMarkdownPreview(editContent);

    }

    /**
     * 显示markdown预览
     *
     * @param str
     */
    private void updateMarkdownPreview(String str) {
//        if(!TextUtils.isEmpty(str)){
//            viewMarkdown.loadMarkdown(str);
//        }
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

        fabPreviewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupWindow = ViewUtils.showPopupWindow(WriteArticleActivity.this, R.layout.popup_markdown_preview, toolbar, 1);
                MarkdownView view_markdown = (MarkdownView) popupWindow.findViewById(R.id.view_markdown);
                String mkString = etMarkdown.getText().toString();
                view_markdown.loadMarkdown(mkString);

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


        etMarkdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateMarkdownPreview(etMarkdown.getText().toString());
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

        editContent = getResources().getString(R.string.md_guide_roles);
        etMarkdown.setText(editContent);
    }

    private void getImgFromNet() {
        if (layoutSelectImg.getVisibility() != View.VISIBLE) {
            layoutSelectImg.setVisibility(View.VISIBLE);
            LogUtils.e(TAG, "Visibility changed! : " + layoutSelectImg.getVisibility());
        }

        if (!NetWorkUtils.isNetworkAvailable(WriteArticleActivity.this)) {
            showSnackToast(getResources().getString(R.string.network_no_can_not_load_img));
            return;
        }


        Drawable drawable = imgSelect.getDrawable();
        Picasso.with(WriteArticleActivity.this).load(Constant.URL_IMG)
                .error(R.mipmap.background_menu_account_info_colorful)
                .placeholder(drawable)
                .resize(800, 450)
                .config(Bitmap.Config.RGB_565)
                .into(imgSelect);

//        new Picasso.Builder(WriteArticleActivity.this).

        Downloader downloader = new Downloader() {
            @Override
            public Response load(Uri uri, int networkPolicy) throws IOException {
                return null;
            }

            @Override
            public void shutdown() {

            }
        };
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
//        String content = etContent.getText().toString();
        String content = null;
        if (TextUtils.isEmpty(content)) {
            content = etMarkdown.getText().toString();
        }
        String date = StringUtils.getToday();
        String title = date;
        Article article = new Article(null, date, null, null, title, content, Constant.TYPE_MARKDOWN, null);
        BaseApplication.getDaoSession().getArticleDao().insert(article);
        LogUtils.e(TAG, "Insert new article, id : " + article.getId());
        showSnackToast("保存成功");
        WriteArticleActivity.this.finish();
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

    @Override
    protected void onStop() {
        if (isFinishing()) {
            Picasso.with(this).cancelRequest(imgSelect);
        }
        super.onStop();

    }
}
