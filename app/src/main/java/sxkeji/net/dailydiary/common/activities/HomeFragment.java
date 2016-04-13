package sxkeji.net.dailydiary.common.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.ArticleDao;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.common.views.adapters.AllArticlesRecyclerAdapter;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.utils.LogUtils;

/**
 * 首页 - 全部文字
 * Created by zhangshixin on 2015/12/10.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private SQLiteDatabase db;
    private ArticleDao articleDao;
    private AllArticlesRecyclerAdapter adapter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, null);
        ButterKnife.bind(this, view);

        initViews();
        return view;
    }

    private void initViews() {
        db = BaseApplication.getDb();
        articleDao = BaseApplication.getDaoSession().getArticleDao();

        setArticlesRecyclerData();
    }

    private void setArticlesRecyclerData() {
        LogUtils.e(TAG,"setArticlesRecyclerData");
        List<Article> tempData = new ArrayList<>();

        Query<Article> query = articleDao.queryBuilder().orderDesc(ArticleDao.Properties.Date).build();
        tempData = query.list();
        adapter = new AllArticlesRecyclerAdapter(tempData);
        adapter.setmOnItemClickListener(new AllArticlesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article, int position) {
                jumpToDetailActivity(article);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * 跳转到详情页面，传入文章详情
     * @param article
     */
    private void jumpToDetailActivity(Article article) {
        Intent detailIntent = new Intent(getActivity(),ArticleDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ARTICLE_BEAN, article);
        detailIntent.putExtras(bundle);
        startActivity(detailIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        setArticlesRecyclerData();
        LogUtils.e(TAG,"OnResume");
        super.onResume();
    }

    @Override
    public void onPause() {

        LogUtils.e(TAG,"onPause");
        super.onPause();
    }
}
