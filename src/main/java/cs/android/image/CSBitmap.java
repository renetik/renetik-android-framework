package cs.android.image;

import static cs.java.lang.Lang.close;
import static cs.java.lang.Lang.error;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;

public class CSBitmap {

	private static final int NO_COLOR = 0x00000001;

	public static NinePatchDrawable createDrawableWithCapInsets(Resources res, Bitmap bitmap,
			int top, int left, int bottom, int right, String srcName) {
		ByteBuffer buffer = getByteBuffer(top, left, bottom, right);
		NinePatchDrawable drawable = new NinePatchDrawable(res, bitmap, buffer.array(), new Rect(),
				srcName);
		return drawable;
	}

	public static NinePatch createNinePatch(Resources res, Bitmap bitmap, int top, int left,
			int bottom, int right, String srcName) {
		ByteBuffer buffer = getByteBuffer(top, left, bottom, right);
		NinePatch patch = new NinePatch(bitmap, buffer.array(), srcName);
		return patch;
	}

	private static ByteBuffer getByteBuffer(int top, int left, int bottom, int right) {
		// Docs check the NinePatchChunkFile
		ByteBuffer buffer = ByteBuffer.allocate(56).order(ByteOrder.nativeOrder());
		// was translated
		buffer.put((byte) 0x01);
		// divx size
		buffer.put((byte) 0x02);
		// divy size
		buffer.put((byte) 0x02);
		// color size
		buffer.put((byte) 0x02);

		// skip
		buffer.putInt(0);
		buffer.putInt(0);

		// padding
		buffer.putInt(0);
		buffer.putInt(0);
		buffer.putInt(0);
		buffer.putInt(0);

		// skip 4 bytes
		buffer.putInt(0);

		buffer.putInt(left);
		buffer.putInt(right);
		buffer.putInt(top);
		buffer.putInt(bottom);
		buffer.putInt(NO_COLOR);
		buffer.putInt(NO_COLOR);

		return buffer;
	}

	private static int getExifOrientation(String src) throws IOException {
		ExifInterface exif = new ExifInterface(src);
		return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
		// if (Build.VERSION.SDK_INT >= 5) {
		// Class<?> exifClass = Class.forName("android.media.ExifInterface");
		// Constructor<?> exifConstructor = exifClass.getConstructor(new Class[] {
		// String.class });
		// Object exifInstance = exifConstructor.newInstance(new Object[] { src });
		// Method getAttributeInt = exifClass.getMethod("getAttributeInt", new
		// Class[] { String.class,
		// int.class });
		// Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
		// String tagOrientation = (String) tagOrientationField.get(null);
		// orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[]
		// { tagOrientation,
		// 1 });
		// }
	}

	public static void resizeImage(String file, int maxTargetWidth, int maxTargetHeight) {
		try {
			InputStream in = new FileInputStream(file);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			close(in);
			in = null;

			int inWidth = options.outWidth;
			int inHeight = options.outHeight;

			// decode full image pre-resized
			in = new FileInputStream(file);
			options = new BitmapFactory.Options();
			// Rough re-size (this is no exact resize)
			options.inSampleSize = Math.max(inWidth / maxTargetWidth, inHeight / maxTargetHeight);
			// decode full image
			Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

			// exact destination size
			Matrix m = new Matrix();
			RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
			RectF outRect = new RectF(0, 0, maxTargetWidth, maxTargetHeight);
			m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
			float[] values = new float[9];
			m.getValues(values);

			// resize bitmap
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
					(int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]),
					true);

			resizedBitmap = rotateBitmap(file, resizedBitmap);
			//
			// save image
			FileOutputStream out = new FileOutputStream(file);
			resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			close(out);
		} catch (Exception e) {
			error(e);
		}
	}

	public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
		try {
			int orientation = getExifOrientation(src);

			if (orientation == 1) return bitmap;

			Matrix matrix = new Matrix();
			switch (orientation) {
			case 2:
				matrix.setScale(-1, 1);
				break;
			case 3:
				matrix.setRotate(180);
				break;
			case 4:
				matrix.setRotate(180);
				matrix.postScale(-1, 1);
				break;
			case 5:
				matrix.setRotate(90);
				matrix.postScale(-1, 1);
				break;
			case 6:
				matrix.setRotate(90);
				break;
			case 7:
				matrix.setRotate(-90);
				matrix.postScale(-1, 1);
				break;
			case 8:
				matrix.setRotate(-90);
				break;
			default:
				return bitmap;
			}

			try {
				Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
						matrix, true);
				bitmap.recycle();
				return oriented;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				return bitmap;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

}
