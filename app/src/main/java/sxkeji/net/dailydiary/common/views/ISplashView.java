package sxkeji.net.dailydiary.common.views;

/**
 * Created by zhangshixin on 2015/11/26.
 *
 * @description Codes there always can be better.
 */
public interface ISplashView {
    boolean showGuideViewPager();
    void showSplashPic();
    void showProgressDialog();
    void hideProgressDialog();
    void showNoNetDialog();
    void jumpToNextActivity();
}
