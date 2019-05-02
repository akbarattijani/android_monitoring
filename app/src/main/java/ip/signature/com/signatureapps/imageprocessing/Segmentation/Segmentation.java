package ip.signature.com.signatureapps.imageprocessing.Segmentation;

/**
 *
 * @author AKBAR
 */

import android.graphics.Bitmap;

import java.util.ArrayList;

import ip.signature.com.signatureapps.imageprocessing.VariableUtil.PixelSpace;
import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Row;
import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Segmen;

public class Segmentation {
    
    public static ArrayList segmenGenerating(int[][] imageLabel, int row, int column, int jumlahLabel, boolean print) {
        ArrayList bufferSegmen = new ArrayList();
        int atas_kanan,
            bawah_kiri,
            kiri_bawah,
            kanan_atas;
        int i,
            j,
            k;
        
        int jmlLabel = jumlahLabel;
        for (int scan = 0; scan < Variable_Segmen.row_start.length; scan++) {
            if(Variable_Segmen.row_end[scan] != 0) {
                for (i = Variable_Row.enterRow[scan]; i <= Variable_Row.enterRow[scan+1]; i++) {
                    try {
                        atas_kanan=0;
                        bawah_kiri=0;
                        kiri_bawah=0;
                        kanan_atas=0;

                        //Scan proses 1
                        for (j = Variable_Segmen.row_start[scan]; j < Variable_Segmen.row_end[scan]; j++) {
                            for(k = 0; k < column; k++) {
                                if (imageLabel[j][k] == i) {
                                    atas_kanan = j-1 < 0 ? 0 : j-1;
                                    break;
                                }
                            }

                            if (atas_kanan != 0)
                                break;
                        }

                        //Scan proses 2
                        for (j = Variable_Segmen.row_end[scan]; j >= Variable_Segmen.row_start[scan]; j--) {
                            for (k = column-1; k > 0; k--) {
                                if (imageLabel[j][k] == i) {
                                    bawah_kiri =  j+1;
                                    break;
                                }
                            }

                            if (bawah_kiri != 0)
                                break;
                        }

                        //Scan proses 3
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

                        //Scan proses 4
                        for (j = column-1; j > 0; j--) {
                            for (k = Variable_Segmen.row_end[scan]; k >= Variable_Segmen.row_start[scan]; k--) {
                                if (imageLabel[k][j] == i) {
                                    kanan_atas =  j+1;
                                    break;
                                }
                            }

                            if (kanan_atas != 0)
                                break;
                        }

                        PixelSpace.getSpace(kanan_atas, kiri_bawah, i);
                        matrixSegmenGenerating(imageLabel, atas_kanan, bawah_kiri, kiri_bawah, kanan_atas, bufferSegmen, print);
        //                }
                    } catch (Exception segmen) {
                        segmen.printStackTrace();
                    }

        //            for(int h=0; h<25; h++)
        //            {
        //                System.out.println(Variable_Spasi.spasi[h]+"");
        //            }
                }
            }
        }
        
        System.out.println("Sukses buat matriks");
        
        return bufferSegmen;
    }
    
    private static void matrixSegmenGenerating(int[][] gambarnya,
                                               int satu,
                                               int dua,
                                               int tiga,
                                               int empat,
                                               ArrayList matrixSegmen,
                                               boolean cetak) {
        int j,k;
        if (satu != 0 && dua != 0 && tiga != 0 && empat != 0) {
            Bitmap bufferImage = Bitmap.createBitmap((empat-tiga) + 1, (dua-satu) + 1, Bitmap.Config.ARGB_8888);
            
            for (j = satu; j < dua; j++) {
                for (k = tiga; k < empat; k++) {
                    try {
                        bufferImage.setPixel(k-tiga < 0 ? 0 : k-tiga, j-satu < 0 ? 0 : j-satu, gambarnya[j][k]);

                        if (bufferImage.getPixel(k-tiga, j-satu) != 0)
                            bufferImage.setPixel(k-tiga, j-satu, 1);
                        if (cetak) {
                            System.out.print(bufferImage.getPixel(k-tiga, j-satu) + "\t");
                        }
                    } catch (Exception iniSeg) {
                        iniSeg.printStackTrace();
                    }
                }

                if (cetak) {
                    System.out.println();
                }
            }

            if(cetak) {
                System.out.println();
            }

            matrixSegmen.add(bufferImage);
        }
    }
    
    //**********************    FOR TRAINING DATA SAMPLE    **********************************************//
    
//    private static BufferedImage bufferImage;
//    
//    public static ArrayList segmentasi(int[][] image, int row, int column, ArrayList imgFeature)
//    {  
//        int atas_kanan,bawah_kiri,kiri_bawah,kanan_atas, i, j, k;
//        
//        for(i=1; i<=1; i++)
//        {
//            try
//            {
//                atas_kanan=0;   bawah_kiri=0;   kiri_bawah=0;   kanan_atas=0;
//                //Scan proses 1
//                for(j=0; j<row; j++)
//                {
//                    for(k=0; k<column; k++)
//                    {
//                        if(image[j][k] == i)
//                        {
//                            atas_kanan = j-1;
//                            break;
//                        }
//                    }
//                    if(atas_kanan!=0)
//                            break;
//                }
//                //Scan proses 2
//                for(j=row-1; j>=0; j--)
//                {
//                    for(k=column-1; k>=0; k--)
//                    {
//                        if(image[j][k] == i)
//                        {
//                            bawah_kiri = j+1;
//                            break;
//                        }
//                    }
//                    if(bawah_kiri!=0)
//                            break;
//                }
//                //Scan proses 3
//                for(j=0; j<column; j++)
//                {
//                    for(k=0; k<row; k++)
//                    {
//                        if(image[k][j] == i)
//                        {
//                            kiri_bawah = j-1;
//                            break;
//                        }
//                    }
//                    if(kiri_bawah!=0)
//                        break;
//                }
//                //Scan proses 4
//                for(j=column-1; j>=0; j--)
//                {
//                    for(k=row-1; k>=0; k--)
//                    {
//                        if(image[k][j] == i)
//                        {
//                            kanan_atas = j+1;
//                            break;
//                        }
//                    }
//                    if(kanan_atas!=0)
//                        break;
//                }
//                
//                createMatrixSegmen(image, atas_kanan, bawah_kiri, kiri_bawah, kanan_atas, imgFeature);
//            }
//            catch(Exception segmen)
//            {
//                System.out.println("Error Segmen ke : "+segmen.getMessage());
//            }
//        }
//        
//        return imgFeature;
//    }
//    
//    private static void createMatrixSegmen(int[][] matriks, int satu, int dua, int tiga, int empat, ArrayList data_sample)
//    {
//        int j,k;
//        System.out.println("ALMT : "+satu+"-"+dua+"-"+tiga+"-"+empat);
//        if(satu!=0 && dua!=0 && tiga!=0 && empat!=0)
//        {  
//            
//            bufferImage = new BufferedImage((empat-tiga)+1, (dua-satu)+1, BufferedImage.TYPE_INT_ARGB);
//            try
//            {
//                for(j=satu; j<=dua; j++)
//                {
//                    for(k=tiga; k<=empat; k++)
//                    {
//                        bufferImage.setRGB(k-tiga, j-satu, matriks[j][k]);
//                        
//                        if(bufferImage.getRGB(k-tiga, j-satu) !=0)
//                        {
//                            bufferImage.setRGB(k-tiga, j-satu, 1);
//                        }
//                        
//                        System.out.print(bufferImage.getRGB(k-tiga, j-satu)+"\t");
//                    }
//                    System.out.println("");
//                }
//                System.out.println("*");
//                
//                data_sample.add(bufferImage);
//            }
//            catch(Exception iniSeg)
//            {
//                System.out.println("Eror dalam pembuatan segmen "+iniSeg.getLocalizedMessage());
//            }
//        }
//    }
}
