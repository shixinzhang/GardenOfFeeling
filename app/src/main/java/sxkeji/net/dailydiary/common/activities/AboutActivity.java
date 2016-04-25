package sxkeji.net.dailydiary.common.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import app.dinus.com.loadingdrawable.LoadingDrawable;
import app.dinus.com.loadingdrawable.render.circle.jump.CollisionLoadingRenderer;
import app.dinus.com.loadingdrawable.render.circle.jump.DanceLoadingRenderer;
import app.dinus.com.loadingdrawable.render.circle.jump.GuardLoadingRenderer;
import app.dinus.com.loadingdrawable.render.circle.jump.SwapLoadingRenderer;
import app.dinus.com.loadingdrawable.render.circle.rotate.MaterialLoadingRenderer;
import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;

/**
 * 关于
 * Created by zhangshixin on 4/14/2016.
 */
public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_img)
    ImageView ivImg;

    private LoadingDrawable loadingDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
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

        DanceLoadingRenderer danceLoadingRenderer = new DanceLoadingRenderer(this);
        danceLoadingRenderer.setDuration(5000);
        loadingDrawable = new LoadingDrawable(danceLoadingRenderer);
//        loadingDrawable = new LoadingDrawable(new SwapLoadingRenderer(this));
//        loadingDrawable = new LoadingDrawable(new GuardLoadingRenderer(this));
        loadingDrawable.start();
        ivImg.setImageDrawable(loadingDrawable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDrawable != null){
            loadingDrawable.stop();
        }
    }
}
