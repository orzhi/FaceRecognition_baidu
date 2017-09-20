package com.wang.testface.bean;

import com.wang.testface.util.JsonUtil;

/**
 * Created by 夜雨飘零 on 2017/9/19.
 */

public class DetectBean {
    private double faceProbability;
    private double age;
    private double beauty;
    private int left;
    private int top;
    private int with;
    private int height;
    private int expression;
    private double expressionProbability;
    private String race;
    private double raceProbability;
    private int glasses;
    private double glassesProbability;
    private String gender;
    private double genderProbability;
    private double cartoon;
    private double human;

    public double getFaceProbability() {
        return faceProbability;
    }

    public void setFaceProbability(double faceProbability) {
        this.faceProbability = faceProbability;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getBeauty() {
        return beauty;
    }

    public void setBeauty(double beauty) {
        this.beauty = beauty;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getExpression() {
        return expression;
    }

    public void setExpression(int expression) {
        this.expression = expression;
    }

    public double getExpressionProbability() {
        return expressionProbability;
    }

    public void setExpressionProbability(double expressionProbability) {
        this.expressionProbability = expressionProbability;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public double getRaceProbability() {
        return raceProbability;
    }

    public void setRaceProbability(double raceProbability) {
        this.raceProbability = raceProbability;
    }

    public int getGlasses() {
        return glasses;
    }

    public void setGlasses(int glasses) {
        this.glasses = glasses;
    }

    public double getGlassesProbability() {
        return glassesProbability;
    }

    public void setGlassesProbability(double glassesProbability) {
        this.glassesProbability = glassesProbability;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getGenderProbability() {
        return genderProbability;
    }

    public void setGenderProbability(double genderProbability) {
        this.genderProbability = genderProbability;
    }

    public double getCartoon() {
        return cartoon;
    }

    public void setCartoon(double cartoon) {
        this.cartoon = cartoon;
    }

    public double getHuman() {
        return human;
    }

    public void setHuman(double human) {
        this.human = human;
    }

    @Override
    public String toString() {
        return "人脸置信度：" + JsonUtil.getProbability(faceProbability) +
                "\n年龄：" + (int)age +
                "\n颜值：" + (int)beauty +
                "\n左边界的距离：" + left +
                "，上边界的距离：" + top +
                "\n宽度：" + with +
                "，高度：" + height +
                "\n表情：" + JsonUtil.getExpression(expression) +
                "，表情置信度：" + JsonUtil.getProbability(expressionProbability) +
                "\n人种：" + JsonUtil.getRace(race) +
                "，人种置信度：" + JsonUtil.getProbability(raceProbability) +
                "\n眼镜种类：" + JsonUtil.getGlasses(glasses) +
                "，眼镜种类置信度：" + JsonUtil.getProbability(glassesProbability) +
                "\n性别：" + JsonUtil.getGender(gender) +
                "，性别置信度：" + JsonUtil.getProbability(genderProbability) +
                "\n人物类型：" + JsonUtil.getType(cartoon, human);
    }
}
