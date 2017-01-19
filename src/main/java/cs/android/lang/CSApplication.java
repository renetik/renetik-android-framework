package cs.android.lang;


import java.io.File;

public interface CSApplication {

    String name();

    CSLogger logger();

    File cacheDir();

    File dataDir();

    boolean isDebugBuild();

    String version();

    String getString(int resourceId);
}
