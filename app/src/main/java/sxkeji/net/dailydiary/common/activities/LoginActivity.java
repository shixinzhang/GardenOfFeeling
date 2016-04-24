package sxkeji.net.dailydiary.common.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.beans.LoginGuideBean;
import sxkeji.net.dailydiary.common.views.adapters.LoginViewPaperAdapter;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.StringUtils;
import sxkeji.net.dailydiary.utils.TimeCount;
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

    private final String TAG = "LoginActivity";
    private final int MSG_WHAT_REQUEST_VERIFYCODE_SUCCESS = 1;
    private final int MSG_WHAT_RESUEST_VERIFYCODE_FAILED = 2;
    private LoginViewPaperAdapter mAdapter;
    private ArrayList<LoginGuideBean> mLoginData;
    private int lastIndicatorPos;  //前一个指示点的位置
    private boolean isAnimating = false;    //是否正在动画，判断状态
    private int[] colors;
    private String userNumber;
    private String userPassword;
    private String verifyCode;

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
                if (llVerify.getVisibility() == View.GONE && !isAnimating) {
                    visibleWithAlphaAnim(true);
                    tvRegister.setTextColor(colors[0]);
                    tvLogin.setTextColor(colors[1]);
                } else {
                    userNumber = etNumber.getText().toString();
                    userPassword = etPwd.getText().toString();
                    verifyCode = etVerifyCode.getText().toString();
                    if (TextUtils.isEmpty(userNumber) || !StringUtils.isMobileNO(userNumber)) {
                        showToast("请输入正确的手机号码");
                        return;
                    } else if (TextUtils.isEmpty(verifyCode) || verifyCode.length() < 6) {
                        showToast("请输入正确格式的验证码");
                        return;
                    } else if (TextUtils.isEmpty(userPassword) || userPassword.length() < 6) {
                        showToast("请输入正确格式的密码");
                        return;
                    }
                    register2Server(userNumber, userPassword, verifyCode);
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llVerify.getVisibility() == View.VISIBLE && !isAnimating) {
                    visibleWithAlphaAnim(false);
                    tvRegister.setTextColor(colors[1]);
                    tvLogin.setTextColor(colors[0]);
                } else {
                    userNumber = etNumber.getText().toString();
                    userPassword = etPwd.getText().toString();
                    if (TextUtils.isEmpty(userNumber) || !StringUtils.isMobileNO(userNumber)) {
                        showToast("请输入正确的手机号码");
                        return;
                    } else if (TextUtils.isEmpty(userPassword) || userPassword.length() < 6) {
                        showToast("请输入正确格式的密码");
                        return;
                    }
                    login2Server(userNumber, userPassword);
                }
            }
        });

        tvSendVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNumber = etNumber.getText().toString();
                if (TextUtils.isEmpty(userNumber) && !StringUtils.isMobileNO(userNumber)) {
                    showToast("请输入正确的手机号码");
                } else {
                    requestVerifyCode(userNumber);
                }

            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                llIndicator.getChildAt(position).setEnabled(true);
                llIndicator.getChildAt(lastIndicatorPos).setEnabled(false);
                lastIndicatorPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 登录
     * @param number
     * @param password
     */
    private void login2Server(String number, String password) {
        if (TextUtils.isEmpty(number) || TextUtils.isEmpty(password)){
            return;
        }
        AVUser.loginByMobilePhoneNumberInBackground(number, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null){
                    showToast("登录成功");
                    jump2NextActivity();
                }else {
                    int errorCode = e.getCode();
                    if (errorCode == 210){
                        showToast("登录失败: 手机号与密码不匹配" );
                    }else if (errorCode == 211) {
                        showToast("登录失败: 手机号尚未注册");
                    }else {
                        showToast("登录失败: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void showToast(String str){
        UIUtils.showToastSafe(LoginActivity.this, str);
    }
    /**
     * 注册
     */
    private void register2Server(final String phoneNumber, final String password, String verifyCod) {
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyCod)){
            return;
        }
        AVUser.signUpOrLoginByMobilePhoneInBackground(phoneNumber, verifyCod, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                // 如果 e 为空就可以表示登录成功了，并且 user 是一个全新的用户
                if (e == null) {
                    showToast("手机号验证成功");
//                    avUser.setUsername("Tom");// 设置用户名
                    avUser.setMobilePhoneNumber(phoneNumber);
                    avUser.setPassword(password);// 设置密码
                    avUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                // 注册成功
                                showToast("注册成功");
                                jump2NextActivity();
                            } else {
                                showToast("注册失败 " + e.getMessage());
                                // 失败的原因可能有多种，常见的是用户名已经存在。
                            }
                        }
                    });
                } else {
                    showToast("手机号与验证码不匹配");
                }
            }
        });

    }

    private void jump2NextActivity() {

        Intent intent = new Intent(LoginActivity.this, CloudBackupActivity.class);
        startActivity(intent);
    }

    /**
     * 请求发送验证码
     * @param phoneNumber
     */
    private void requestVerifyCode(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || !StringUtils.isMobileNO(phoneNumber)){
            return ;
        }

        AVOSCloud.requestSMSCodeInBackground(phoneNumber, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    handler.sendEmptyMessage(MSG_WHAT_REQUEST_VERIFYCODE_SUCCESS);
                } else {
                    LogUtils.e(TAG,"requestVerifyCode failed :" + e.getMessage());
                    handler.sendEmptyMessage(MSG_WHAT_RESUEST_VERIFYCODE_FAILED);
                }
            }
        });
    }

    /**
     * 伴随着动画显示或者隐藏
     *
     * @param show
     */
    private void visibleWithAlphaAnim(final boolean show) {
        isAnimating = true;
        float starAlpha, endAlpha;
        if (show) {
            starAlpha = 0f;
            endAlpha = 1f;
        } else {
            starAlpha = 1f;
            endAlpha = 0f;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(llVerify, "alpha", starAlpha, endAlpha).setDuration(500);
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
                isAnimating = false;
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
        colors = new int[]{getResources().getColor(R.color.white), getResources().getColor(R.color.secondary_text_half_white)};

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

    /**
     * 添加指示点
     */
    private void initLoginViewIndicator() {
        for (int i = 0; i < mLoginData.size(); i++) {
            //添加指示点
            ImageView ivIndicator = new ImageView(this);    //灰、白指示点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 20;
            ivIndicator.setLayoutParams(params);
            ivIndicator.setBackgroundResource(R.drawable.guide_indicator_bg);
            //初始时第一个为白色，其余灰色
            if (i == 0) {
                ivIndicator.setEnabled(true);
            } else {
                ivIndicator.setEnabled(false);
            }
            llIndicator.addView(ivIndicator);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_WHAT_REQUEST_VERIFYCODE_SUCCESS:
                    showToast("验证码已发送");
                    TimeCount timeCount = new TimeCount(60 * 1000, 1000, tvSendVerifyCode);
                    timeCount.start();
                    break;
                case MSG_WHAT_RESUEST_VERIFYCODE_FAILED:
                    showToast("验证码发送失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
