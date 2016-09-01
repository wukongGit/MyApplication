package com.suncheng.myapplication.framework;

import android.app.Application;
import android.content.res.Configuration;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.suncheng.crashcatch.CrashHandler;
import com.yolanda.nohttp.NoHttp;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    /**
     * 获取当前应用全局的Application，注意使用该方法必须要在AndroidManifest.xml中配置MyApplication
     *
     * @Title: getAppContext
     * @return Application
     */
    public static MyApplication getApp() {
        if (null == myApplication) {
            throw new RuntimeException("请在AndroidManifest.xml中配置MyApplication");
        }
        return myApplication;
    }

    /**
     * Called when the application is starting, before any other application
     * objects have been created. Implementations should be as quick as possible
     * (for example using lazy initialization of state) since the time spent in
     * this function directly impacts the performance of starting the first
     * activity, service, or receiver in a process. If you override this method,
     * be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
        AppConfig.getInstance();
        NoHttp.initialize(this);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

    /**
     * Called when the application is stopping. There are no more application
     * objects running and the process will exit. <em>Note: never depend on
     * this method being called; in many cases an unneeded application process
     * will simply be killed by the kernel without executing any application
     * code.</em> If you override this method, be sure to call
     * super.onTerminate().
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onLowMemory() {
        super.onLowMemory();
    }
}
