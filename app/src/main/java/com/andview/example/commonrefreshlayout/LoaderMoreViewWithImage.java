package com.andview.example.commonrefreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.example.R;


public class LoaderMoreViewWithImage extends MyFooterLoadViewLayout {
    private ImageView ivArrow;

    private ImageView ivSuccess;

    private TextView tvLoadMore;

    private ProgressBar progressBar;

    private int mFooterHeight;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;


    public LoaderMoreViewWithImage(Context context) {
        this(context, null);
    }

    public LoaderMoreViewWithImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaderMoreViewWithImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(
                R.dimen.refresh_header_height_twitter);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLoadMore = (TextView) findViewById(R.id.tvLoadMore);
        ivArrow = (ImageView) findViewById(R.id.ivArrow);
        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }


    @Override
    public void onLoadMore() {
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvLoadMore.setText("REFRESHING");
    }

    @Override
    public void onPrepare() {
        Log.d("TwitterRefreshHeader", "onPrepare()");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivArrow.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            ivSuccess.setVisibility(GONE);

            if (-y >= mFooterHeight) {
                tvLoadMore.setText("RELEASE TO LOAD MORE");
                if (rotated) {
                    Log.d("onMove", ">>>>>5555555555>>>>>>>>");
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateDown);
                    rotated = false;
                }

            } else {
                tvLoadMore.setText("SWIPE TO LOAD MORE");
                if (!rotated) {
                    Log.d("onMove", ">>>>>333333333333333>>>>>>>>");
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateUp);
                    rotated = true;
                }
            }


//            if (-y >= mFooterHeight) {
//                tvLoadMore.setText("RELEASE TO LOAD MORE");
//                if (!rotated) {
//                    Log.d("onMove", ">>>>>333333333333333>>>>>>>>");
//                    ivArrow.clearAnimation();
//                    ivArrow.startAnimation(rotateUp);
//                    rotated = true;
//                }
//            } else {
//                tvLoadMore.setText("SWIPE TO LOAD MORE");
//                if (rotated) {
//                    Log.d("onMove", ">>>>>5555555555>>>>>>>>");
//                    ivArrow.clearAnimation();
//                    ivArrow.startAnimation(rotateDown);
//                    rotated = false;
//                }
//            }

        }
    }

    @Override
    public void onRelease() {

        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        rotated = false;
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvLoadMore.setText("COMPLETE");
    }

    @Override
    public void onReset() {

        rotated = false;
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }
}
