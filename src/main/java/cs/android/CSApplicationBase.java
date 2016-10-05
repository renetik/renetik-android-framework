package cs.android;

import java.io.File;

import cs.android.lang.IApplication;
import cs.android.viewbase.CSContextController;
import cs.java.lang.Lang;

public abstract class CSApplicationBase extends CSContextController implements IApplication {

    public CSApplicationBase() {
        super(CSAndroidApplication.instance());
        Lang.setApplication(this);
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
