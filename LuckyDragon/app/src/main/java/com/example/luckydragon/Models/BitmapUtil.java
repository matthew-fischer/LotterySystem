package com.example.luckydragon.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {
    /**
     * Converts bitmap to string
     * @param image: the bitmap to convert to base 64 string
     * @return image encoded as string
     */
    public static String bitmapToString(Bitmap image) {
        // reference: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        if (image == null) return "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Converts base64 string to bitmap
     * @param base64Str: The base 64 string to convert to bitmap
     * @return base64Str decoded to bitmap
     */
    public static Bitmap stringToBitmap(String base64Str) {
        // reference: https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        if (base64Str == null || base64Str.isEmpty()) return null;
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
