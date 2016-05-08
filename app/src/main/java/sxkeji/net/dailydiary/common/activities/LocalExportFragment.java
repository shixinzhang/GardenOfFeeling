package sxkeji.net.dailydiary.common.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import net.sxkeji.dailydiary.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.ArticleDao;
import sxkeji.net.dailydiary.Todo;
import sxkeji.net.dailydiary.TodoDao;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.common.views.adapters.ExportExpandAdapter;
import sxkeji.net.dailydiary.storage.Constant;

/**
 * 本地导出
 * Created by zhangshixin on 5/8/2016.
 */
public class LocalExportFragment extends Fragment {
    @Bind(R.id.expand_list)
    ExpandableListView expandList;
    private ExportExpandAdapter adapter;
    private ArrayList<String> mParentList;
    private ArrayList<Article> mArticleList;
    private ArrayList<Todo> mTodoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_export, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
        initView();
    }

    private void loadData() {
        loadParentData();
        loadArticleData();
        loadTodoData();
    }

    private void initView() {
        adapter = new ExportExpandAdapter(getContext());
        adapter.setParentData(mParentList)
                .setArticlesData(mArticleList)
                .setTodosData(mTodoList);
        expandList.setAdapter(adapter);
    }

    private void loadParentData() {
        mParentList = new ArrayList<>();
        mParentList.add(new String("文章"));
        mParentList.add(new String("待做事项"));
    }

    private void loadTodoData() {
        Query<Todo> query = BaseApplication.getDaoSession().getTodoDao()
                .queryBuilder()
                .orderDesc(TodoDao.Properties.Date)
                .build();
        mTodoList = (ArrayList<Todo>) query.list();
    }

    private void loadArticleData() {
        Query<Article> query = BaseApplication.getDaoSession().getArticleDao()
                .queryBuilder()
                .where(ArticleDao.Properties.Type.notEq(Constant.TYPE_DRAFT))
                .orderDesc(ArticleDao.Properties.Date)
                .build();
        mArticleList = (ArrayList<Article>) query.list();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
