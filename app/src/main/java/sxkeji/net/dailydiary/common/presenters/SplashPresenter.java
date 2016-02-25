package sxkeji.net.dailydiary.common.presenters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import sxkeji.net.dailydiary.common.models.INetWorkBiz;
import sxkeji.net.dailydiary.common.models.NetWorkBiz;
import sxkeji.net.dailydiary.common.views.ISplashView;


/**
 *
 * Created by zhangshixin on 2015/11/26.
 *
 * @description Codes there always can be better.
 */
public class SplashPresenter {
    private INetWorkBiz mINetWorkBiz;
    private ISplashView mISplashView;
    private final int DELAY_TIME = 2000;

    public SplashPresenter(ISplashView iView){
        this.mISplashView = iView;
        mINetWorkBiz = new NetWorkBiz();
    }

    public void doBusiness(Context context){

        if(mISplashView.showGuideViewPager()){
            return;
        }

        mISplashView.showSplashPic();
        handler.sendEmptyMessageDelayed(0, DELAY_TIME);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        mISplashView.jumpToNextActivity();
//
//        mISplashView.showProgressDialog();
//        if(mINetWorkBiz.isNetWorkConnected(context)){
//            handler.sendEmptyMessageDelayed(0, DELAY_TIME);
//        }else{
//            handler.sendEmptyMessageDelayed(1, DELAY_TIME);
//        }
//        handler.sendEmptyMessageDelayed(2, DELAY_TIME);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mISplashView.jumpToNextActivity();
                    break;
                case 1:
                    mISplashView.showNoNetDialog();
                    break;
                case 2:
                    mISplashView.hideProgressDialog();
                    break;
            }
        }
    };
}
