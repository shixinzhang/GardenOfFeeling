package sxkeji.net.dailydiary.common.models;

import android.content.Context;

import sxkeji.net.dailydiary.utils.NetWorkUtils;

/**
 * Created by zhangshixin on 2015/11/26.
 *
 * @description Codes there always can be better.
 */
public class NetWorkBiz implements INetWorkBiz {
    @Override
    public boolean isNetWorkConnected(Context context) {
        return NetWorkUtils.isNetworkAvailable(context);
    }
}
