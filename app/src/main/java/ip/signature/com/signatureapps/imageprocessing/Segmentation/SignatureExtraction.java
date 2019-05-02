package ip.signature.com.signatureapps.imageprocessing.Segmentation;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

/**
 *
 * @author AKBAR
 */

public class SignatureExtraction {
    public static Bitmap getExtraction(int[][] imgBiner, boolean print) {
        ArrayList<ArrayList<Integer>> tempResult = new ArrayList<>();
        int topLeftToRight = 0; // titik awal baris
        int bottomLeftToRight = 0; // titik akhir baris
        int leftTopToBottom = 0; // titik awal kolom
        int rightTopToBottom = 0; //titik akhir kolom
        int maxRow = imgBiner.length;
        int maxColumn = imgBiner[0].length;

        // Scan mulai dari atas, siklus kiri ke kanan
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxColumn; j++) {
                if (imgBiner[i][j] == 1) {
                    topLeftToRight = i;
                }
            }
        }

        // Scan mulai dari bawah, siklus kiri ke kanan
        for (int i = maxRow - 1; i >= 0; i--) {
            for (int j = 0; j < maxColumn; j++) {
                if (imgBiner[i][j] == 1) {
                    bottomLeftToRight = i;
                }
            }
        }

        // Scan mulai dari kiri, siklus atas ke bawah
        for (int i = 0; i < maxColumn; i++) {
            for (int j = 0; j < maxRow; j++) {
                if (imgBiner[j][i] == 1) {
                    leftTopToBottom = i;
                }
            }
        }

        // Scan mulai dari kanan, siklus atas ke bawah
        for (int i = maxColumn - 1; i >= 0; i--) {
            for (int j = 0; j < maxRow; j++) {
                if (imgBiner[j][i] == 1) {
                    rightTopToBottom = i;
                }
            }
        }

        for (int i = bottomLeftToRight; i < topLeftToRight; i++){
            ArrayList<Integer> column = new ArrayList<>();
            for (int j = rightTopToBottom; j < leftTopToBottom; j++){
                column.add(imgBiner[i][j]);
            }

            tempResult.add(column);
        }

//        int[][] result = new int[tempResult.size()][tempResult.get(0).size()];
        Bitmap result = Bitmap.createBitmap(tempResult.get(0).size(), tempResult.size(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < tempResult.size(); i++) {
            for (int j = 0; j < tempResult.get(0).size(); j++) {
                if (tempResult.get(i).get(j) == 0) {
                    result.setPixel(j, i, Color.WHITE);
                } else if (tempResult.get(i).get(j) == 1) {
                    result.setPixel(j, i, Color.BLACK);
                }
//                result[i][j] = tempResult.get(i).get(j);
            }
        }

        if (print) {
            for (int i = 0; i < result.getHeight(); i++) {
                for (int j = 0; j < result.getWidth(); j++) {
                    System.out.print(result.getPixel(j, i) + "\t");
                }

                System.out.println();
            }
        }

        return result;
    }
}
