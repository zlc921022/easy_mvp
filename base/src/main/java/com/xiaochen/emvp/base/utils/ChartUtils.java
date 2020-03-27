package com.xiaochen.emvp.base.utils;

import android.graphics.PointF;

/**
 * Created by Horrarndoo on 2018/1/19.
 * <p>
 * 圆环坐标工具类
 */

public class ChartUtils {
    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     *
     * @param cirX     圆centerX
     * @param cirY     圆centerY
     * @param radius   圆半径
     * @param cirAngle 当前弧角度
     * @return 扇形终射线与圆弧交叉点的xy坐标
     */
    public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float
            cirAngle) {
        float posX = 0.0f;
        float posY = 0.0f;
        //将角度转换为弧度
        float arcAngle = (float) Math.toRadians(cirAngle);//float) (Math.PI * cirAngle / 180.0)
        if(cirAngle < 0  && cirAngle > -90){
            posX = cirX + (float) (Math.cos(arcAngle)) * radius;
            posY = cirY + (float) (Math.sin(arcAngle)) * radius;
        }else if(cirAngle == 0){
            posX = cirX + radius;
            posY = cirY;
        }else if (cirAngle > 0 && cirAngle < 90) {
            posX = cirX + (float) (Math.cos(arcAngle)) * radius;
            posY = cirY + (float) (Math.sin(arcAngle)) * radius;
        } else if (cirAngle == 90) {
            posX = cirX;
            posY = cirY + radius;
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = (float) Math.toRadians(180 - cirAngle);//(float) (Math.PI * (180 - cirAngle) / 180.0);
            posX = cirX - (float) (Math.cos(arcAngle)) * radius;
            posY = cirY + (float) (Math.sin(arcAngle)) * radius;
        } else if (cirAngle == 180) {
            posX = cirX - radius;
            posY = cirY;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = (float) Math.toRadians(cirAngle - 180);// (Math.PI * (cirAngle - 180) / 180.0);
            posX = cirX - (float) (Math.cos(arcAngle)) * radius;
            posY = cirY - (float) (Math.sin(arcAngle)) * radius;
        } else if (cirAngle == 270) {
            posX = cirX;
            posY = cirY - radius;
        } else {
            arcAngle = (float) Math.toRadians(360 - cirAngle);// (Math.PI * (360 - cirAngle) / 180.0);
            posX = cirX + (float) (Math.cos(arcAngle)) * radius;
            posY = cirY - (float) (Math.sin(arcAngle)) * radius;
        }
        return new PointF(posX, posY);
    }

    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     *
     * @param cirX       圆centerX
     * @param cirY       圆centerY
     * @param radius     圆半径
     * @param cirAngle   当前弧角度
     * @param orginAngle 起点弧角度
     * @return 扇形终射线与圆弧交叉点的xy坐标
     */
    public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float
            cirAngle, float orginAngle) {
        cirAngle = (orginAngle + cirAngle) % 360;
        return calcArcEndPointXY(cirX, cirY, radius, cirAngle);
    }
}
