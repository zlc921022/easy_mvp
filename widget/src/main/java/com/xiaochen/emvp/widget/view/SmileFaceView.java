package com.xiaochen.emvp.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.xiaochen.emvp.widget.R;

/**
 * @author zlc
 * @created 20/06/2018
 * @desc
 * 仿土豆loading
 */
public class SmileFaceView extends View {

    private Paint mPaint;
    private int mCenterX;
    private int mCenterY;
    private Paint mLinePaint;
    private Paint mPonitPaint;
    private RectF mRectF;

    //动画相关
    private ValueAnimator animator;
    private float animatedValue;
    private static final long DURATION = 3000;

    public SmileFaceView(Context context) {
        this(context,null);
    }

    public SmileFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        //画圆环
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(8));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mRectF = new RectF();

        //画坐标轴
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setAntiAlias(true);

        //画点
        mPonitPaint = new Paint();
        mPonitPaint.setColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
        mPonitPaint.setStyle(Paint.Style.STROKE);
        mPonitPaint.setStrokeCap(Paint.Cap.ROUND);
        mPonitPaint.setStrokeWidth(10);
        mPonitPaint.setAntiAlias(true);
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

        canvas.translate(mCenterX,mCenterY);

//        drawCoordinate(canvas);

        drawCircleRing(canvas);
    }

    //画笑脸 画圆环
    private void drawCircleRing(Canvas canvas) {

        float radius = dp2px(21);
        float left = -radius;
        float top = -radius;
        float right = radius;
        float bottom = radius;
        mRectF.set(left,top,right,bottom);

        canvas.save();
        // rotate
        if (animatedValue>= 135){
            canvas.rotate(animatedValue-135);
        }

        // draw mouth
        float startAngle=0, sweepAngle=0;

        if (animatedValue < 135){
            startAngle = animatedValue + 5;
            sweepAngle = 170 + animatedValue/ 3;
        }else if (animatedValue< 270){
            startAngle = 135 + 5;
            sweepAngle = 170 + animatedValue/ 3;
        }else if (animatedValue< 630){
            startAngle = 135 + 5;
            sweepAngle = 260-(animatedValue-270)/5;
        }else if (animatedValue< 720){
            startAngle = 135-(animatedValue-630)/2 + 5;
            sweepAngle = 260-(animatedValue-270)/5;
        }else{
            startAngle = 135- (animatedValue- 630) / 2 - (animatedValue-720) / 6 +5;
            sweepAngle = 170;
        }

        //画圆弧
        canvas.drawArc(mRectF,startAngle,sweepAngle,false,mPaint);

        float pointY = -radius + dp2px(6);
        float ponitX = radius - dp2px(6);
        //笑脸右边的点
        canvas.drawPoint(ponitX,pointY,mPaint);
        //笑脸左边的点
        canvas.drawPoint(-ponitX,pointY,mPaint);

        canvas.restore();
//        canvas.drawPoints(new float[]{
//                ponitX,pointY,
//                -ponitX,pointY
//        },mPaint);

    }

    //画坐标轴
    private void drawCoordinate(Canvas canvas) {

        float startX = -mCenterX + dp2px(20);
        float stopX = mCenterX - dp2px(20);
        float startY = -mCenterY + dp2px(40);
        float stopY = mCenterY - dp2px(40);

        //画点
        canvas.drawPoint(0,0,mPonitPaint);
        canvas.drawPoint(startX - dp2px(2),0,mPonitPaint);
        canvas.drawPoint(stopX + dp2px(2),0,mPonitPaint);
        canvas.drawPoint(0,startY - dp2px(2),mPonitPaint);
        canvas.drawPoint(0,stopY + dp2px(2),mPonitPaint);

        //画横纵坐标轴
        canvas.drawLine(startX,0,stopX,0, mLinePaint);
        canvas.drawLine(0,startY,0,stopY, mLinePaint);

        //画箭头
        float arrowsStartX1 = stopX - dp2px(5);
        float arrowsStopY1 = 0 - dp2px(6);
       
        canvas.drawLine(arrowsStartX1,arrowsStopY1,stopX + dp2px(1),0, mLinePaint);
        canvas.drawLine(arrowsStartX1,-arrowsStopY1,stopX + dp2px(1),0, mLinePaint);

        float arrowsStartX2 = dp2px(6);
        float arrowsStopY2 = stopY - dp2px(5);
        canvas.drawLine(arrowsStartX2,arrowsStopY2,0,stopY + dp2px(1), mLinePaint);
        canvas.drawLine(-arrowsStartX2,arrowsStopY2,0,stopY + dp2px(1), mLinePaint);
    }

    //添加动画
    public void playAnimation(){
        if (animator !=null && animator.isRunning()){
            animator.cancel();
            animator.start();
        }else {
            animator = ValueAnimator.ofFloat(0,855).setDuration(DURATION);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatedValue = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animator.start();
        }
    }

    public  int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
