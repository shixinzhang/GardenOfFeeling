package sxkeji.net.dailydiary.common.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import net.sxkeji.dailydiary.R;

/**
 * Created by zhangshixin on 3/9/2016.
 */
public class TestActivity extends Activity {
    @Bind(R.id.tv_font_test)
    TextView tvFontTest;
    @Bind(R.id.tv_font_2)
    TextView tvFont2;
    @Bind(R.id.tv_font_3)
    TextView tvFont3;
    @Bind(R.id.tv_font_4)
    TextView tvFont4;
    @Bind(R.id.tv_font_5)
    TextView tvFont5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fonts);
        ButterKnife.bind(this);

        initFonts();
    }

    private void initFonts() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/lishu.ttf");    //samplefont.ttf
        tvFontTest.setTypeface(typeface);

        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf");    //samplefont.ttf
        tvFont2.setTypeface(typeface2);

        Typeface typeface3 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");    //samplefont.ttf
        tvFont3.setTypeface(typeface3);

        Typeface typeface4 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");    //samplefont.ttf
        tvFont4.setTypeface(typeface4);

        Typeface typeface5 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");    //samplefont.ttf
        tvFont5.setTypeface(typeface5);
    }
}
