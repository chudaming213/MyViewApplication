package com.onon.test.myviewapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.onon.test.myviewapplication.viewactivities.ConstraintlayoutActivity;
import com.onon.test.myviewapplication.viewactivities.FlexboxlayoutActivity;

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
    }
}
