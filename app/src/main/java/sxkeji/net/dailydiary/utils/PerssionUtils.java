package sxkeji.net.dailydiary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 6.0需要的权限类，需要主动提示用户是否允许
 * Created by zhangshixin on 2015/11/26.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class PerssionUtils {
    public static boolean checkPerssion(Context context, String perssion) {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                perssion);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    perssion)) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{perssion},
                        123);
                return false;
            }
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{perssion},
                    123);
            return false;
        }
        return true;
    }
}
