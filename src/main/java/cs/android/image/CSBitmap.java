package cs.android.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Bitmap.createScaledBitmap;
import static android.graphics.Matrix.ScaleToFit.CENTER;
import static cs.java.lang.Lang.close;
import static cs.java.lang.Lang.error;

public class CSBitmap {

//    public static void resizeImage(final String file, int maxTargetWidth, int maxTargetHeight, Context context) {
//        with(context).load(file).resize(maxTargetWidth, maxTargetHeight).memoryPolicy(MemoryPolicy.NO_CACHE).
//                networkPolicy(NO_CACHE).centerInside().into(new Target() {
//            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {
//                try {
//                    FileOutputStream out = new FileOutputStream(file);
//                    bitmap.compress(JPEG, 80, out);
//                    close(out);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onBitmapFailed(Drawable drawable) {
//                warn("CSBitmap resizeImage onBitmapFailed", drawable);
//            }
//
//            public void onPrepareLoad(Drawable drawable) {
//
//            }
//        });
//    }

    public static void resizeImage(String file, int maxTargetWidth, int maxTargetHeight) {
        try {
            InputStream in = new FileInputStream(file);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            close(in);

            int inWidth = options.outWidth;
            int inHeight = options.outHeight;

            in = new FileInputStream(file);
            options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(inWidth / maxTargetWidth, inHeight / maxTargetHeight);
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, maxTargetWidth, maxTargetHeight);
            m.setRectToRect(inRect, outRect, CENTER);
            float[] values = new float[9];
            m.getValues(values);

            Bitmap resizedBitmap = createScaledBitmap(roughBitmap,
                    (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]),
                    true);

            resizedBitmap = rotateBitmap(file, resizedBitmap);
            FileOutputStream out = new FileOutputStream(file);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            close(out);
        } catch (Exception e) {
            error(e);
        }
    }

    private static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = new ExifInterface(src).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            if (orientation == 1) return bitmap;

            Matrix matrix = new Matrix();
            if (orientation == 2) matrix.setScale(1, 1);
            else if (orientation == 3) matrix.setRotate(180);
            else if (orientation == 4) {
                matrix.setRotate(180);
                matrix.postScale(1, 1);
            } else if (orientation == 5) {
                matrix.setRotate(90);
                matrix.postScale(1, 1);
            } else if (orientation == 6) {
                matrix.setRotate(90);
            } else if (orientation == 7) {
                matrix.setRotate(90);
                matrix.postScale(1, 1);
            } else if (orientation == 8) {
                matrix.setRotate(90);
            } else return bitmap;
            try {
                Bitmap oriented = createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                error(e);
                return bitmap;
            }
        } catch (IOException e) {
            error(e);
        }
        return bitmap;
    }

}
