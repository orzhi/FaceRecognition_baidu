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

import java.util.HashMap;

public class GetGroupUsersActivity extends AppCompatActivity {

    private EditText acount;
    private TextView resultTv;
    private AipFace client;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_group_users);

        acount = (EditText) findViewById(R.id.get_group_users_acount);
        resultTv = (TextView) findViewById(R.id.get_group_users_result);
        Button getUserBtn = (Button) findViewById(R.id.get_group_users);

        //获取该组的所有用户
        getUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD("正在查询");
                final String group = acount.getText().toString().trim();
                if (group.length() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final StringBuilder sb = new StringBuilder();
                            HashMap<String, Object> options = new HashMap<String, Object>(2);
                            //从第0个开始
                            options.put("start", 0);
                            //查找10个
                            options.put("num", 10);
                            //获取结果
                            final JSONObject res = client.getGroupUsers(group, options);

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
                                        if (getUsers.length > 0){
                                            for (int i = 0; i < getUsers.length; i++) {
                                                GetUser g = getUsers[i];
                                                sb.append("第").append(i+1).append("个结果").append("\n");
                                                sb.append(g.toString2()).append("\n\n");
                                            }
                                        }else {
                                            sb.append("该组无用户");
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
