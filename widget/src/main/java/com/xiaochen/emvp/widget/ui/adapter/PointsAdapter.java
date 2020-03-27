package com.xiaochen.emvp.widget.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaochen.emvp.widget.R;
import com.xiaochen.emvp.widget.view.QQBezierView;
import com.xiaochen.emvp.widget.ui.bean.PointBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zlc
 * @created 27/07/2018
 * @desc
 */
public class PointsAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<PointBean> mDatas;

    public PointsAdapter(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            PointBean bean = new PointBean();
            bean.msgNum = (int)(Math.random()*100+1)+"";
            bean.position = i+"";
            mDatas.add(bean);
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.list_points_item,viewGroup,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PointBean bean = mDatas.get(position);
        viewHolder.mTvData.setText("我是第"+position+"条数据");
        viewHolder.mQqPoint.setText(Integer.valueOf(bean.msgNum) > 99 ? "99+" : bean.msgNum);
        viewHolder.mQqPoint.setVisibility(bean.msgNum.equals("0") ? View.GONE : View.VISIBLE);
        viewHolder.mQqPoint.setOnDragListener(new QQBezierView.onDragStatusListener() {
            @Override
            public void onDrag() {
            }
            @Override
            public void onMove() {
            }
            @Override
            public void onDismiss() {
                bean.msgNum = "0";
            }
        });
        return convertView;
    }

    private static class ViewHolder{

        private final TextView mTvData;
        private final QQBezierView mQqPoint;

        public ViewHolder(View v){
            mTvData = v.findViewById(R.id.tv_data);
            mQqPoint = v.findViewById(R.id.qq_point);
        }
    }
}
