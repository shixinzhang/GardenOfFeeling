package sxkeji.net.dailydiary.common.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.sxkeji.dailydiary.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.common.views.adapters.LocalExportVPAdapter;

/**
 * 本地导入、导出
 * Created by zhangshixin on 4/14/2016.
 */
public class LocalExportActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_tab_content)
    ViewPager vpTabContent;

    private LocalExportVPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    private void loadData() {
        loadViewPagers();
        loadTabsData();
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
    }

    /**
     * 加载Tab列表的viewPager的内容
     */
    private void loadViewPagers() {
        adapter = new LocalExportVPAdapter(getSupportFragmentManager());
        adapter.addFragment(new LocalExportFragment(), "导出");
        adapter.addFragment(new TodoListFragment(), "导入");
        vpTabContent.setAdapter(adapter);
    }

    /**
     * 加载Tab列表
     */
    private void loadTabsData() {
        for (int i = 0; i < adapter.getCount(); i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setupWithViewPager(vpTabContent);
    }
}
