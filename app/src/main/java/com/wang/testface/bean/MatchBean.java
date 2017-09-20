package com.wang.testface.bean;

import com.wang.testface.util.JsonUtil;

/**
 * Created by 夜雨飘零 on 2017/9/20.
 */

public class MatchBean {
    private int index_i;
    private int index_j;
    private double score;
    private double faceliveness_i;
    private double faceliveness_j;

    public int getIndex_i() {
        return index_i;
    }

    public void setIndex_i(int index_i) {
        this.index_i = index_i;
    }

    public int getIndex_j() {
        return index_j;
    }

    public void setIndex_j(int index_j) {
        this.index_j = index_j;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getFaceliveness_i() {
        return faceliveness_i;
    }

    public void setFaceliveness_i(double faceliveness_i) {
        this.faceliveness_i = faceliveness_i;
    }

    public double getFaceliveness_j() {
        return faceliveness_j;
    }

    public void setFaceliveness_j(double faceliveness_j) {
        this.faceliveness_j = faceliveness_j;
    }

    @Override
    public String toString() {
        return "照片" + (index_i + 1) + " 跟 照片" + (index_j + 1) +
                "\n相似度为：" + (int) score + "%" +
                "\n照片" + (index_i + 1) + "：" + JsonUtil.getFaceliveness(faceliveness_i) +
                "\n照片" + (index_j + 1) + "：" + JsonUtil.getFaceliveness(faceliveness_j);
    }
}
