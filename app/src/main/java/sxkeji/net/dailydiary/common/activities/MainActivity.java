package sxkeji.net.dailydiary.common.activities;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.beans.OpenEyeDailyBean;
import sxkeji.net.dailydiary.common.BaseActivity;
import sxkeji.net.dailydiary.common.views.adapters.MainTabsVPAdapter;
import sxkeji.net.dailydiary.http.HttpClient;
import sxkeji.net.dailydiary.http.HttpResponseHandler;
import sxkeji.net.dailydiary.storage.ACache;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.storage.SharedPreferencesUtils;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.UIUtils;

/**
 * 主页
 * Created by zhangshixin on 3/14/2016.
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.vp_tab_content)
    ViewPager vpTabContent;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    @Bind(R.id.img_user)
    ImageView imgUser;
    @Bind(R.id.tv_name)
    TextView userName;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fab_add)
    FloatingActionButton fabAdd;
    @Bind(R.id.tv_motto)
    TextView tvMotto;
    @Bind(R.id.tv_diary_count)
    TextView tvDiaryCount;
    @Bind(R.id.rl_home)
    RelativeLayout rlHome;
    @Bind(R.id.tv_draft_count)
    TextView tvDraftCount;
    @Bind(R.id.rl_draft)
    RelativeLayout rlDraft;
    @Bind(R.id.tv_reminder_count)
    TextView tvReminderCount;
    @Bind(R.id.rl_reminder)
    RelativeLayout rlReminder;
    @Bind(R.id.rl_backup)
    RelativeLayout rlBackup;
    @Bind(R.id.rl_local_export)
    RelativeLayout rlLocalExport;
    @Bind(R.id.rl_change_theme)
    RelativeLayout rlChangeTheme;
    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;
    @Bind(R.id.rl_about)
    RelativeLayout rlAbout;
    private RelativeLayout rlSelectView;
    private ActionBarDrawerToggle mDrawerToggle;
    private OpenEyeDailyBean mDailyBean;
    long[] mHits = new long[2];
    ACache aCache;
    private boolean isLogin;
    private String userNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        loadData();
        setListeners();
    }

    private void setListeners() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator.ofFloat(fabAdd, "rotation", 0f, 225f).setDuration(1000).start();
                showChooseBottomSheet();
            }
        });

        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlHome);
            }
        });

        rlDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlDraft);
            }
        });

        rlReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlReminder);
            }
        });

        rlBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlBackup);
                isLogin = (boolean) SharedPreferencesUtils.get(MainActivity.this, Constant.ACCOUNT_IS_LOGIN, false);
                if (!isLogin) {   //未登录
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.EXTRA_TO, Constant.ACTIVITY_CLOUD_BACK);
                    startActivity(intent);
                } else {
                    jumpToActivity(CloudBackupActivity.class);
                }
            }
        });

        rlLocalExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlLocalExport);
                isLogin = (boolean) SharedPreferencesUtils.get(MainActivity.this, Constant.ACCOUNT_IS_LOGIN, false);
                if (!isLogin) {   //未登录
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.EXTRA_TO, Constant.ACTIVITY_LOCAL_EXPROT);
                    startActivity(intent);
                } else {
                    jumpToActivity(LocalExportActivity.class);
                }
            }
        });

        rlChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlChangeTheme);
//                changeTheme();
            }
        });

        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlSetting);
                jumpToActivity(SettingActivity.class);
//                jumpToActivity(CreateGestureActivity.class);
            }
        });

        rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBgAndCloseDrawer(rlAbout);
                jumpToActivity(AboutActivity.class);
            }
        });
    }

    /**
     * 检查是否登录，有些操作需要登录后才可以进行
     */
    private void checkLoginState() {
        isLogin = (boolean) SharedPreferencesUtils.get(this, Constant.ACCOUNT_IS_LOGIN, false);
        userNumber = (String) SharedPreferencesUtils.get(this, Constant.ACCOUNT_USER_NUMBER, "");
        if (TextUtils.isEmpty(userNumber)) {
            LogUtils.e("checkLoginState", "userNumber is null");
        } else {
            LogUtils.e("checkLoginState", "userNumber " + userNumber);
        }

        if (!TextUtils.isEmpty(userNumber)) {
            userName.setText(userNumber);
        }
    }

    /**
     * 选择要添加的类型
     */
    private void showChooseBottomSheet() {
        new BottomSheet.Builder(MainActivity.this)
                .title(getResources().getString(R.string.add_title))
                .sheet(R.menu.menu_list_add)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.item_article:
                                //TODO: startActivityForResult 当创建成功后再查询

                                jumpToActivity(ArticleWriteActivity.class);
                                break;
                            case R.id.item_reminder:
                                jumpToActivity(TodoWriteActivity.class);
                                break;
                            case R.id.item_cancel:
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ObjectAnimator.ofFloat(fabAdd, "rotation", 225f, 360f).setDuration(1000).start();
                    }
                }).show();
    }

    /**
     * 跳转
     *
     * @param clazz
     */
    private void jumpToActivity(Class<?> clazz) {
        startActivity(new Intent(MainActivity.this, clazz));
    }

    /**
     * 显示SnackBar
     *
     * @param str
     */
    private void showSnackToast(String str) {
        if (str != null) {
            Snackbar.make(mainContent, str, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        aCache = ACache.get(this);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);

//        Intent intent = new Intent();
//        intent.resolveActivity(getPackageManager());
    }

    /**
     * 加载数据
     */
    private void loadData() {
        checkLoginState();
//        loadRecommandData();
        loadTabsViewPagerData();
        loadTabsData();
    }

    private void loadRecommandData() {
        final Date now = new Date();
        //上次更新开眼数据时间
        long lastUpdateOpenEyeTime = (long) SharedPreferencesUtils.get(this, Constant.LAST_UPDATE_OPEN_EYE, now.getTime());
        long updateTime = now.getTime() - lastUpdateOpenEyeTime;        //距离上次更新多久了，单位毫秒
        long oneDay = 1000 * 60 * 60 * 24;
        if (updateTime == 0 || updateTime > oneDay) {                   //头一次启动或者上次更新是一天前，更新
            LogUtils.e("loadRecommandData", "It's time to update open eye");
            Map<String, String> map = new HashMap<>();
            map.put("num", "2");
            HttpClient.builder(MainActivity.this).get(Constant.URL_OPEN_EYE_DIALY, map, new HttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    if (!TextUtils.isEmpty(content)) {
                        aCache.put(Constant.OPEN_EYE_DATA, content);
                        SharedPreferencesUtils.put(MainActivity.this, Constant.LAST_UPDATE_OPEN_EYE, now.getTime());
                    }
                    Log.e("loadRecommandData", content.toString());
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    super.onFailure(request, e);
                }
            });

        } else {        //上次更新到目前为止不足一天，不更新
            LogUtils.e("loadRecommandData", "left time : " + updateTime + " / " + oneDay);
        }
    }

    /**
     * 加载Tab列表的viewPager的内容
     */

    private void loadTabsViewPagerData() {
        MainTabsVPAdapter mTabsVPAdapter = new MainTabsVPAdapter(getSupportFragmentManager());
        mTabsVPAdapter.addFragment(new HomeFragment(), "文章");
        mTabsVPAdapter.addFragment(new TodoListFragment(), "待做");
        mTabsVPAdapter.addFragment(new RecommandFragment(), "发现");
        vpTabContent.setAdapter(mTabsVPAdapter);
    }

    /**
     * 加载Tab列表
     */
    private void loadTabsData() {
        for (int i = 0; i < 3; i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setupWithViewPager(vpTabContent);
    }

    /**
     * 设置选中背景色，关闭抽屉
     *
     * @param rlSelect
     */
    private void changeBgAndCloseDrawer(RelativeLayout rlSelect) {
        if (rlSelectView != null) {
            rlSelectView.setBackgroundColor(Color.WHITE);
        }
        rlSelectView = rlSelect;
        rlSelectView.setBackgroundColor(Color.parseColor("#eeeeee"));
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
    }

    @Override
    public void
    onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        UIUtils.showToastSafe(MainActivity.this, "再次点击退出365记");

        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();

        if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {

            System.exit(0);
            Process.killProcess(Process.myPid());

        }
    }
}
