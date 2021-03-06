package com.wang.testface.bean;

import com.wang.testface.util.JsonUtil;

import java.util.Arrays;

/**
 * Created by 夜雨飘零 on 2017/9/20.
 */

public class IdentifyUserBean {
    private String group_id;
    private String uid;
    private String user_info;
    private double scores;
    private double faceliveness;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public double getScores() {
        return scores;
    }

    public void setScores(double scores) {
        this.scores = scores;
    }

    public double getFaceliveness() {
        return faceliveness;
    }

    public void setFaceliveness(double faceliveness) {
        this.faceliveness = faceliveness;
    }

    @Override
    public String toString() {
        return "用户组号：" + group_id +
                "\n用户的UID：" + uid +
                "\n用户信息：" + user_info +
                "\n识别分数：" + scores +
                "\n是否照片攻击：" + JsonUtil.getFaceliveness(faceliveness);
    }
}
