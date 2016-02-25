package sxkeji.net.dailydiary.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.utils.UIUtils;


/**
 * 退出时弹出的对话框
 * Created by zhangshixin on 2015/11/12.
 */
public class ExitAlertDialog {
    private Context mContext;
    private Dialog mDialog;
    private int mLayoutId;
    private int mWidthPx;
    private int mHeightPx;
    private TextView tv_yes, tv_no, tv_message;
    private View mDialogView;
    private String msg;

    public ExitAlertDialog(Context context) {
        this.mContext = context;
        setDefaultParams();
    }

    /**
     * 设置 默认参数
     */
    private void setDefaultParams() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.dialog);
        this.mDialog = builder.create();
        this.mLayoutId = R.layout.dialog_cancel_apply_master;
        this.mWidthPx = 253;
        this.mHeightPx = 137;

        initView();
    }

    private void initView() {
        mDialogView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
        tv_message = (TextView) mDialogView.findViewById(R.id.tv_message);
        tv_yes = (TextView) mDialogView.findViewById(R.id.tv_yes);
        tv_no = (TextView) mDialogView.findViewById(R.id.tv_no);
    }

    public void show(String msg) {
        this.msg = msg;
        if (msg != null && !TextUtils.isEmpty(msg))
            tv_message.setText(msg);
        this.show();
    }

    public void show() {

        mDialog.show();
        mDialog.getWindow().setContentView(mDialogView);
        mDialog.getWindow().setLayout(UIUtils.dip2px(mContext, mWidthPx), UIUtils.dip2px(mContext, mHeightPx));
        mDialog.setCanceledOnTouchOutside(false);

        /**
         * 默认点击事件
         * "是"：对话框消失、退出当前页面 ；
         * "否"：对话框消失
         * 需要自定义点击事件时，可通过 getPositiveTextView().setOnClick....设置
         */
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
//                if (!TextUtils.isEmpty(msg)) {
//                    if (msg.equals(mContext.getResources().getString(R.string.cancel_reset_pwd))) {
//                        UmengEventUtils.onEventId(mContext, UmengEventUtils.YesToQuitResetPwd);
//                    } else {
//                        UmengEventUtils.onEventId(mContext, UmengEventUtils.YesToQuitApplyCarMaster);
//                    }
//                }
                ((Activity) mContext).finish();

            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!TextUtils.isEmpty(msg)) {
//                    if (msg.equals(mContext.getResources().getString(R.string.cancel_reset_pwd))) {
//                        UmengEventUtils.onEventId(mContext, UmengEventUtils.NotToQuitResetPwd);
//                    } else {
//                        UmengEventUtils.onEventId(mContext, UmengEventUtils.NotToQuitApplyCarMaster);
//                    }
//                }
                mDialog.dismiss();
            }
        });
    }


    public Dialog getmDialog() {
        return mDialog;
    }

    public void setmDialog(Dialog mDialog) {
        this.mDialog = mDialog;
    }

    public int getmLayoutId() {
        return mLayoutId;
    }

    public void setmLayoutId(int mLayoutId) {
        this.mLayoutId = mLayoutId;
    }

    public int getmWidthPx() {
        return mWidthPx;
    }

    public void setmWidthPx(int mWidthPx) {
        this.mWidthPx = mWidthPx;
    }

    public int getmHeightPx() {
        return mHeightPx;
    }

    public void setmHeightPx(int mHeightPx) {
        this.mHeightPx = mHeightPx;
    }

    public View getPositiveTextView() {
        return this.tv_yes;
    }

    public View getNegativeTextView() {
        return this.tv_no;
    }


}
