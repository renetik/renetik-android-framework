package cs.android;

import android.app.Activity;
import android.content.Context;

import cs.android.viewbase.CSActivity;

public interface CSActivityInterface extends CSContextInterface {
    <T extends Activity & CSActivity> T activity();

    Context context();
}
