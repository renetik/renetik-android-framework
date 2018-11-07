package renetik.android;

import android.app.Application;

import androidx.annotation.NonNull;

import static renetik.android.lang.CSLang.info;
import static renetik.android.lang.CSLang.model;

public class CSApplication extends Application {

    private static CSApplication application;

    public CSApplication() {
        application = this;
    }

    @NonNull
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
