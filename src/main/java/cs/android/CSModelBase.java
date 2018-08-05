package cs.android;

import java.io.File;

import cs.android.model.CSSettings;

import static cs.android.CSApplication.application;
import static cs.java.lang.CSLang.setModel;

public abstract class CSModelBase implements cs.android.lang.CSModel {

    private CSSettings settings = new CSSettings("ApplicationSettings");

    public CSModelBase() {
        setModel(this);
    }

    public File cacheDir() {
        return application().getCacheDir();
    }

    public File dataDir() {
        return application().getFilesDir();
    }

    public CSSettings settings() { return settings;}

}
