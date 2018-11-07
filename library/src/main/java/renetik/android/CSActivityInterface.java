package renetik.android;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import renetik.android.viewbase.CSActivity;

public interface CSActivityInterface extends CSContextInterface {
    <T extends AppCompatActivity & CSActivity> T activity();

    Context context();
}
