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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.bumptech.glide.Glide;
import com.wang.testface.bean.IdentifyUserBean;
import com.wang.testface.constant.FaceKey;
import com.wang.testface.util.AnalysisJson;
import com.wang.testface.util.CameraUtil;
import com.wang.testface.util.CompressBitmapUtil;
import com.wang.testface.util.JsonUtil;
import com.wang.testface.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 人脸认证
 */
public class VerifyUserActivity extends AppCompatActivity {
    private ImageView photo;
    private TextView result;
    private AipFace client;
    private Uri imageUri;
    private ProgressDialog pd;
    private EditText inputAcount;
    private String acount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);
        photo = (ImageView) findViewById(R.id.verify_user_iv);
        result = (TextView) findViewById(R.id.verify_user_account);
        inputAcount = (EditText) findViewById(R.id.verify_user_account_ev);
        Button getPhotoBtn = (Button) findViewById(R.id.verify_user_getPhoto_btn);
        Button startCameraBtn = (Button) findViewById(R.id.verify_user_startCamera_btn);

        //从相册获取照片
        getPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acount = inputAcount.getText().toString().trim();
                if (acount.length() > 0) {
                    if (ContextCompat.checkSelfPermission(VerifyUserActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(VerifyUserActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        getPhoto();
                    }
                } else {
                    ToastUtil.show(VerifyUserActivity.this, "请输入账号");
                }
            }
        });

        //打开相机拍照
        startCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acount = inputAcount.getText().toString().trim();
                if (acount.length() > 0) {
                    if (ContextCompat.checkSelfPermission(VerifyUserActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(VerifyUserActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 2);
                    } else {
                        imageUri = CameraUtil.startCamera(VerifyUserActivity.this, 2);
                    }
                } else {
                    ToastUtil.show(VerifyUserActivity.this, "请输入账号");
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
                    imageUri = CameraUtil.startCamera(VerifyUserActivity.this, 2);
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
                    showPD("正在认证");
                    Uri uri;
                    if (requestCode == 1) {
                        //获取相册的URI
                        uri = data.getData();
                    } else {
                        //获取相机拍的URI
                        uri = imageUri;
                    }
                    Glide.with(VerifyUserActivity.this).load(uri).into(photo);

                    //获取压缩的图片
                    final String filePath = CompressBitmapUtil.CompressBitmap(
                            CameraUtil.getRealPathFromURI(VerifyUserActivity.this, uri));
                    final HashMap<String, Object> options = new HashMap<String, Object>(1);
                    options.put("top_num", 5);
                    options.put("ext_fields", "faceliveness");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject res = client.verifyUser(acount,
                                    Arrays.asList("group1", "group2"), filePath, options);
                            Log.e("返回的数据", res.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        int errorCode = res.getInt("error_code");
                                        String errorMsg = res.getString("error_msg");
                                        sb.append("识别失败，").append(errorMsg);
                                        result.setText(sb);
                                        if (errorCode == 216611) {
                                            ToastUtil.show(VerifyUserActivity.this, "用户不存在");
                                        }
                                    } catch (JSONException e) {
                                        try {
                                            double results = JsonUtil.getScores(res.getString("result"));
                                            JSONObject jsonObject = res.getJSONObject("ext_info");
                                            double faceliveness = jsonObject.getDouble("faceliveness");
                                            sb.append("认证结果：").append(JsonUtil.getResult(results))
                                                    .append("相似度为：").append((int) results).append("%")
                                                    .append("\n");
                                            sb.append("是否照片攻击:")
                                                    .append(JsonUtil.getFaceliveness(faceliveness))
                                                    .append("\n");
                                            result.setText(sb);
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    dismissPD();
                                    //删除临时照片
                                    File file = new File(filePath);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    }

    //打开相册
    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void showPD(String title) {
        pd = new ProgressDialog(this);
        pd.setTitle(title);
        pd.setMessage("任务正在执行，请稍等");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    private void dismissPD() {
        if (pd != null) {
            pd.dismiss();
        }
    }

}

