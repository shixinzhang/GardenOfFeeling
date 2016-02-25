package sxkeji.net.dailydiary.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zhangshixin on 2015/11/26.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class AssetsFileData {
//    private CharacterParser characterParser;
    private ThreadLocal<Object> t = new ThreadLocal<Object>();

    //    /**
//     * 为ListView填充数据
//     *
//     * @param date
//     * @return
//     */
//    public List<SortModel> filledData(String[] date) {
//        //实例化汉字转拼音类
//        characterParser = CharacterParser.getInstance();
//        List<SortModel> mSortList = new ArrayList<SortModel>();
//        for (int i = 0; i < date.length; i++) {
//            SortModel sortModel = new SortModel();
//            sortModel.setName(date[i]);
//            //汉字转换成拼音
//            String pinyin = characterParser.getSelling(date[i]);
//            String sortString = pinyin.substring(0, 1).toUpperCase();
//
//            // 正则表达式，判断首字母是否是英文字母
//            if (sortString.matches("[A-Z]")) {
//                sortModel.setSortLetters(sortString.toUpperCase());
//            } else {
//                sortModel.setSortLetters("~");
//            }
//
//            mSortList.add(sortModel);
//        }
//        return mSortList;
//
//    }
//
    public void filledData(final Context context, final AssetGetDataInterface assetGetDataInterface, final Object cl, final String fileName) {
        if (t.get() == null) {
            ThreadManager.getLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    t.set((new Gson()).fromJson(getFromAssets(context, fileName), cl.getClass()));
                    assetGetDataInterface.initData(t.get());
                }
            });
        } else {
            assetGetDataInterface.initData(t.get());
        }


    }

    public void filledData(final Context context, final AssetGetDataInterface assetGetDataInterface, final String fileName) {
        if (t.get() == null) {
            ThreadManager.getLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    t.set(getFromAssets(context, fileName));
                    assetGetDataInterface.initData(t.get());
                }
            });
        } else {
            assetGetDataInterface.initData(t.get());
        }


    }

    public String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface AssetGetDataInterface {
        public void initData(Object data);
    }
}

