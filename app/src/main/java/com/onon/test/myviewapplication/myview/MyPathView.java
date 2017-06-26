package com.onon.test.myviewapplication.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class MyPathView extends View {

    private Paint paint = new Paint();
    private final Path path = new Path();
    private float length;

    public MyPathView(Context context) {
        super(context);
        init();
    }

    public MyPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public void init() {
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(15);
        path.moveTo(200, 200);
        path.lineTo(300, 300);
        path.lineTo(500, 100);
        PathMeasure pm = new PathMeasure(path, false);
        length = pm.getLength();
    }

    int a = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setPathEffect(new DashPathEffect(new float[]{a, length - a < 0 ? 0 : length - a}, 0));
        canvas.drawPath(path, paint);
        if (a < length) {
            a += 20;
            postInvalidateOnAnimation();
        } else {
            a = 0;
        }
    }
}
