package sxkeji.net.dailydiary.common.activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import net.sxkeji.dailydiary.R;
import sxkeji.net.dailydiary.beans.OpenEyeDailyBean;
import sxkeji.net.dailydiary.common.models.NetWorkBiz;
import sxkeji.net.dailydiary.common.views.adapters.AllRecommandAdapter;
import sxkeji.net.dailydiary.storage.ACache;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.utils.GsonUtils;
import sxkeji.net.dailydiary.utils.NetWorkUtils;
import sxkeji.net.dailydiary.utils.UIUtils;

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
        if (!TextUtils.isEmpty(openEyeJson)) {
            Log.e(TAG, "openEyeJson " + openEyeJson);
            OpenEyeDailyBean eyeDailyBean = GsonUtils.fromJson(openEyeJson, OpenEyeDailyBean.class);
            openEyeDataList = eyeDailyBean.getIssueList().get(0).getItemList();
        } else {

            Log.e(TAG, "openEyeJson is null");
        }
    }

    private void initView() {
        context = getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new AllRecommandAdapter(openEyeDataList);
        adapter.setmOnItemClickListener(new AllRecommandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OpenEyeDailyBean.IssueListEntity.ItemListEntity.DataEntity eyeDailyBean, int position) {
                playTheMedia(eyeDailyBean);

            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void playTheMedia(OpenEyeDailyBean.IssueListEntity.ItemListEntity.DataEntity eyeDailyBean) {
        boolean networkAvailable = NetWorkUtils.isNetworkAvailable(getContext());
        if (networkAvailable) {
            String networkTypeName = NetWorkUtils.getNetworkTypeName(getContext());
            showToast("当前网络环境为 " + networkTypeName);
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(Constant.PLAY_TITLE, eyeDailyBean.getTitle());
            intent.putExtra(Constant.PLAY_URL, eyeDailyBean.getPlayUrl());
            startActivity(intent);
        } else {
            showToast("无法播放，请检查网络连接");
        }
    }

    void showToast(String str) {
        if (!TextUtils.isEmpty(str)){
            UIUtils.showToastSafe(getContext(),str);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
