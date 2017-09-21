package com.wang.testface.bean;

/**
 * Created by 夜雨飘零 on 2017/9/21.
 */

public class GetUser {

    private String uid;
    private String userInfo;
    private String groupId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "用户的UID：" + uid +
                "\n用户信息：" + userInfo +
                "\n用户所在的组别：" + groupId;
    }

    public String toString2() {
        return "用户的UID：" + uid +
                "\n用户信息：" + userInfo;
    }
}
