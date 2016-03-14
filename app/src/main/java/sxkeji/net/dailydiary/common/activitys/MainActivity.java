package sxkeji.net.dailydiary.common.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

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

    private ActionBarDrawerToggle mDrawerToggle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        loadData();
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
        MainTabsVPAdapter  mTabsVPAdapter = new MainTabsVPAdapter(getSupportFragmentManager());
        for (int i = 0; i < 3; i++) {
            mTabsVPAdapter.addFragment(new HomeFragment(), "目录" + i);
        }

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
