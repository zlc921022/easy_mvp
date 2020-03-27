package com.xiaochen.emvp.base.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author zlc
 * email : zlc921022@163.com
 * desc : sp工具类
 */
public class SPUtil {

    private final static String SP_NAME = "userInfo";

    private SPUtil() {
    }

    /**
     * 获取sp对象
     *
     * @return
     */
    public static SharedPreferences getSp() {
        return ContextUtil.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据
     *
     * @param key
     * @param obj
     */
    public static void put(String key, Object obj) {

        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        if (obj instanceof String) {
            edit.putString(key, (String) obj);
        } else if (obj instanceof Integer) {
            edit.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {
            edit.putLong(key, (Long) obj);
        } else if (obj instanceof Float) {
            edit.putFloat(key, (Float) obj);
        } else if (obj instanceof Boolean) {
            edit.putBoolean(key, (Boolean) obj);
        } else {
            edit.putString(key, obj.toString());
        }

        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 获取sp保存的数据
     *
     * @param key
     * @param defObj
     * @return
     */
    public static Object get(String key, Object defObj) {

        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        if (defObj instanceof String) {
            return sp.getString(key, (String) defObj);
        } else if (defObj instanceof Integer) {
            return sp.getInt(key, (Integer) defObj);
        } else if (defObj instanceof Long) {
            return sp.getLong(key, (Long) defObj);
        } else if (defObj instanceof Float) {
            return sp.getFloat(key, (Float) defObj);
        } else if (defObj instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defObj);
        }

        return null;
    }

    /**
     * 移除某一个key值对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 查询某个key是否存在
     *
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 给sp中存入String类型的数据
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 获取String类型的数据
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    public static String getString(String key, String defValue) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * 存储int类型数据
     *
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 获取int类型数据
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defValue) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }


    /**
     * 存储long类型数据
     *
     * @param key
     * @param value
     */
    public static void putLong(String key, long value) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 获取long类型数据
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defValue) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    /**
     * 存储boolean类型数据
     *
     * @param key
     * @param value
     */
    public static void putBolean(String key, boolean value) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 获取boolean类型数据
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 存储float类型数据
     *
     * @param key
     * @param value
     */
    public static void putFloat(String key, float value) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putFloat(key, value);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 获取float类型数据
     *
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public static float getFloat(String key, float defValue) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    /**
     * 存储一个set集合的数据
     *
     * @param key
     * @param set
     */
    public static void putSet(String key, Set<String> set) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putStringSet(key, set);
        SharedPreferencesCompat.apply(edit);
    }

    /**
     * 获取set集合数据
     *
     * @param key
     * @return
     */
    public static Set getSet(String key) {
        return getSet(key, null);
    }

    public static Set getSet(String key, Set<String> set) {
        final SharedPreferences sp = ContextUtil.getContext()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getStringSet(key, set);
    }

    private static class SharedPreferencesCompat {

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        static void apply(SharedPreferences.Editor editor) {
            try {
                Method applyMethod = findApplyMethod();
                if (applyMethod != null) {
                    applyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }

    /**
     * 存储蓝牙设备地址
     * 针对一体机
     *
     * @param device
     */
    public static void saveBleDeviceAddress(BluetoothDevice device) {
        if (device != null) {
            put("connectedAddress", device.getAddress());
            put("atoAddress", device.getAddress());
        }
    }

    /**
     * 获取已连接设备地址
     *
     * @return
     */
    public static String getConnectedAddress() {
        return getString("connectedAddress", "");
    }

    /**
     * 获取一体机设备地址
     *
     * @return
     */
    public static String getAtoDeviceAddress() {
        return getString("atoAddress", "");
    }

    /**
     * 清除一体机设备地址
     */
    public static void clearAtoDeviceAdress(){
        put("atoAddress", "");
        put("connectedAddress", "");
    }

    /**
     * 清除设备地址
     * 针对一体机
     */
    public static void clearBleDeviceAddress() {
        put("connectedAddress", "");
    }

    /**
     * 存储蓝牙设备地址
     * 针对体重秤
     */
    public static void saveWeightDeviceAddress(String address) {
        put("weightAddress", address);
    }

    /**
     * 获取一体机设备地址
     *
     * @return
     */
    public static String getWeightDeviceAddress() {
        return getString("weightAddress", "");
    }

    /**
     * 清除设备地址
     * 针对体重秤
     */
    public static void clearWeightDeviceAddress() {
        put("weightAddress", "");
    }

}
