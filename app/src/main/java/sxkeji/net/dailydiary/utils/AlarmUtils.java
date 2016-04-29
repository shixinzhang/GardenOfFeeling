package sxkeji.net.dailydiary.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 提示器
 * Created by zhangshixin on 4/29/2016.
 */
public class AlarmUtils {
    private AlarmManager mAlarmManager;
    private Context mContext;

    public AlarmUtils(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 创建一个提醒
     *
     * @param intent
     * @param requestCode
     * @param timeInMillis
     */
    public void createAlarm(Intent intent, int requestCode, long timeInMillis) {
        PendingIntent pendingIntent = PendingIntent.getService(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        LogUtils.e("AlarmUtils", "createAlarm");
    }

    /**
     * 删除一个提醒
     *
     * @param intent
     * @param requestCode
     */
    public void deleteAlarm(Intent intent, int requestCode) {
        PendingIntent pendingIntent = PendingIntent.getService(mContext, requestCode, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            pendingIntent.cancel();
            mAlarmManager.cancel(pendingIntent);
            LogUtils.e("AlarmUtils", "deleteAlarm");
        }
    }
}
