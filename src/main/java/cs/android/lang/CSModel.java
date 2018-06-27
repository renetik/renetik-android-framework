package cs.android.lang;


import java.io.File;

public interface CSModel {

    String name();

    CSLogger logger();

    File cacheDir();

    File dataDir();

    boolean isDebugBuild();

}
