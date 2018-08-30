package cs.android.lang;


import java.io.File;

import cs.android.model.CSValueStore;

public interface CSModel {

    String applicationName();

    CSLogger logger();

    File cacheDir();

    File dataDir();

    boolean isDebugBuild();

    CSValueStore store();

}
