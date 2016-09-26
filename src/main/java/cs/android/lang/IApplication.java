package cs.android.lang;


import java.io.File;

public interface IApplication extends IsContext {

	String name();

	CSLogger logger();

	File cacheDir();

	File dataDir();

	boolean isDebugBuild();

	String version();
}
