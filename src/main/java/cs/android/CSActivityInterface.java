package cs.android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import cs.android.viewbase.CSActivity;

public interface CSActivityInterface extends CSContextInterface {
    <T extends AppCompatActivity & CSActivity> T activity();

    Context context();
}
