package cs.android;

import android.app.Activity;
import android.content.Context;
import cs.android.viewbase.CSActivity;

public interface HasActivity extends HasContext {
	<T extends Activity & CSActivity> T activity();

	@Override Context context();
}
