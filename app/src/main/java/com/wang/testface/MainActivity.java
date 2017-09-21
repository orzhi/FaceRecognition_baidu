package com.wang.testface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        Button button8 = (Button) findViewById(R.id.getUser);
        Button button9 = (Button) findViewById(R.id.getGroupList);
        Button button10 = (Button) findViewById(R.id.getGroupUsers);
        Button button11 = (Button) findViewById(R.id.addGroupUser);
        Button button12 = (Button) findViewById(R.id.deleteGroupUser);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_detect_btn:
                Intent intent = new Intent(MainActivity.this, DetectFaceActivity.class);
                startActivity(intent);
                break;
            case R.id.face_match_btn:
                Intent intent1 = new Intent(MainActivity.this, MatchFaceActivity.class);
                startActivity(intent1);
                break;
            case R.id.face_addUser_btn:
                Intent intent2 = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(intent2);
                break;
            case R.id.face_updateUser_btn:
                Intent intent3 = new Intent(MainActivity.this, UpdateUserActivity.class);
                startActivity(intent3);
                break;
            case R.id.face_deleteUser_btn:
                Intent intent4 = new Intent(MainActivity.this, DeleteUserActivity.class);
                startActivity(intent4);
                break;
            case R.id.face_identifyUser_btn:
                Intent intent5 = new Intent(MainActivity.this, IdentifyUserActivity.class);
                startActivity(intent5);
                break;
            case R.id.face_verifyUser_btn:
                Intent intent6 = new Intent(MainActivity.this, VerifyUserActivity.class);
                startActivity(intent6);
                break;
            case R.id.getUser:
                Intent intent7 = new Intent(MainActivity.this, GetUserActivity.class);
                startActivity(intent7);
                break;
            case R.id.getGroupList:
                Intent intent8 = new Intent(MainActivity.this, GetGroupListActivity.class);
                startActivity(intent8);
                break;
            case R.id.getGroupUsers:
                Intent intent9 = new Intent(MainActivity.this, GetGroupUsersActivity.class);
                startActivity(intent9);
                break;
            case R.id.addGroupUser:
                Intent intent10 = new Intent(MainActivity.this, AddGroupUserActivity.class);
                startActivity(intent10);
                break;
            case R.id.deleteGroupUser:
                Intent intent11 = new Intent(MainActivity.this, DeleteGroupUserActivity.class);
                startActivity(intent11);
                break;

        }
    }
}
