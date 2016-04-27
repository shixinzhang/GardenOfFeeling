package sxkeji.net.dailydiary.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.storage.SharedPreferencesUtils;
import sxkeji.net.dailydiary.utils.LogUtils;

/**
 * BaseActivity
 * Created by zhangshixin on 4/27/2016.
 */
public class BaseActivity extends AppCompatActivity {
    private final String TAG = "BaseActivity";
    private String mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
    }


    /**
     * 根据设置选择主题
     */
    public void setTheme() {
        mTheme = (String) SharedPreferencesUtils.get(this, Constant.THEME, Constant.THEME_LIGHT);
        if (mTheme.equals(Constant.THEME_LIGHT)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        LogUtils.e(TAG, "setTheme " + mTheme);
    }

    /**
     * 切换主题
     */
    public void changeTheme() {
        if (mTheme.equals(Constant.THEME_LIGHT)) {
            SharedPreferencesUtils.put(this, Constant.THEME, Constant.THEME_DARK);
        } else {
            SharedPreferencesUtils.put(this, Constant.THEME, Constant.THEME_LIGHT);
        }
        recreate();
    }

    /**
     * 隐藏输入法
     * @param view
     */
    public void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
