package com.qc.facialexpressionrecognition.Helpers;

import android.graphics.Bitmap;

public class BitmapToFloatArrayHelper {

    private static final String TAG = BitmapToFloatArrayHelper.class.getSimpleName();
    private boolean mIsFloatBufferBlack;

    /**
     * Method to convert bitmap to grayscale
     * @param inputBitmap
     * @return
     */
    public float[] convertToGrayScale(Bitmap inputBitmap) {
        float[] floats = new float[48 * 48];
        int i = 0;
        //get image width and height
        int width = inputBitmap.getWidth();
        int height = inputBitmap.getHeight();

        //convert to grayscale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = inputBitmap.getPixel(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                //calculate average
                int avg = (r + g + b) / 3;

                //replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                inputBitmap.setPixel(x, y, p);
                floats[i] = avg;
                i++;
            }
        }
        return floats;

    }


    public boolean isFloatBufferBlack() {
        return mIsFloatBufferBlack;
    }
}
