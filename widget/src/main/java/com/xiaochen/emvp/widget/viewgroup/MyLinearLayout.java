package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.xiaochen.emvp.widget.R;


/**
 * @author zlc
 * @created 25/06/2018
 * @desc
 */
public class MyLinearLayout extends ViewGroup {


    //horizontal   横向
    //vertical     纵向
    private String mOrientation;

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout);
        mOrientation = typedArray.getString(R.styleable.MyLinearLayout_ll_orientation);
        if (TextUtils.isEmpty(mOrientation)) {
            mOrientation = "vertical";
        }

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //记录如果是wrap_content时设置的宽和高
        int width, height;
        int totalWidth = 0;
        int totalHeight = 0;

        //测量所有子孩子的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams mParams = (MarginLayoutParams) childView.getLayoutParams();

            if ("horizontal".equals(mOrientation)) {
                //横向高度取子孩子中高度最大值 带上margin和父View的padding值
                int cHeight = childHeight + mParams.topMargin + mParams.bottomMargin + this.getPaddingBottom() + this.getPaddingTop();
                if (cHeight > totalHeight) {
                    totalHeight = cHeight;
                }
                //横向第一个子孩子和最后一个子孩子需要带上父View的padding值
                if (i == 0) {
                    totalWidth += getPaddingLeft();
                } else if (i == getChildCount() - 1) {
                    totalWidth += getPaddingRight();
                }
                totalWidth += childWidth + mParams.leftMargin + mParams.rightMargin;

            } else {

                //竖向宽度取其中一个子孩子的最大高度
                int cWidth = childWidth + mParams.leftMargin + mParams.rightMargin + this.getPaddingLeft() + this.getPaddingRight();
                if (cWidth > totalWidth) {
                    totalWidth = cWidth;
                }

                //竖向的第一子孩子和最后一个子孩子需要带上父View的padding值
                if (i == 0) {
                    totalHeight += getPaddingTop();
                } else if (i == getChildCount() - 1) {
                    totalHeight += getPaddingBottom();
                }
                totalHeight += childHeight + mParams.topMargin + mParams.bottomMargin;

            }
        }

        width = totalWidth;
        height = totalHeight;
        int measureWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : width;
        int measureHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : height;

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childLeft;
        int childTop;
        int childRight;
        int childBottom;
        int lastTotalHeight = 0;
        int lastTotalWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();

            if ("horizontal".equals(mOrientation)) {
                //横向第一个带上父View的左边padding值
                if (i == 0) {
                    childLeft = cParams.leftMargin + getPaddingLeft();
                } else {
                    childLeft = lastTotalWidth + cParams.leftMargin;
                }
                //横向最后一个带上父View的右边padding值
                if (i == getChildCount() - 1) {
                    childRight = childLeft + childWidth + getPaddingRight();
                } else {
                    childRight = childLeft + childWidth;
                }
                lastTotalWidth = childRight;

                //横向上下带上上下padding值
                childTop = cParams.topMargin + getPaddingTop();
                childBottom = childTop + childHeight + getPaddingBottom();
            } else {
                //竖向左右带上左右padding值
                childLeft = cParams.leftMargin + getPaddingLeft();
                childRight = childLeft + childWidth + getPaddingRight();

                //竖向第一个带上父View的上边padding值
                if (i == 0) {
                    childTop = cParams.topMargin + getPaddingTop();
                } else {
                    childTop = lastTotalHeight + cParams.topMargin;
                }

                ////竖向最后个带上父View的下边padding值
                if (i == getChildCount() - 1) {
                    childBottom = childTop + childHeight + getPaddingBottom();
                } else {
                    childBottom = childTop + childHeight;
                }
                lastTotalHeight = childBottom;
            }

            childView.layout(childLeft, childTop, childRight, childBottom);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
