package com.wang.testface.util;

import android.util.Log;

import com.wang.testface.bean.DetectBean;
import com.wang.testface.bean.MatchBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 夜雨飘零 on 2017/9/19.
 */

public class AnalysisJson {

    /**
     *
     * @param jsonObject response
     * @return 是一个数组
     */
    public static DetectBean[] DetectJson(JSONObject jsonObject) {
        DetectBean[] detectBeans = null;
        int resultNum = 0;
        try {
            resultNum = jsonObject.getInt("result_num");
            detectBeans = new DetectBean[resultNum];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray result = null;
        try {
            result = jsonObject.getJSONArray("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result != null) {
            for (int i = 0; i < resultNum; i++) {
                DetectBean detectBean = new DetectBean();
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = result.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject1 != null) {

                    try {
                        detectBean.setFaceProbability(jsonObject1.getDouble("face_probability"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        detectBean.setAge(jsonObject1.getDouble("age"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        detectBean.setBeauty(jsonObject1.getDouble("beauty"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        detectBean.setExpression(jsonObject1.getInt("expression"));
                        detectBean.setExpressionProbability(jsonObject1.getDouble("expression_probablity"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        detectBean.setRace(jsonObject1.getString("race"));
                        detectBean.setRaceProbability(jsonObject1.getDouble("race_probability"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        detectBean.setGlasses(jsonObject1.getInt("glasses"));
                        detectBean.setGlassesProbability(jsonObject1.getDouble("glasses_probability"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        detectBean.setGender(jsonObject1.getString("gender"));
                        detectBean.setGenderProbability(jsonObject1.getDouble("gender_probability"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject location = jsonObject1.getJSONObject("location");
                        detectBean.setLeft(location.getInt("left"));
                        detectBean.setTop(location.getInt("top"));
                        detectBean.setWith(location.getInt("width"));
                        detectBean.setHeight(location.getInt("height"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject qualities = jsonObject1.getJSONObject("qualities");
                        JSONObject type = qualities.getJSONObject("type");
                        detectBean.setCartoon(type.getDouble("cartoon"));
                        detectBean.setHuman(type.getDouble("human"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                detectBeans[i] = detectBean;
            }
        }

        return detectBeans;
    }


    public static MatchBean[] MatchJson(JSONObject jsonObject){
        MatchBean[] matchBeen = null;
        int resultNum = 0;
        try {
            resultNum = jsonObject.getInt("result_num");
            matchBeen = new MatchBean[resultNum];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray result = null;
        try {
            result = jsonObject.getJSONArray("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result != null) {
            for (int i = 0; i < resultNum; i++) {
                MatchBean match = new MatchBean();
                try {
                    JSONObject jsonObject1 = result.getJSONObject(i);
                    match.setIndex_i(jsonObject1.getInt("index_i"));
                    match.setIndex_j(jsonObject1.getInt("index_j"));
                    match.setScore(jsonObject1.getDouble("score"));
                } catch (JSONException e) {
                    Log.e("这里3",e.getMessage());
                    e.printStackTrace();
                }

                try {
                    JSONObject extInfo = jsonObject.getJSONObject("ext_info");
                    String faceliveness = extInfo.getString("faceliveness");
                    String[] split = faceliveness.split(",");
                    match.setFaceliveness_i(Double.parseDouble(split[0]));
                    match.setFaceliveness_j(Double.parseDouble(split[1]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                matchBeen[i] = match;
            }
        }
        return matchBeen;
    }
}
