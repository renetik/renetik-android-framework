package cs.android;

import java.io.File;

import cs.java.lang.CSLang;

public abstract class CSModelBase implements cs.android.lang.CSModel {

    public CSModelBase() {
        CSLang.setApplication(this);
    }

    public File cacheDir() {
        return CSApplication.instance().getCacheDir();
    }

    public File dataDir() {
        return CSApplication.instance().getFilesDir();
    }

}
