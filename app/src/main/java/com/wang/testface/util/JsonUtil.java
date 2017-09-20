package com.wang.testface.util;

/**
 * Created by 夜雨飘零 on 2017/9/19.
 */

public class JsonUtil {

    public static String getType(double cartoon, double human) {
        return cartoon > human ? "卡通" : "人类";
    }

    public static String getProbability(double probability) {
        int v = (int) (probability * 100);
        return v + "%";
    }

    public static String getExpression(int e) {
        String expression = "UNknown";
        switch (e) {
            case 0:
                expression = "不笑";
                break;
            case 1:
                expression = "微笑";
                break;
            case 2:
                expression = "大笑";
                break;
        }
        return expression;
    }

    public static String getRace(String r) {
        String race = "Unknown";
        switch (r) {
            case "yellow":
                race = "黄种人";
                break;
            case "white":
                race = "白种人";
                break;
            case "black":
                race = "黑种人";
                break;
            case "arabs":
                race = "阿拉伯人";
                break;
        }
        return race;
    }

    public static String getGlasses(int g){
        String glasses = "Unknown";
        switch (g){
            case 0:
                glasses = "不带眼镜";
                break;
            case 1:
                glasses = "普通眼镜";
                break;
            case 2:
                glasses = "墨镜";
                break;
        }
        return glasses;
    }

    public static String getGender(String g){
        if (g.equals("male")){
            return "男";
        }else {
            return "女";
        }
    }

    public static String getFaceliveness(double f){
        if (f > 0.4494){
            return "非照片攻击";
        }else {
            return "照片攻击";
        }
    }
}