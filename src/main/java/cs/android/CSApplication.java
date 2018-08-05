package cs.android;

import android.app.Application;

import static cs.java.lang.CSLang.info;
import static cs.java.lang.CSLang.model;

public class CSApplication extends Application {

    private static CSApplication application;

    public CSApplication() {
        application = this;
    }

    public static CSApplication application() {
        return application;
    }

    public void onLowMemory() {
        super.onLowMemory();
        info("onLowMemory");
        model().logger().onLowMemory();
    }

    public void onTerminate() {
        super.onTerminate();
        info("onTerminate");
    }
}
