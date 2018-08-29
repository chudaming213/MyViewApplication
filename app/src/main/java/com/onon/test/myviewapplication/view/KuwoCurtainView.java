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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cdm on 2018/8/24.
 * 自动滚动的弹幕
 *
 *  对orientationMode取值{ HORIZONTAL，VERTICAL } 改变 滚动方向
 *  改变speedFactor改变 滚动速度
 */

public class KuwoCurtainView extends FrameLayout {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
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

    //速度因子 速度与之成正比
    private int speedFactor = 3;

    //水平弹幕or 垂直弹幕
    private int orientationMode = VERTICAL;


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
        if (!animItems()) {
            if (curtainViewAdapter != null) {
                postDelayed(delayShowNext, curtainViewAdapter.getItemStayOutDuration(mCurPos));
            }
        }
    }
    /**
     * @return childcount>0  返回 true 否则返回false
     */
    private boolean animItems() {
        if (getChildCount() > 0) {
            View bottomView = getChildAt(getChildCount() - 1);
            if (orientationMode == VERTICAL) {
                //垂直的情况
                if (bottomView.getY() + bottomView.getHeight() > getHeight()) {
                    //每次刷新的更新所有子视图位置
                    for (int i = 0; i < getChildCount(); i++) {
                        getChildAt(i).setTranslationY(getChildAt(i).getTranslationY() - speedFactor);
                    }
                    //尝试回收 第一个可视的条目
                    View firstView = getChildAt(0);
                    if (firstView.getY() + firstView.getHeight() < 0) {
                        removeView(firstView);
                        itemManager.storeItem(firstView);
                    }
                    //不断刷新界面
                    postInvalidateOnAnimation();
                } else {
                    tryAddNextItem();
                }
            } else {
                //水平的情况
                if (bottomView.getX() + bottomView.getWidth() > getWidth()) {
                    //每次刷新的更新所有子视图位置
                    for (int i = 0; i < getChildCount(); i++) {
                        getChildAt(i).setTranslationX(getChildAt(i).getTranslationX() - speedFactor);
                    }
                    //尝试回收 第一个可视的条目
                    View firstView = getChildAt(0);
                    if (firstView.getX() + firstView.getWidth() < 0) {
                        removeView(firstView);
                        itemManager.storeItem(firstView);
                    }
                    //不断刷新界面
                    postInvalidateOnAnimation();
                } else {
                    tryAddNextItem();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 尝试展示下一个
     */
    private void tryAddNextItem() {
        if (curtainViewAdapter != null) {
            postDelayed(delayShowNext, curtainViewAdapter.getItemStayOutDuration(mCurPos));
        } else {
            removeCallbacks(delayShowNext);
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (orientationMode == VERTICAL) {
            int layerSave = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            boolean drawChild = super.drawChild(canvas, child, drawingTime);
            canvas.drawRect(0, 0, getWidth(), drawSize, mPaint);
            canvas.restoreToCount(layerSave);
            return drawChild;
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    /**
     * 展示下一个
     */
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

    /**
     * 添加view 可以水平添加和垂直添加
     *
     * @param item
     */
    private void addItem(View item) {
        if (item == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) item.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (orientationMode == VERTICAL) {
            item.setX(0);
            item.setY(getHeight());
        } else {
            item.setX(0);
            item.setY(0);
            item.setTranslationX(getWidth());
            if (item instanceof TextView) {
                CharSequence text = ((TextView) item).getText();
                layoutParams.width = (int) ((TextView) item).getPaint().measureText(text, 0, text.length());
            }
        }
        addView(item, layoutParams);
    }

    private void release() {
        removeCallbacks(delayShowNext);
        itemManager.clearStorage();
    }

    @Override
    protected void onDetachedFromWindow() {
        release();
        super.onDetachedFromWindow();
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
         * @return 条目在可视区外部持续等待多久再滚动浮出
         */
        public abstract long getItemStayOutDuration(int pos);
    }

    public int getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(int speedFactor) {
        this.speedFactor = speedFactor;
    }

    public int getOrientationMode() {
        return orientationMode;
    }

    public void setOrientationMode(int orientationMode) {
        this.orientationMode = orientationMode;
        setAdapter(curtainViewAdapter);
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
