package com.xiaochen.emvp.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author zlc
 * @created 27/07/2018
 * @desc
 */
public class QQBezierView extends TextView{

    private int mWidth;
    private int mHeight;
    private DragStickyView mDragView;

    public QQBezierView(Context context) {
        this(context,null);
    }

    public QQBezierView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragView = new DragStickyView(getContext());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获得根View
        View rootView = getRootView();
        //获得触摸位置在全屏所在位置
        float mRawX = event.getRawX();
        float mRawY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                int[] cLocation = new int[2];
                getLocationOnScreen(cLocation);
                if (rootView instanceof ViewGroup) {
                    //初始化拖拽时显示的View
                    mDragView.setQQBezierView(this);
                    //设置固定圆和拖拽圆的圆心坐标
                    mDragView.setStickyPoint(cLocation[0] + mWidth / 2, cLocation[1] + mHeight / 2, mRawX, mRawY);
                    //获得缓存的bitmap，滑动时直接通过drawBitmap绘制出来
                    setDrawingCacheEnabled(true);
                    Bitmap bitmap = getDrawingCache();
                    if (bitmap != null && mDragView.getParent() == null) {
                        mDragView.setCacheBitmap(bitmap);
                        //将DragView添加到RootView中，这样就可以全屏滑动了
                        ((ViewGroup) rootView).addView(mDragView);
                        setVisibility(INVISIBLE);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                if(mDragView != null){
                    mDragView.updateDragCenterXY(mRawX,mRawY);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                if(mDragView != null){
                    //手抬起时来判断各种情况
                    mDragView.touchUp();
                }
                break;
        }
        return true;
    }

    public interface onDragStatusListener {
        void onDrag();
        void onMove();
        void onDismiss();
    }

    public onDragStatusListener mOnDragListener;
    public void setOnDragListener(onDragStatusListener onDragListener) {
        mOnDragListener = onDragListener;
    }
}
