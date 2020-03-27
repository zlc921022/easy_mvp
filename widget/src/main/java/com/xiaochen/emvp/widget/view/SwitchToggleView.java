package com.xiaochen.emvp.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.xiaochen.emvp.base.utils.DensityUtil;
import com.xiaochen.emvp.widget.R;

/**
 * @author zlc
 * @created 28/06/2018
 * @desc
 * 开关按钮自定义
 */
public class SwitchToggleView extends View implements View.OnClickListener{

    private Paint mTogglePaint;
    private static final String TAG = "SwitchToggleView";
    private Bitmap mCloseBgBitmap;
    private int mViewX;
    private int mViewY;
    private Bitmap mToggleBitmap;
    private float mStartX;
    private Bitmap mOpenBgBitmap;
    private int mToggleOpenColor;
    private int mToggleCloseColor;
    private int mToggleColor;
    private float mToggleRoundRadius;
    private Paint mBackGroundPaint;
    private RectF mBgRectF;
    private RectF mToggleRectF;
    //x方向偏移量
    private float mDistanceX = 0f;
    //开关能不能移动
    private boolean mIsToggleMove = false;
    //开关按钮偏移量
    private float mToggleOffsetX;
    /*
     *  0 or 指定任意值 完全自定义View 不用指定背景图片
     *  1 指定背景图片类型为shape背景图片 / jpg/png等类型的背景图片
     */
    private int mToggleType;
    //开关关闭 打开背景图片和开关切换的图片
    private int mToggleCloseBackGroundImg;
    private int mToggleOpenBackGroundImg;
    private int mToggleSwitchImg;
    //动画相关
    private int mLastX;
    private int mCurrentX;
    private static final long DURATION = 500;
    private boolean isAnimationFinished = false;

    public SwitchToggleView(Context context) {
        this(context,null);
    }

    public SwitchToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        initPaint();
        setOnClickListener(this);
    }

    private void initAttrs(Context context,AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchToggleView);
        mToggleOpenColor = typedArray.getColor(R.styleable.SwitchToggleView_toggle_openColor,
                ContextCompat.getColor(context, R.color.color_85f9c4));
        mToggleCloseColor = typedArray.getColor(R.styleable.SwitchToggleView_toggle_closeColor,
                ContextCompat.getColor(context, R.color.color_e8e8e8));
        mToggleColor = typedArray.getColor(R.styleable.SwitchToggleView_toggle_color,
                ContextCompat.getColor(context, R.color.color_ffffff));
        mToggleRoundRadius = typedArray.getDimension(R.styleable.SwitchToggleView_toggle_round_radius, dp2px(30));
        mToggleType = typedArray.getInteger(R.styleable.SwitchToggleView_toggle_type, 0);
        mToggleCloseBackGroundImg = typedArray.getResourceId(R.styleable.SwitchToggleView_toggle_closeBackGroundImg,
                R.drawable.switch_close_bg);
        mToggleOpenBackGroundImg = typedArray.getResourceId(R.styleable.SwitchToggleView_toggle_openBackGroundImg,
                R.drawable.switch_open_bg);
        mToggleSwitchImg = typedArray.getResourceId(R.styleable.SwitchToggleView_toggle_image,
                R.drawable.switch_toggle);
        //不要忘记回收
        typedArray.recycle();
    }

    private void initPaint() {

        //初始化开关画笔
        mTogglePaint = new Paint();
        mTogglePaint.setAntiAlias(true);
        mTogglePaint.setStyle(Paint.Style.FILL);
        mTogglePaint.setStrokeWidth(dp2px(1));
        mTogglePaint.setColor(mToggleColor);

        //初始化背景画笔
        mBackGroundPaint = new Paint();
        mBackGroundPaint.setAntiAlias(true);
        mBackGroundPaint.setStyle(Paint.Style.FILL);
        mBackGroundPaint.setStrokeWidth(dp2px(1));

        mToggleRectF = new RectF();
        mBgRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //wrap_content时默认指定一个宽高
        int width = dp2px(100);
        int height = dp2px(48);
        int measureWidth = (widthMode == MeasureSpec.AT_MOST) ? width : widthSize;
        int measureHeight = (heightMode == MeasureSpec.AT_MOST) ? height : heightSize;

        setMeasuredDimension(measureWidth,measureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewX = w;
        mViewY = h;
        mToggleOffsetX = -mViewX / 2 + dp2px(1);

        if(mToggleType == 1){

            Resources resources = getResources();

            Drawable closeDrawable = resources.getDrawable(mToggleCloseBackGroundImg);
            mCloseBgBitmap = drawableToBitmap(closeDrawable, w, h);

            Drawable openDrawable = resources.getDrawable(mToggleOpenBackGroundImg);
            mOpenBgBitmap = drawableToBitmap(openDrawable, w, h);

            Drawable leftDrawable = resources.getDrawable(mToggleSwitchImg);
            mToggleBitmap = drawableToBitmap(leftDrawable, (int) (w * 0.5f), h - dp2px(2));
        }

//        setToggleAnimation((int)mToggleOffsetX, (int) (mToggleOffsetX + mViewX / 2) - dp2px(2));
    }

    public Bitmap drawableToBitmap(Drawable drawable,int w,int h) {

        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mViewX / 2,mViewY / 2);

        if(mToggleType == 1){
            drawSwitchToggle2(canvas);
        }else{
            drawSwitchToggle1(canvas);
        }
    }

    private void drawSwitchToggle1(Canvas canvas) {

        //画背景圆角矩形
        if(mToggleOffsetX == -mViewX / 2 + dp2px(1)){
            mBackGroundPaint.setColor(mToggleCloseColor);
        }else{
            mBackGroundPaint.setColor(mToggleOpenColor);
        }
        mBgRectF.set(-mViewX / 2,-mViewY / 2,mViewX / 2,mViewY / 2);
        canvas.drawRoundRect(mBgRectF,mToggleRoundRadius,mToggleRoundRadius,mBackGroundPaint);

        //画开关按钮圆角矩形
        mToggleRectF.set(mToggleOffsetX, -mViewY / 2 + dp2px(1), mToggleOffsetX + mViewX / 2, mViewY / 2 - dp2px(1));
        canvas.drawRoundRect(mToggleRectF,mToggleRoundRadius,mToggleRoundRadius, mTogglePaint);
    }

    private void drawSwitchToggle2(Canvas canvas) {
        //画背景图
        if(mToggleOffsetX == -mViewX / 2 + dp2px(1)) {
            canvas.drawBitmap(mCloseBgBitmap, -mCloseBgBitmap.getWidth() / 2, -mCloseBgBitmap.getHeight() / 2, mTogglePaint);
        }else{
            canvas.drawBitmap(mOpenBgBitmap, -mCloseBgBitmap.getWidth() / 2, -mCloseBgBitmap.getHeight() / 2, mTogglePaint);
        }
        //画开关按钮图
        canvas.drawBitmap(mToggleBitmap, mToggleOffsetX,-mCloseBgBitmap.getHeight() / 2 + dp2px(1), mTogglePaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float distanceY = 0f;
        float startY = 0f;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                mIsToggleMove = false;
                mStartX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                mDistanceX = moveX - startY;
                distanceY = moveY - startY;
                if(Math.abs(mDistanceX) > Math.abs(distanceY) && Math.abs(mDistanceX) >= mViewX / 2){
                    mIsToggleMove = true;
                }else{
                    mIsToggleMove = false;
                }
                mStartX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                if(mIsToggleMove){
                    if(mDistanceX < 0){
                        mToggleOffsetX = -mViewX / 2 + dp2px(1);
                        mLastX = -dp2px(1);
                        mCurrentX = (int) mToggleOffsetX;
                    }else{
                        mToggleOffsetX = 0 - dp2px(1);
                        mLastX = - mViewX / 2 + dp2px(1);
                        mCurrentX = (int) mToggleOffsetX;
                    }
//                    invalidate();
                    setToggleAnimation();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
       Log.e("startX",mStartX+"");
        //点击开关同一侧 不做处理
        if((mToggleOffsetX == 0 - dp2px(1) && mStartX >= mViewX / 2)
                || (mToggleOffsetX == -mViewX / 2 + dp2px(1) && mStartX < mViewX / 2)){
            return;
        }

        if(mStartX >= mViewX / 2){
            mToggleOffsetX = 0 - dp2px(1);
            mLastX = - mViewX / 2 + dp2px(1);
            mCurrentX = (int) mToggleOffsetX;
        }else {
            mToggleOffsetX = -mViewX / 2 + dp2px(1);
            mLastX = -dp2px(1);
            mCurrentX = (int) mToggleOffsetX;
        }
        setToggleAnimation();
       // invalidate();
    }

    //给按钮添加动画
    public void setToggleAnimation(){
        ValueAnimator toggleAnimator = ValueAnimator.ofFloat(mLastX, mCurrentX);
        toggleAnimator.setDuration(DURATION);
        toggleAnimator.setInterpolator(new AccelerateInterpolator());
        toggleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mToggleOffsetX = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        toggleAnimator.start();
    }

    public void setToggleCloseColor(int toggleCloseColor) {
        mToggleCloseColor = toggleCloseColor;
    }

    public void setToggleOpenColor(int toggleOpenColor) {
        mToggleOpenColor = toggleOpenColor;
    }

    public void setToggleColor(int toggleColor) {
        mToggleColor = toggleColor;
    }

    public void setToggleRoundRadius(float toggleRoundRadius) {
        mToggleRoundRadius = toggleRoundRadius;
    }

    public void setToggleCloseBackGroundImg(int toggleCloseBackGroundImg) {
        mToggleCloseBackGroundImg = toggleCloseBackGroundImg;
    }

    public void setToggleOpenBackGroundImg(int toggleOpenBackGroundImg) {
        mToggleOpenBackGroundImg = toggleOpenBackGroundImg;
    }

    public void setToggleSwitchImg(int toggleSwitchImg) {
        mToggleSwitchImg = toggleSwitchImg;
    }

    public int dp2px(float dpValue){
        return DensityUtil.dp2px(getContext(),dpValue);
    }
}
