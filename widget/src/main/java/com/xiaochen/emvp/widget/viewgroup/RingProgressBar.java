package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xiaochen.emvp.base.utils.DensityUtil;


/**
 * @author zlc
 * @created 26/06/2018
 * @desc
 */
public class RingProgressBar extends View{

    private Paint mPaint;
    private float mRingRadius;
    private int mRingColor;
    private int mCenterX;
    private int mCenterY;
    private RectF mRectF;

    public RingProgressBar(Context context) {
        this(context,null);
    }

    public RingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRingRadius = dp2px(44);
        initPaint();
    }

    private void initPaint() {

        //初始化圆环画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(8));

        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //当圆环宽高为wrap_content 给他指定一个默认的大小
        int w = (int) (mRingRadius * 2 + dp2px(8));
        int width = widthMode == MeasureSpec.AT_MOST ? w : widthSize;
        int height = heightMode == MeasureSpec.AT_MOST ? w : heightSize;

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //平移坐标系到中心点
        canvas.translate(mCenterX,mCenterY);
        mPaint.setColor(mRingColor);
        //两种思路 画圆 或者画圆环
        canvas.drawCircle(0,0,mRingRadius,mPaint);

        //或者画圆环
//        float left = -mRingRadius;
//        float top = -mRingRadius;
//        float right = mRingRadius;
//        float bottom = mRingRadius;
//        mRectF.set(left,top,right,bottom);
//        canvas.drawArc(mRectF,0,360,false,mPaint);

    }

    public int dp2px(float dpValue) {
        return DensityUtil.dp2px(getContext(),dpValue);
    }

    public void setRingRadius(float ringRadius) {
        mRingRadius = ringRadius;
    }

    public void setRingColor(int ringColor) {
        mRingColor = ringColor;
        invalidate();
    }
}
