package sxkeji.net.dailydiary.common.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

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
import sxkeji.net.dailydiary.utils.JWordUtils;
import sxkeji.net.dailydiary.utils.LogUtils;

/**
 * 本地导出
 * Created by zhangshixin on 5/8/2016.
 */
public class LocalExportFragment extends Fragment {
    private final String TAG = "LocalExportFragment";
    @Bind(R.id.expand_list)
    ExpandableListView expandList;
    @Bind(R.id.fab_export)
    FloatingActionButton fabExport;
    private ExportExpandAdapter adapter;
    private ArrayList<String> mParentList;
    private ArrayList<Article> mArticleList;
    private ArrayList<Todo> mTodoList;
    private ArrayList<Article> mSelectArticles;
    private ArrayList<Todo> mSelectTodos;

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
        setListeners();
    }

    private void setListeners() {
        fabExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mSelectArticles == null || mSelectArticles.size() < 1) &&
                        (mSelectTodos == null || mSelectTodos.size() < 1) ) {
                    showToast("您还没有选择要导出的内容");
                } else {
                    if (mSelectArticles.size() > 0){
                        JWordUtils jWordUtils = new JWordUtils(getContext());
                        jWordUtils.createArticle2Word(mSelectArticles);
                    }
                }
            }
        });
    }

    private void showToast(String str) {
        if (!TextUtils.isEmpty(str)) {
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        mSelectArticles = new ArrayList<>();
        loadParentData();
        loadArticleData();
        loadTodoData();
    }

    private void initView() {
        adapter = new ExportExpandAdapter(getContext());
        adapter.setParentData(mParentList)
                .setArticlesData(mArticleList)
                .setTodosData(mTodoList)
                .setOnArticleSelectAllListener(new ExportExpandAdapter.OnArticleSelectAllListener() {
                    @Override
                    public void OnArticleSelect(Article article, CheckBox checkBox) {
                        checkBox.setChecked(!checkBox.isChecked());
                        if (checkBox.isChecked()) {         //添加
                            mSelectArticles.add(article);
                            LogUtils.e(TAG, "mSelectArticles.add size " + mSelectArticles.size());
                        } else {         //移除
                            mSelectArticles.remove(article);
                            LogUtils.e(TAG, "mSelectArticles.remove size " + mSelectArticles.size());
                        }
                    }
                });
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
