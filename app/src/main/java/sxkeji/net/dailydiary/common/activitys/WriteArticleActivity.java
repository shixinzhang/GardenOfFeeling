package sxkeji.net.dailydiary.common.activitys;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;

/**
 * Created by zhangshixin on 3/17/2016.
 */
public class WriteArticleActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_action_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackToast("取消编辑");
            }
        });
    }

    private void showSnackToast(String str){
        if(str != null){
            Snackbar.make(toolbar, str, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_article,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu_undo:
                break;
            case R.id.item_menu_redo:
                break;
            case R.id.item_menu_done:
                showSnackToast("done");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
