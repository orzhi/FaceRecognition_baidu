package com.wang.testface;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.bumptech.glide.Glide;
import com.wang.testface.bean.MatchBean;
import com.wang.testface.constant.FaceKey;
import com.wang.testface.util.AnalysisJson;
import com.wang.testface.util.CameraUtil;
import com.wang.testface.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchFaceActivity extends AppCompatActivity {

    private ImageView[] imageIv = new ImageView[2];
    private RadioGroup radioGroup;
    private TextView photoInfo;
    private Button getPhotoBtn;
    private Button startCameraBtn;
    private ProgressDialog pd;
    private int photoIndex = 0;
    private String[] photoPath = new String[2];
    private Uri imageUri;
    private Button matchBtn;
    private AipFace client;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dismissPD();
                    photoInfo.setText((String) msg.obj);
                    break;
                case 2:
                    dismissPD();
                    ToastUtil.show(MatchFaceActivity.this, (String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_face);

        imageIv[0] = (ImageView) findViewById(R.id.match_image1);
        imageIv[1] = (ImageView) findViewById(R.id.match_image2);
        radioGroup = (RadioGroup) findViewById(R.id.match_image_group);
        photoInfo = (TextView) findViewById(R.id.match_info);
        getPhotoBtn = (Button) findViewById(R.id.match_photo_btn);
        startCameraBtn = (Button) findViewById(R.id.match_camera_btn);
        matchBtn = (Button) findViewById(R.id.match_result_btn);
        listent();

        client = new AipFace(FaceKey.APP_ID, FaceKey.API_KEY, FaceKey.SECRET_KEY);
    }

    private void listent() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                photoIndex = checkedId == R.id.match_image1_rb ? 0 : 1;
            }
        });

        getPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MatchFaceActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MatchFaceActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
        });

        startCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MatchFaceActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MatchFaceActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 2);
                } else {
                    imageUri = CameraUtil.startCameraSave(MatchFaceActivity.this, 2);
                }
            }
        });

        matchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPD("正在对比中");
                final StringBuilder sb = new StringBuilder();
                final ArrayList<String> pathArray = new ArrayList<String>();
                final HashMap<String, String> options = new HashMap<String, String>();
                options.put("image_liveness", "faceliveness,faceliveness");
                pathArray.add(photoPath[0]);
                pathArray.add(photoPath[1]);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject response = client.match(pathArray, options);
                        try {
                            response.getInt("log_id");
                            MatchBean[] matchBeen = AnalysisJson.MatchJson(response);
                            Log.e("大小", String.valueOf(matchBeen.length));
                            if (matchBeen.length > 0) {
                                for (MatchBean m : matchBeen) {
                                    sb.append(m.toString());
                                }
                            }
                            Log.e("JSON数据", response.toString());
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = sb.toString();
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                Message message = Message.obtain();
                                message.what = 2;
                                message.obj = "识别错误" + response.getString("error_msg");
                                handler.sendMessage(message);
                                Log.e("识别错误", response.getString("error_msg"));
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
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPhoto();
                } else {
                    ToastUtil.show(MatchFaceActivity.this, "你拒绝了授权");
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageUri = CameraUtil.startCameraSave(MatchFaceActivity.this, 2);
                } else {
                    ToastUtil.show(MatchFaceActivity.this, "你拒绝了授权");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    photoPath[photoIndex] = CameraUtil.getRealPathFromURI(MatchFaceActivity.this, uri);
                    Glide.with(MatchFaceActivity.this).load(uri).into(imageIv[photoIndex]);
                    break;
                case 2:
                    photoPath[photoIndex] = CameraUtil.getRealPathFromURI(MatchFaceActivity.this, imageUri);
                    Glide.with(MatchFaceActivity.this).load(imageUri).into(imageIv[photoIndex]);
                    break;
            }
        }
    }

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
