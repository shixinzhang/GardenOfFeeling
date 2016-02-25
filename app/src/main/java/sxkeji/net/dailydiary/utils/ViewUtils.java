package sxkeji.net.dailydiary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sxkeji.net.dailydiary.common.BaseApplication;

/**
 * View相关工具类
 * Created by zhangshixin on 2015/11/26.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class ViewUtils {
    /**
     * 把自身从父View中移除
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 请求View树重新布局，用于解决中层View有布局状态而导致上层View状态断裂
     */
    public static void requestLayoutParent(View view, boolean isAll) {
        ViewParent parent = view.getParent();
        while (parent != null && parent instanceof View) {
            if (!parent.isLayoutRequested()) {
                parent.requestLayout();
                if (!isAll) {
                    break;
                }
            }
            parent = parent.getParent();
        }
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    /**
     * 判断触点是否落在该View上
     */
    public static boolean isTouchInView(MotionEvent ev, View v) {
        int[] vLoc = new int[2];
        v.getLocationOnScreen(vLoc);
        float motionX = ev.getRawX();
        float motionY = ev.getRawY();
        return motionX >= vLoc[0] && motionX <= (vLoc[0] + v.getWidth()) && motionY >= vLoc[1] && motionY <= (vLoc[1] + v.getHeight());
    }

    /**
     * FindViewById的泛型封装，减少强转代码
     */
    public static <T extends View> T findViewById(View layout, int id) {
        return (T) layout.findViewById(id);
    }

    public static Bitmap bigImage(Bitmap bmp, float big) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        /* 产生reSize后的Bitmap对象 */
        Matrix matrix = new Matrix();
        matrix.postScale(big, big);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,
                bmpHeight, matrix, true);

        return resizeBmp;
    }

    /**
     * sp2Px
     *
     * @param context context
     * @param sp      sp
     * @return float
     */
    public static float sp2Px(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp2Px
     *
     * @param sp sp
     * @return float
     */
    public static float sp2Px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                BaseApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * dp2Px
     *
     * @param context context
     * @param dp      dp
     * @return float
     */
    public static float dp2Px(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * dp2Px
     *
     * @param dp dp
     * @return float
     */
    public static float dp2Px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                BaseApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * px2Dp
     *
     * @param context context
     * @param px      px
     * @return float
     */
    public static float px2Dp(Context context, int px) {
        return context.getResources().getDisplayMetrics().density * px;
    }

    /**
     * px2Dp
     *
     * @param px px
     * @return float
     */
    public static float px2Dp(int px) {
        return BaseApplication.getInstance().getResources().getDisplayMetrics().density * px;
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public static void blur(Context context, Bitmap bkg, View view) {
//        int radius = 20;
//        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()), (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(overlay);
//        canvas.translate(-view.getLeft(), -view.getTop());
//        canvas.drawBitmap(bkg, 0, 0, null);
//        view.setBackground(new BitmapDrawable(context.getResources(), Blur.apply(context, overlay, radius)));
//    }

    public static void tvUnderLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }
}
