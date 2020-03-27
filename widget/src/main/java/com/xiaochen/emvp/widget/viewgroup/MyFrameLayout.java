package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author zlc
 * @created 25/06/2018
 * @desc
 */
public class MyFrameLayout extends ViewGroup{


    public MyFrameLayout(Context context) {
        this(context,null);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //记录如果是wrap_content时设置的宽和高
        int width, height;
        //左右的高度
        int lHeight = 0,rHeight = 0;
        //上下的宽度
        int tWidth = 0, bWidth = 0;

        //测量所有子孩子的宽高
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams mParams = (MarginLayoutParams) childView.getLayoutParams();

            if(i ==0 || i == 1){
                tWidth += childWidth + mParams.leftMargin + mParams.rightMargin;
            }

            if(i == 2 || i == 3){
                bWidth += childWidth + mParams.leftMargin + mParams.rightMargin;
            }

            if(i == 0 || i== 2){
                lHeight += childHeight + mParams.topMargin + mParams.bottomMargin;
            }

            if(i == 1 || i == 3){
                rHeight += childHeight + mParams.topMargin + mParams.bottomMargin;
            }
        }

        width = Math.max(tWidth,bWidth);
        height = Math.max(lHeight,rHeight);

        int measureWidth  = widthMode == MeasureSpec.EXACTLY ? widthSize : width;
        int measureHeight  = heightMode == MeasureSpec.EXACTLY ? heightSize : height;
        setMeasuredDimension(measureWidth,measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
            int childLeft = 0,childTop = 0,childRight = 0,childBottom = 0;
            switch (i){
                case 0:
                    childLeft = cParams.leftMargin;
                    childTop = cParams.topMargin+ getPaddingTop();
                    break;
                case 1:
                    childLeft = getMeasuredWidth() - childWidth - cParams.rightMargin;
                    childTop = cParams.topMargin + getPaddingTop();
                    break;
                case 2:
                    childLeft = cParams.leftMargin;
                    childTop =  getMeasuredHeight() - childHeight - cParams.bottomMargin
                            - getPaddingBottom() - getPaddingTop();
                    break;
                case 3:
                    childLeft = getMeasuredWidth() - childWidth - cParams.rightMargin;
                    childTop =  getMeasuredHeight() - childHeight - cParams.bottomMargin
                            -getPaddingTop() - getPaddingBottom();
                    break;
                default:
            }

            childRight = childLeft + childWidth;
            childBottom = childTop + childHeight;

            childView.layout(childLeft,childTop,childRight,childBottom);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
