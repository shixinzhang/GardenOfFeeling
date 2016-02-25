package sxkeji.net.dailydiary.common.activitys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.transition.Fade;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;


public class MainActivity extends Activity {

    @Bind(R.id.tv_test)
    TextView tvTest;
    @Bind(R.id.tv_test1)
    TextView tvTest1;
    @Bind(R.id.tv_test2)
    TextView tvTest2;
    @Bind(R.id.tv_test3)
    TextView tvTest3;
    @Bind(R.id.tv_test4)
    TextView tvTest4;
    @Bind(R.id.tv_test5)
    TextView tvTest5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransitionAnimation();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        ViewOutlineProvider viewOutlineProvider =
                new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setOval(0, 0, view.getWidth(), view.getHeight());
                    }
                };
        tvTest.setOutlineProvider(viewOutlineProvider);
        testPalette();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTransitionAnimation() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Explode());
//        getWindow().setEnterTransition(new Fade());
//        getWindow().setExitTransition(new Slide());
        getWindow().setExitTransition(new Fade());
    }

    private void testPalette() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test3);
        //����Palette����
        Palette.generateAsync(bitmap,
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        //ͨ��palette��ȡ��Ӧ��ɫ��
                        Palette.Swatch swatch = palette.getVibrantSwatch();       //����
                        Palette.Swatch swatch1 = palette.getDarkVibrantSwatch();   //������ɫ
                        Palette.Swatch swatch2 = palette.getLightVibrantSwatch();    //����ǳɫ
                        Palette.Swatch swatch3 = palette.getMutedSwatch();         //���
                        Palette.Swatch swatch4 = palette.getDarkMutedSwatch();     //�����ɫ
                        Palette.Swatch swatch5 = palette.getLightMutedSwatch();    //���ǳɫ

                        if(swatch != null)
                            tvTest.setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));
                        if(swatch1 != null) {
                            tvTest1.setBackgroundDrawable(new ColorDrawable(swatch1.getRgb()));
//                            ObjectAnimator animator = ObjectAnimator.ofFloat(tvTest1,"translationZ",100);
//                            animator.start();
                        }

                        if(swatch2 != null)
                            tvTest2.setBackgroundDrawable(new ColorDrawable(swatch2.getRgb()));
                        if(swatch3 != null)
                            tvTest3.setBackgroundDrawable(new ColorDrawable(swatch3.getRgb()));
                        if(swatch4 != null)
                            tvTest4.setBackgroundDrawable(new ColorDrawable(swatch4.getRgb()));
                        if(swatch5 != null)
                            tvTest5.setBackgroundDrawable(new ColorDrawable(swatch5.getRgb()));
                    }
                });

    }
}
