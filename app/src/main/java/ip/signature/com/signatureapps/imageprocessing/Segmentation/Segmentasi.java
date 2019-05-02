/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ip.signature.com.signatureapps.imageprocessing.Segmentation;

import java.util.ArrayList;

import ip.signature.com.signatureapps.imageprocessing.VariableUtil.MarkScan;
import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Stack_Char;
import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Label;
import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Label2;

/**
 *
 * @author AKBAR
 */
public class Segmentasi {
    
    //Melakukan Segmentasi Citra menggunakan Algoritma Connected Component Labeling (CCL)
    //Lalu hasil dari pelabelan yang di lakukan CCL, akan di segmentasi
    //Menghasilkan 1 matriks 1 karakter
    
    private static ArrayList data_tanda_baca = new ArrayList();
    
    public ArrayList segmentasi(int[] scan_baris, 
                                int[][] image, 
                                int baris, 
                                int kolom) {
        
        int[][] imgOri = image = SplitCharacter.dropFalling(image,
                                                            false,
                                                            baris,
                                                            kolom);

        //Memanggil Class untuk Algoritma Connected Component Labeling
        image = Labeling.connectedComponentLabeling(scan_baris,
                                                    image, 
                                                    baris, 
                                                    kolom, 
                                                    false);
        
        image = Stack_Char.findPoint(image, imgOri, 0, 0);

        // Untuk cetak hasil findPoint
        for (int x = 0; x < baris; x++) {
            for (int y = 0; y < kolom; y++) {
                System.out.print(image[x][y] + "\t");
            }
            System.out.println();
        }
        
        //Melakukan Segmentasi karakter dari hasil Connected Component Labeling
        ArrayList data_pengujian = Segmentation.segmenGenerating( image,
                                                            baris, 
                                                            kolom, 
                                                            Variable_Label.numberingLabel,
                                                            false);

        data_tanda_baca = MarkScan.getMark(image,
                                            baris, 
                                            kolom, 
                                            Variable_Label2.lab2);

        return data_pengujian;
    }
    
    public static ArrayList segmen_tanda_baca()
    {
        return data_tanda_baca;
    }
}
