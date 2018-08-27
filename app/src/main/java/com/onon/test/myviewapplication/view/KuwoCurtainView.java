package com.onon.test.myviewapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by cdm on 2018/8/24.
 * 自动向上滚动的弹幕
 */

public class KuwoCurtainView extends RelativeLayout {
    private Paint mPaint;
    //渐变颜色
    private int[] mGradientColors = {0xffffffff, 0x00000000};
    //渐变位置
    private float[] mGradientPosition = new float[]{0, 1};
    private int drawSize = 300;
    private int mCurPos;
    private Runnable delayShowNext = new Runnable() {
        @Override
        public void run() {
            showNext();
        }
    };
    private CurtainViewAdapter curtainViewAdapter;
    private ItemManager itemManager;

    public KuwoCurtainView(Context context) {
        super(context);
        init(context, null);
    }

    public KuwoCurtainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public KuwoCurtainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mPaint.setShader(new LinearGradient(0, 0, 0, drawSize, mGradientColors, mGradientPosition, Shader.TileMode.CLAMP));
        itemManager = new ItemManager();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getChildCount() > 0) {
            View bottomView = getChildAt(getChildCount() - 1);
            int height = getHeight();
            int height1 = bottomView.getHeight();
            float y = bottomView.getY();
            if (y + height1 > height) {
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).setTranslationY(getChildAt(i).getTranslationY() - 3);
                }
                View firstView = getChildAt(0);
                if (firstView.getY() + firstView.getHeight() < 0) {
                    removeView(firstView);
                    itemManager.storeItem(firstView);
                }
                postInvalidateOnAnimation();
            } else{
                if (curtainViewAdapter != null) {
                    postDelayed(delayShowNext, curtainViewAdapter.getItemBottemStayDuration(mCurPos));
                } else {
                    removeCallbacks(delayShowNext);
                }
            }
        } else {
            if (curtainViewAdapter != null) {
                postDelayed(delayShowNext, curtainViewAdapter.getItemBottemStayDuration(mCurPos));
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int layerSave = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        boolean drawChild = super.drawChild(canvas, child, drawingTime);
        canvas.drawRect(0, 0, getWidth(), drawSize, mPaint);
        canvas.restoreToCount(layerSave);
        return drawChild;
    }

    public void showNext() {
        if (curtainViewAdapter != null) {
            if (curtainViewAdapter.getItemCount() < 0) {
                return;
            }
            View item = curtainViewAdapter.getItem(itemManager.getAnItem(mCurPos + 1), mCurPos);
            addItem(item);
            mCurPos = mCurPos + 1 == curtainViewAdapter.getItemCount() ? 0 : mCurPos + 1;
        }
    }

    private void addItem(View item) {
        item.setX(0);
        item.setY(getHeight());
        addView(item);
    }

    public void release() {
        removeCallbacks(delayShowNext);
        itemManager.clearStorage();
    }

    /**
     * item 复用管理类
     */
    private class ItemManager {
        ArrayList<View> storageItems = new ArrayList<>();

        private View getAnItem(int pos) {
            View tem;
            if (storageItems.size() > 0) {
                tem = storageItems.get(0);
                storageItems.remove(tem);
            } else {
                tem = curtainViewAdapter.getItem(null, pos);
            }
            return tem;
        }

        private void storeItem(View view) {
            storageItems.add(view);
        }

        public void clearStorage() {
            storageItems.clear();
        }
    }


    public abstract static class CurtainViewAdapter {
        public abstract int getItemCount();

        /**
         * @param view 复用的view
         * @param pos
         * @return
         */
        public abstract View getItem(View view, int pos);

        /**
         * @param pos
         * @return 条目在底部持续等待多久再向上滚动浮出
         */
        public abstract long getItemBottemStayDuration(int pos);
    }

    public CurtainViewAdapter getAdapter() {
        return curtainViewAdapter;
    }

    public void setAdapter(CurtainViewAdapter curtainViewAdapter) {
        removeCallbacks(delayShowNext);
        removeAllViews();
        itemManager.clearStorage();
        mCurPos = 0;
        this.curtainViewAdapter = curtainViewAdapter;
        invalidate();
    }
}
