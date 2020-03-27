package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author zlc
 * @created 26/06/2018
 * @desc
 */
public class MyRelativeLayout extends ViewGroup{


    public MyRelativeLayout(Context context) {
        this(context,null);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //用于计算wrap_content时候的宽高
        int width = 0;
        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            //测量每一个子孩子
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            CustomLayoutParam lp = (CustomLayoutParam) child.getLayoutParams();
            int lrMargin = lp.leftMargin + lp.rightMargin;
            int tbMargin = lp.topMargin + lp.bottomMargin;

            int childWidth = child.getMeasuredWidth() + lrMargin;
            int childHeight = child.getMeasuredHeight() + tbMargin;

            //获取子孩子中最大的子孩子宽度
            if(width < childWidth){
                width = childWidth;
            }

            //获取子孩子总最大的子孩子高度
            if(height < childHeight){
                height = childHeight;
            }
        }

        //带上父View的上下左右的pading
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        int measureWidth = widthMode == MeasureSpec.AT_MOST ? width : widthSize;
        int measureHeight = heightMode == MeasureSpec.AT_MOST ? height : heightSize;

        setMeasuredDimension(measureWidth,measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int pWidth = getMeasuredWidth();
        int pHeight = getMeasuredHeight();

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            CustomLayoutParam lp = (CustomLayoutParam) child.getLayoutParams();
            int lrMargin = lp.leftMargin + lp.rightMargin;
            int tbMargin = lp.topMargin + lp.bottomMargin;
            int cWidthAndMargin = child.getMeasuredWidth() + lrMargin;
            int cHeightAndMargin = child.getMeasuredHeight() + tbMargin;
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            switch (lp.getPosition()){
                case "leftTop":
                    left = lp.leftMargin + getPaddingLeft();
                    top = lp.topMargin + getPaddingTop();
                    break;
                case "rightTop":
                    left = pWidth - cWidthAndMargin- getPaddingRight();
                    top = lp.topMargin + getPaddingTop();
                    right += getPaddingRight();
                    break;
                case "leftBottom":
                    left = lp.leftMargin + getPaddingLeft();
                    top = pHeight - cHeightAndMargin;
                    bottom += getPaddingBottom();
                    break;
                case "rightBottom":
                    left = pWidth - cWidthAndMargin- getPaddingLeft();
                    top = pHeight - cHeightAndMargin;
                    bottom += getPaddingBottom();
                    break;
                case "horizontalCenter":
                    left = (pWidth - cWidth) / 2;
                    top = lp.topMargin + getPaddingTop();
                    break;
                case "verticalCenter":
                    left = lp.leftMargin + getPaddingLeft();
                    top = (pHeight - cHeight) / 2;
                    break;
                case "rightVerticalCenter":
                    left = pWidth - cWidthAndMargin- getPaddingLeft();
                    top = (pHeight - cHeight) / 2;

                    break;
                case "bottomHorizontalCenter":
                    left = (pWidth - cWidth) / 2;
                    top = pHeight - cHeightAndMargin;
                    bottom += getPaddingBottom();
                    break;
                case "center":
                    left = (pWidth - cWidth) / 2;
                    top = (pHeight - cHeight) / 2;
                    break;
            }

            right += left + child.getMeasuredWidth();
            bottom += top + child.getMeasuredHeight();
            child.layout(left,top,right,bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParam(getContext(),attrs);
    }

}
