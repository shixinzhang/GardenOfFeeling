package sxkeji.net.dailydiary.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by zhangshixin on 2015/11/26.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class MediaUtils {
    /**
     * 水平方向模糊度
     */
    private static float hRadius = 10;
    /**
     * 竖直方向模糊度
     */
    private static float vRadius = 10;
    /**
     * 模糊迭代度
     */
    private static int iterations = 7;

    private MediaUtils() {
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static Drawable
    BoxBlurFilter(Bitmap bmp) {
        int width
                = bmp.getWidth();
        int height
                = bmp.getHeight();
        int[]
                inPixels = new int[width
                * height];
        int[]
                outPixels = new int[width
                * height];
        Bitmap
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.getPixels(inPixels,
                0,
                width, 0,
                0,
                width, height);
        for (int i
             = 0;
             i < iterations; i++) {
            blur(inPixels,
                    outPixels, width, height, hRadius);
            blur(outPixels,
                    inPixels, height, width, vRadius);
        }
        blurFractional(inPixels,
                outPixels, width, height, hRadius);
        blurFractional(outPixels,
                inPixels, height, width, vRadius);
        bitmap.setPixels(inPixels,
                0,
                width, 0,
                0,
                width, height);
        Drawable
                drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public static void blur(int[]
                                    in, int[]
                                    out, int width,
                            int height,
                            float radius) {
        int widthMinus1
                = width - 1;
        int r
                = (int)
                radius;
        int tableSize
                = 2 *
                r + 1;
        int divide[]
                = new int[256 *
                tableSize];

        for (int i
             = 0;
             i < 256 *
                     tableSize; i++)
            divide[i]
                    = i / tableSize;

        int inIndex
                = 0;

        for (int y
             = 0;
             y < height; y++) {
            int outIndex
                    = y;
            int ta
                    = 0,
                    tr = 0,
                    tg = 0,
                    tb = 0;

            for (int i
                 = -r; i <= r; i++) {
                int rgb
                        = in[inIndex + clamp(i, 0,
                        width - 1)];
                ta
                        += (rgb >> 24)
                        & 0xff;
                tr
                        += (rgb >> 16)
                        & 0xff;
                tg
                        += (rgb >> 8)
                        & 0xff;
                tb
                        += rgb & 0xff;
            }

            for (int x
                 = 0;
                 x < width; x++) {
                out[outIndex]
                        = (divide[ta] << 24)
                        | (divide[tr] << 16)
                        |
                        (divide[tg] << 8)
                        | divide[tb];

                int i1
                        = x + r + 1;
                if (i1
                        > widthMinus1)
                    i1
                            = widthMinus1;
                int i2
                        = x - r;
                if (i2
                        < 0)
                    i2
                            = 0;
                int rgb1
                        = in[inIndex + i1];
                int rgb2
                        = in[inIndex + i2];

                ta
                        += ((rgb1 >> 24)
                        & 0xff)
                        - ((rgb2 >> 24)
                        & 0xff);
                tr
                        += ((rgb1 & 0xff0000)
                        - (rgb2 & 0xff0000))
                        >> 16;
                tg
                        += ((rgb1 & 0xff00)
                        - (rgb2 & 0xff00))
                        >> 8;
                tb
                        += (rgb1 & 0xff)
                        - (rgb2 & 0xff);
                outIndex
                        += height;
            }
            inIndex
                    += width;
        }
    }

    public static void blurFractional(int[]
                                              in, int[]
                                              out, int width,
                                      int height,
                                      float radius) {
        radius
                -= (int)
                radius;
        float f
                = 1.0f
                / (1 +
                2 *
                        radius);
        int inIndex
                = 0;

        for (int y
             = 0;
             y < height; y++) {
            int outIndex
                    = y;

            out[outIndex]
                    = in[0];
            outIndex
                    += height;
            for (int x
                 = 1;
                 x < width - 1;
                 x++) {
                int i
                        = inIndex + x;
                int rgb1
                        = in[i - 1];
                int rgb2
                        = in[i];
                int rgb3
                        = in[i + 1];

                int a1
                        = (rgb1 >> 24)
                        & 0xff;
                int r1
                        = (rgb1 >> 16)
                        & 0xff;
                int g1
                        = (rgb1 >> 8)
                        & 0xff;
                int b1
                        = rgb1 & 0xff;
                int a2
                        = (rgb2 >> 24)
                        & 0xff;
                int r2
                        = (rgb2 >> 16)
                        & 0xff;
                int g2
                        = (rgb2 >> 8)
                        & 0xff;
                int b2
                        = rgb2 & 0xff;
                int a3
                        = (rgb3 >> 24)
                        & 0xff;
                int r3
                        = (rgb3 >> 16)
                        & 0xff;
                int g3
                        = (rgb3 >> 8)
                        & 0xff;
                int b3
                        = rgb3 & 0xff;
                a1
                        = a2 + (int)
                        ((a1 + a3) * radius);
                r1
                        = r2 + (int)
                        ((r1 + r3) * radius);
                g1
                        = g2 + (int)
                        ((g1 + g3) * radius);
                b1
                        = b2 + (int)
                        ((b1 + b3) * radius);
                a1
                        *= f;
                r1
                        *= f;
                g1
                        *= f;
                b1
                        *= f;
                out[outIndex]
                        = (a1 << 24)
                        | (r1 << 16)
                        | (g1 << 8)
                        | b1;
                outIndex
                        += height;
            }
            out[outIndex]
                    = in[width - 1];
            inIndex
                    += width;
        }
    }

    public static int clamp(int x,
                            int a,
                            int b) {
        return (x
                < a) ? a : (x > b) ? b : x;
    }
}