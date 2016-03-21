package sxkeji.net.dailydiary.common.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.R;

/**
 * Created by zhangshixin on 2015/12/2.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class AllArticlesRecyclerAdapter extends RecyclerView.Adapter<AllArticlesRecyclerAdapter.ViewHolder>{
    private List<Article> mData;
    private OnItemClickListener mOnItemClickListener;

    public AllArticlesRecyclerAdapter(List<Article> mData){
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_home,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(mData != null && mData.size() > 0) {
            viewHolder.tvTitle.setText(mData.get(i).getTitle());
            viewHolder.tvContent.setText(mData.get(i).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvTitle,tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
//            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v,getPosition());
            }
        }
    }
}
