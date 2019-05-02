package ip.signature.com.signatureapps.imageprocessing.Resize;

/**
 *
 * @author AKBAR
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;

import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Matrix;

public class Normalisasi {
    Bitmap[] matrixSegmen;

    public Bitmap[] getBitmaps() {
        return matrixSegmen;
    }

    public int[][] getBitmap(int index, boolean print) {
        Bitmap bitmap = matrixSegmen[index];
        int[][] result = new int[bitmap.getHeight()][bitmap.getWidth()];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                if (bitmap.getPixel(j, i) == Color.WHITE) {
                    result[i][j] = 0;
                } else {
                    result[i][j] = 1;
                }
            }
        }

        if (print) {
            for (int[] aResult : result) {
                for (int value : aResult) {
                    System.out.print(value + "\t");
                }

                System.out.println();
            }
        }

        return result;
    }

    public Normalisasi resize(ArrayList<Bitmap> arrBitmap, int jmlLabel, int normal_baris, int normal_kolom) {
        matrixSegmen = new Bitmap[jmlLabel];
        Variable_Matrix.stack_matrix = 0;

        for (int i = 0; i < arrBitmap.size(); i++) {
            //Rescale (Resize) Gambar per segmen
            Bitmap img = arrBitmap.get(i);
            Bitmap scaledBitmap = Bitmap.createBitmap(normal_kolom, normal_baris, Bitmap.Config.ARGB_8888);

            float ratioX = normal_kolom / (float) img.getWidth();
            float ratioY = normal_baris / (float) img.getHeight();
            float middleX = normal_kolom / 2.0f;
            float middleY = normal_baris / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(img, middleX - img.getWidth() / 2, middleY - img.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//            Bitmap scaledBitmap = Bitmap.createScaledBitmap(img, normal_kolom, normal_baris, true);

            try {
                matrixSegmen[Variable_Matrix.stack_matrix] = scaledBitmap;
                Variable_Matrix.plus_stack();
            } catch (Exception normal) {
                normal.printStackTrace();
            }
        }

        Variable_Matrix.stack_matrix = 0;
        
        return this;
    }
}
