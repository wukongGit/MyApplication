package com.suncheng.myapplication.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class AndroidUtils {

    private static int GL_MAX_TEXTURE_SIZE = 0;

    static {
        try {
            GL_MAX_TEXTURE_SIZE = glMaxTextureSize();
        } catch (Throwable ignored) {
        }
    }

    public static void goHome(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            activity.moveTaskToBack(true);
        }
    }

    public static void goHome1(Activity activity) {
        activity.moveTaskToBack(true);
    }

    public static void showInputMethod(Activity context, View v) {
        InputMethodManager manager =
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.showSoftInput(v, 0);
    }

    public static void showInputMethod(Context context, View v) {
        InputMethodManager manager =
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.showSoftInput(v, 0);
        }
    }

    public static void hideInputMethod(Activity activity) {
        View view = activity.getCurrentFocus();
        if (null != view)
            hideInputMethod(activity, view);
    }

    public static void hideInputMethod(Context context, View v) {
        hideInputMethod(context, v.getWindowToken());
    }

    public static void hideInputMethod(Context context, IBinder token) {
        InputMethodManager manager =
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void market(Activity activity, String packageName) {
        String str = String.format("market://details?id=%s", packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(str));
        activity.startActivity(intent);
    }

    public static void checkNotMain() {
        if (isMain()) {
            throw new IllegalStateException("Method call should not happen from the main thread.");
        }
    }

    public static void checkMain() {
        if (!isMain()) {
            throw new IllegalStateException("Method call should happen from the main thread.");
        }
    }

    public static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static int getGlMaxTextureSize() {
        return GL_MAX_TEXTURE_SIZE;
    }

    private static int glMaxTextureSize() {
        EGL10 egl = (EGL10) EGLContext.getEGL();

        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);

        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER, EGL10.EGL_LEVEL, 0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT, EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {
            // TROUBLE! No config found.
        }
        EGLConfig config = configs[0];

        int[] surfAttr = {
                EGL10.EGL_WIDTH, 64, EGL10.EGL_HEIGHT, 64, EGL10.EGL_NONE
        };
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;  // missing in EGL10
        int[] ctxAttrib = {
                EGL_CONTEXT_CLIENT_VERSION, 1, EGL10.EGL_NONE
        };
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }

    public static boolean isPackageInstall(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return versionCode;
    }

    public static String getAppName(Context context) {
        String versionName = null;
        try {
            int nameRes = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes;
            if (nameRes != 0) versionName = context.getString(nameRes);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return versionName;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null) {
            return info.getType();
        }
        return -1;
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo wiFiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wiFiNetworkInfo != null) {
            return wiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager manager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo mobileNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetworkInfo != null) {
            return mobileNetworkInfo.isAvailable();
        }
        return false;
    }

    public static int getStatusBarHeight(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavBarHeight(Resources res) {
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return res.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    public static int dp2px(Context context, float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
}
