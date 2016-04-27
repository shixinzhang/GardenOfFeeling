package sxkeji.net.dailydiary.common.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.Todo;
import sxkeji.net.dailydiary.TodoDao;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.common.views.adapters.AllArticlesRecyclerAdapter;

/**
 * Todo列表
 * Created by zhangshixin on 4/27/2016.
 */
public class TodoListFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.rl_empty_view)
    RelativeLayout rlEmptyView;

    private TodoDao todoDao;
    private List<Todo> todoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_todo_list, null);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        initViews();
        initTodoListData();
    }

    private void initViews() {
        todoDao = BaseApplication.getDaoSession().getTodoDao();
    }

    private void initTodoListData() {
        todoList = new ArrayList<>();
        Query<Todo> query = todoDao.queryBuilder().orderDesc(TodoDao.Properties.Date).build();
        todoList = query.list();
        if (todoList == null || todoList.size() == 0){
            rlEmptyView.setVisibility(View.VISIBLE);
            return;
        }else {
            rlEmptyView.setVisibility(View.INVISIBLE);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
