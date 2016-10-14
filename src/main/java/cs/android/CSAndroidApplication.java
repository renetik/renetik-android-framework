package cs.android;

import android.app.Application;

import com.androidquery.callback.BitmapAjaxCallback;

import cs.java.lang.Lang;

import static cs.java.lang.Lang.info;

public class CSAndroidApplication extends Application {

    private static CSAndroidApplication _instance;

    public CSAndroidApplication() {
        _instance = this;
    }

    public static CSAndroidApplication instance() {
        return _instance;
    }

    public void onLowMemory() {
        info("onLowMemory");
        BitmapAjaxCallback.clearCache();
        Lang.application().logger().onLowMemory();
        super.onLowMemory();
    }

    public void onTerminate() {
        info("onTerminate");
        super.onTerminate();
    }

    public void onCreate() {
        super.onCreate();
    }

}
