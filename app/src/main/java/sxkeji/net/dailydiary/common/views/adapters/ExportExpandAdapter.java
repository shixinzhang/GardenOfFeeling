package sxkeji.net.dailydiary.common.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.sxkeji.dailydiary.R;

import java.util.ArrayList;

import sxkeji.net.dailydiary.Article;
import sxkeji.net.dailydiary.Todo;

/**
 * 本地导出的expandableList的adapter
 * Created by zhangshixin on 5/8/2016.
 */
public class ExportExpandAdapter extends BaseExpandableListAdapter {
    private final int TYPE_ARTICLE = 0;
    private final int TYPE_TODO = 1;
    private Context mContext;
    private ArrayList<String> mParentList;
    private ArrayList<Article> mArticleList;
    private ArrayList<Todo> mTodoList;

    public ExportExpandAdapter(Context context) {
        mContext = context;
    }

    public ExportExpandAdapter setParentData(ArrayList<String> parentList) {
        this.mParentList = parentList;
        return this;
    }

    public ExportExpandAdapter setArticlesData(ArrayList<Article> mArticleList) {
        this.mArticleList = mArticleList;
        return this;
    }

    public ExportExpandAdapter setTodosData(ArrayList<Todo> mTodoList) {
        this.mTodoList = mTodoList;
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

    @Override
    public int getGroupCount() {
        return mParentList == null ? 0 : mParentList.size();
    }

    /**
     * 获取父布局对应的子View的个数
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
        return mParentList.get(groupPosition);
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
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_export_parent, parent, false);
        }
        TextView tvParent = (TextView) convertView.findViewById(R.id.tv_parent);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
        tvParent.setText(mParentList.get(groupPosition));
        if (groupPosition == 0){
            ivIcon.setImageResource(R.mipmap.icon_draft);
        }else if (groupPosition == 1){
            ivIcon.setImageResource(R.mipmap.icon_reminder);
        }
        return convertView;
    }

    /**
     * 子布局
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_export_parent, parent, false);
        }
//
//        TextView tvParent = (TextView) convertView.findViewById(R.id.tv_parent);
//        tvParent.setText(mParentList.get(groupPosition));
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
}
