package sxkeji.net.dailydiary.common.views.adapters;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.beans.OpenEyeDailyBean;
import sxkeji.net.dailydiary.common.BaseApplication;

/**
 * 推荐list的adapter
 * Created by zhangshixin on 4/13/2016.
 */
public class AllRecommandAdapter extends RecyclerView.Adapter<AllRecommandAdapter.ViewHolder> {
    private List<OpenEyeDailyBean.IssueListEntity.ItemListEntity> mData;
    private OnItemClickListener mOnItemClickListener;

    public AllRecommandAdapter(List<OpenEyeDailyBean.IssueListEntity.ItemListEntity> mData) {
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_recommand,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (mData != null && mData.size() > 0) {
            final OpenEyeDailyBean.IssueListEntity.ItemListEntity listEntity = mData.get(i);
            final OpenEyeDailyBean.IssueListEntity.ItemListEntity.DataEntity data = listEntity.getData();
            String type = listEntity.getType();
            if (type.equals("banner1")) {

            } else if (type.equals("video")) {
                String category = data.getCategory();
                String title = data.getTitle();
                int duration = data.getDuration();
                OpenEyeDailyBean.IssueListEntity.ItemListEntity.DataEntity.CoverEntity cover = data.getCover();
                if (cover != null) {
                    String imgPath = cover.getFeed();
                    if (!TextUtils.isEmpty(imgPath)) {
                        BaseApplication.getPicassoSingleton().load(imgPath)
                                .error(R.mipmap.background_menu_account_info_colorful)
                                .skipMemoryCache()
                                .placeholder(R.mipmap.background_menu_account_info_colorful)
                                .priority(Picasso.Priority.LOW)
                                .config(Bitmap.Config.RGB_565).into(viewHolder.ivImg);
                    } else {
                        int color = Color.parseColor("#03A9F4");
                        viewHolder.ivImg.setImageDrawable(new ColorDrawable(color));
                    }
                }
                viewHolder.tvCategory.setText("#" + category);
                viewHolder.tvTitle.setText(title);
                viewHolder.tvTime.setText(duration / 60 + "' " + duration % 60 + "''");
            }
            if (mOnItemClickListener != null) {
                viewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(data, i);
                        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",  1.5f);
                        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",  1.5f);
                        ObjectAnimator.ofPropertyValuesHolder(viewHolder.ivImg,scaleX,scaleY).setDuration(1000).start();
//                        ObjectAnimator.ofFloat(viewHolder.ivImg,"scale",0f,1.5f).setDuration(1000).start();
//                        viewHolder.ivImg.animate().scaleX(1.5f).setDuration(1000).start();
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
        void onItemClick(OpenEyeDailyBean.IssueListEntity.ItemListEntity.DataEntity eyeDailyBean, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        private TextView tvCategory, tvTime, tvTitle;
        private ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_bg);
        }

    }
}
