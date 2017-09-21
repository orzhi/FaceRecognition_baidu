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

import java.util.HashMap;

public class GetGroupListActivity extends AppCompatActivity {

    private TextView group;
    private AipFace client;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_group_list);

        group = (TextView) findViewById(R.id.getGroupList_tv);
        Button getGroup = (Button) findViewById(R.id.getGroupList);

        getGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD("正在查询");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Object> options = new HashMap<String, Object>(2);
                        options.put("start", 0);
                        options.put("num", 10);
                        final JSONObject res = client.getGroupList(options);
                        Log.e("结果",res.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    group.setText(res.toString(4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dismissPD();
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
