package com.onon.test.myviewapplication.viewactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onon.test.myviewapplication.R;
import com.onon.test.myviewapplication.view.KuwoCurtainView;

public class HorizentalCurtainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizental_curtain);
        final KuwoCurtainView hcurtainview = (KuwoCurtainView) findViewById(R.id.hcurtainview);
        hcurtainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hcurtainview.showNext();
            }
        });
        hcurtainview.setOrientationMode(KuwoCurtainView.HORIZONTAL);
        hcurtainview.setSpeedFactor(8);
        hcurtainview.setAdapter(new KuwoCurtainView.CurtainViewAdapter() {
            @Override
            public int getItemCount() {
                return 1000;
            }

            @Override
            public View getItem(View view, int pos) {
                if (view == null) {
                    view = View.inflate(HorizentalCurtainActivity.this, R.layout.item_textview, null);
                } else {
                }
//                TextView tv_pos = (TextView) view.findViewById(R.id.tv_pos);
                TextView tv_pos = (TextView) view;
                tv_pos.setSingleLine(true);
                tv_pos.setText("电脑有装。。但是十天半但是十天半但是十天半但是十天半个吗？==" + pos);
                return view;
            }

            @Override
            public long getItemStayOutDuration(int pos) {
                return 1000;
            }
        });
    }
}
