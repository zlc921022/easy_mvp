package com.xiaochen.emvp.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.xiaochen.emvp.base.utils.ChartUtils;
import com.xiaochen.emvp.base.utils.DensityUtil;
import com.xiaochen.emvp.widget.R;


/**
 * @author zlc
 * @created 07/06/2018
 * @desc
 * 自定义圆环
 */
public class CircleRingProgress extends View {

    //自定义属性
    private int mRpbDefaultColor;
    private int mRingColor;
    private int mProgressColor;
    private int mTextColor;
    private int mRingWidth;
    //外圆的半径
    private int mCircleRadius;
    private int mTextSize;
    private float mCurrentProgress;
    private float mMaxProgress;
    //最外圈圆环画笔
    private Paint mBigRingPaint;
    //中间圆环画笔
    private Paint mRingPaint;
    //圆环进度画笔
    private Paint mProgressPaint;
    //中间带刻度线的圆环画笔
    private Paint mScaleRingPaint;
    private Paint mTextPaint;

    //圆心坐标
    private int mCenterX, mCenterY;

    //圆环开始角度
    private static final float startAngle = -90;
    //圆环的总角度
    private static final float sweepAngle = 360;
    //圆环之间间距
    private int dp12 = dp2px(12);
    private int dp20 = dp2px(24);
    //当前角度
    private float mCurrentAngle;
    //最大步数
    private int mMaxStepNum = 10000;
    //控制圆环滑动的按钮
    private Paint mDrawCirclePaint;
    private Bitmap mBitmap;
    //圆环控制按钮最小，最大有效点击半径
    private int mMinValidateTouchArcRadius;
    private int mMaxValidateTouchArcRadius;
    //圆环的半径
    private float mRingRadius;
    //控制圆环进度范围大小的矩形 左上右下
    private RectF mProgressReacF;
    //控制带刻度线圆环进度范围大小的矩形 左上右下
    private RectF mScaleReacF;
    //中间文字步数
    private int mStepNum;
    //测量文字宽高
    private Rect mTextRect;
    //1弧度
    private static final double RADIAN = 180 / Math.PI;
    //是否显示控制滑动的小图标
    private boolean mIsShowControlIcon;
    //给圆环设置渐变色
    private SweepGradient mSweepGradient;


    public CircleRingProgress(Context context) {
        this(context, null);
    }

    public CircleRingProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaints();
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressBar);
        //获取自定义属性和默认值
        mRpbDefaultColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_default_color, Color.BLUE);
        mRingColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_ringColor, Color.GRAY);
        mProgressColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_ringProgressColor, Color.GREEN);
        mTextColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_textColor1, Color.BLUE);
        mRingWidth = typedArray.getDimensionPixelSize(R.styleable.RingProgressBar_rpb_ringWidth, dp2px(10));
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.RingProgressBar_rpb_circleRadius, dp2px(120));
        mRingRadius = typedArray.getDimensionPixelSize(R.styleable.RingProgressBar_rpb_ringRadius, dp2px(108));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.RingProgressBar_rpb_textSize1, dp2px(20));
        mCurrentProgress = typedArray.getFloat(R.styleable.RingProgressBar_rpb_currentProgress, 0);
        mMaxProgress = typedArray.getFloat(R.styleable.RingProgressBar_rpb_maxProgress, 100);
        mIsShowControlIcon = typedArray.getBoolean(R.styleable.RingProgressBar_rpb_isShowControlIcon, true);
        typedArray.recycle();

        mCurrentAngle = (mCurrentProgress / mMaxProgress) * sweepAngle;

    }

    private void initPaints() {

        //外面大圆画笔
        mBigRingPaint = new Paint();
        mBigRingPaint.setStrokeWidth(2);
        mBigRingPaint.setAntiAlias(true);
        mBigRingPaint.setStyle(Paint.Style.STROKE);
        mBigRingPaint.setColor(mRpbDefaultColor);

        //中间灰色圆画笔
        mRingPaint = new Paint();
        mRingPaint.setStrokeWidth(mRingWidth - dp2px(2));
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setColor(mRingColor);

        //圆环画笔
        mProgressPaint = new Paint();
        mProgressPaint.setStrokeWidth(mRingWidth);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setColor(mProgressColor);
        mProgressReacF = new RectF();

        //中间带刻度圆画笔
        mScaleRingPaint = new Paint();
        mScaleRingPaint.setStrokeWidth(dp2px(7));
        mScaleRingPaint.setAntiAlias(true);
        mScaleRingPaint.setStyle(Paint.Style.STROKE);
        mScaleRingPaint.setColor(mRpbDefaultColor);
        mScaleReacF = new RectF();

        //中间文字步数画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();

        //圆环控制按钮画笔
        mDrawCirclePaint = new Paint();
        mDrawCirclePaint.setAntiAlias(true);
        mDrawCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDrawCirclePaint.setColor(Color.BLACK);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;

        int[] colors = {Color.RED, Color.GREEN, Color.YELLOW};
        mSweepGradient = new SweepGradient(mCenterX,mCenterY, colors,null);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ring_dot);
        mBitmap = conversionBitmap(mBitmap,dp2px(30),dp2px(30));
        mMinValidateTouchArcRadius = (int) (mCircleRadius - mBitmap.getWidth() / 2 * 1.5);
        mMaxValidateTouchArcRadius = (int) mCircleRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外层圆
        drawOutCircle(canvas);
        //画中间圆
        drawMidCircle(canvas);
        //画进度
        drawRingProgress(canvas);
        //画带刻度线的圆环
        drawScaleRing(canvas);
        //画中间的数字
        drawMidNum(canvas);
        //画可以拖拽的圆
        drawDrawCircle(canvas);
    }

    //画外层圆
    private void drawOutCircle(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mBigRingPaint);
    }

    /**
     * 画中间圆
     */
    private void drawMidCircle(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mRingRadius, mRingPaint);
    }

    /**
     * 画进度
     */
    private void drawRingProgress(Canvas canvas) {
        canvas.save();
        //canvas.rotate(-90);
        float left = mCenterX - mRingRadius;
        float top = mCenterY - mRingRadius;
        float right = left + 2 * mRingRadius;
        float bottom = top + 2 * mRingRadius;
        mProgressReacF.set(left,top,right,bottom);
        mProgressPaint.setShader(mSweepGradient);
        float sweepAngle = mCurrentAngle;
        canvas.drawArc(mProgressReacF, startAngle, sweepAngle, false, mProgressPaint);
        canvas.restore();
    }

    /**
     * 画带刻度线的圆环
     */
    private void drawScaleRing(Canvas canvas) {
        float margin = mRingRadius - dp20;
        float left = mCenterX - margin;
        float top = mCenterY - margin;
        float right = left + 2 * margin;
        float bottom = top + 2 * margin;
        mScaleReacF.set(left,top,right,bottom);
        for (int i = 0; i < sweepAngle / 6; i++) {
            canvas.drawArc(mScaleReacF, startAngle + i * 6, 1f, false, mScaleRingPaint);
        }
    }

    /**
     * 画中间的数字
     */
    private void drawMidNum(Canvas canvas) {
        String value = String.valueOf(mStepNum);
        mTextPaint.getTextBounds(value, 0, value.length(), mTextRect);
        int width = mTextRect.width();//文字宽
        int height = mTextRect.height();//文字高
        canvas.drawText(value, mCenterX - width / 2, mCenterY + height / 2, mTextPaint);
    }

    //画可以拖拽的按钮
    private void drawDrawCircle(Canvas canvas){
        if(mIsShowControlIcon) {
            PointF progressPoint = ChartUtils.calcArcEndPointXY(mCenterX, mCenterY, mRingRadius,
                    mCurrentAngle, -90);
            float left = progressPoint.x - mBitmap.getWidth() / 2;
            float top = progressPoint.y - mBitmap.getHeight() / 2;
            Log.e("left", left + " top:" + top);
            canvas.drawBitmap(mBitmap, left, top, mDrawCirclePaint);
        }
    }

    //加载进度条动画
    public void setProgressAnimation(float last, float currentProgress, long duration) {
        mCurrentAngle = (currentProgress / mMaxProgress) * sweepAngle;
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, mCurrentAngle);
        progressAnimator.setDuration(duration);
        progressAnimator.setTarget(mCurrentAngle);
        progressAnimator.setInterpolator(new AccelerateInterpolator());
        progressAnimator.start();
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**每次要绘制的圆弧角度**/
                mCurrentAngle = (float) animation.getAnimatedValue();
                mStepNum = (int) (mCurrentAngle / sweepAngle * mMaxStepNum);
                postInvalidate();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (mIsShowControlIcon && (event.getAction() == MotionEvent.ACTION_MOVE || isTouchArc(x, y))) {
            // 通过当前触摸点搞到cos角度值
            float cos = computeCos(x, y);
            // 通过反三角函数获得角度值
            double angle;
            if (x < mCenterX) { // 滑动超过180度
                angle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
            } else { // 没有超过180度
                angle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
            }
            if (mCurrentAngle > 270 && angle < 90) {
                mCurrentAngle = 360;
                cos = -1;
            } else if (mCurrentAngle < 90 && angle > 270) {
                mCurrentAngle = 0;
                cos = -1;
            } else {
                mCurrentAngle = (float) angle;
            }
            mCurrentProgress = getSelectedValue();
            invalidate();
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    private int getSelectedValue() {
        return Math.round(mMaxProgress * (mCurrentAngle / sweepAngle));
    }

    /**
     * 按下时判断按下的点是否按在圆边范围内
     * @param x
     * @param y
     */
    private boolean isTouchArc(int x, int y) {
        double d = getTouchRadius(x, y);
        return d >= mMinValidateTouchArcRadius && d <= mMaxValidateTouchArcRadius;
    }

    /**
     * 计算某点到圆点的距离
     * @param x
     * @param y
     */
    private double getTouchRadius(int x, int y) {
        int cx = x - mCenterX;
        int cy = y - mCenterY;
        return Math.hypot(cx, cy);
    }

    /**
     * 拿到倾斜的cos值
     */
    private float computeCos(float x, float y) {
        float width = x - mCenterX;
        float height = y - mCenterY;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    private int dp2px(float dp) {
        return DensityUtil.dp2px(getContext(), dp);
    }

    //缩放图片大小
    public Bitmap conversionBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap b = bitmap;
        int width = b.getWidth();
        int height = b.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);
    }

    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
    }

    public void setMaxProgress(float maxProgress) {
        mMaxProgress = maxProgress;
    }

    public void setRingRadius(float ringRadius) {
        mRingRadius = ringRadius;
    }

    public void setCircleRadius(int circleRadius) {
        mCircleRadius = circleRadius;
    }

    public void setRingColor(int ringColor) {
        mRingColor = ringColor;
    }

    public void setRingWidth(int ringWidth) {
        mRingWidth = ringWidth;
    }

    public void setMaxStepNum(int maxStepNum) {
        mMaxStepNum = maxStepNum;
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public void setStepNum(int stepNum) {
        mStepNum = stepNum;
    }
}

