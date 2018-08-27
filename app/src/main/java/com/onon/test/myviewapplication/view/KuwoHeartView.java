package com.onon.test.myviewapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.onon.test.myviewapplication.R;

import java.util.Random;

/**
 * Created by cdm on 2018/8/17.
 * 直播页面许愿飘心控件
 */

public class KuwoHeartView extends View {
    //飘心路线数量
    int heartCount = 3;
    int heartResId = R.mipmap.heart_red_small;
    private Bitmap bitmap;
    private Paint paint;
    private Random random;
    private PathCompute pathComputes[];

    public KuwoHeartView(Context context) {
        super(context);
        init(context);
    }

    public KuwoHeartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KuwoHeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        random = new Random();
        pathComputes = new PathCompute[heartCount];
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), heartResId);
        }
        for (int i = 0; i < pathComputes.length; i++) {
            PathCompute pathCompute = pathComputes[i];
            if (pathCompute == null) {
                pathCompute = new PathCompute();
                pathCompute.setStartPoint(new Point((getWidth() / 2), getHeight() - getPaddingBottom()));
                pathCompute.setEndPoint(new Point((getWidth() / (pathComputes.length + 1) * (i + 1)), getPaddingTop()));
                pathCompute.init();
                pathComputes[i] = pathCompute;
            }
            //先计算产生位置相关的一系类参数，然后分别获取
            pathCompute.compute();
            canvas.save();
            canvas.rotate(pathCompute.degrees, pathCompute.curX, pathCompute.curY);
            paint.setAlpha(pathCompute.alpha);
            canvas.scale(pathCompute.scale, pathCompute.scale, pathCompute.curX, pathCompute.curY);
            canvas.drawBitmap(bitmap, pathCompute.curX, pathCompute.curY, paint);
            canvas.restore();
        }
        postInvalidateOnAnimation();
    }

    /**
     * 产生路径，并且计算当前位置的类
     */
    private class PathCompute {
        Path path;
        Point startPoint;
        Point endPoint;
        private float length;
        private float curX;
        private float curY;
        private float curTanX;
        private float curTanY;
        private float[] pos;
        private float[] tan;
        private float distance;
        private PathMeasure pathMeasure;
        private float degrees;
        private float scale = 0.0f;
        private int alpha = 255;
        private int speed = random.nextInt(3);

        public void init() {
            resetPath(startPoint, endPoint);
            pathMeasure = new PathMeasure(path, false);
            pos = new float[2];
            tan = new float[2];
        }

        private void compute() {
            length = pathMeasure.getLength();
            pathMeasure.getPosTan(distance, pos, tan);
            curX = pos[0];
            curY = pos[1];
            curTanX = tan[0];
            curTanY = tan[1];
            degrees = (float) (Math.atan2(curTanX, curTanY) * 180.0 / Math.PI) + 180;
            distance += 2 + speed;

            if (distance > length) {
                distance = 0;
                resetPath(startPoint, endPoint);
                speed = random.nextInt(3);
                pathMeasure = new PathMeasure(path, false);
                length = pathMeasure.getLength();
                alpha = 255;
            }
            //快飘到顶部时，透明度改变
            if (distance > (length * 3 / 5)) {
                alpha = 255 - (int) (255 * (distance - (length * 3 / 5)) / (length * 2 / 5));
            }
            //刚开始要从小变大
            if (distance < (length / 5)) {
                scale = distance / (length / 5);
            }
        }

        private void resetPath(Point startPoint, Point endPoint) {
            if (path == null) {
                path = new Path();
            } else {
                path.reset();
            }
            float ran = random.nextFloat();
            if (ran * 10 % 2 == 0) {
                ran = ran * -1;
            }
            path.moveTo(startPoint.x, startPoint.y);
            int centerX = (endPoint.x + startPoint.x) / 2;
            int centerY = (endPoint.y + startPoint.y) / 2;
            int ctrlX = (centerX + startPoint.x) / 2;
            int ctrlY = (centerY + startPoint.y) / 2;
            float ranDis = ran * getWidth() / 10;
            path.quadTo(ctrlX + ranDis, ctrlY + ranDis, centerX, centerY);
            ctrlX = (endPoint.x + centerX) / 2;
            ctrlY = (endPoint.y + centerY) / 2;
            path.quadTo(ctrlX - ranDis, ctrlY - ranDis, endPoint.x, endPoint.y);
        }

        public void setStartPoint(Point startPoint) {
            this.startPoint = startPoint;
        }

        public void setEndPoint(Point endPoint) {
            this.endPoint = endPoint;
        }
    }

    public void setHeartResId(int heartResId) {
        this.heartResId = heartResId;
    }

}
