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
        //����View�󴫵ݸ�ViewHolder
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                mLayoutId, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(mData != null && mData.size() > 0) {
            //������䵽ViewHolder��Ԫ����
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
     * ����ʵ��RecyclerView����ĵ���¼��ص��ӿ�
     */
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    /**
     * �Զ���ViewHolder������ʵ��һ�����췽���������Զ���
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_item);
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {       //ͨ���ӿڻص���ʵ��RecyclerView�ĵ���¼�
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v,getPosition());
            }
        }
    }
}
