package sxkeji.net.dailydiary.common.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sxkeji.net.dailydiary.R;

/**
 * Created by zhangshixin on 2015/12/2.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
    private List<String> mData;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener;

    public MyRecyclerAdapter(List<String> mData,int mLayoutId){
        this.mData = mData;
        this.mLayoutId = mLayoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //解析View后传递给ViewHolder
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                mLayoutId, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(mData != null && mData.size() > 0) {
            //数据填充到ViewHolder的元素中
            viewHolder.mTextView.setText(mData.get(i) + i);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 用于实现RecyclerView子项的点击事件回调接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    /**
     * 自定义ViewHolder，必须实现一个构造方法，参数自定义
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_item);
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {       //通过接口回调来实现RecyclerView的点击事件
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v,getPosition());
            }
        }
    }
}
