package sxkeji.net.dailydiary.common.activitys;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.views.adapters.MainTabsVPAdapter;

/**
 * Created by zhangshixin on 3/14/2016.
 */
public class MainActivity extends AppCompatActivity {
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
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fab_add)
    FloatingActionButton fabAdd;

    private ActionBarDrawerToggle mDrawerToggle;

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
                ObjectAnimator.ofFloat(fabAdd,"rotation",0f,225f).setDuration(1000).start();
                showChooseBottomSheet();
            }
        });
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
                                showSnackToast("创建随笔");
                                break;
                            case R.id.item_reminder:
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
                        ObjectAnimator.ofFloat(fabAdd,"rotation",225f,360f).setDuration(1000).start();
                    }
                }).show();
    }

    /**
     * 显示SnackBar
     * @param str
     */
    private void showSnackToast(String str) {
        if (str != null) {
            Snackbar.make(mainContent, str, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);

    }

    /**
     * 加载数据
     */
    private void loadData() {
        loadTabsViewPagerData();
        loadTabsData();
    }

    /**
     * 加载Tab列表的viewPager的内容
     */
    private void loadTabsViewPagerData() {
        MainTabsVPAdapter mTabsVPAdapter = new MainTabsVPAdapter(getSupportFragmentManager());
        mTabsVPAdapter.addFragment(new HomeFragment(), "全部");
        mTabsVPAdapter.addFragment(new HomeFragment(), "文件夹");
        mTabsVPAdapter.addFragment(new HomeFragment(), "推荐");
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
}
