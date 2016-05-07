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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import sxkeji.net.dailydiary.Article;
import net.sxkeji.dailydiary.R;
import sxkeji.net.dailydiary.beans.OpenEyeDailyBean;
import sxkeji.net.dailydiary.common.BaseApplication;

/**
 * 推荐list的adapter
 * Created by zhangshixin on 4/13/2016.
 */
public class AllRecommandAdapter extends RecyclerView.Adapter<AllRecommandAdapter.ViewHolder> {
    private Picasso mPicasso;
    private List<OpenEyeDailyBean.IssueListEntity.ItemListEntity> mData;
    private OnItemClickListener mOnItemClickListener;
    private FoldingCell mLastFoldCell = null;

    public AllRecommandAdapter(List<OpenEyeDailyBean.IssueListEntity.ItemListEntity> mData) {
        this.mData = mData;
        mPicasso = BaseApplication.getPicassoSingleton();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_recommand2,
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
                viewHolder.ivImg.setImageResource(R.mipmap.background_menu_account_info);
                viewHolder.tvTitle.setText("[ 由于特殊原因此视频无法显示 ]");
                viewHolder.tvCategory.setText("");
                viewHolder.tvTime.setText("");
                //默认折叠
                viewHolder.foldingCell.fold(true);
                //覆盖之前的点击事件
                if (mOnItemClickListener != null) {
                    viewHolder.foldingCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // do nothing
                        }
                    });
                }
            } else if (type.equals("video")) {
                String category = data.getCategory();
                String title = data.getTitle();
                String description = data.getDescription();
                int duration = data.getDuration();

                OpenEyeDailyBean.IssueListEntity.ItemListEntity.DataEntity.CoverEntity cover = data.getCover();
                if (cover != null) {
                    String imgPath = cover.getFeed();
                    if (!TextUtils.isEmpty(imgPath)) {
                        mPicasso.load(imgPath)
                                .error(R.mipmap.background_menu_account_info_colorful)
                                .skipMemoryCache()
                                .placeholder(R.mipmap.background_menu_account_info_colorful)
                                .priority(Picasso.Priority.LOW)
                                .config(Bitmap.Config.RGB_565).into(viewHolder.ivImg);

                        mPicasso.load(imgPath)
                                .error(R.mipmap.background_menu_account_info_colorful)
                                .skipMemoryCache()
                                .placeholder(R.mipmap.background_menu_account_info_colorful)
                                .priority(Picasso.Priority.LOW)
                                .config(Bitmap.Config.RGB_565).into(viewHolder.ivContentBg);
                    } else {
                        int color = Color.parseColor("#03A9F4");
                        viewHolder.ivImg.setImageDrawable(new ColorDrawable(color));
                    }
                }
                viewHolder.tvCategory.setText("#" + category);
                viewHolder.tvContentCategory.setText("#" + category);
                viewHolder.tvTitle.setText(title);
                viewHolder.tvContentTitle.setText(title);
                String time = duration / 60 + "' " + duration % 60 + "''";
                viewHolder.tvTime.setText(time);
                viewHolder.tvContentTime.setText(time);
                viewHolder.tvContentDesc.setText(description);

                if (mOnItemClickListener != null) {
                    viewHolder.ivContentPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(data, i);
                        }
                    });

                    viewHolder.foldingCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //折叠之前打开的
                            if (mLastFoldCell != null && mLastFoldCell != viewHolder.foldingCell) {
                                mLastFoldCell.fold(true);
                            }
                            viewHolder.foldingCell.toggle(false);
                            mLastFoldCell = viewHolder.foldingCell;
                        }
                    });


                }
            }

//                viewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",  1.3f);
//                        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",  1.3f);
//                        ObjectAnimator.ofPropertyValuesHolder(viewHolder.ivImg,scaleX,scaleY).setDuration(1000).start();
//                        mOnItemClickListener.onItemClick(data, i);
////                        ObjectAnimator.ofFloat(viewHolder.ivImg,"scale",0f,1.5f).setDuration(1000).start();
////                        viewHolder.ivImg.animate().scaleX(1.5f).setDuration(1000).start();
//                    }
//                });

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
        private FoldingCell foldingCell;
        private FrameLayout flCellContent;
        private View rootView;
        private TextView tvCategory, tvTime, tvTitle, tvContentCategory, tvContentTime, tvContentTitle, tvContentDesc;
        private ImageView ivImg, ivContentBg, ivContentPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            foldingCell = (FoldingCell) itemView.findViewById(R.id.fl_folding_cell);
            flCellContent = (FrameLayout) itemView.findViewById(R.id.fl_cell_content);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_title_bg);

            ivContentPlay = (ImageView) itemView.findViewById(R.id.iv_content_play);
            tvContentCategory = (TextView) itemView.findViewById(R.id.tv_content_category);
            tvContentTime = (TextView) itemView.findViewById(R.id.tv_content_time);
            tvContentTitle = (TextView) itemView.findViewById(R.id.tv_content_title);
            tvContentDesc = (TextView) itemView.findViewById(R.id.tv_content_desc);
            ivContentBg = (ImageView) itemView.findViewById(R.id.iv_content_bg);
        }

    }
}
