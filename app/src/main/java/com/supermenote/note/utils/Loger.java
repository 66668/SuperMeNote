package com.supermenote.note.utils;

import android.text.TextUtils;
import android.util.Log;


/**
 * 最全信息打印的log封装
 *
 * <p>
 * 使用步骤1：在Application中初始化
 * 在代码中要打印log,就直接Loger.d(....).
 */

public class Loger {
    public static boolean DEBUG = false;
    private static String TAG = "SJY";
    private static final int LOG_I = 0;
    private static final int LOG_V = 1;
    private static final int LOG_D = 2;
    private static final int LOG_W = 3;
    private static final int LOG_E = 4;

    public static void init(boolean isDebug) {
        DEBUG = isDebug;
        //大写的日志，默认为作者姓名简写
        init(isDebug, "SJY");
    }

    public static void init(boolean isDebug, String logTag) {
        DEBUG = isDebug;
        if (!TextUtils.isEmpty(logTag)) {
            TAG = logTag;
        }
    }

    //-----------------------------------------------------------------
    //-----------------------------d------------------------------------
    //-----------------------------------------------------------------

    public static void d(int message) {
        if (isDebug()) {
            Log.d(TAG, getLogMessage(String.valueOf(message)));
        }
    }

    public static void D(String tag, int message) {
        if (isDebug()) {
            Log.d(tag, getLogMessage(String.valueOf(message)));
        }
    }

    public static void d(Object... message) {
        if (isDebug()) {
            show(LOG_D, TAG, getLogcat(message));
        }
    }


    public static void D(String tag, Object... message) {
        if (isDebug()) {
            show(LOG_D, tag, getLogcat(message));
        }
    }


    //-----------------------------------------------------------------
    //-----------------------------i------------------------------------
    //-----------------------------------------------------------------

    public static void i(Object... message) {
        if (isDebug()) {
            Log.i(TAG, getLogMessage(String.valueOf(message)));
        }
    }

    public static void I(String tag, int message) {
        if (isDebug()) {
            Log.i(tag, getLogMessage(String.valueOf(message)));
        }
    }

    public static void i(int message) {
        if (isDebug()) {
            show(LOG_I, TAG, getLogcat(message));
        }
    }

    public static void I(String tag, Object... message) {
        if (isDebug()) {
            show(LOG_I, tag, getLogcat(message));
        }
    }
    //-----------------------------------------------------------------
    //-----------------------------w------------------------------------
    //-----------------------------------------------------------------

    public static void w(int message) {
        if (isDebug()) {
            Log.w(TAG, getLogMessage(String.valueOf(message)));
        }
    }

    public static void W(String tag, int message) {
        if (isDebug()) {
            Log.w(tag, getLogMessage(String.valueOf(message)));
        }
    }

    public static void w(Object... message) {
        if (isDebug()) {
            show(LOG_W, TAG, getLogcat(message));
        }
    }

    public static void W(String tag, Object... message) {
        if (isDebug()) {
            show(LOG_W, tag, getLogcat(message));
        }
    }

    //-----------------------------------------------------------------
    //-----------------------------e------------------------------------
    //-----------------------------------------------------------------

    public static void e(int message) {
        if (isDebug()) {
            Log.e(TAG, getLogMessage(String.valueOf(message)));
        }
    }

    public static void E(String tag, int message) {
        if (isDebug()) {
            Log.e(tag, getLogMessage(String.valueOf(message)));
        }
    }

    public static void e(Object... message) {
        if (isDebug()) {
            show(LOG_E, TAG, getLogcat(message));
        }
    }

    public static void E(String tag, Object... message) {
        if (isDebug()) {
            show(LOG_E, tag, getLogcat(message));
        }
    }


    //-----------------------------------------------------------------
    //-----------------------------v------------------------------------
    //-----------------------------------------------------------------

    public static void v(int message) {
        if (isDebug()) {
            Log.v(TAG, getLogMessage(String.valueOf(message)));
        }
    }

    public static void V(String tag, int message) {
        if (isDebug()) {
            Log.e(tag, getLogMessage(String.valueOf(message)));
        }
    }


    public static void v(Object... message) {
        if (isDebug()) {
            show(LOG_V, TAG, getLogcat(message));
        }
    }

    public static void V(String tag, Object... message) {
        if (isDebug()) {
            show(LOG_V, tag, getLogcat(message));
        }
    }


    //******************************************************************************************
    //******************************************************************************************
    //******************************************************************************************

    private static boolean isDebug() {
        return DEBUG;
    }

    private static String getLogcat(Object... message) {
        StringBuilder sb = new StringBuilder();

        if (message != null) {
            for (Object object : message) {
                sb.append("--->> ");
                sb.append(object);
            }
        }

        sb.append("<<---");
        return sb.toString();
    }

    private static String getLogMessage(String message) {
        //        String timeFor24 = LFDateTimeUtils.getTimeForSS(new Date(System.currentTimeMillis()));
        return "time--".concat(System.currentTimeMillis() + "").concat("--").concat(message);
    }


    /**
     * 大于3000长度的数据也能完整打印
     *
     * @param type
     * @param tag
     * @param msg
     */
    private static void show(int type, String tag, String msg) {

        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) return;
        msg = msg.trim();
        int index = 0;
        int segmentSize = 3 * 1024;
        String logContent;
        while (index < msg.length()) {
            if (msg.length() <= index + segmentSize) {
                logContent = msg.substring(index);
            } else {
                logContent = msg.substring(index, segmentSize + index);
            }
            index += segmentSize;
            switch (type) {
                case LOG_I:
                    Log.i(TAG, logContent.trim());
                    break;
                case LOG_V:
                    Log.v(TAG, logContent.trim());
                    break;
                case LOG_D:
                    Log.d(TAG, logContent.trim());
                    break;
                case LOG_W:
                    Log.w(TAG, logContent.trim());
                    break;
                case LOG_E:
                    Log.w(TAG, logContent.trim());
                    break;
                default:
                    break;
            }
        }
    }

}