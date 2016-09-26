package cs.android.viewbase;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;

import cs.android.HasActivity;

/**
 * Created by Rene Dohan on 22/04/15.
 */
public class CSIntent {

    public static Intent create(HasActivity activity, Class type, String key, String value) {
        Intent intent = new Intent(activity.activity(), type);
        intent.putExtra(key, value);
        return intent;
    }

    public static Intent create(HasActivity activity, Class type, String key, Serializable value) {
        Intent intent = new Intent(activity.activity(), type);
        intent.putExtra(key, value);
        return intent;
    }

}
