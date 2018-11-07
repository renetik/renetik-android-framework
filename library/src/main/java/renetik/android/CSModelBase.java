package renetik.android;

import java.io.File;

import renetik.android.model.CSValueStore;

import static renetik.android.CSApplication.application;
import static renetik.android.lang.CSLang.setModel;

public abstract class CSModelBase implements renetik.android.lang.CSModel {

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
