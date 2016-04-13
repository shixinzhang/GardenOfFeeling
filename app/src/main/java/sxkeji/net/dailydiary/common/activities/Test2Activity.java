package sxkeji.net.dailydiary.common.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;


public class Test2Activity extends Activity {

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
        setContentView(R.layout.activity_test_palette);
        ButterKnife.bind(this);



        ViewOutlineProvider viewOutlineProvider =
                new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setOval(0, 0, view.getWidth(), view.getHeight());
                    }
                };
        tvTest.setOutlineProvider(viewOutlineProvider);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTransitionAnimation() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Explode());
//        getWindow().setEnterTransition(new Fade());
//        getWindow().setExitTransition(new Slide());
        getWindow().setExitTransition(new Fade());
    }

}
