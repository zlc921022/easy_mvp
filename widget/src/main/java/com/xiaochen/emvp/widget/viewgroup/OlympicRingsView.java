package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.xiaochen.emvp.base.utils.DensityUtil;


/**
 * @author zlc
 * @created 26/06/2018
 * @desc
 * 自定义奥运五环
 */
public class OlympicRingsView extends ViewGroup{


    //圆环颜色定义
    private int[] mColors = {Color.BLUE,Color.BLACK,
            Color.RED,Color.YELLOW,Color.GREEN};
    private Paint mPaint;
    private int mCenterX;
    private int mCenterY;
    private int mRingMaxHeight;
    private String chineseDesc = "同一个世界，同一个梦想";
    private String englishDesc = "One World，One Dream";
    private int mCNDescHeight;
    private int mENDescHeight;
    //文字间距
    private int textSpacing = dp2px(8);

    public OlympicRingsView(Context context) {
        this(context,null);
    }

    public OlympicRingsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        setBackgroundDrawable(new BitmapDrawable());
        int margin = dp2px(8);
        //添加5个圆环
        for (int i = 0; i < 5; i++) {
            MarginLayoutParams lp = new MarginLayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.setMargins(margin, margin, 0, margin);
            addView(new RingProgressBar(context, attrs),lp);
        }
    }

    private void initPaint() {

        //初始化文字画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setTextSize(dp2px(14));
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextAlign(Paint.Align.CENTER);

        //计算中文描述的高度
        Rect cnRect = new Rect();
        mPaint.getTextBounds(chineseDesc,0,chineseDesc.length(),cnRect);
        mCNDescHeight = cnRect.height();

        //计算英文描述的高度
        Rect enRect = new Rect();
        mPaint.getTextBounds(englishDesc,0,englishDesc.length(),enRect);
        mENDescHeight = enRect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //wrap_content计算宽高
        int width = 0;
        int height = 0;

        //计算上面 下面的宽高
        int tWidth = 0;
        int bWidth = 0;
        int tHeight = 0;
        int bHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            //测量每一个子孩子的宽高
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth() + lp.leftMargin;
            int cHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if(i < 3){
                tWidth += cWidth;
                tHeight = cHeight;
            }else{
                bWidth += cWidth;
                bHeight = tHeight / 2 + cHeight;
            }
        }

        mRingMaxHeight = Math.max(tHeight,bHeight);
        width = Math.max(tWidth,bWidth);
        //要加上文字的高度和上下间距
        height = Math.max(tHeight,bHeight) + mENDescHeight + mCNDescHeight+ textSpacing * 2;

        //wrap_content 取子View测量的宽高
        int measureWidth = widthMode == MeasureSpec.AT_MOST ? width : widthSize;
        int measureHeight  = heightMode == MeasureSpec.AT_MOST ? height : heightSize;

        setMeasuredDimension(measureWidth,measureHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //上面下面子view距离左边的间距
        int tLeft = 0;
        int bLeft = 0;
        int top = 0;
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            if(child.getVisibility() == View.GONE){
                continue;
            }

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();

            if(i < 3){

                if(i == 0){
                    tLeft = lp.leftMargin;
                }else {
                    tLeft = tLeft + cWidth + lp.leftMargin;
                }
                top = lp.topMargin;
                child.layout(tLeft, top, tLeft + cWidth, top + cHeight);

            }else {

                if( i == 3){
                    bLeft = (int) (0.55f * cWidth + lp.leftMargin);
                }else{
                    bLeft = bLeft + cWidth + lp.leftMargin;
                }
                top = (int) (lp.topMargin + 0.5 * cHeight);
                child.layout(bLeft,top,bLeft + cWidth,top + cHeight);

            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
        for (int i = 0; i < getChildCount(); i++) {
            RingProgressBar progressBar = (RingProgressBar) getChildAt(i);
            progressBar.setRingColor(mColors[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画文字
        float cnY = mRingMaxHeight + textSpacing;
        float enY = cnY + mCNDescHeight+ textSpacing;
        canvas.drawText(chineseDesc,mCenterX,cnY,mPaint);
        canvas.drawText(englishDesc,mCenterX, enY,mPaint);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    public int dp2px(float dpValue){
        return DensityUtil.dp2px(getContext(),dpValue);
    }
}
