package cs.android;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import cs.android.viewbase.CSActivity;

public interface CSActivityInterface extends CSContextInterface {
    <T extends AppCompatActivity & CSActivity> T activity();

    Context context();
}
