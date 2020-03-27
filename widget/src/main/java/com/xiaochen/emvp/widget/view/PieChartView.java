package com.xiaochen.emvp.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;

import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.xiaochen.emvp.widget.R;

import java.text.DecimalFormat;

/**
 * @author zlc
 * @created 21/06/2018
 * @desc
 * 饼状图
 */
public class PieChartView extends View{

    private Paint mInPaint;
    private int mCenterX;
    private int mCenterY;
    private float mInRadius;
    private float mOutRadius;
    private Paint mOutPaint;
    private RectF mOutRectF;
    private Paint mWhiteCirclePaint;
    private Rect mTextRect;
    private Paint mTextPaint;

    //引入Path
    private Path mPath;
    private Path mOutPath;
    private Path mInPath;

    //画圆环角度相关
    private int mPieChartNum = 5;
    private float mScaleAngle = 360 / mPieChartNum;
    private float mDividLineWidth = 2;
    private float mDrawAngle = mScaleAngle - mDividLineWidth;
    private float mStartAngle = 0;
    // 颜色表
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636,
            0xFF800000, 0xFF808000};
    //动画相关
    private ValueAnimator mProgressAnimator;
    private float mAnimatedValue;
    public final static int DURATION = 1000;
    private Rect mPercentRect;
    //是否需要动画处理
    private boolean mIsShowAnimation;
    private RectF mRingRectF;
    private DecimalFormat mDf;

    public PieChartView(Context context) {
        super(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAttrs(attrs);
    }

    private void initPaint() {

        //内部渐变圆环
        mInPaint = new Paint();
        mInPaint.setStyle(Paint.Style.STROKE);
        mInPaint.setColor(Color.WHITE);
        mInPaint.setAlpha(153);
        mInPaint.setStrokeWidth(dp2px(15));
        mInPaint.setAntiAlias(true);

        //白色圆
        mWhiteCirclePaint = new Paint();
        mWhiteCirclePaint.setColor(Color.WHITE);
        mWhiteCirclePaint.setAntiAlias(true);

        //中间文字和百分比文字
        mTextPaint = new Paint();
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
        mTextPaint.setTextSize(dp2px(15));
        mTextPaint.setAntiAlias(true);
        mTextRect = new Rect();
        mPercentRect = new Rect();
        mRingRectF = new RectF();

        //外圆环扇形
        mOutPaint = new Paint();
        mOutPaint.setColor(Color.GREEN);
        mOutPaint.setStyle(Paint.Style.FILL);
        mOutPaint.setStrokeWidth(2);
        mOutPaint.setAntiAlias(true);
        mOutRectF = new RectF();
        //path相关
        mPath = new Path();
        mInPath = new Path();
        mOutPath = new Path();

        mDf = new DecimalFormat("0");
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PieChartView);
        mIsShowAnimation = typedArray.getBoolean(R.styleable.PieChartView_isShowAnimation, false);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

        mInRadius = dp2px(60);
        mOutRadius = dp2px(120);

//        if(mIsShowAnimation) {
//            setProgressAnimation(DURATION);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //坐标移动到中心
        canvas.translate(mCenterX,mCenterY);
        drawWhiteCircle(canvas);
        drawText(canvas);
        drawPieChart(canvas);
        //drawInRing(canvas);
    }

    //画中间白色的圆
    private void drawWhiteCircle(Canvas canvas) {
        canvas.drawCircle(0,0,mInRadius,mWhiteCirclePaint);
    }

    //画中间饼状图文字
    private void drawText(Canvas canvas) {
        String text = "饼状图";
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.getTextBounds(text,0,text.length(), mTextRect);
        int height = mTextRect.height();
        canvas.drawText(text,0,height / 2,mTextPaint);
    }

    //画透明度圆环
    private void drawInRing(Canvas canvas,float angle,float drawAngle){

//        RectF rectF = new RectF();
        mRingRectF.set(-mInRadius,-mInRadius,mInRadius,mInRadius);
        canvas.drawArc(mRingRectF,angle,drawAngle,false,mInPaint);
        //canvas.drawCircle(0,0,mInRadius + dp2px(5),mInPaint);
    }

    //画饼状扇形
    private void drawPieChart(Canvas canvas) {

        canvas.save();
        mInPath.reset();
        mOutRectF.set(-mOutRadius, -mOutRadius, mOutRadius, mOutRadius);
        mInPath.addCircle(0,0,mInRadius, Path.Direction.CW);

        for (int i = 0; i < mPieChartNum; i++) {
            mStartAngle = (i == 0) ? 0 : mStartAngle + mScaleAngle;
            if(mIsShowAnimation) {
                if (Math.min(mDrawAngle, mAnimatedValue - mStartAngle) >= 0) {
                    float drawAngle = Math.min(mDrawAngle, mAnimatedValue - mStartAngle);
                    drawPieChartRing(canvas, mColors[i], drawAngle);
                }
            }else{
                drawPieChartRing(canvas, mColors[i], mDrawAngle);
            }
            mPath.reset();
            mOutPath.reset();
        }
        canvas.restore();
    }

    //绘制饼状图圆环
    private void drawPieChartRing(Canvas canvas, int color, float drawAngle) {
        mOutPaint.setColor(color);
        mOutPath.lineTo(mOutRadius*(float) Math.cos(Math.toRadians(mStartAngle)),
                mOutRadius*(float) Math.sin(Math.toRadians(mStartAngle)));
        mOutPath.arcTo(mOutRectF, mStartAngle, drawAngle);
        // op(a,b,Path.Op.REVERSE_DIFFERENCE)  b-a的交集
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPath.op(mInPath,mOutPath, Path.Op.REVERSE_DIFFERENCE);
        }
        canvas.drawPath(mPath,mOutPaint);
        //画透明度圆环
        drawInRing(canvas,mStartAngle,drawAngle);
        //画完一段圆弧再画百分比文字
        if(drawAngle % mDrawAngle== 0) {
            drawPercentText(canvas, mStartAngle);
        }
    }

    //画百分比文字
    private void drawPercentText(Canvas canvas,float startAngle) {

        float angle = mDrawAngle / 2 + startAngle;
        //计算x,y的时候其实并不需要在不同象限单独计算  比如说 cos0 = 1  -cos（180-180） = cos 180 = -1
        float x = (float) (0.75 * mOutRadius * Math.cos(Math.toRadians(angle))) ;
        float y = (float) (0.75 * mOutRadius * Math.sin(Math.toRadians(angle))) ;
        String format = mDf.format(100 * 1.0f / mPieChartNum) + "%";
        mTextPaint.getTextBounds(format,0,format.length(),mPercentRect);
        canvas.drawText(format,x,y + mPercentRect.height() / 2,mTextPaint);
    }


    //设置进度条动画
    public void setProgressAnimation(long duration) {
        if(mProgressAnimator != null && mProgressAnimator.isRunning()){
            mProgressAnimator.cancel();
            mProgressAnimator.start();
        }else {
            mProgressAnimator = ValueAnimator.ofFloat(0, 360).setDuration(duration);
            mProgressAnimator.setInterpolator(new AccelerateInterpolator());
            mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    /**每次要绘制的圆弧角度**/
                    mAnimatedValue = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            mProgressAnimator.start();
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
