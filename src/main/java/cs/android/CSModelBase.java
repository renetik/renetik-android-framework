package cs.android;

import java.io.File;

import cs.android.model.CSSettings;
import cs.java.lang.CSLang;

public abstract class CSModelBase implements cs.android.lang.CSModel {

    private CSSettings settings = new CSSettings("ApplicationSettings");

    public CSModelBase() {
        CSLang.setApplication(this);
    }

    public File cacheDir() {
        return CSApplication.application().getCacheDir();
    }

    public File dataDir() {
        return CSApplication.application().getFilesDir();
    }

    public CSSettings settings() { return settings;}

}
