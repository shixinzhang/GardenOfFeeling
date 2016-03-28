package sxkeji.net.dailydiary.common.activitys;

import android.database.Cursor;
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
import sxkeji.net.dailydiary.utils.LogUtils;

/**
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
    private Cursor cursor;
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

        String columnName = ArticleDao.Properties.Date.columnName;
        String orderBy = columnName + " COLLATE LOCALIZED ASC";
        cursor = db.query(articleDao.getTablename(), articleDao.getAllColumns(), null, null, null, null, orderBy);

        Query<Article> query = articleDao.queryBuilder().orderDesc(ArticleDao.Properties.Date).build();
        tempData = query.list();
        adapter = new AllArticlesRecyclerAdapter(tempData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        cursor.close();
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
