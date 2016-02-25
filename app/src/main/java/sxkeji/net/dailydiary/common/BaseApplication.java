package sxkeji.net.dailydiary.common;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import sxkeji.net.dailydiary.BuildConfig;
import sxkeji.net.dailydiary.utils.AppUtils;
import sxkeji.net.dailydiary.utils.LogUtils;

/**
 * Created by qiujie on 2014/7/11.
 */
public class BaseApplication extends Application {
    public static final String TAG = "VolleyPatterns";
    /**
     * 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了
     */
    private static BaseApplication mInstance;
    /**
     * 主线程ID
     */
    private static int mMainThreadId = -1;
    /**
     * 主线程ID
     */
    private static Thread mMainThread;
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;
    /**
     * 主线程Looper
     */
    private static Looper mMainLooper;

    public static Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        mInstance = this;

        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();

        super.onCreate();
        if (!BuildConfig.DEBUG) {
            LogUtils.mDebuggable = LogUtils.LEVEL_NONE;
        }

        if (checkIfIsAppRunning(getPackageName())) {
            initialize();
        }


    }


    private void initialize() {
        //TODO 设置Buggly 渠道 和 App的版本
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
//        strategy.setAppChannel(AppUtils.getAppChannel(this));
        strategy.setAppVersion(AppUtils.getVersionName(this));
        CrashReport.initCrashReport(this, "900010286", BuildConfig.DEBUG, strategy);

        setUncaughtExcept();//全局捕获异常

    }


    private void setUncaughtExcept() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);

                    Field[] fileds = Build.class.getDeclaredFields();
                    for (Field filed : fileds) {

                        sw.write(filed.getName() + "--" + filed.get(null) + "\n");
                    }
                    ex.printStackTrace(pw);
                    File file = new File(Environment.getExternalStorageDirectory(), "log.txt");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(sw.toString().getBytes());
                    fos.close();
                    pw.close();
                    sw.close();

                    android.os.Process.killProcess(android.os.Process.myPid());

                } catch (Exception e) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    e.printStackTrace();
                }
            }
        });

    }


    public static BaseApplication getApplication() {
        return mInstance;
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    /**
     * volley 请求对列
     *
     * @return
     */


    /**
     * 判断程序是否运行
     *
     * @param processName
     * @return
     */
    private boolean checkIfIsAppRunning(String processName) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> mRunningList = am.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> iterator = mRunningList.iterator();
        while (iterator.hasNext()) {
            RunningAppProcessInfo info = (RunningAppProcessInfo) iterator.next();
            if (info.pid == pid) {
                if (processName.equals(info.processName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
