package sxkeji.net.dailydiary.common.views.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import net.sxkeji.dailydiary.R;
import sxkeji.net.dailydiary.beans.LoginGuideBean;

/**
 * 登录页ViewPager的adapter
 * Created by sxzhang on 2016/2/23.
 * Codes can never be perfect!
 */
public class LoginViewPaperAdapter extends PagerAdapter {
    private ArrayList<LoginGuideBean> mData;
    private Context mContext;

    public LoginViewPaperAdapter(Context context, ArrayList<LoginGuideBean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        } else
            return 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_login_viewpager, null);
        view.findViewById(R.id.iv_img).setBackgroundResource(mData.get(position).getImgId());
        ((TextView)view.findViewById(R.id.tv_title)).setText(mData.get(position).getTitle());
        ((ViewPager) container).addView(view, 0);
        return view;
    }


}
