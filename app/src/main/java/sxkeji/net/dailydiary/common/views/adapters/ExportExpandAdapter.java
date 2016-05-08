package sxkeji.net.dailydiary.common.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.sxkeji.dailydiary.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.Todo;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.widgets.TextDrawable;

/**
 * 本地导出的expandableList的adapter
 * Created by zhangshixin on 5/8/2016.
 */
public class ExportExpandAdapter extends BaseExpandableListAdapter {
    private final String TAG = "ExportExpandAdapter";
    private final int TYPE_ARTICLE = 0;
    private final int TYPE_TODO = 1;
    private Context mContext;
    private ArrayList<String> mParentList;
    private ArrayList<Article> mArticleList;
    private ArrayList<Todo> mTodoList;
    private OnArticleSelectAllListener mArticleListener;

    public ExportExpandAdapter(Context context) {
        mContext = context;
    }

    public ExportExpandAdapter setParentData(ArrayList<String> parentList) {
        this.mParentList = parentList;
        return this;
    }

    public ExportExpandAdapter setArticlesData(ArrayList<Article> mArticleList) {
        this.mArticleList = mArticleList;
        LogUtils.e(TAG, "setArticlesData size " + mArticleList.size());
        return this;
    }

    public ExportExpandAdapter setTodosData(ArrayList<Todo> mTodoList) {
        this.mTodoList = mTodoList;
        LogUtils.e(TAG, "setTodosData size " + mTodoList.size());
        return this;
    }

    public ExportExpandAdapter setOnArticleSelectAllListener(OnArticleSelectAllListener mArticleListener){
        this.mArticleListener = mArticleListener;
        return this;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (groupPosition == 0) {        //第一个父布局下的是article
            return TYPE_ARTICLE;
        } else {         //第二个父布局下的是todo
            return TYPE_TODO;
        }
    }

    /**
     * 【当子View有多种布局时一定要记得重写这个方法，默认值只有一种】
     *
     * @return
     */
    @Override
    public int getChildTypeCount() {
        return getGroupCount();
    }

    @Override
    public int getGroupCount() {
        return mParentList == null ? 0 : mParentList.size();
    }

    /**
     * 获取父布局对应的子View的个数
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        int childType = getChildType(groupPosition, 0);
        if (childType == TYPE_ARTICLE) {
            return mArticleList == null ? 0 : mArticleList.size();
        } else {
            return mTodoList == null ? 0 : mTodoList.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition < mParentList.size()) {
            return mParentList.get(groupPosition);
        } else {
            return null;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        int childType = getChildType(groupPosition, childPosition);
        if (childType == TYPE_ARTICLE) {
            return mArticleList.get(childPosition);
        } else {
            return mTodoList.get(childPosition);
        }
    }

    /**
     * 父布局
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_export_parent, parent, false);
            viewHolder = new ParentViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_parent);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.cbSelectAll = (CheckBox) convertView.findViewById(R.id.cb_select_all);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ParentViewHolder) convertView.getTag();
        viewHolder.tvName.setText(mParentList.get(groupPosition));
        if (groupPosition == 0) {
            viewHolder.ivIcon.setImageResource(R.mipmap.icon_draft);
        } else if (groupPosition == 1) {
            viewHolder.ivIcon.setImageResource(R.mipmap.icon_reminder);
        }
        viewHolder.cbSelectAll.setChecked(false);
        return convertView;
    }

    /**
     * 子布局
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int childType = getChildType(groupPosition, childPosition);
        if (childType == TYPE_ARTICLE) {        //↓↓↓↓↓↓  子View为文章  ↓↓↓↓↓↓↓↓↓↓↓↓
            convertView = getArticleChildView(convertView, childPosition, parent);

        } else if (childType == TYPE_TODO) {      //↓↓↓↓↓↓  子View为Todo  ↓↓↓↓↓↓↓↓↓↓↓↓
            convertView = getTodoChildView(convertView, childPosition, parent);

        }
        return convertView;
    }

    /**
     * 解析文章子View
     *
     * @param convertView
     * @param childPosition
     * @param parent
     * @return
     */
    private View getArticleChildView(View convertView, int childPosition, ViewGroup parent) {
        ArticleViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ArticleViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_export_article, parent, false);
            viewHolder.rlRoot = (RelativeLayout) convertView.findViewById(R.id.rl_root);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.ivImg = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ArticleViewHolder) convertView.getTag();
        if (mArticleList != null && mArticleList.size() > 0) {
            final Article article = mArticleList.get(childPosition);
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
            } else {
                int color = Color.parseColor("#03A9F4");
                viewHolder.ivImg.setImageDrawable(new ColorDrawable(color));
            }
            viewHolder.tvDate.setText(article.getDate());
            viewHolder.tvContent.setText(article.getContent());

            if (mArticleListener != null) {
                final ArticleViewHolder finalViewHolder = viewHolder;
                viewHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mArticleListener.OnArticleSelect(article, finalViewHolder.cbSelect);
                    }
                });
            }
        }
        return convertView;
    }

    /**
     * 解析todo子View
     */
    private View getTodoChildView(View convertView, int childPosition, ViewGroup parent) {
        TodoViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new TodoViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_todo_list, parent, false);
            viewHolder.llContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.ivDrawableText = (ImageView) convertView.findViewById(R.id.iv_drawable_text);
            viewHolder.cbFinished = (CheckBox) convertView.findViewById(R.id.cb_finished);
            convertView.setTag(viewHolder);
        }
        viewHolder = (TodoViewHolder) convertView.getTag();
        final Todo todo = mTodoList.get(childPosition);
        String todoContent = todo.getContent();
        int todoColor = todo.getColor();
        boolean isFinished = todo.getIsFinished();

//            viewHolder.cbFinished.setChecked(isFinished);
        viewHolder.tvContent.setText(todoContent);
        viewHolder.tvContent.setMaxLines(2);
        viewHolder.tvDate.setVisibility(View.GONE);
        TextDrawable textDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(todoContent.trim().substring(0, 1), todoColor);
        viewHolder.ivDrawableText.setImageDrawable(textDrawable);
        return convertView;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnParentSelectAllListener {
        void OnParentSelectAll();
    }

    public interface OnArticleSelectAllListener {
        void OnArticleSelect(Article article, CheckBox checkBox);
    }

    public interface OnTodoSelectAllListener {
        void OnTodoSelectAll();
    }

    /**
     * 父布局ViewHolder
     */
    class ParentViewHolder {
        TextView tvName;
        ImageView ivIcon;
        CheckBox cbSelectAll;
    }

    /**
     * 文章布局的ViewHolder
     */
    public class ArticleViewHolder {
        private RelativeLayout rlRoot;
        private TextView tvDate, tvContent;
        private ImageView ivImg;
        private CheckBox cbSelect;
    }

    /**
     * Todo布局的ViewHolder
     */
    public class TodoViewHolder {
        private LinearLayout llContainer;
        private ImageView ivDrawableText;
        private TextView tvContent;
        private TextView tvDate;
        private CheckBox cbFinished;
    }
}
