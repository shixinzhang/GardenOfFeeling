package sxkeji.net.dailydiary.common.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地导出Tabs的适配器
 * Created by zhangshixin on 3/7/2016.
 */
public class LocalExportVPAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments;    //每个Tab对应的界面
    private final List<String> mFragmentTitles; //每个Tab的标题

    public LocalExportVPAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragmentTitles = new ArrayList<>();
    }

    /**
     * 添加Fragment及标题
     * @param fragment
     * @param title
     */
    public void addFragment(Fragment fragment,String title){
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
