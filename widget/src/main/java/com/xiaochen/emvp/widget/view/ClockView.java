package com.xiaochen.emvp.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.xiaochen.emvp.base.utils.DensityUtil;
import com.xiaochen.emvp.widget.R;

import java.util.Calendar;

/**
 * @author zlc
 * @created 10/07/2018
 * @desc
 * 自定义钟表盘
 */
public class ClockView extends View{


    //外面圆的半径
    private int mOutCircleRadius;
    //钟表的半径
    private int mClockRadius;
    //中间小圆点半径
    private int mCenterRadius;
    //圆心坐标
    private int mCenterX;
    private int mCenterY;
    //时钟刻度线的高度
    private int mScaleLineHeight;
    //画笔对象
    private Paint mClockPaint;
    private Paint mCirclePaint;
    private Paint mTimePaint;
    //时间操作辅助类
    private Calendar mCalendar;
    //刻度线数量 钟表要分多少等分 一般是12根线
    private static final int SCALE_NUM = 12;
    //外圆和钟表的边的宽度
    private int mOutCircleStrokeWidth;
    private int mClockStrokeWidth;
    //初始化各种颜色值
    private int mClockColor;
    private int mCircleColor;
    private int mHourColor;
    private int mMinuteColor;
    private int mSecondColor;
    private int mScaleColor;
    private int mMidCircleColor;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initPaints();
    }

    private void initAttrs(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClockView);
        mClockColor = typedArray.getColor(R.styleable.ClockView_clock_color, Color.RED);
        mCircleColor = typedArray.getColor(R.styleable.ClockView_out_circle_color, Color.GRAY);
        mHourColor = typedArray.getColor(R.styleable.ClockView_hour_line_color, Color.RED);
        mMinuteColor = typedArray.getColor(R.styleable.ClockView_minute_line_color, Color.BLUE);
        mSecondColor = typedArray.getColor(R.styleable.ClockView_second_line_color, Color.GREEN);
        mScaleColor = typedArray.getColor(R.styleable.ClockView_scale_line_color, Color.RED);
        mMidCircleColor = typedArray.getColor(R.styleable.ClockView_mid_circle_color, Color.RED);
        typedArray.recycle();
    }

    private void initPaints() {

        mOutCircleStrokeWidth = dp2px(6);
        mClockStrokeWidth = dp2px(4);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mOutCircleStrokeWidth);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setAntiAlias(true);

        mClockPaint = new Paint();
        mClockPaint.setStyle(Paint.Style.STROKE);
        mClockPaint.setStrokeWidth(mClockStrokeWidth);
        mClockPaint.setColor(mClockColor);
        mClockPaint.setAntiAlias(true);
        mClockPaint.setStrokeCap(Paint.Cap.ROUND);

        mTimePaint = new Paint();
        mTimePaint.setStyle(Paint.Style.STROKE);
        mTimePaint.setStrokeWidth(mClockStrokeWidth);
        mTimePaint.setAntiAlias(true);
        mTimePaint.setStrokeCap(Paint.Cap.ROUND);

        mCalendar = Calendar.getInstance();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //当宽度为wrap_content时我们给一个默认值
        int width = dp2px(140);
        int height = dp2px(140);
        width = widthMode == MeasureSpec.AT_MOST ? width : widthSize;
        height = heightMode == MeasureSpec.AT_MOST ? height : heightSize;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
        //外圆半径 = 控件宽度的一半 - 外圆边的宽度
        mOutCircleRadius = w / 2 - mOutCircleStrokeWidth;
        //钟表半径 = 控件宽度的一半 - 外圆边的宽度 - 钟表边的宽度
        mClockRadius = w / 2 - mOutCircleStrokeWidth - mClockStrokeWidth;
        mCenterRadius = dp2px(3);

        mScaleLineHeight = dp2px(6);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //坐标系平移到中心位置
        canvas.translate(mCenterX, mCenterY);

        drawClockCircle(canvas);
        drawClockScale(canvas);
        drawTimeScaleLine(canvas);
    }

    //绘制圆
    private void drawClockCircle(Canvas canvas) {

        canvas.save();
        //画钟表的圆
        mClockPaint.setColor(mClockColor);
        mClockPaint.setStrokeWidth(mClockStrokeWidth);
        canvas.drawCircle(0,0, mClockRadius, mClockPaint);

        //画钟表的外圆
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(mCircleColor);
        canvas.drawCircle(0,0, mOutCircleRadius, mCirclePaint);

        //画钟表的中心圆点
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mMidCircleColor);
        canvas.drawCircle(0,0, mCenterRadius,mCirclePaint);
        canvas.restore();
    }

    //绘制钟表的刻度
    private void drawClockScale(Canvas canvas) {
        mClockPaint.setStrokeWidth(dp2px(3));
        mClockPaint.setColor(mScaleColor);
        //刻度线与钟表的间距
        int lineSpacing = dp2px(6);
        //通过旋转画布的方式来简化绘制过程，不然得计算每一根刻度线的角度和x,y坐标值
        //比如我要绘制2点钟的刻度线 我顺时针旋转2 * 30 = 60就可以了
        //根据刻度线的数量来获取旋转画布的角度
        float angle = 360 / SCALE_NUM;
        for (int i = 0; i < SCALE_NUM; i++) {
            canvas.save();
            canvas.rotate(i * angle);
            //从12点位置开始绘制
            canvas.drawLine(0, - mClockRadius + lineSpacing, 0, - mClockRadius +
                    mScaleLineHeight + lineSpacing,mClockPaint);
            canvas.restore();
        }
    }

    //绘制小时，分钟，秒的线
    private void drawTimeScaleLine(Canvas canvas) {

        mCalendar = Calendar.getInstance();
        //绘制小时的刻度线
        canvas.save();
        mTimePaint.setStrokeWidth(dp2px(3));
        mTimePaint.setColor(mHourColor);
        int hour = mCalendar.get(Calendar.HOUR);
        //360 / 12小时 = 30度
        canvas.rotate(hour * 30f);
        //基于12点位置画线 所以x坐标就是圆心的x坐标 坐标系向下为正方向 所以y为负值
        canvas.drawLine(0,-dp2px(6),0, - mClockRadius * 0.50f,mTimePaint);
        canvas.restore();

        //绘制分钟的刻度线
        canvas.save();
        mTimePaint.setStrokeWidth(dp2px(2));
        mTimePaint.setColor(mMinuteColor);
        int minute = mCalendar.get(Calendar.MINUTE);
        //360 / 60分钟 = 6度
        canvas.rotate(minute * 6f);
        canvas.drawLine(0,-dp2px(6),0,- mClockRadius * 0.62f,mTimePaint);
        canvas.restore();

        //绘制秒钟的刻度线
        canvas.save();
        mTimePaint.setStrokeWidth(dp2px(1.5f));
        mTimePaint.setColor(mSecondColor);
        int second = mCalendar.get(Calendar.SECOND);
        Log.e("ClockView second",second+"");
        // 360 / 60秒 = 6度
        canvas.rotate(second * 6f);
        canvas.drawLine(0,-dp2px(6),0,- mClockRadius * 0.74f,mTimePaint);
        canvas.restore();

        //一秒钟发一次延迟消息 刷新控件
        postInvalidateDelayed(1000);
    }

    public int dp2px(float dpValue){
        return DensityUtil.dp2px(getContext(),dpValue);
    }
}
