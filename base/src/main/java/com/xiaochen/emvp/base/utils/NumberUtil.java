package com.xiaochen.emvp.base.utils;

import java.util.UUID;

/**
 * @author zlc
 * email : zlc921022@163.com
 * desc :
 */
public class NumberUtil {

    private NumberUtil(){

    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 创建随机的uuid
     * @return 返回字符串类型的uuid
     */
    public static String createStringUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","");
    }

    public static int hexStringToInt(String str){
        return Integer.parseInt(str,16);
    }
}
