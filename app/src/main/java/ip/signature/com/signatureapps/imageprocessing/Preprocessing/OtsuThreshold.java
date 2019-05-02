/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ip.signature.com.signatureapps.imageprocessing.Preprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 *
 * @author AKBAR
 */
public class OtsuThreshold {
    public static int[][] execute(Bitmap rgbBitmap, int baris, int kolom, boolean print) {
        int pixel;
        
        double otsuThreshold = otsuThreshold(rgbBitmap);
        int[][] biner = new int[rgbBitmap.getHeight()][rgbBitmap.getWidth()];
        
        for (int y = 0; y < baris; y++) {
            for (int x = 0; x < kolom; x++) {
                pixel =  Color.red(rgbBitmap.getPixel(x, y));

                if (pixel > otsuThreshold) {
                    biner[y][x] = 0;
                } else {
                    biner[y][x] = 1;
                }

            }
        }
        
        if(print) {
            for (int i = 0; i < baris; i++) {
                for (int j = 0; j < kolom; j++) {
                    System.out.print(biner[i][j] + "\t");
                }
                System.out.println();
            }
        }

        return biner;
    }
    
    public static int otsuThreshold(Bitmap original) {

        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for (int i = 0; i < 256; i++) sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) continue;
            wF = total - wB;

            if (wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private static int[] imageHistogram(Bitmap bmp) {

        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++) histogram[i] = 0;

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int color = bmp.getPixel(i, j);
                int red = Color.red(color);
                histogram[red]++;
            }
        }

        return histogram;
    }
}
