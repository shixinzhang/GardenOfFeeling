package sxkeji.net.dailydiary.widgets;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import sxkeji.net.dailydiary.R;


/**
 * Created by sxzhang on 2016/2/24.
 * Codes can never be perfect!
 */
public class GuidePageTransformer implements PageTransformer {

    private static final float MIN_SCALE = 0.85f;
//            private static final float MIN_ALPHA = 0.5f;
    private static final float MIN_TXT_SCALE = 0.0f;
    private static final float MIN_TXT_ALPHA = 0.0f;

    /**
     * @param position
     * 0 is front and center.
     * 1 is one full page position to the right,
     * and -1 is one page position to the left.
     */
    @SuppressLint("NewApi")
    @Override
    public void transformPage(View view, float position) {
        View mGuideImage = view.findViewById(R.id.tv_pic);
        View mTitle = view.findViewById(R.id.tv_title);
        View mDesc = view.findViewById(R.id.tv_desc);

        int viewWidth = mDesc.getWidth();

        if (position < -1) {
            mTitle.setAlpha(0);
            mDesc.setAlpha(0);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float scaleTxtFactor = Math.max(MIN_TXT_SCALE, 1 - Math.abs(position));

            mGuideImage.setScaleX(scaleFactor);
            mGuideImage.setScaleY(scaleFactor);

            mTitle.setScaleX(scaleTxtFactor);
            mTitle.setScaleY(scaleTxtFactor);
            mTitle.setAlpha(MIN_TXT_ALPHA + (scaleTxtFactor - MIN_TXT_SCALE) / (1 - MIN_TXT_SCALE) * (1 - MIN_TXT_ALPHA));

            mDesc.setAlpha(mTitle.getAlpha());
            mDesc.setScaleX(scaleTxtFactor);
            mDesc.setScaleY(scaleTxtFactor);
        } else {
            mTitle.setAlpha(0);
            mDesc.setAlpha(0);
        }
    }
}
