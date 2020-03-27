package com.xiaochen.emvp.widget.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;


import com.xiaochen.emvp.base.utils.DensityUtil;
import com.xiaochen.emvp.base.utils.GeometryUtil;
import com.xiaochen.emvp.widget.R;

import java.lang.ref.SoftReference;


/**
 * Created by zlc on 2018/6/29.
 * 仿QQ小红点
 */
public class DragStickyView extends View{

    //画笔对象
    private Paint mPaint;
    //固定圆默认半径
    private int mDefaultRadius;
    //固定的圆圆心坐标和半径
    private PointF mFixedPoint;
    private float mFixedRadius;
    //拖拽的圆圆心坐标和半径
    private PointF mDragPoint;
    private float mDragRadius;
    //最大拖拽范围
    private float mMaxDragRange;
    //控制点的坐标
    private PointF mControlPoint;
    //固定圆和拖拽圆切点坐标
    private PointF[] mFixedTangentPoint;
    private PointF[] mDragTangentPoint;
    //拖拽距离
    private float mDragDistance;
    //动画图片数组的index
    private int mPopIndex;
    //路径对象
    private Path mPath;
    //缓存图片 用软引用 防止内存泄漏
    private SoftReference<Bitmap> mSoftReference;
//    private Bitmap mCacheBitmap;
    //图片宽高
    private int mWidth;
    private int mHeight;
    /**
     * 当前红点状态
     * 0 默认静止状态
     * 1 拖拽状态
     * 2 移动状态
     * 3 消失状态
     */
    private static final int STATE_INIT = 0;
    private static final int STATE_DRAG = 1;
    private static final int STATE_MOVE = 2;
    private static final int STATE_DISMISS = 3;
    private int mState = STATE_INIT;

    //动画消失图片数组
    private int[] mPopRes = {
            R.drawable.pop1, R.drawable.pop2, R.drawable.pop3,
            R.drawable.pop4,  R.drawable.pop5};
    private Bitmap[] mBitmaps;

    public DragStickyView(Context context) {
        this(context,null);
    }

    public DragStickyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        //固定圆 拖拽圆的圆心坐标
        mFixedPoint = new PointF();
        mDragPoint = new PointF();
        //初始化各个坐标点
        mControlPoint = new PointF();
        mFixedTangentPoint = new PointF[2];
        mDragTangentPoint = new PointF[2];

        //初始化各个半径
        mDefaultRadius = 30;
        mFixedRadius = dip2px(12);
        mDragRadius = dip2px(14);
        mMaxDragRange = dip2px(100);
        mPath = new Path();

        //初始化消失动画资源
        mBitmaps = new Bitmap[mPopRes.length];
        for (int i = 0; i < mBitmaps.length; i++) {
            mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mPopRes[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        //拖拽范围之内才绘制连接部分和固定圆
        if(isInsideRange() && mState == STATE_DRAG) {
            //画固定圆
            canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, mFixedRadius, mPaint);
            drawBezier(canvas);
        }
        //绘制缓存的Bitmap
        if (mSoftReference != null && mSoftReference.get() != null && mState != STATE_DISMISS) {
            canvas.drawBitmap(mSoftReference.get(), mDragPoint.x - mWidth / 2, mDragPoint.y - mHeight / 2, mPaint);
        }
        //绘制小红点消失时的爆炸动画
        if (mState == STATE_DISMISS && mPopIndex < mPopRes.length) {
            canvas.drawBitmap(mBitmaps[mPopIndex], mDragPoint.x - mWidth / 2, mDragPoint.y - mHeight / 2, mPaint);
        }
        canvas.restore();
    }

    //画贝塞尔曲线
    private void drawBezier(Canvas canvas){

        //控制点设置为两圆心点连线的中心坐标
        mControlPoint.set((mFixedPoint.x + mDragPoint.x) / 2.0f,(mFixedPoint.y + mDragPoint.y) / 2.0f);
        //计算斜率 和 切点坐标
        float dy = mDragPoint.y - mFixedPoint.y;
        float dx = mDragPoint.x - mFixedPoint.x;
        //k1 * k2 = -1 斜率k = (y0 - y1)/(x0 - x1)
        mDragRadius = Math.min(mWidth, mHeight) / 2;
        if(dx == 0){
            mFixedTangentPoint = GeometryUtil.getIntersectionPoints(mFixedPoint,mFixedRadius,0f);
            mDragTangentPoint = GeometryUtil.getIntersectionPoints(mDragPoint, mDragRadius,0f);
        }else{
            float k1 = dy / dx;
            float k2 = -1 / k1;
            mFixedTangentPoint = GeometryUtil.getIntersectionPoints(mFixedPoint,mFixedRadius,k2);
            mDragTangentPoint = GeometryUtil.getIntersectionPoints(mDragPoint, mDragRadius,k2);
        }
        //需要重置 否则线会重叠
        mPath.reset();
        //移动起点到固定圆的外切点
        mPath.moveTo(mFixedTangentPoint[0].x,mFixedTangentPoint[0].y);
        //绘制二阶贝塞尔曲线 需要一个控制点 和终点（拖拽圆的一个切点）
        mPath.quadTo(mControlPoint.x,mControlPoint.y, mDragTangentPoint[0].x, mDragTangentPoint[0].y);
        //连接拖拽圆的另一个外切点
        mPath.lineTo(mDragTangentPoint[1].x, mDragTangentPoint[1].y);
        //再次绘制二阶贝塞尔曲线 需要一个控制点 和终点（固定圆的另外一个切点）
        mPath.quadTo(mControlPoint.x,mControlPoint.y,mFixedTangentPoint[1].x,mFixedTangentPoint[1].y);
        //闭合曲线
        mPath.close();
        //绘制path
        canvas.drawPath(mPath,mPaint);
    }

    //画拖拽圆
    private void drawDragCircle(Canvas canvas){
        //当在范围外松手的时候不再绘制拖拽圆
        if(mState == STATE_DRAG && isInsideRange()) {
            canvas.drawCircle(mDragPoint.x, mDragPoint.y, mDragRadius, mPaint);
        }
    }

    //是不是在拖拽范围内
    private boolean isInsideRange(){
        return mDragDistance <= mMaxDragRange;
    }

    //手指抬起事件处理
    public void touchUp(){
        if(isInsideRange()){
            if(mState == STATE_DRAG){
                //拖拽一直在范围内
                startResetAnimation();
            }else if(mState == STATE_MOVE){
                //拖拽出了范围，松手在范围内 因为要回到原位置
                // 所以拖拽圆的圆心坐标用固定圆的圆心坐标代替
                mDragPoint.set(mFixedPoint.x,mFixedPoint.y);
                invalidate();
                clearDragView();
            }
        }else if(mState == STATE_MOVE){
            //拖拽范围之外 消失动画处理
            mState = STATE_DISMISS;
            startPopAnimation();
        }
    }

    //拖拽的距离大小
    public float drawDistance(){
        return GeometryUtil.getDistanceBetween2Points(mFixedPoint, mDragPoint);
    }

    //移动的时候一直在范围内，最后在范围内做松手回弹动画处理
    private void startResetAnimation() {

        final PointF startPoint = new PointF(mDragPoint.x,
                mDragPoint.y);
        final PointF endPoint = new PointF(mFixedPoint.x,
                mFixedPoint.y);
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
        animator.setInterpolator(new OvershootInterpolator(5.0f));
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                PointF byPercent = GeometryUtil.getPointByPercent(
                        startPoint, endPoint, fraction);
                mDragPoint.set(byPercent.x,byPercent.y);
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clearDragView();
                if (mQQBezierView.mOnDragListener != null) {
                    mQQBezierView.mOnDragListener.onDrag();
                }
            }
        });
        animator.start();
    }

    // 爆炸动画
    private void startPopAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mPopRes.length);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPopIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(mQQBezierView.mOnDragListener != null){
                    mQQBezierView.mOnDragListener.onDismiss();
                }
                if(mSoftReference != null) {
                    mSoftReference.clear();
                }
            }
        });
        animator.start();
    }

    //设置固定圆和拖拽圆的圆心坐标
    public void setStickyPoint(int fixedX, int fixedY, float dragX, float dragY) {
        mFixedPoint.set(fixedX,fixedY);
        mDragPoint.set(dragX,dragY);
        //计算拖拽的距离
        mDragDistance = drawDistance();
        if(isInsideRange()){
            //如果拖拽距离小于规定最大距离，则固定的圆应该越来越小，这样看着才符合逻辑
            float distance = mDefaultRadius - mDragDistance / 10;
            mFixedRadius = distance < 10 ? 10 : distance;
            mState = STATE_DRAG;
        }else{
            mState = STATE_INIT;
        }
    }

    //更新拖拽圆的圆心坐标
    public void updateDragCenterXY(float x, float y){
        mDragPoint.set(x,y);
        //实时计算圆心距
        mDragDistance = drawDistance();
        if(mState == STATE_DRAG){
            if(isInsideRange()){
                float distance = mDefaultRadius - mDragDistance / 10;
                mFixedRadius = distance < 10 ? 10 : distance;
            }else{
                mState = STATE_MOVE;
                if(mQQBezierView.mOnDragListener != null){
                    mQQBezierView.mOnDragListener.onMove();
                }
            }
        }
        invalidate();
    }

    //设置缓存图片
    public void setCacheBitmap(Bitmap bitmap) {
       // this.mCacheBitmap = bitmap;
        mSoftReference = new SoftReference<>(bitmap);
        this.mWidth = bitmap.getWidth();
        this.mHeight = bitmap.getHeight();
    }

    private QQBezierView mQQBezierView;
    public void setQQBezierView(QQBezierView QQBezierView) {
        mQQBezierView = QQBezierView;
    }

    //清除当前View
    private void clearDragView() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        viewGroup.removeView(this);
        mQQBezierView.setVisibility(VISIBLE);
    }

    public int dip2px(float dpValue) {
        return DensityUtil.dp2px(getContext(),dpValue);
    }
}
