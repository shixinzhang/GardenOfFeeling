package sxkeji.net.dailydiary.common.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.beans.OpenEyeDailyBean;
import sxkeji.net.dailydiary.common.views.adapters.AllRecommandAdapter;
import sxkeji.net.dailydiary.storage.ACache;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.utils.GsonUtils;

/**
 * 每日推荐
 * Created by zhangshixin on 4/13/2016.
 */
public class RecommandFragment extends Fragment {
    private final String TAG = "RecommandFragment";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private OpenEyeDailyBean openEyeDailyBean;
    private AllRecommandAdapter adapter;
    private ACache aCache;
    private String openEyeJson;
    private List<OpenEyeDailyBean.IssueListEntity.ItemListEntity> openEyeDataList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recommand, null);
        ButterKnife.bind(this, view);
        loadData();
        initView();
        return view;
    }

    private void loadData() {
        aCache = ACache.get(getContext());
        openEyeJson = aCache.getAsString(Constant.OPEN_EYE_DATA);
        if (!TextUtils.isEmpty(openEyeJson)){
            Log.e(TAG,"openEyeJson " + openEyeJson);
            OpenEyeDailyBean eyeDailyBean = GsonUtils.fromJson(openEyeJson, OpenEyeDailyBean.class);
            openEyeDataList = eyeDailyBean.getIssueList().get(0).getItemList();
        }else {

            Log.e(TAG,"openEyeJson is null" );
        }
    }

    private void initView() {
        context = getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new AllRecommandAdapter(openEyeDataList);
        adapter.setmOnItemClickListener(new AllRecommandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OpenEyeDailyBean.IssueListEntity.ItemListEntity.DataEntity eyeDailyBean, int position) {
                Toast.makeText(getContext(),"播放",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
