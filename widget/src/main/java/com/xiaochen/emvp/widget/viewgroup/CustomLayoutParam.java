package com.xiaochen.emvp.widget.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.xiaochen.emvp.widget.R;


/**
 * @author zlc
 * @created 26/06/2018
 * @desc
 */
public class CustomLayoutParam extends ViewGroup.MarginLayoutParams{

    /**
     * leftTop
     * rightTop
     * horizontalCenter
     * verticalCenter
     * rightVerticalCenter
     * bottomHorizontalCenter
     * center
     * leftBottom
     * rightBottom
     */
    private String mPosition;

    public CustomLayoutParam(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRelativeLayout);
        mPosition = typedArray.getString(R.styleable.MyRelativeLayout_layout_position);
        if(TextUtils.isEmpty(mPosition)){
            mPosition = "leftTop";
        }
        typedArray.recycle();
    }

    public String getPosition() {
        return mPosition;
    }
}
