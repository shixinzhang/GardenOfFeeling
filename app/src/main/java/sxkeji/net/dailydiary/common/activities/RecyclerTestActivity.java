package sxkeji.net.dailydiary.common.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import net.sxkeji.dailydiary.R;
import sxkeji.net.dailydiary.common.views.adapters.MyRecyclerAdapter;

/**
 * Created by zhangshixin on 2015/12/2.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class RecyclerTestActivity extends Activity {
    @Bind(R.id.btn_jump)
    Button btnJump;

    private Button btn_change;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> listData = new ArrayList<>();
    private boolean isLinearLayout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initView();
        setRecyclerData();
    }

//    @OnClick(R.id.btn_jump)
//    void jumpToNext(){
//        Intent intent = new Intent(RecyclerTestActivity.this, Test2Activity.class);
////        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this
////                , btnJump ,"jump").toBundle());
//        startActivity(intent);
//    }

    private void setRecyclerData() {
        for (int i = 0; i < 10; i++)
            listData.add("RecyclerView");

        mRecyclerAdapter = new MyRecyclerAdapter(listData, R.layout.recycler_test_item);
        recyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setmOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {

//                if (isLinearLayout) {
//                    addRecycler(view);
//                } else {
//                    delRecycler(view);
//                }
                view.animate().translationX(300).setDuration(3000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                view.animate().translationX(0).setDuration(3000).start();
                            }
                        }).start();
            }
        });
    }

    private void initView() {
        btn_change = (Button) findViewById(R.id.btn_change);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setHasFixedSize(true);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLinearLayout) {
                    recyclerView.setLayoutManager(new GridLayoutManager(RecyclerTestActivity.this, 3));
                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerTestActivity.this));
                }
                isLinearLayout = !isLinearLayout;
            }
        });
    }

    public void addRecycler(View view) {
        listData.add("Recycler_new");
        int position = listData.size();
        if (position > 0) {
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public void delRecycler(View view) {
        int position = listData.size();
        if (position > 0) {
            listData.remove(position - 1);
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
