package sxkeji.net.dailydiary.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.storage.SharedPreferencesUtils;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.ViewUtils;

/**
 * 设置
 * Created by zhangshixin on 4/14/2016.
 */
public class SettingActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title_todo)
    TextView tvTitleTodo;
    @Bind(R.id.tv_todo_auto_save)
    TextView tvTodoAutoSave;
    @Bind(R.id.switch_todo_auto_save)
    SwitchCompat switchTodoAutoSave;
    @Bind(R.id.rl_todo_auto_save)
    RelativeLayout rlTodoAutoSave;
    @Bind(R.id.tv_todo_show_when_lock)
    TextView tvTodoShowWhenLock;
    @Bind(R.id.switch_todo_show_when_lock)
    SwitchCompat switchTodoShowWhenLock;
    @Bind(R.id.rl_todo_show_when_lock)
    RelativeLayout rlTodoShowWhenLock;
    @Bind(R.id.tv_title_sync)
    TextView tvTitleSync;
    @Bind(R.id.tv_sync_auto)
    TextView tvSyncAuto;
    @Bind(R.id.switch_sync_auto)
    SwitchCompat switchSyncAuto;
    @Bind(R.id.rl_sync_auto)
    RelativeLayout rlSyncAuto;
    @Bind(R.id.tv_sync_platform)
    TextView tvSyncPlatform;
    @Bind(R.id.rl_sync_platform)
    RelativeLayout rlSyncPlatform;
    @Bind(R.id.tv_title_security)
    TextView tvTitleSecurity;
    @Bind(R.id.tv_security_app_lock)
    TextView tvSecurityAppLock;
    @Bind(R.id.switch_security_app_lock)
    SwitchCompat switchSecurityAppLock;
    @Bind(R.id.rl_security_app_lock)
    RelativeLayout rlSecurityAppLock;
    @Bind(R.id.tv_title_reminder)
    TextView tvTitleReminder;
    @Bind(R.id.tv_reminder_every_day)
    TextView tvReminderEveryDay;
    @Bind(R.id.switch_reminder_every_day)
    SwitchCompat switchReminderEveryDay;
    @Bind(R.id.rl_reminder_every_day)
    RelativeLayout rlReminderEveryDay;
    @Bind(R.id.tv_reminder_time)
    TextView tvReminderTime;
    @Bind(R.id.rl_reminder_time)
    RelativeLayout rlReminderTime;
    @Bind(R.id.tv_reminder_way)
    TextView tvReminderWay;
    @Bind(R.id.rl_reminder_way)
    RelativeLayout rlReminderWay;
    @Bind(R.id.tv_title_about)
    TextView tvTitleAbout;
    @Bind(R.id.tv_about_app)
    TextView tvAboutApp;
    @Bind(R.id.tv_about_pay)
    TextView tvAboutPay;
    @Bind(R.id.tv_title_feedback)
    TextView tvTitleFeedback;
    @Bind(R.id.tv_feedback_make_better_together)
    TextView tvFeedbackMakeBetterTogether;
    @Bind(R.id.tv_feedback_evaluate)
    TextView tvFeedbackEvaluate;
    @Bind(R.id.tv_feedback_directly)
    TextView tvFeedbackDirectly;
    @Bind(R.id.btn_login_out)
    TextView btnLoginOut;

    private boolean isLogin;
    private boolean autoSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initData();
        initViews();
        setListeners();
    }

    private void initData() {
        isLogin = (boolean) SharedPreferencesUtils.get(this, Constant.ACCOUNT_IS_LOGIN, false);
        autoSync = (boolean) SharedPreferencesUtils.get(this, Constant.SETTING_TODO_AUTO_SYNC, false);
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

        if (isLogin) {
            btnLoginOut.setText("退出当前账号");
        } else {
            btnLoginOut.setText("登录");
        }

        switchSyncAuto.setChecked(autoSync);
    }

    private void setListeners() {
        switchSyncAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isLogin) {
                    // isChecked == true ? 开启自动上传 : 关闭自动上传
                    saveSettingState(Constant.SETTING_TODO_AUTO_SYNC, switchSyncAuto.isChecked());
                } else {
                    // TODO: 4/28/2016  弹出是否去登录
                    View yesTextView = showPopupWindow("您还没有登录，是否去登录?");
                    if (yesTextView != null) {
                        yesTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                intent.putExtra(Constant.EXTRA_TO, Constant.ACTIVITY_SETTING);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }
        });

        btnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    View yesTextView = showPopupWindow("你将要退出当前账号");
                    if (yesTextView != null) {
                        yesTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferencesUtils.clear(SettingActivity.this);
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                intent.putExtra(Constant.EXTRA_TO, Constant.ACTIVITY_SETTING);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } else {
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.EXTRA_TO, Constant.ACTIVITY_SETTING);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 保存状态
     *
     * @param name
     * @param value
     */
    private void saveSettingState(String name, boolean value) {
        SharedPreferencesUtils.put(SettingActivity.this, name, value);
        LogUtils.e("saveSettingState", "save name " + name + "/ value " + value);
    }


    /**
     * 弹出提示框，
     *
     * @param title 提示标题
     * @return 确认按钮的View, 便于后续操作
     */
    private View showPopupWindow(String title) {
        if (TextUtils.isEmpty(title)) {
            return null;
        }
        View view = ViewUtils.showPopupWindow(SettingActivity.this, R.layout.pop_login_warning, toolbar, 1);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
        TextView tvYes = (TextView) view.findViewById(R.id.tv_yes);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.dismissPopup();
            }
        });
        return tvYes;
    }

    private void jump2Activity(Class claz) {
        Intent intent = new Intent(SettingActivity.this, claz);
        startActivity(intent);
    }
}
