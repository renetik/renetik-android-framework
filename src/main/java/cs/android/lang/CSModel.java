package cs.android.lang;


import java.io.File;

import androidx.annotation.NonNull;
import cs.android.model.CSValueStore;

public interface CSModel {

    @NonNull
    String applicationName();

    @NonNull
    CSLogger logger();

    File cacheDir();

    File dataDir();

    boolean isDebugBuild();

    CSValueStore store();

}
