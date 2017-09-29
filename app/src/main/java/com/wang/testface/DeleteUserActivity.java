package com.wang.testface;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.wang.testface.constant.FaceKey;
import com.wang.testface.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class DeleteUserActivity extends AppCompatActivity {

    private AipFace client;
    private ProgressDialog pd;
    private TextView result;
    private EditText inputAcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        result = (TextView) findViewById(R.id.delete_result);
        inputAcount = (EditText) findViewById(R.id.delete_user_account_ev);
        Button deleteGroup = (Button) findViewById(R.id.delete_group_btn);
        Button deleteUser = (Button) findViewById(R.id.delete_user_btn);

        //删除用户
        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String acount = inputAcount.getText().toString().trim();
                if (acount.length() > 0) {
                    showPD("正在删除");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 只从指定组中删除用户
                            final JSONObject res = client.deleteUser(acount, Arrays.asList("group1"));
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
                } else {
                    ToastUtil.show(DeleteUserActivity.this, "请输入账号");
                }
            }
        });

        // 从人脸库中彻底删除用户
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String acount = inputAcount.getText().toString().trim();
                if (acount.length() > 0) {
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
                } else {
                    ToastUtil.show(DeleteUserActivity.this, "请输入账号");
                }
            }
        });

        //初始化API
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
