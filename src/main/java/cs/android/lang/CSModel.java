package cs.android.lang;


import java.io.File;

import cs.android.model.CSSettings;

public interface CSModel {

    String applicationName();

    CSLogger logger();

    File cacheDir();

    File dataDir();

    boolean isDebugBuild();

    CSSettings settings();

}
