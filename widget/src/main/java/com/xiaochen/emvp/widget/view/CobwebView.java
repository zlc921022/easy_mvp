package com.xiaochen.emvp.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.xiaochen.emvp.base.utils.DensityUtil;
import com.xiaochen.emvp.widget.R;

import java.text.DecimalFormat;

/**
 * Created by zlc on 2018/6/24.
 * 雷达图（蜘蛛网）
 */
public class CobwebView extends View{

    private Paint mPaint;
    private Path mPath;
    //边的数量
    private static final int SIDE_NUM = 6;
    //多边形的数量
    private static final float POLYGON_NUM = 4;
    private float mLength;
    /**
     * 获取多边形各个角度cos sin
     */
    private PathMeasure mMeasure;
    private float[] mPos;
    private float[] mTan;
    private float[] mCosArray;
    private float[] mSinArray;
    private int mCenterX;
    private int mCenterY;
    private PointF mPointF;
    private Paint mTextPaint;
    private String[] mStrings = {"A","B","C","D","E","F","G","H"};
    private float[] mValues = {10.0f,47.0f,11.0f,38.0f,9.0f,52.0f,14.0f,37.0f};
    private Rect mScaleTextRect;

    private Paint mDataFillPaint;
    private Paint mStrokePaint;
    private Paint mScaleTextPaint;

    public CobwebView(Context context) {
        this(context,null);
    }

    public CobwebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    private void initPaints() {

        //多边形画笔
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

        //顶点文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DensityUtil.dp2px(getContext(),12));

        //刻度文字画笔
        mScaleTextPaint = new Paint();
        mScaleTextPaint.setAntiAlias(true);
        mScaleTextPaint.setColor(Color.BLACK);
        mScaleTextPaint.setTextSize(DensityUtil.dp2px(getContext(),10));

        //绘制数据区域画笔
        mDataFillPaint = new Paint();
        mDataFillPaint.setColor(Color.GRAY);
        mDataFillPaint.setStyle(Paint.Style.FILL);
        mDataFillPaint.setAlpha(152);

        //绘制数据区域描边画笔
        mStrokePaint = new Paint();
        mStrokePaint.setColor(ContextCompat.getColor(getContext(), R.color.color_96d15d));
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAlpha(166);
        mStrokePaint.setStrokeWidth(DensityUtil.dp2px(getContext(),3));

        mScaleTextRect = new Rect();
        mPath = new Path();
        mMeasure = new PathMeasure();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
        mPointF = new PointF();
        mLength = DensityUtil.dp2px(getContext(),120);
        mPath.addCircle(0,0,mLength, Path.Direction.CW);
        mMeasure.setPath(mPath,true);
        mPos = new float[2];
        mTan = new float[2];
        mCosArray = new float[SIDE_NUM];
        mSinArray = new float[SIDE_NUM];

        for (int i=0; i < SIDE_NUM; i++){
            mMeasure.getPosTan((float) (Math.PI * 2 * mLength * i / SIDE_NUM), mPos, mTan);
            mCosArray[i] = mTan[0];
            mSinArray[i] = mTan[1];
        }
        mPath.reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mCenterX,mCenterY);

        canvas.save();
        canvas.rotate(-180);

        drawPolygon(canvas);
        drawPolygonLine(canvas);
        drawPolygonScale(canvas);
        drawPolygonData(canvas);

        canvas.restore();

    }

    //绘制多边形
    private void drawPolygon(Canvas canvas) {

        mPaint.setColor(Color.GRAY);
        for (int i = 0; i < POLYGON_NUM; i++){
            canvas.save();
            canvas.scale(1 - i / POLYGON_NUM,1 - i / POLYGON_NUM);
            mPath.moveTo(0,mLength);
            for (int j = 0; j < SIDE_NUM; j++){
                mPath.lineTo(mLength* mCosArray[j],
                        mLength* mSinArray[j]);
            }
            mPath.close();
            canvas.drawPath(mPath,mPaint);
            mPath.reset();
            canvas.restore();
        }
    }

    //绘制多边形的连线
    private void drawPolygonLine(Canvas canvas) {

        mPaint.setColor(Color.BLUE);
        //连线
        for (int j=0; j< SIDE_NUM; j++){
            mPath.moveTo(0,0);
            mPath.lineTo(mLength*mCosArray[j],
                    mLength*mSinArray[j]);
            drawPolygonText(canvas,j);
        }
        mPath.close();
        canvas.drawPath(mPath,mPaint);
        mPath.reset();
    }

    //绘制多边形各顶点文字
    private void drawPolygonText(Canvas canvas,int index) {
        canvas.save();
        canvas.rotate(180);
        mPointF.x = -mLength * mCosArray[index] * 1.1f;
        mPointF.y = -mLength * mSinArray[index] * 1.1f;
        if (mCosArray[index] > 0.2){
            textCenter(new String[]{mStrings[index]},mTextPaint,canvas,mPointF, Paint.Align.RIGHT);
        }else if (mCosArray[index] < -0.2){
            textCenter(new String[]{mStrings[index]},mTextPaint,canvas,mPointF, Paint.Align.LEFT);
        }else {
            textCenter(new String[]{mStrings[index]},mTextPaint,canvas,mPointF, Paint.Align.CENTER);
        }
        canvas.restore();
    }

    //绘制刻度
    private void drawPolygonScale(Canvas canvas) {

        canvas.save();
        canvas.rotate(180);
        DecimalFormat sdf = new DecimalFormat ("0");

        //外循环控制边数
        for (int i = 0; i < SIDE_NUM; i++) {
            //内循环控制多边形数量
            for (int j = 1; j < POLYGON_NUM; j++){
                mPointF.x =  mLength * (1 - j / POLYGON_NUM) * mCosArray[i];
                mPointF.y =  mLength * (1 - j / POLYGON_NUM) * mSinArray[i];
                String text = sdf.format(10 * (POLYGON_NUM - j));
                mScaleTextPaint.getTextBounds(text,0,text.length(),mScaleTextRect);
                canvas.drawText(text, mPointF.x - mScaleTextRect.width() / 2, mPointF.y + mScaleTextRect.height() / 2, mScaleTextPaint);
            }
        }
        //绘制中心点数字
        String centerValue = "0";
        mScaleTextPaint.getTextBounds(centerValue,0,centerValue.length(),mScaleTextRect);
        canvas.drawText(centerValue,0 - mScaleTextRect.width() / 2,0 + mScaleTextRect.height() / 2,mScaleTextPaint);
        canvas.restore();
    }

    //绘制数据区域
    private void drawPolygonData(Canvas canvas) {

        for (int i = 0; i < SIDE_NUM; i++){
            float value = mValues[i];
            float yValue = value * 5.20f;
            Log.e("drawGraph value",value+" ;yValue: "+yValue);
            if ( i == 0 ){
                mPath.moveTo(yValue * mCosArray[i], yValue * mSinArray[i]);
            }else {
                mPath.lineTo(yValue * mCosArray[i], yValue*mSinArray[i]);
            }
        }
        mPath.close();
        canvas.drawPath(mPath, mDataFillPaint);
        canvas.drawPath(mPath,mStrokePaint);
        mPath.reset();
    }

    /**
     * 多行文本居中、居右、居左
     * @param strings 文本字符串列表
     * @param point 点的坐标
     */
    protected void textCenter(String[] strings, Paint paint, Canvas canvas, PointF point, Paint.Align align){

        paint.setTextAlign(align);
        Paint.FontMetrics fontMetrics= paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        float ascent = fontMetrics.ascent;
        float descent = fontMetrics.descent;
        int length = strings.length;
        float total = (length - 1) * (-top + bottom) + (-ascent + descent);
        float offset = total / 2 - bottom;
        for (int i = 0; i < length; i++) {
            float yAxis = -(length - i - 1) * (-top + bottom) + offset;
            canvas.drawText(strings[i], point.x, point.y + yAxis, paint);
        }
    }

    public void setStrings(String[] strings) {
        mStrings = strings;
    }

    public void setValues(float[] values) {
        mValues = values;
    }
}
