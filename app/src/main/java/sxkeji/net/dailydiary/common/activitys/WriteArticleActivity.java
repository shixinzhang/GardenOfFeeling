package sxkeji.net.dailydiary.common.activitys;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;

/**
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article);
        ButterKnife.bind(this);
        initViews();
        setListeners();

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
                showSnackToast("done");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
