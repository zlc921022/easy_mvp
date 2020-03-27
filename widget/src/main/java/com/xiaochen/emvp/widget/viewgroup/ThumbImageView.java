package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * @author zlc
 * @created 01/06/2018
 * @desc
 * 手指滑动ImageView
 */
public class ThumbImageView extends ImageView{

    private boolean mIsMoving;      //游标是否正在移动
    private int mCenterX;           //游标的中心位置
    private Rect mRect;
    private int mWidth;             //图片宽度
    private int mLeftMin;
    private int mRightMax;
    private RangeSeekBar mRangeSeekBar;

    public ThumbImageView(Context context) {
        this(context,null);
    }

    public ThumbImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThumbImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isMoving() {
        return mIsMoving;
    }

    public void setMoving(boolean moving) {
        mIsMoving = moving;
    }

    public void setLeftRightLimit(int leftMin,int rightMax){
        this.mLeftMin = leftMin;
        this.mRightMax = rightMax;
    }

    public void setRangeSeekBar(RangeSeekBar rangeSeekBar) {
        mRangeSeekBar = rangeSeekBar;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mRect = new Rect(left,top,right,bottom);
    }

    private int mDownX = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                mDownX = (int) event.getX();
                mIsMoving=false;
                break;
            case MotionEvent.ACTION_MOVE:
                mIsMoving = true;
                int moveX = (int) event.getX();
                int distanceX = moveX - mDownX;
                int left = mRect.left + distanceX;
                int right = mRect.right + distanceX;
                setCenterX((left + right) / 2);
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                mIsMoving = false;
                mRangeSeekBar.postInvalidate();
                break;
            default:
        }
        return true;
    }

    /**
     * 设置中心位置，不超过左右的limit，就刷新整个控件，并且回调onThumbChange()
     * @param centerX
     */
    public void setCenterX(int centerX) {

        int left = centerX - mWidth / 2;
        int right = centerX + mWidth / 2;

        if(centerX < mLeftMin + mWidth / 2){
            Log.e("setCenterX",centerX+" : "+mLeftMin);
            left = mLeftMin;
            right = left + mWidth;
        }

        if(centerX > mRightMax - mWidth / 2){
            Log.e("setCenterX",centerX+" : "+mRightMax);
            left = mRightMax - mWidth;
            right = left + mWidth;
        }

        this.mCenterX = (left + right) / 2;

        if(left != mRect.left || right != mRect.right){
            mRect.union(left,mRect.top,right,mRect.bottom);
            layout(left, mRect.top, right, mRect.bottom);
            mRangeSeekBar.postInvalidate();

            if(mOnScrollListener != null){
                int value = 100 * (mCenterX - mLeftMin - mWidth / 2) / (mRightMax - mLeftMin - mWidth);
                mOnScrollListener.onValue(this,value);
            }
        }
    }

    public int getCenterX() {
        return mCenterX;
    }

    //滑动监听接口
    public interface OnScrollListener{
        void onValue(View v, int value);
    }

    private  OnScrollListener mOnScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }
}
