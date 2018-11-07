package cs.android;

import java.io.File;

import cs.android.model.CSValueStore;

import static cs.android.CSApplication.application;
import static cs.java.lang.CSLang.setModel;

public abstract class CSModelBase implements cs.android.lang.CSModel {

    private CSValueStore store = new CSValueStore("ApplicationSettings");

    public CSModelBase() {
        setModel(this);
    }

    public File cacheDir() {
        return application().getCacheDir();
    }

    public File dataDir() {
        return application().getFilesDir();
    }

    public CSValueStore store() { return store;}

}
