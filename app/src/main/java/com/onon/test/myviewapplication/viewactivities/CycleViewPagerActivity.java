package com.onon.test.myviewapplication.viewactivities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.onon.test.myviewapplication.R;
import com.onon.test.myviewapplication.cycleviewpager.AutoScrollViewPager;

public class CycleViewPagerActivity extends AppCompatActivity {
    private int[] mImagesSrc = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_view_pager);
        AutoScrollViewPager carouselView = (AutoScrollViewPager) findViewById(R.id.carouselView);
        carouselView.setAdapter(new AutoScrollViewPager.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                ImageView imageView = new ImageView(CycleViewPagerActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setBackgroundColor(Color.RED);
                imageView.setImageResource(mImagesSrc[position]);
                return imageView;
            }

            @Override
            public int getCount() {
                return mImagesSrc.length;
            }
        });
    }
}
