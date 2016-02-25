package sxkeji.net.dailydiary.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

import sxkeji.net.dailydiary.BuildConfig;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/10/25
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class AppUtils {

    /**
     * AppUtils
     */
    public AppUtils() {
        throw new AssertionError();
    }

    /**
     * whether this process is named with processName
     *
     * @param context     context
     * @param processName processName
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     * true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (processInfoList == null || processInfoList.isEmpty()) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null && processInfo.pid == pid
                    && ObjectUtils.isEquals(processName, processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether context is system application
     *
     * @param context
     * @return
     */
    public static boolean isSystemApplication(Context context) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context, context.getPackageName());
    }

    /**
     * whether packageName is system application
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context.getPackageManager(), packageName);
    }

    /**
     * whether packageName is system application
     *
     * @param packageManager
     * @param packageName
     * @return <ul>
     * <li>if packageManager is null, return false</li>
     * <li>if package name is null or is empty, return false</li>
     * <li>if package name not exit, return false</li>
     * <li>if package name exit, but not system app, return false</li>
     * <li>else return true</li>
     * </ul>
     */
    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * whether the app whost package's name is packageName is on the top of the stack
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should add <strong>android.permission.GET_TASKS</strong> in manifest</li>
     * </ul>
     *
     * @param context
     * @param packageName
     * @return if params error or task stack is null, return null, otherwise retun whether the app is on the top of
     * stack
     */
    public static Boolean isTopActivity(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return null;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (ObjectUtils.isEmpty(tasksInfo)) {
            return null;
        }
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get app version code
     *
     * @param context context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * 获得App渠道，如果用户是从老渠道升级的，那么会返回老渠道
     */
//    public static String getAppChannel(Context context) {
//        if (BuildConfig.DEBUG) {
//            return "debug";
//        }
//        try {
//            SharedPreferences pref = context.getSharedPreferences(Config.BUGLYFA, Context.MODE_PRIVATE);
//            String channel = pref.getString(Config.APP_CHANNEL, null);
//            if (TextUtils.isEmpty(channel)) {
//                channel = getApkChannel();
//            }
//            if (!TextUtils.isEmpty(channel)) {
//                pref.edit().putString(Config.APP_CHANNEL, channel).apply();
//                return channel;
//            }
//        } catch (Exception ignore) {
//        }
//        return "noChannel";
//    }

    /**
     * 获得内置进APK的渠道
     */
    public static String getApkChannel() {
        String channel = BuildConfig.FLAVOR;
        if (channel.startsWith("_")) {
            channel = channel.substring(1);
        }
        return channel;
    }

    /**
     * 获得当前版本的versionName
     *
     * @return String
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }


    /**
     * 获取手机号码
     *
     * @return 手机号码
     */
    public static String getPhoneNumber(Context context) {
        try {
            String phone = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
            if (!TextUtils.isEmpty(phone) && phone.startsWith("+86")) {
                phone = phone.substring(3);
            }
            return phone;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得cpu构架信息，将会返回ARM、MIPS、X86之一
     *
     * @return cpuInfo
     */
    public static String getCpuInfo() {
        try {
            String arc = System.getProperty("os.arch").toUpperCase();
            if (arc.startsWith("ARM")) {
                arc = "ARM";
            } else if (arc.startsWith("MIPS")) {
                arc = "MIPS";
            } else if (arc.startsWith("X86") || arc.endsWith("86")) {
                arc = "X86";
            } else {
                arc = "ARM";
            }
            return arc;
        } catch (Exception e) {
            return "ARM";
        }
    }

}
