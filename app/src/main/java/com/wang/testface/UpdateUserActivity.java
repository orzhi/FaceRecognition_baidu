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
import com.wang.testface.constant.FaceKey;
import com.wang.testface.util.CameraUtil;
import com.wang.testface.util.CompressBitmapUtil;
import com.wang.testface.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 更新用户
 */
public class UpdateUserActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView account;
    private AipFace client;
    private Uri imageUri;
    private ProgressDialog pd;
    private EditText inputAcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        photo = (ImageView) findViewById(R.id.update_user_iv);
        account = (TextView) findViewById(R.id.update_user_account);
        inputAcount = (EditText) findViewById(R.id.update_user_account_ev);
        Button getPhotoBtn = (Button) findViewById(R.id.update_user_getPhoto_btn);
        Button startCameraBtn = (Button) findViewById(R.id.update_user_startCamera_btn);

        //从相机获取图片
        getPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acount = inputAcount.getText().toString().trim();
                if (acount.length() > 0) {
                    if (ContextCompat.checkSelfPermission(UpdateUserActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateUserActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        getPhoto();
                    }
                } else {
                    ToastUtil.show(UpdateUserActivity.this, "请输入账号");
                }
            }
        });

        //启动相机拍照
        startCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acount = inputAcount.getText().toString().trim();
                if (acount.length() > 0) {
                    if (ContextCompat.checkSelfPermission(UpdateUserActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateUserActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 2);
                    } else {
                        imageUri = CameraUtil.startCamera(UpdateUserActivity.this, 2);
                    }
                } else {
                    ToastUtil.show(UpdateUserActivity.this, "请输入账号");
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
                    imageUri = CameraUtil.startCamera(UpdateUserActivity.this, 2);
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
                    Uri uri;
                    if (requestCode == 1) {
                        //获取相册的照片的URI
                        uri = data.getData();
                    } else {
                        //获取相机拍的照片的URI
                        uri = imageUri;
                    }
                    //使用图片框架显示图片
                    Glide.with(UpdateUserActivity.this).load(uri).into(photo);

                    final String acount = inputAcount.getText().toString().trim();
                    showPD("正在更新");
                    //获取压缩过的图片，包括通过URI获取图片路径
                    final String filePath = CompressBitmapUtil.CompressBitmap(
                            CameraUtil.getRealPathFromURI(UpdateUserActivity.this,uri));
                    final HashMap<String, String> options = new HashMap<String, String>();
                    options.put("action_type", "replace");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject res = client.updateUser(acount, "更新信息" + acount,
                                    "group1", filePath, options);
                            Log.e("返回的数据", res.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissPD();
                                    try {
                                        account.setText(res.toString(4));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //删除临时照片
                                    File file = new File(filePath);
                                    if (file.exists()){
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
        pd.dismiss();
    }

}
