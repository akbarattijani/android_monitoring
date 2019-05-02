package ip.signature.com.signatureapps.imageprocessing.Preprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Thresholding {
    public static int[][] execute(int[][] gray, Bitmap citra, boolean print) {
        //14 atau 23 atau 24 atau 33 atau 35
        int threshold = OtsuThreshold.otsuThreshold(citra);
        
        for (int j = 0; j < gray.length; j++) {
            for(int i = 0; i < gray[j].length; i++) {
                try {
                    if (gray[j][i] < threshold) {
                        gray[j][i] = 0;
//                        bw1d[i+kolom*j]=0;
                    } else {
                        gray[j][i] = 1;
//                        bw1d[i+kolom*j]=1;
                    }
                } catch (Exception thres) {
                    thres.printStackTrace();
                }
            }
        }
        
        if (print) {
            for (int[] aGray : gray) {
                for (int anAGray : aGray) {
                    System.out.print(anAGray + "\t");
                }
                System.out.println();
            }
        }
        
        return gray;
    }

    public static int[][] execute(Bitmap gray, boolean print) {
        //14 atau 23 atau 24 atau 33 atau 35
        int threshold = OtsuThreshold.otsuThreshold(gray);
        int[][] result = new int[gray.getHeight()][gray.getWidth()];

        for (int j = 0; j < gray.getHeight(); j++) {
            for(int i = 0; i < gray.getWidth(); i++) {
                try {
                    if (gray.getPixel(i, j) < threshold) {
                        result[j][i] = 0;
                    } else {
                        result[j][i] = 1;
                    }
                } catch (Exception thres) {
                    thres.printStackTrace();
                }
            }
        }

        if (print) {
            for (int j = 0; j < result.length; j++) {
                for(int i = 0; i < result[0].length; i++) {
                    System.out.print(result[j][i] + "\t");
                }

                System.out.println();
            }
        }

        return result;
    }
}
