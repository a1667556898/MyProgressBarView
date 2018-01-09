package com.example.administrator.myspeedprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView level1;
    private TextView level2;
    private TextView level3;
    private TextView level4;
    private MySpeedProgressView mySpeedProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        level1= (TextView) findViewById(R.id.level1);
        level2= (TextView) findViewById(R.id.level2);
        level3= (TextView) findViewById(R.id.level3);
        level4= (TextView) findViewById(R.id.level4);
        mySpeedProgress= (MySpeedProgressView) findViewById(R.id.mySpeedProgress);
        level1.setOnClickListener(this);
        level2.setOnClickListener(this);
        level3.setOnClickListener(this);
        level4.setOnClickListener(this);
        String[] texts=new String[]{"倔强的青铜","尊贵黄金","永恒钻石","最强王者"};
        mySpeedProgress.setLevelTexts(texts);
        mySpeedProgress.setAnimInterval(10);
        mySpeedProgress.setLevels(4);
        mySpeedProgress.setCurrentLevel(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.level1:
                mySpeedProgress.setCurrentLevel(1);
                break;
            case R.id.level2:
                mySpeedProgress.setCurrentLevel(2);
                break;
            case R.id.level3:
                mySpeedProgress.setCurrentLevel(3);
                break;
            case R.id.level4:
                mySpeedProgress.setCurrentLevel(4);
                break;
        }
    }
}
