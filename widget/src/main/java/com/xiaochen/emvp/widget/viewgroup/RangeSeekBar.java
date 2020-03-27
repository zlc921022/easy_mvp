package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xiaochen.emvp.base.utils.DensityUtil;
import com.xiaochen.emvp.widget.R;

/**
 * @author zlc
 * @created 01/06/2018
 * @desc
 * 自定义游标卡尺
 */
public class RangeSeekBar extends ViewGroup implements ThumbImageView.OnScrollListener{

    private ThumbImageView mLeftIcon;
    private ThumbImageView mRightIcon;
    private Paint mPbPaint;
    private Paint mRulerPaint;

    private int mRulerMax = 100;

    //刻度尺进度条的高度
    private int mPbHeight;
    //刻度长线的高度
    private float mLineMaxHeight;
    //刻度短线的高度
    private float mLineMinHeight;
    //每一小份刻度的宽度
    private float mPartWidth;
    //图片左右最小最大值
    private int mLeftMin;
    private int mRightMax;
    //刻度的左右间距
    private int mLeftMargin;
    private int mRightMargin;
    private Paint mValuePaint;
    //左右两边图片id
    private static final int LEFT_ICON_ID = 0x166666;
    private static final int RIGHT_ICON_ID = 0x166667;
    //文字大小
    private float mTextSize;
    //进度值的背景图片高度
    private int mValueBitmapHeight;
    //y轴上偏差值 防止高度太紧凑
    private int mOffsetY = dip2px(4);
    //控件的宽度
    private  int  mViewWidth;

    public RangeSeekBar(Context context) {
        this(context,null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundDrawable(new BitmapDrawable());
        addChildViews(context);
        initPaints();
    }

    private void addChildViews(Context context) {

        mLeftIcon = new ThumbImageView(context);
        mLeftIcon.setImageResource(R.drawable.rod_handshank_butten);
        mLeftIcon.setRangeSeekBar(this);
        mLeftIcon.setId(LEFT_ICON_ID);
        mLeftIcon.setOnScrollListener(this);

        mRightIcon = new ThumbImageView(context);
        mRightIcon.setImageResource(R.drawable.rod_handshank_butten);
        mRightIcon.setRangeSeekBar(this);
        mRightIcon.setId(RIGHT_ICON_ID);
        mRightIcon.setOnScrollListener(this);

        this.addView(mLeftIcon);
        this.addView(mRightIcon);

        mLeftMargin = dip2px(16);
        mRightMargin = dip2px(16);

        mValueBitmapHeight = dip2px(23);
        mPbHeight = dip2px(8);
        mLineMaxHeight = dip2px(26);
        mLineMinHeight = dip2px(14);
        mTextSize = dip2px(10);
    }

    private void initPaints() {

        //初始化进度条画笔
        mPbPaint = new Paint();
        mPbPaint.setAntiAlias(true);
        mPbPaint.setColor(Color.GRAY);

        //初始化刻度尺画笔
        mRulerPaint = new Paint();
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setColor(Color.GRAY);
        mRulerPaint.setStrokeWidth(1);
        mRulerPaint.setTextSize(mTextSize);
        mRulerPaint.setTextAlign(Paint.Align.CENTER);

        //初始化刻度值画笔
        mValuePaint = new Paint();
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(mTextSize);
    }

    public int dip2px(float dpValue){
        return DensityUtil.dp2px(getContext(),dpValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量所有子控件
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        //得到当前控件的测量模式和测量大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //wrap_content
        int width = widthSize;
        int height = dip2px(120);

        width = widthMode == MeasureSpec.AT_MOST ? width : widthSize;
        height = heightMode == MeasureSpec.AT_MOST ? height : heightSize;

        setMeasuredDimension(width,height);

        this.mViewWidth = width;
        mLeftMin = mLeftMargin;
        mRightMax = width - mRightMargin;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            int left = 0;
            //左右可以拖动的图片距上面的高度=进度条的高度 + 最长刻度线的高度 + 刻度值字体大小的高度
            // + 拖动显示刻度值背景图片的高度 + 偏移量
            int top = (int) (mPbHeight + mLineMaxHeight + mTextSize
                    + mValueBitmapHeight + mOffsetY);
            int right = 0;
            //距底部的高度 = top + 图片的高度 - 距离底部的padding值
            int bottom = top + cHeight - getPaddingBottom();
            if(i == 0){
                left = mLeftMargin + getPaddingLeft();
                right = left + cWidth;
            }else if(i == 1){
                left = (mViewWidth - mRightMargin - cWidth - getPaddingRight());
                right = left + cWidth;
            }
            childView.layout(left,top,right,bottom);
        }

        int leftWidth = mLeftIcon.getMeasuredWidth();
        int rightWidth = mRightIcon.getMeasuredWidth();

        mPartWidth = (mViewWidth - (mLeftMargin + leftWidth / 2)
                - (mRightMargin + rightWidth / 2)) /  (float) mRulerMax;

        mLeftIcon.setLeftRightLimit(mLeftMargin,mRightMax);
        mRightIcon.setLeftRightLimit(mLeftMargin,mRightMax);

        mLeftIcon.setCenterX(mLeftMargin + leftWidth / 2);
        mRightIcon.setCenterX(mRightMax - rightWidth / 2 );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgressBar(canvas);

        if(mLeftIcon.isMoving()){
            drawRulerValue(canvas,mLeftIcon);
        }else if(mRightIcon.isMoving()){
            drawRulerValue(canvas,mRightIcon);
        }

        drawRuler(canvas);
    }

    private void drawRulerValue(Canvas canvas,ThumbImageView imageView) {
        int centerX = imageView.getCenterX();
        Bitmap valueBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rod_place_icon);
        int top = 0;
        canvas.drawBitmap(valueBitmap,centerX - valueBitmap.getWidth() / 2,top,mValuePaint);

        canvas.drawText(String.valueOf(getValue(imageView)),centerX,top + valueBitmap.getHeight() / 2 + dip2px(3),mValuePaint);
    }

    private  int getValue(ThumbImageView imageView){
        //todo 这里只是计算了100之多少的值，需要自行转换成刻度上的值
        return 100 * (imageView.getCenterX() - mLeftMin - imageView.getWidth() / 2)
                / (mRightMax - mLeftMin - imageView.getWidth());
    }

    //画进度条
    private void drawProgressBar(Canvas canvas) {
        //画进度条
        mPbPaint.setColor(Color.GRAY);
        int left = 0;
        int top = (int) (mLineMaxHeight + mTextSize + mValueBitmapHeight + mOffsetY);
        int right = mViewWidth;
        int bottom = top + mPbHeight;
        Rect rect = new Rect(left ,top,right,bottom);
        canvas.drawRect(rect, mPbPaint);

        //游标卡尺拨动过程中 实时进度条的绘制
        mPbPaint.setColor(Color.YELLOW);
        rect = new Rect(mLeftIcon.getCenterX(),top,mRightIcon.getCenterX(),bottom);
        canvas.drawRect(rect, mPbPaint);
    }

    //画刻度尺和刻度值
    private void drawRuler(Canvas canvas) {

        for (int i = 0; i <= mRulerMax; i+=2) {

            float startX = mLeftMargin + mLeftIcon.getMeasuredWidth()  / 2 + i * mPartWidth;
            float startY,stopY;
            if(i % 10 == 0){
                mRulerPaint.setColor(ContextCompat.getColor(getContext(),R.color.color_333));
                startY = mValueBitmapHeight + mTextSize + mOffsetY;
                stopY = startY + mLineMaxHeight;
                canvas.drawText(String.valueOf(i),startX,startY - mOffsetY,mRulerPaint);
            }else{
                startY = mValueBitmapHeight + (mLineMaxHeight - mLineMinHeight) + mTextSize + mOffsetY;
                stopY = startY + mLineMinHeight;
            }
            mRulerPaint.setColor(ContextCompat.getColor(getContext(),R.color.color_666));
            canvas.drawLine(startX,startY,startX,stopY,mRulerPaint);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    public void onValue(View v,int value) {
        switch (v.getId()){
            case LEFT_ICON_ID:
                Log.e("left onValue",value+"");
                break;
            case RIGHT_ICON_ID:
                Log.e("right onValue",value+"");
                break;
        }
    }
}
