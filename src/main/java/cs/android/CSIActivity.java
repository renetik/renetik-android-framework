package cs.android;

import android.app.Activity;
import android.content.Context;
import cs.android.viewbase.CSActivity;

public interface CSIActivity extends CSIContext {
	<T extends Activity & CSActivity> T activity();

	@Override Context context();
}
