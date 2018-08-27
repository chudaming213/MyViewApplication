package com.onon.test.myviewapplication.viewactivities;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onon.test.myviewapplication.R;
import com.onon.test.myviewapplication.view.KuwoCurtainView;

/**
 * 循环弹幕效果
 */
public class CurtainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);
        final KuwoCurtainView curtain = (KuwoCurtainView) findViewById(R.id.curtain);
        final KuwoCurtainView.CurtainViewAdapter curtainViewAdapter = new KuwoCurtainView.CurtainViewAdapter() {
            @Override
            public int getItemCount() {
                return 100;
            }

            @Override
            public View getItem(View view, int pos) {
                if (view == null) {
                    view = View.inflate(CurtainActivity.this, R.layout.item_view, null);
                } else {
                }
                TextView tv_pos = (TextView) view.findViewById(R.id.tv_pos);
                tv_pos.setText("pos==" + pos);
                return view;
            }

            @Override
            public long getItemBottemStayDuration(int pos) {
                return 1000 * (pos%5+1);
            }
        };
        curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                curtain.showNext();
                curtain.setAdapter(curtainViewAdapter);
            }
        });

//        curtain.setAdapter(curtainViewAdapter);

    }
}
