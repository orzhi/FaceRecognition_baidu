package com.wang.testface;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.bumptech.glide.Glide;
import com.wang.testface.bean.IdentifyUserBean;
import com.wang.testface.constant.FaceKey;
import com.wang.testface.util.AnalysisJson;
import com.wang.testface.util.CameraUtil;
import com.wang.testface.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class IdentifyUserActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView result;
    private AipFace client;
    private Uri imageUri;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_user);
        photo = (ImageView) findViewById(R.id.identify_user_iv);
        result = (TextView) findViewById(R.id.identify_user_account);
        Button getPhotoBtn = (Button) findViewById(R.id.identify_user_getPhoto_btn);
        Button startCameraBtn = (Button) findViewById(R.id.identify_user_startCamera_btn);

        getPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(IdentifyUserActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(IdentifyUserActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
        });

        startCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(IdentifyUserActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(IdentifyUserActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 2);
                } else {
                    imageUri = CameraUtil.startCamera(IdentifyUserActivity.this, 2);
                }
            }
        });

        client = new AipFace(FaceKey.APP_ID, FaceKey.API_KEY, FaceKey.SECRET_KEY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPhoto();
                } else {
                    ToastUtil.show(this, "你拒绝了授权");
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageUri = CameraUtil.startCamera(IdentifyUserActivity.this, 2);
                } else {
                    ToastUtil.show(this, "你拒绝了授权");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                case 2:
                    showPD("正在识别");
                    Uri uri;
                    if (requestCode == 1) {
                        uri = data.getData();
                    } else {
                        uri = imageUri;
                    }
                    Glide.with(IdentifyUserActivity.this).load(uri).into(photo);

                    final String filePath = CameraUtil.getRealPathFromURI(IdentifyUserActivity.this, uri);
                    final HashMap<String, Object> options = new HashMap<String, Object>();
                    options.put("user_top_num", 5);
                    options.put("ext_fields","faceliveness");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject res = client.identifyUser(Arrays.asList("group1", "group2"),
                                    filePath, options);
                            Log.e("返回的数据", res.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        String errorCode = res.getString("error_code");
                                        String errorMsg = res.getString("error_msg");
                                        sb.append("识别失败，").append(errorMsg);
                                    } catch (JSONException e) {
                                        IdentifyUserBean[] userBeen = AnalysisJson.IdentifyUserJson(res);
                                        for (int i = 0;i<userBeen.length;i++) {
                                            sb.append("第").append(i).append("个识别结果").append("\n");
                                            sb.append(userBeen[i]).append("\n\n");
                                        }
                                        result.setText(sb);
                                    }
                                    dismissPD();
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void showPD(String title){
        pd = new ProgressDialog(this);
        pd.setTitle(title);
        pd.setMessage("任务正在执行，请稍等");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    private void dismissPD(){
        pd.dismiss();
    }

}

