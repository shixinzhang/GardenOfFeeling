package sxkeji.net.dailydiary.common.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.beans.LoginGuideBean;
import sxkeji.net.dailydiary.common.views.adapters.LoginViewPaperAdapter;
import sxkeji.net.dailydiary.utils.UIUtils;

/**
 * 登录
 * Created by zhangshixin on 4/14/2016.
 */
public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.tv_send_verify_code)
    TextView tvSendVerifyCode;
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    @Bind(R.id.ll_verify)
    LinearLayout llVerify;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.ll_indicator)
    LinearLayout llIndicator;
    private LoginViewPaperAdapter mAdapter;
    private ArrayList<LoginGuideBean> mLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();
        setListeners();
    }

    private void setListeners() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llVerify.getVisibility() == View.GONE) {
                    visibleWithAlphaAnim(true);
                }
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llVerify.getVisibility() == View.VISIBLE) {
                    visibleWithAlphaAnim(false);
                }
            }
        });
    }

    private void visibleWithAlphaAnim(final boolean show) {
        float starAlpha, endAlpha;
        if (show) {
            starAlpha = 0f;
            endAlpha = 1f;
        } else {
            starAlpha = 1f;
            endAlpha = 0f;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(llVerify, "alpha", starAlpha, endAlpha).setDuration(1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (show) {
                    llVerify.setVisibility(View.VISIBLE);
                    tvSendVerifyCode.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!show) {
                    llVerify.setVisibility(View.GONE);
                    tvSendVerifyCode.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
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

        initLoginViewData();
        mAdapter = new LoginViewPaperAdapter(this, mLoginData);
        viewpager.setPageMargin(-UIUtils.dip2px(LoginActivity.this, 50));
        viewpager.setAdapter(mAdapter);
    }

    /**
     * 初始化ViewPager的数据
     */
    private void initLoginViewData() {
        mLoginData = new ArrayList<>();
        LoginGuideBean loginGuideBean1 = new LoginGuideBean();
        loginGuideBean1.setImgId(R.mipmap.bg_user_guide_0);
        loginGuideBean1.setTitle("云端数据保存");
        mLoginData.add(loginGuideBean1);

        LoginGuideBean loginGuideBean2 = new LoginGuideBean();
        loginGuideBean2.setImgId(R.mipmap.bg_user_guide_1);
        loginGuideBean2.setTitle("登录一键还原");
        mLoginData.add(loginGuideBean2);

        initLoginViewIndicator();
    }

    private void initLoginViewIndicator() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
