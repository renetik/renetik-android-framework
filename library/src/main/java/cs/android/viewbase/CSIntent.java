package cs.android.viewbase;

import android.content.Intent;

import java.io.Serializable;

import cs.android.CSActivityInterface;

/**
 * Created by Rene Dohan on 22/04/15.
 */
public class CSIntent {

    public static Intent create(CSActivityInterface activity, Class type, String key, String value) {
        Intent intent = new Intent(activity.activity(), type);
        intent.putExtra(key, value);
        return intent;
    }

    public static Intent create(CSActivityInterface activity, Class type, String key, Serializable value) {
        Intent intent = new Intent(activity.activity(), type);
        intent.putExtra(key, value);
        return intent;
    }

}
