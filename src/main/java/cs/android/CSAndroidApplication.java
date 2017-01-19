package cs.android;

import android.support.multidex.MultiDexApplication;

import cs.java.lang.CSLang;

import static cs.java.lang.CSLang.info;

public class CSAndroidApplication extends MultiDexApplication {

    private static CSAndroidApplication _instance;

    public CSAndroidApplication() {
        _instance = this;
    }

    public static CSAndroidApplication instance() {
        return _instance;
    }

    public void onLowMemory() {
        info("onLowMemory");
        CSLang.application().logger().onLowMemory();
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
