package com.wang.testface;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.wang.testface.bean.GetUser;
import com.wang.testface.constant.FaceKey;
import com.wang.testface.util.AnalysisJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class GetUserActivity extends AppCompatActivity {

    private EditText acount;
    private TextView resultTv;
    private AipFace client;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user);
        acount = (EditText) findViewById(R.id.get_user_acount);
        resultTv = (TextView) findViewById(R.id.get_user_result);
        Button one = (Button) findViewById(R.id.get_user_one);
        Button all = (Button) findViewById(R.id.get_user_all);

        //查找用户在所有组的信息
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD("正在查询");
                final String acoutStr = acount.getText().toString().trim();
                if (acoutStr.length() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final StringBuilder sb = new StringBuilder();
                            // 查询一个用户在所有组内的信息
                            final JSONObject res = client.getUser(acoutStr);
                            Log.e("返回的结果", res.toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //判断返回是否是错误码
                                        String errorMsg = res.getString("error_msg");
                                        sb.append("发生错误").append(errorMsg);
                                    } catch (JSONException e) {
                                        //获取成功
                                        GetUser[] getUsers = AnalysisJson.GetUserJson(res);
                                        for (int i = 0; i < getUsers.length; i++) {
                                            GetUser g = getUsers[i];
                                            sb.append("第").append(i+1).append("个结果").append("\n");
                                            sb.append(g.toString()).append("\n\n");
                                        }
                                    }
                                    dismissPD();
                                    resultTv.setText(sb);
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        //查找用户在特定组的信息
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD("正在查询");
                final String acoutStr = acount.getText().toString().trim();
                if (acoutStr.length() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final StringBuilder sb = new StringBuilder();
                            // 查询一个用户在指定组内的信息
                            final JSONObject res = client.getUser(acoutStr, Arrays.asList("group1"));
                            Log.e("返回的结果", res.toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String errorMsg = res.getString("error_msg");
                                        sb.append("发生错误").append(errorMsg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        GetUser[] getUsers = AnalysisJson.GetUserJson(res);
                                        for (GetUser g:getUsers) {
                                            sb.append(g.toString());
                                        }
                                    }
                                    dismissPD();
                                    resultTv.setText(sb);
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        client = new AipFace(FaceKey.APP_ID, FaceKey.API_KEY, FaceKey.SECRET_KEY);
    }


    private void showPD(String title) {
        pd = new ProgressDialog(this);
        pd.setTitle(title);
        pd.setMessage("任务正在执行，请稍等");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    private void dismissPD() {
        pd.dismiss();
    }
}
