package com.onon.test.myviewapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.onon.test.myviewapplication.viewactivities.CurtainActivity;
import com.onon.test.myviewapplication.viewactivities.ConstraintlayoutActivity;
import com.onon.test.myviewapplication.viewactivities.CycleViewPagerActivity;
import com.onon.test.myviewapplication.viewactivities.FlexboxlayoutActivity;
import com.onon.test.myviewapplication.viewactivities.HeartActivity;
import com.onon.test.myviewapplication.viewactivities.HorizentalCurtainActivity;
import com.onon.test.myviewapplication.viewactivities.SVGActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.flexboxlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FlexboxlayoutActivity.class));
            }
        });
        findViewById(R.id.constraintlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConstraintlayoutActivity.class));
            }
        });
        findViewById(R.id.svg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SVGActivity.class));
            }
        });
        findViewById(R.id.cycleviewpager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CycleViewPagerActivity.class));
            }
        });
        findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HeartActivity.class));
            }
        });
        findViewById(R.id.curtain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CurtainActivity.class));
            }
        });
        findViewById(R.id.curtain_horizontal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HorizentalCurtainActivity.class));
            }
        });
    }
}
