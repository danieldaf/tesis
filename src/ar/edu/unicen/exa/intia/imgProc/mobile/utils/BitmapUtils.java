package ar.edu.unicen.exa.intia.imgProc.mobile.utils;

import java.io.File;

import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapUtils {

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	public static BitmapFactory.Options buildBmpOptions(String filePath, int width, int height) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		opts.inSampleSize = BitmapUtils.calculateInSampleSize(opts, width, height);
		opts.inJustDecodeBounds = false;
		return opts;
	}
	
	public static BitmapFactory.Options buildBmpOptions(Uri fileUri, int width, int height) {
		String filePath = pathFromUri(fileUri);
		BitmapFactory.Options result = null;
		if (fileUri != null) {
			result = buildBmpOptions(filePath, width, height);
		}
		return result;
	}
	
	public static String pathFromUri(Uri fileUri) {
		String filePath = null;
		if (fileUri != null && "file".equals(fileUri.getScheme())) {
			filePath = fileUri.toString().substring("file://".length());
		}
		return filePath;
	}
	
	public static Uri uriFromPath(String filePath) {
		return Uri.fromFile(new File(filePath)); 		
	}
}
