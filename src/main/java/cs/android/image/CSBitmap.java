package cs.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static cs.java.lang.Lang.close;
import static cs.java.lang.Lang.warn;

public class CSBitmap {

    public static void resizeImage(final String file, int maxTargetWidth, int maxTargetHeight, Context context) {
        Picasso.with(context).load(file).resize(maxTargetWidth, maxTargetHeight).into(new Target() {
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    close(out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            public void onBitmapFailed(Drawable drawable) {
                warn("onBitmapFailed", drawable);
            }

            public void onPrepareLoad(Drawable drawable) {

            }
        });
    }
}
