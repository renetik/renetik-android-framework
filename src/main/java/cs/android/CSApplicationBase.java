package cs.android;

import java.io.File;

import cs.android.lang.CSApplication;
import cs.android.viewbase.CSContextController;
import cs.java.lang.CSLang;

public abstract class CSApplicationBase extends CSContextController implements CSApplication {

    public CSApplicationBase() {
        super(CSAndroidApplication.applicationInstance());
        CSLang.setApplication(this);
    }

    public File cacheDir() {
        return context().getCacheDir();
    }

    public File dataDir() {
        return context().getFilesDir();
    }

    public String version() {
        return getPackageInfo().versionCode + "-" + getPackageInfo().versionName;
    }
}
