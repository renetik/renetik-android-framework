package cs.android;

import android.app.Application;

import cs.java.lang.CSLang;

import static cs.java.lang.CSLang.info;

public class CSApplication extends Application {

    private static CSApplication _instance;

    public CSApplication() {
        _instance = this;
    }

    public static CSApplication instance() {
        return _instance;
    }

    public void onLowMemory() {
        info("onLowMemory");
        CSLang.model().logger().onLowMemory();
        super.onLowMemory();
    }

    public void onTerminate() {
//        info("onTerminate");
        super.onTerminate();
    }

    public void onCreate() {
        super.onCreate();
    }

}
