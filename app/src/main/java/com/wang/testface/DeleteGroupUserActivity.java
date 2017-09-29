package com.wang.testface;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DeleteGroupUserActivity extends AppCompatActivity {
    private EditText acount, oldGroup;
    private TextView resultTv;
    private AipFace client;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group_user);
        oldGroup = (EditText) findViewById(R.id.get_delete_group_users_old);
        acount = (EditText) findViewById(R.id.get_delete_group_users_acount);
        resultTv = (TextView) findViewById(R.id.get_delete_group_users_result);
        Button delete = (Button) findViewById(R.id.get_delete_group_users_delete);

        //删除用户
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String group1 = oldGroup.getText().toString().trim();
                final String uid1 = acount.getText().toString().trim();
                if (group1.length() > 0 || uid1.length() > 0) {
                    showPD("正在删除");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //删除操作
                            final JSONObject res = client.deleteGroupUser(Arrays.asList(group1), uid1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //显示返回的JSON数据
                                        resultTv.setText(res.toString(4));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    dismissPD();
                                }
                            });
                        }
                    }).start();
                } else {
                    ToastUtil.show(DeleteGroupUserActivity.this, "请补充信息");
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
