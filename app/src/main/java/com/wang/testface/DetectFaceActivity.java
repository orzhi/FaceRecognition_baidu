package com.wang.testface;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.FaceConsts;
import com.bumptech.glide.Glide;
import com.wang.testface.bean.DetectBean;
import com.wang.testface.constant.FaceKey;
import com.wang.testface.util.AnalysisJson;
import com.wang.testface.util.CameraUtil;
import com.wang.testface.util.CompressBitmapUtil;
import com.wang.testface.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DetectFaceActivity extends AppCompatActivity {

    private TextView photoInfo;
    private ImageView image;
    private AipFace client;
    private Uri cameraUri;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dismissPD();
                    photoInfo.setText((String) msg.obj);
                    //删除临时照片
                    File file = new File(filePath);
                    if (file.exists()){
                        file.delete();
                    }
                    break;
                case 2:
                    ToastUtil.show(DetectFaceActivity.this, (String) msg.obj);
                    dismissPD();
                    //删除临时照片
                    File file2 = new File(filePath);
                    if (file2.exists()){
                        file2.delete();
                    }
                    break;
            }
        }
    };
    private ProgressDialog pd;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_face);
        Button getPhotoBtn = (Button) findViewById(R.id.detect_photo_btn);
        Button startCameraBtn = (Button) findViewById(R.id.detect_camera_btn);
        photoInfo = (TextView) findViewById(R.id.detect_info);
        image = (ImageView) findViewById(R.id.detect_image);

        //从相册获取图片
        getPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DetectFaceActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetectFaceActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
        });

        //调用相机拍照
        startCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DetectFaceActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetectFaceActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 2);
                } else {
                    cameraUri = CameraUtil.startCameraSave(DetectFaceActivity.this, 2);
                }
            }
        });

        //初始化API
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
                    //调用工具类打开相机
                    cameraUri = CameraUtil.startCameraSave(DetectFaceActivity.this, 2);
                } else {
                    ToastUtil.show(this, "你拒绝了授权");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    showPD("正在识别图片");

                    Uri uri;
                    if (requestCode == 1) {
                        //获取相册图片的URI
                        uri = data.getData();
                    } else {
                        uri = cameraUri;
                    }

                    //获取压缩过的图片，包括通过URI获取图片路径
                    filePath = CompressBitmapUtil.CompressBitmap(
                            CameraUtil.getRealPathFromURI(DetectFaceActivity.this,uri));

                    //使用图片框架显示图片
                    Glide.with(DetectFaceActivity.this).load(uri).into(image);

                    final HashMap<String, String> options = new HashMap<>();
                    //最多识别10个人
                    options.put("max_face_num", "10");
                    //要显示人脸的信息
                    options.put("face_fields", "age,beauty,expression,gender,glasses,race,qualities");
                    // 参数为本地图片路径
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            //获取识别结果
                            JSONObject response = client.detect(filePath, options);
                            Log.e("返回的结果", response.toString());
                            try {
                                response.getInt("log_id");
                                //使用工具类解析JSON
                                DetectBean[] detectBeen = AnalysisJson.DetectJson(response);
                                if (detectBeen != null) {
                                    for (int i = 0; i < detectBeen.length; i++) {
                                        DetectBean d = detectBeen[i];
                                        sb.append("第").append(i + 1).append("张脸：").append("\n");
                                        sb.append(d.toString()).append("\n\n");
                                    }
                                    //显示信息
                                    Message message = Message.obtain();
                                    message.what = 1;
                                    message.obj = sb.toString();
                                    handler.sendMessage(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //返回错误数据
                                try {
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    message.obj = "识别错误" + response.getString("error_msg");
                                    handler.sendMessage(message);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    message.obj = "识别错误";
                                    handler.sendMessage(message);
                                }
                            }
                        }
                    }).start();
                }
                break;
        }
    }


    //获取图片
    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void showPD(String title) {
        pd = new ProgressDialog(this);
        pd.setTitle(title);
        pd.setMessage("任务正在执行.请稍等");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    private void dismissPD() {
        pd.dismiss();
    }
}
