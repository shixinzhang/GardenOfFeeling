package sxkeji.net.dailydiary.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.Todo;
import sxkeji.net.dailydiary.TodoDao;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.common.views.adapters.AllTodoRecyclerAdapter;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.storage.SharedPreferencesUtils;
import sxkeji.net.dailydiary.utils.UIUtils;
import sxkeji.net.dailydiary.utils.ViewUtils;

/**
 * Todo列表
 * Created by zhangshixin on 4/27/2016.
 */
public class TodoListFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.rl_empty_view)
    RelativeLayout rlEmptyView;

    private AllTodoRecyclerAdapter adapter;
    private TodoDao todoDao;
    private ArrayList<Todo> todoList;
    private boolean orderByDec = true;     //按照剩余时间升序还是降序排列，默认降序
    private boolean isUpdating = false;    //是否正在更新数据库
    private int REQUEST_UPDATE_TODO = 1;

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
        initData();
        initViews();
    }

    private void initData() {
        todoDao = BaseApplication.getDaoSession().getTodoDao();
        orderByDec = (boolean) SharedPreferencesUtils.get(getContext(), Constant.SETTING_TODO_ORDER, true);

        initTodoListData();
    }

    /**
     * 获取待做事项数据
     */
    private void initTodoListData() {
        todoList = new ArrayList<>();
        Query<Todo> query;
        if (orderByDec) {
            query = todoDao.queryBuilder().orderDesc(TodoDao.Properties.Date).build();
        } else {
            query = todoDao.queryBuilder().orderAsc(TodoDao.Properties.Date).build();
        }
        todoList = (ArrayList<Todo>) query.list();
    }

    private void initViews() {
        if (todoList == null || todoList.size() == 0) {
            rlEmptyView.setVisibility(View.VISIBLE);
            return;
        } else {
            rlEmptyView.setVisibility(View.INVISIBLE);
            adapter = new AllTodoRecyclerAdapter(todoList);
            adapter.setOnItemLongClickListener(new AllTodoRecyclerAdapter.OnItemLongClickListener() {
                @Override
                public void OnItemLongClick(Todo todo, View view, CheckBox checkBox) {
                    //长按显示
                    showDeleteOrFinishedPopup(todo, view, checkBox);
                }
            }).setOnItemLinearLayoutClickListener(new AllTodoRecyclerAdapter.OnItemLinearLayoutClickListener() {
                @Override
                public void OnItemLinearLayoutClick(Todo todo) {
                    //跳转到详情
                    Intent intent = new Intent(getActivity(), TodoWriteActivity.class);
                    intent.putExtra(Constant.EXTRA_TODO, todo);
                    startActivityForResult(intent, REQUEST_UPDATE_TODO);
                }
            }).setOnItemCheckBoxClickListener(new AllTodoRecyclerAdapter.OnItemCheckBoxClickListener() {
                @Override
                public void OnItemCheckBoxClick(Todo todo, boolean cb) {
                    //更新todo的状态
                    todo.setIsFinished(cb);
                    BaseApplication.getDaoSession().getTodoDao().update(todo);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    private void showToast(String str) {
        if (!TextUtils.isEmpty(str)) {
            UIUtils.showToastSafe(getContext(), str);
        }
    }

    /**
     * 弹出删除或者完成的popup
     *
     * @param todo
     * @param view
     */
    private void showDeleteOrFinishedPopup(final Todo todo, View view, final CheckBox checkBox) {
        View popupView = ViewUtils.showPopupWindow(getContext(), R.layout.pop_todo_delete_or_finished, view, 2);
        TextView tvDelete = (TextView) popupView.findViewById(R.id.tv_delete);
        final TextView tvFinished = (TextView) popupView.findViewById(R.id.tv_finished);

        if (checkBox.isChecked()) {
            tvFinished.setText("恢复");
        } else {
            tvFinished.setText("完成");
        }
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.getDaoSession().getTodoDao().delete(todo);
                ViewUtils.dismissPopup();
            }
        });
        tvFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
                if (checkBox.isChecked()) {
                    tvFinished.setText("恢复");
                } else {
                    tvFinished.setText("完成");
                }
                ViewUtils.dismissPopup();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
