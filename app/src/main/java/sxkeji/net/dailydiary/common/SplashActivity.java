package sxkeji.net.dailydiary.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.activities.MainActivity;
import sxkeji.net.dailydiary.common.presenters.SplashPresenter;
import sxkeji.net.dailydiary.common.views.ISplashView;
import sxkeji.net.dailydiary.common.views.adapters.GuideViewPaperAdapter;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.storage.SharedPreferencesUtils;
import sxkeji.net.dailydiary.utils.FileUtils;
import sxkeji.net.dailydiary.utils.SystemUtils;
import sxkeji.net.dailydiary.utils.UIUtils;
import sxkeji.net.dailydiary.utils.ViewUtils;
import sxkeji.net.dailydiary.widgets.GuidePageTransformer;


/**
 * What's MVP?
 * <p>
 * Model(业务相关的操作、数据，2个部分：接口、实现)-----> Presenter(根据业务选择调用哪些操作) <-----View(布局里涉及的操作接口)
 * ↓
 * Activity(布局里操作的具体实现，最终的调用者)
 * <p>
 * Created by zhangshixin on 2015/11/26.
 *
 * @description Codes there always can be better.
 */
public class SplashActivity extends Activity implements ISplashView {
    private final String TAG = "SplashActivity";
    @Bind(R.id.ll_splash)
    LinearLayout llSplash;
    private ViewPager vpGuide;
    private Button btnRegist, btnLogin;
    private GuideViewPaperAdapter vpAdapter;
    private LinearLayout ll_guide;
    private ImageView iv_splash;

    private SplashPresenter mSplashPresenter;
    private ProgressDialog mProgressDialog;
    private boolean notShowGuide = false;
    private Target mDownloadTarget;
    private Picasso mPicasso;
    private File mLatestImge;
    private int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initViews();
        getDataFromNet();
        mSplashPresenter = new SplashPresenter(this);
    }

    private void getDataFromNet() {

        saveImgFromNet();
        mLatestImge = FileUtils.getLatestSaveImgFilr(this);

        getRecommandData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "this is from thread");

            }
        }).start();
    }

    private void saveImgFromNet() {
        mDownloadTarget = FileUtils.getDownloadTarget(this);
        mPicasso.load(Constant.URL_IMG).skipMemoryCache().into(mDownloadTarget);
    }

    private void initViews() {
        ll_guide = (LinearLayout) findViewById(R.id.ll_guide);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        mPicasso = BaseApplication.getPicassoSingleton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSplashPresenter.doBusiness(this);
    }

    @Override
    public boolean showGuideViewPager() {
        notShowGuide = (boolean) SharedPreferencesUtils.get(SplashActivity.this, Constant.showGuide, false);
        if (notShowGuide) {
            return false;
        } else {
            ll_guide.setVisibility(View.VISIBLE);
            SharedPreferencesUtils.put(SplashActivity.this, Constant.showGuide, true);
            vpGuide = (ViewPager) findViewById(R.id.vpGuide);
            btnRegist = (Button) findViewById(R.id.btnRegist);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnRegist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SplashActivity.this, "进入注册页面", Toast.LENGTH_SHORT).show();
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SplashActivity.this, "进入登录页面", Toast.LENGTH_SHORT).show();
                }
            });

            View view1 = View.inflate(this, R.layout.guide_view, null);
            View view2 = View.inflate(this, R.layout.guide_view, null);
            View view3 = View.inflate(this, R.layout.guide_view, null);

            ((ImageView) view1.findViewById(R.id.tv_pic)).setImageResource(R.mipmap.android_guide_step_1);
            ((ImageView) view2.findViewById(R.id.tv_pic)).setImageResource(R.mipmap.android_guide_step_2);
            ((ImageView) view3.findViewById(R.id.tv_pic)).setImageResource(R.mipmap.android_guide_step_3);

            view1.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpGuide.setCurrentItem(0);
                }
            });
            view2.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpGuide.setCurrentItem(1);
                }
            });
            view3.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpGuide.setCurrentItem(2);
                }
            });

            ((TextView) view1.findViewById(R.id.tv_title)).setText("软件功能介绍");
            ((TextView) view1.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#FF5000"));
            ((TextView) view2.findViewById(R.id.tv_title)).setText("角色划分和定位");
            ((TextView) view2.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#49ca65"));
            ((TextView) view3.findViewById(R.id.tv_title)).setText("如何使用软件");
            ((TextView) view3.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#16c5c6"));

            ((TextView) view1.findViewById(R.id.tv_desc)).setText("Extalk 功能强大\n");
            ((TextView) view2.findViewById(R.id.tv_desc)).setText("Extalk 角色分为2种\n");
            ((TextView) view3.findViewById(R.id.tv_desc)).setText("Extalk 使用说明\n");

            vpGuide = (ViewPager) findViewById(R.id.vpGuide);

            ArrayList<View> views = new ArrayList<>();
            views.add(view1);
            views.add(view2);
            views.add(view3);

            vpAdapter = new GuideViewPaperAdapter(views);

            vpGuide.setOffscreenPageLimit(views.size());
            vpGuide.setPageMargin(-UIUtils.dip2px(SplashActivity.this, 100));
            vpGuide.setAdapter(vpAdapter);
            vpGuide.setPageTransformer(false, new GuidePageTransformer());

            return true;
        }
    }

    @Override
    public void showSplashPic() {
        iv_splash.setVisibility(View.VISIBLE);


        //Todo: 设置splash图片缓存的过期：1天、或者半天
        //显示上次缓存的图片
        if (mLatestImge != null) {
            Log.e("showSplashPic", mLatestImge.getName());
            mPicasso.load(mLatestImge)
                    .config(Bitmap.Config.RGB_565)
                    .resize(screenWidth,screenHeight)
                    .centerInside()
                    .skipMemoryCache()
                    .priority(Picasso.Priority.LOW)
                    .into(iv_splash);
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                jumpToNextActivity();
            }
        }, Constant.SPLASH_DELAYED_TIME);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    jumpToNextActivity();
                    break;
                case 1:
                    showNoNetDialog();
                    break;
                case 2:
                    hideProgressDialog();
                    break;
            }
        }
    };


    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage("更新数据中...");
        }

        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            //mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showNoNetDialog() {
        Toast.makeText(SplashActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void jumpToNextActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
        mHandler.removeCallbacksAndMessages(null);
//        mPicasso.cancelRequest(mDownloadTarget);
    }

    public void getRecommandData() {

    }
}
