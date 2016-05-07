package sxkeji.net.dailydiary.common.views.adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import sxkeji.net.dailydiary.Article;
import net.sxkeji.dailydiary.R;
import sxkeji.net.dailydiary.common.BaseApplication;

/**
 * Created by zhangshixin on 2015/12/2.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class AllArticlesRecyclerAdapter extends RecyclerView.Adapter<AllArticlesRecyclerAdapter.ViewHolder> {
    private List<Article> mData;
    private OnItemClickListener mOnItemClickListener;

    public AllArticlesRecyclerAdapter(List<Article> mData) {
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_home,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (mData != null && mData.size() > 0) {
            final Article article = mData.get(i);
            String imgPath = article.getImg_path();
            if (!TextUtils.isEmpty(imgPath)) {
                BaseApplication.getPicassoSingleton().load(new File(imgPath))
                        .error(R.mipmap.background_menu_account_info_colorful)
                        .skipMemoryCache()
                        .placeholder(R.mipmap.background_menu_account_info_colorful)
                        .resize(400, 400)
                        .centerInside()
                        .priority(Picasso.Priority.LOW)
                        .config(Bitmap.Config.RGB_565).into(viewHolder.ivImg);
            }else {
                int color = Color.parseColor("#03A9F4");
                viewHolder.ivImg.setImageDrawable(new ColorDrawable(color));
            }

            viewHolder.tvDate.setText(article.getDate());
            viewHolder.tvContent.setText(article.getContent());
            if (mOnItemClickListener != null) {
                viewHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(article, i);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(Article article, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlRoot;
        private TextView tvDate, tvContent;
        private ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            rlRoot = (RelativeLayout) itemView.findViewById(R.id.rl_root);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
        }

    }
}
