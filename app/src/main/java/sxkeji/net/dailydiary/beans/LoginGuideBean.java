package sxkeji.net.dailydiary.beans;

import java.io.Serializable;

/**
 * 登录引导的实体类
 * Created by zhangshixin on 4/15/2016.
 */
public class LoginGuideBean implements Serializable {
    private int imgId;
    private String title;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
