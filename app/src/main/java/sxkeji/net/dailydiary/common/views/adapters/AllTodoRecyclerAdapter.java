package sxkeji.net.dailydiary.common.views.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.Todo;
import sxkeji.net.dailydiary.utils.StringUtils;
import sxkeji.net.dailydiary.widgets.TextDrawable;

/**
 * TodoList 的 adapter
 * Created by zhangshixin on 4/27/2016.
 */
public class AllTodoRecyclerAdapter extends RecyclerView.Adapter<AllTodoRecyclerAdapter.ViewHolder> {
    private ArrayList<Todo> todoList;
    private OnItemLongClickListener longClickListener;
    private OnItemLinearLayoutClickListener layoutClickListener;
    private OnItemCheckBoxClickListener cbClickListener;
    private long oneYear, oneMonth, oneDay, oneHour, oneMinute, oneSecond;

    public AllTodoRecyclerAdapter(ArrayList<Todo> todoList) {
        this.todoList = todoList;
        oneSecond = 1000;
        oneMinute = oneSecond * 60;
        oneHour = oneMinute * 60;
        oneDay = oneHour * 24;
        oneMonth = oneDay * 30;
        oneYear = oneMonth * 12;
    }

    public void addData(Todo todo) {
        todoList.add(todo);
        notifyDataSetChanged();
    }


    public AllTodoRecyclerAdapter setOnItemLongClickListener(OnItemLongClickListener listener) {
        longClickListener = listener;
        return this;
    }

    public AllTodoRecyclerAdapter setOnItemLinearLayoutClickListener(OnItemLinearLayoutClickListener listener) {
        layoutClickListener = listener;
        return this;
    }

    public AllTodoRecyclerAdapter setOnItemCheckBoxClickListener(OnItemCheckBoxClickListener listener) {
        cbClickListener = listener;
        return this;
    }

    @Override
    public AllTodoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Date nowDate = new Date();
        final Todo todo = todoList.get(position);
        String todoContent = todo.getContent();
        int todoColor = todo.getColor();
        boolean hasReminder = todo.getHasReminder();
        Date todoDate = todo.getDate();
        boolean isFinished = todo.getIsFinished();

        holder.cbFinished.setChecked(isFinished);
        holder.tvContent.setText(todoContent);
        if (hasReminder && todoDate != null) {
            holder.tvContent.setMaxLines(1);
            holder.tvDate.setVisibility(View.VISIBLE);
            //剩余时间,单位毫秒
            long leftTime = (todoDate.getTime() - nowDate.getTime());
            showLeftTime(holder.tvDate, leftTime);
            holder.tvDate.setTextColor(todoColor);
        } else {
            holder.tvContent.setMaxLines(2);
            holder.tvDate.setVisibility(View.GONE);
        }
        TextDrawable textDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(todoContent.substring(0, 1), todoColor);
        holder.ivDrawableText.setImageDrawable(textDrawable);

        if (longClickListener != null) {
            holder.llContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.OnItemLongClick(todo, holder.llContainer, holder.cbFinished);
                    return false;
                }
            });
        }
        if (layoutClickListener != null){
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutClickListener.OnItemLinearLayoutClick(todo);
                }
            });
        }
        if (cbClickListener != null){
            holder.cbFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cbClickListener.OnItemCheckBoxClick(todo, isChecked);
                }
            });
        }
    }

    /**
     * 计算剩余多少年、月、日、小时、分钟
     */
    private void showLeftTime(TextView textView, long leftTime) {
        if (leftTime > oneYear) {
            textView.setText("还剩 " + leftTime / oneYear + "年 ");
            return;
        }
        if (leftTime > oneMonth) {
            textView.setText("还剩 " + leftTime / oneMonth + "个月 ");
            return;
        }
        if (leftTime > oneDay) {
            textView.setText("还剩 " + leftTime / oneDay + "天 ");
            return;
        }
        if (leftTime > oneHour) {
            textView.setText("还剩 " + leftTime / oneHour + "小时 ");
            return;
        }
        if (leftTime > oneMinute) {
            textView.setText("还剩 " + leftTime / oneMinute + "分钟 ");
            return;
        }
        if (leftTime > oneSecond) {
            textView.setText("还剩 " + leftTime / oneSecond + "秒 ");
            return;
        } else {
            textView.setText("已过期");
            return;
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @SuppressWarnings("deprecation")
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llContainer;
        private ImageView ivDrawableText;
        private TextView tvContent;
        private TextView tvDate;
        private CheckBox cbFinished;

        public ViewHolder(View v) {
            super(v);

            llContainer = (LinearLayout) v.findViewById(R.id.ll_container);
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            ivDrawableText = (ImageView) v.findViewById(R.id.iv_drawable_text);
            cbFinished = (CheckBox) v.findViewById(R.id.cb_finished);
        }
    }

    /**
     * checkBox点击监听，修改状态
     */
    public interface OnItemCheckBoxClickListener {
        void OnItemCheckBoxClick(Todo todo, boolean cb);
    }

    /**
     * 整体点击监听，进入详情
     */
    public interface OnItemLinearLayoutClickListener {
        void OnItemLinearLayoutClick(Todo todo);
    }

    /**
     * 长按监听
     */
    public interface OnItemLongClickListener {
        void OnItemLongClick(Todo todo, View view, CheckBox cb);
    }
}
