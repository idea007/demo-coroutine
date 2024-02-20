package com.dafay.demo.lib.base.utils;

import android.util.Log;


/**
 * @description:
 * @Author: lipengfei
 * @Date: 2023/3/9
 * @Last Modified by: lipengfei
 * @Last Modified time: 2023/3/9
 */
public class LogUtils {
    private static boolean isDebug = true;
    //    private static boolean isDebug = BuildConfig.DEBUG;
    private static final String PREFIX = "======> ";
    private static final String DEFAULT_TAG = "LogUtils";

    private LogUtils() {
    }

    private static String getTag(String tag) {
        if (null != tag) {
            return tag;
        }
        StackTraceElement stackTraceElement = getLogStackTrackInfo();
        if (null == stackTraceElement) {
            return DEFAULT_TAG;
        }
        return getSimpleClassName(getLogStackTrackInfo().getClassName());
    }

    /**
     * 获取栈信息
     */
    private static StackTraceElement getLogStackTrackInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement e : stackTrace) {
            if (!e.getClassName().startsWith("dalvik.system.VMStack")
                    && !e.getClassName().startsWith("java.lang.Thread")
                    && !e.getClassName().startsWith(LogUtils.class.getName())
            ) {
                return e;
            }
        }
        return null;
    }

    private static String getSimpleClassName(String className) {
        if (null == className) {
            return "";
        }
        return className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * 构建日志信息 [线程名]类名.方法名（代码行数）
     */
    private static String buildLogHeader() {
        StackTraceElement ste = getLogStackTrackInfo();
        StringBuilder buf = new StringBuilder("[");
        buf.append(Thread.currentThread().getName()).append("]");
        if (ste == null) {
            return buf.append(":").toString();
        }
//        buf.append(getSimpleClassName(ste.getClassName())).append(".").append(ste.getMethodName());
        if (ste.isNativeMethod()) {
            buf.append("(Native Method)");
        } else {
            String fName = ste.getFileName();
            if (fName == null) {
                buf.append("(Unknown Source)");
            } else {
                int lineNum = ste.getLineNumber();
                buf.append('(');
                buf.append(fName);
                if (lineNum >= 0) {
                    buf.append(':').append(lineNum);
                }
                buf.append("):");
            }
        }
        buf.append(" ");
        return buf.toString();
    }

    public static void d(String msg) {
        d(getTag(null), msg, null);
    }

    public static void d(String tag, String msg) {
        d(getTag(tag), msg, null);
    }

    public static void d(String msg, Throwable throwable) {
        d(getTag(null), msg, throwable);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (!isDebug) {
            return;
        }
        Log.d(getTag(tag), PREFIX + buildLogHeader() + msg, throwable);
    }

    public static void i(String msg) {
        i(getTag(null), msg, null);
    }

    public static void i(String tag, String msg) {
        d(getTag(tag), msg, null);
    }

    public static void i(String msg, Throwable throwable) {
        i(getTag(null), msg, throwable);
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (!isDebug) {
            return;
        }
        Log.i(getTag(tag), PREFIX + buildLogHeader() + msg, throwable);
    }

    public static void w(String msg) {
        w(getTag(null), msg, null);
    }

    public static void w(String tag, String msg) {
        w(getTag(tag), msg, null);
    }

    public static void w(String msg, Throwable throwable) {
        w(getTag(null), msg, throwable);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (!isDebug) {
            return;
        }
        Log.e(getTag(tag), PREFIX + buildLogHeader() + msg, throwable);
    }

    public static void e(String msg) {
        e(getTag(null), msg, null);
    }

    public static void e(String tag, String msg) {
        e(getTag(tag), msg, null);
    }

    public static void e(String msg, Throwable throwable) {
        e(getTag(null), msg, throwable);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (!isDebug) {
            return;
        }
        Log.e(getTag(tag), PREFIX + buildLogHeader() + msg, throwable);
    }
}
