package com.onon.test.myviewapplication.viewactivities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.onon.test.myviewapplication.R;
import com.onon.test.myviewapplication.view.svgblog.AnimatedSvgView;
import com.onon.test.myviewapplication.view.svgblog.GAStudioPath;

public class SVGActivity extends Activity {

    private float mInitialLogoOffset;
    private Button mReset;
    private AnimatedSvgView mAnimatedSvgView;
    private ImageView mSubtitleView;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);

        mInitialLogoOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49,
                getResources().getDisplayMetrics());

        mReset = (Button) findViewById(R.id.reset);
        mAnimatedSvgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        mSubtitleView = (ImageView) findViewById(R.id.wt_logo_bottom);
        mSubtitleView.setVisibility(View.INVISIBLE);
        ViewHelper.setTranslationY(mAnimatedSvgView, mInitialLogoOffset);

        mAnimatedSvgView.setGlyphStrings(GAStudioPath.STUDIO_PATH);

        // ARGB values for each glyph
        mAnimatedSvgView.setFillPaints(
                new int[] {
                        210
                },
                new int[] {
                        231
                },
                new int[] {
                        84
                },
                new int[] {
                        84
                });

        // �����ߵ���ɫ
        int traceColor = Color.argb(255, 231, 84, 84);
        int[] traceColors = new int[2]; // 4 glyphs
        // ��Ե�ߵ���ɫ
        int residueColor = Color.argb(80, 231, 84, 84);
        int[] residueColors = new int[2]; // 4 glyphs

        // Every glyph will have the same trace/residue
        for (int i = 0; i < traceColors.length; i++) {
            traceColors[i] = traceColor;
            residueColors[i] = residueColor;
        }
        mAnimatedSvgView.setTraceColors(traceColors);
        mAnimatedSvgView.setTraceResidueColors(residueColors);

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnimatedSvgView.reset();
                ViewHelper.setTranslationY(mAnimatedSvgView, mInitialLogoOffset);
                mSubtitleView.setVisibility(View.INVISIBLE);

                animateLogo();
            }
        });

        mAnimatedSvgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == AnimatedSvgView.STATE_FILL_STARTED) {
                    ViewHelper.setAlpha(mSubtitleView, 0);

                    mSubtitleView.setVisibility(View.VISIBLE);

                    AnimatorSet set = new AnimatorSet();
                    Interpolator interpolator = new DecelerateInterpolator();
                    ObjectAnimator a1 = ObjectAnimator.ofFloat(mAnimatedSvgView, "translationY", 0);
                    ObjectAnimator a2 = ObjectAnimator.ofFloat(mSubtitleView, "alpha", 1);
                    a1.setInterpolator(interpolator);
                    set.setDuration(1000).playTogether(a1, a2);
                    set.start();
                }
            }
        });
    }

    private void animateLogo() {
        mAnimatedSvgView.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateLogo();
            }
        }, 1000);
    }
}
