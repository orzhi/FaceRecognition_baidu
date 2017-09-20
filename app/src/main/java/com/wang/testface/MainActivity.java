package com.wang.testface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.face_detect_btn);
        Button button2 = (Button) findViewById(R.id.face_match_btn);
        Button button3 = (Button) findViewById(R.id.face_addUser_btn);
        Button button4 = (Button) findViewById(R.id.face_updateUser_btn);
        Button button5 = (Button) findViewById(R.id.face_deleteUser_btn);
        Button button6 = (Button) findViewById(R.id.face_identifyUser_btn);
        Button button7 = (Button) findViewById(R.id.face_verifyUser_btn);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.face_detect_btn:
                Intent intent = new Intent(MainActivity.this,DetectFaceActivity.class);
                startActivity(intent);
                break;
            case R.id.face_match_btn:
                Intent intent1 = new Intent(MainActivity.this,MatchFaceActivity.class);
                startActivity(intent1);
                break;
            case R.id.face_addUser_btn:
                Intent intent2 = new Intent(MainActivity.this,AddUserActivity.class);
                startActivity(intent2);
                break;
            case R.id.face_updateUser_btn:
                Intent intent3 = new Intent(MainActivity.this,UpdateUserActivity.class);
                startActivity(intent3);
                break;
            case R.id.face_deleteUser_btn:
                Intent intent4 = new Intent(MainActivity.this,DeleteUserActivity.class);
                startActivity(intent4);
                break;
            case R.id.face_identifyUser_btn:

                break;
            case R.id.face_verifyUser_btn:

                break;

        }
    }
}
