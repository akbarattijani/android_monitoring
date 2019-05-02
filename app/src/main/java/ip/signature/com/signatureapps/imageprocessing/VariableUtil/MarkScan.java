/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ip.signature.com.signatureapps.imageprocessing.VariableUtil;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 *
 * @author AKBAR
 */
public class MarkScan {

    public static ArrayList getMark(int[][] imageLabel, int row, int column, int AllLabel) {
        ArrayList bufferSegmen = new ArrayList();
        int atas_kanan,
            bawah_kiri,
            kiri_bawah,
            kanan_atas,
            ditek_kolom,
            atas=0, 
            bawah=0;
        int i,
            j,
            k,
            l;
        int jmlLabel = AllLabel * -1;
        
        for (int scan = 0; scan < Variable_Segmen.row_start.length; scan++) {
            if (Variable_Segmen.row_end[scan] != 0) {
                for (i = Variable_Mark_Row.enterMark[scan]; i >= Variable_Mark_Row.enterMark[scan+1]; i--) {
                    try {
                        atas_kanan=0;
                        bawah_kiri=0;
                        kiri_bawah=0;
                        kanan_atas=0;
                        ditek_kolom = 0;//Scan proses 1
                        
                        for (j = Variable_Segmen.row_start[scan]; j < Variable_Segmen.row_end[scan]; j++) {
                            for (k = 0; k < column; k++) {
                                if (imageLabel[j][k] == i) {
                                    atas_kanan = j-1 < 0 ? 0 : j-1;
                                    ditek_kolom = k;
                                    break;
                                }
                            }
                            
                            if (atas_kanan != 0)
                                break;
                        }
                        
                        for (j = Variable_Segmen.row_end[scan]; j >= Variable_Segmen.row_start[scan]; j--) {
                            for (k = column-1; k > 0; k--) {
                                if (imageLabel[j][k] == i) {
                                    bawah_kiri = j+1;
                                    break;
                                }
                            }
                            
                            if (bawah_kiri != 0)
                                break;
                        }
                        
                        for (j = 0; j < column; j++) {
                            for (k = Variable_Segmen.row_start[scan]; k < Variable_Segmen.row_end[scan]; k++) {
                                if (imageLabel[k][j] == i) {
                                    kiri_bawah = j-1 < 0 ? 0 : j-1;
                                    break;
                                }
                            }
                            
                            if (kiri_bawah != 0)
                                break;
                        }
                        
                        for (j = column-1; j>0; j--) {
                            for (k = Variable_Segmen.row_end[scan]; k>=Variable_Segmen.row_start[scan]; k--) {
                                if (imageLabel[k][j] == i) {
                                    kanan_atas =  j+1;
                                    break; 
                                }
                            }
                            
                            if (kanan_atas != 0)
                                break;
                        }
                        
                        createMatrix(imageLabel, atas_kanan, bawah_kiri, kiri_bawah, kanan_atas, bufferSegmen);
                        scan_label(imageLabel, atas_kanan, ditek_kolom);
                    } catch(Exception segmen){
                        segmen.printStackTrace();
                    }
                }
            }
        }
        
        return bufferSegmen;
    }
    
    private static void scan_label(int[][] imgLab, int pos, int kol) {
        boolean scaning = true;
        while(scaning) {
            try {
                
                if(imgLab[pos][kol-1] != 0) {
                    Position_Mark.position[Position_Mark.index] = imgLab[pos][kol-1];
                    scaning = false;
                    Position_Mark.plus_position();
                } else {
                    kol--;
                    scaning = true;   
                }
            } catch(Exception sl) {
                break;
            }
        }
    }
    
    private static void createMatrix(int[][] gambarnya,
                                     int satu,
                                     int dua,
                                     int tiga,
                                     int empat,
                                     ArrayList matrixSegmen) {
        int j,k;
        if (satu != 0 && dua != 0 && tiga != 0 && empat != 0) {
            Bitmap bufferImage = Bitmap.createBitmap((empat-tiga)+1, (dua-satu)+1, Bitmap.Config.ARGB_8888);
            
            for (j = satu; j < dua; j++){
                for (k = tiga; k < empat; k++) {
                    try {
                        bufferImage.setPixel(
                                            k-tiga<0 ? 0 : k-tiga , 
                                            j-satu<0 ? 0 : j-satu, 
                                            gambarnya[j][k]
                        );

                        if(bufferImage.getPixel(k-tiga, j-satu) !=0)
                            bufferImage.setPixel(k-tiga, j-satu, 1);

                    } catch (Exception iniSeg) {
                        iniSeg.printStackTrace();
                    }
                }
            }

            matrixSegmen.add(bufferImage);
        }
    }
}
