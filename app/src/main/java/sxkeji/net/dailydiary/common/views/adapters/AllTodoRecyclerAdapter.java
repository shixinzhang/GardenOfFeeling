package sxkeji.net.dailydiary.common.views.adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.Todo;

/**
 * TodoList 的 adapter
 * Created by zhangshixin on 4/27/2016.
 */
public class AllTodoRecyclerAdapter extends RecyclerView.Adapter<AllTodoRecyclerAdapter.ViewHolder> {
    private ArrayList<Todo> todoList;

    public AllTodoRecyclerAdapter(ArrayList<Todo> todoList) {
        this.todoList = todoList;
    }

    public void addData(Todo todo) {
        todoList.add(todo);
        notifyDataSetChanged();
    }

    @Override
    public AllTodoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.tvContent.setText(todo.getContent());
        Date date = todo.getDate();
        if (date!=null){
            holder.tvContent.setMaxLines(1);
            holder.tvDate.setVisibility(View.VISIBLE);
        }else {
            holder.tvContent.setMaxLines(2);
            holder.tvDate.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    @SuppressWarnings("deprecation")
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDrawableText;
        private TextView tvContent;
        private TextView tvDate;

        public ViewHolder(View v) {
            super(v);

            tvContent = (TextView) v.findViewById(R.id.tv_content);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            ivDrawableText = (ImageView) v.findViewById(R.id.iv_drawable_text);
        }


    }
}
