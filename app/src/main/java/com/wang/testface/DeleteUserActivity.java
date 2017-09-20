package com.wang.testface;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.wang.testface.constant.FaceKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class DeleteUserActivity extends AppCompatActivity {

    private AipFace client;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        final TextView result = (TextView) findViewById(R.id.delete_result);
        Button deleteGroup = (Button) findViewById(R.id.delete_group_btn);
        Button deleteUser = (Button) findViewById(R.id.delete_user_btn);

        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD("正在删除");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 只从指定组中删除用户
                        final JSONObject res = client.deleteUser("uid1", Arrays.asList("group1"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissPD();
                                    try {
                                        result.setText(res.toString(4));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    }
                }).start();
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD("正在删除");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 从人脸库中彻底删除用户
                        final JSONObject res = client.deleteUser("uid1");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissPD();
                                    try {
                                        result.setText(res.toString(4));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    }
                }).start();
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
