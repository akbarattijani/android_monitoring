package ip.signature.com.signatureapps.imageprocessing.Segmentation;

/**
 *
 * @author AKBAR
 */

/*
    Proses Pelabelan perkarakter (Objek) dengan Algoritma Connected Component Labeling
    Metode One Pass
    Menggunakan 8 Neightboard (Connectivity)
*/

import java.util.Stack;

import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Label;
import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Row;
import ip.signature.com.signatureapps.imageprocessing.VariableUtil.Variable_Segmen;

public class Labeling {
    public static int[][] connectedComponentLabeling(int[] scan_baris, int[][] image, int baris, int kolom, boolean print) {
        int i,j;
        int[] pos;
        Stack stack = new Stack();
        int[][] label = new int[baris][kolom];
        
        for (int row = 1; row < scan_baris.length; row++) {
            if (scan_baris[row] != 0) {
                for (int column = 0; column < kolom; column++) {
                    if (image[scan_baris[row]][column] == 0) continue;
                    if (label[scan_baris[row]][column] > 0) continue;

                    stack.push(new int[] {scan_baris[row], column});
                    label[scan_baris[row]][column] = Variable_Label.numberingLabel;

                    while (!stack.isEmpty()) {
                        pos = (int[]) stack.pop();
                        i = pos[0];
                        j = pos[1];

                        try {
                            //Melakukan pengecekan 8 Connectivity ( 8-Neighbors)
                            if (checkIndexOfBounds(image, i-1, j-1, 1) && checkIndexOfBounds(label, i-1, j-1, 0)) {
                                stack.push(new int[] {i-1, j-1});
                                label[i-1][j-1] = Variable_Label.numberingLabel;
                            }
                            if (checkIndexOfBounds(image, i-1, j, 1) && checkIndexOfBounds(label, i-1, j, 0)) {
                                stack.push(new int[] {i-1, j});
                                label[i-1][j] = Variable_Label.numberingLabel;
                            }
                            if (checkIndexOfBounds(image, i-1, j+1, 1) && checkIndexOfBounds(label, i-1, j+1, 0)) {
                                stack.push(new int[] {i-1, j+1});
                                label[i-1][j+1] = Variable_Label.numberingLabel;
                            }
                            if (checkIndexOfBounds(image, i, j-1, 1) && checkIndexOfBounds(label, i, j-1, 0)) {
                                stack.push( new int[] {i, j-1} );    
                                label[i][j-1] = Variable_Label.numberingLabel;
                            } 
                            if (checkIndexOfBounds(image, i, j+1, 1) && checkIndexOfBounds(label, i, j+1, 0)) {
                                stack.push( new int[] {i, j+1} );               
                                label[i][j+1] = Variable_Label.numberingLabel;
                            } 
                            if (checkIndexOfBounds(image, i+1, j-1, 1) && checkIndexOfBounds(label, i+1, j-1, 0)) {
                                stack.push( new int[] {i+1, j-1} );              
                                label[i+1][j-1] = Variable_Label.numberingLabel;
                            } 
                            if (checkIndexOfBounds(image, i+1, j, 1) && checkIndexOfBounds(label, i+1, j, 0)) {
                                stack.push( new int[] {i+1,j} );                
                                label[i+1][j] = Variable_Label.numberingLabel;
                            } 
                            if (checkIndexOfBounds(image, i+1, j+1, 1) && checkIndexOfBounds(label, i+1, j+1, 0)) {
                                stack.push( new int[] {i+1,j+1} );                  
                                label[i+1][j+1] = Variable_Label.numberingLabel;
                            }  
                        } catch (Exception labeling) {
                            labeling.printStackTrace();
                        }
                    }
                    
                    secondScan(Variable_Segmen.row_start[row-1], Variable_Segmen.row_end[row-1], column, label, image);
                    Variable_Label.increase();
                }
                
                Variable_Row.enterRow[row] = Variable_Label.numberingLabel;
            }
        }
        
        if (print) {
            for (int k = 0; k < baris; k++){
               for (int l = 0; l < kolom; l++){
                   System.out.print(label[k][l] + "\t");
               }

               System.out.println();
            }   
        }
        
        return label;
    }
    
    private static void secondScan(int baris_awal, int baris_akhir, int kolom, int[][] imgLabel, int[][] imgOri) {
        Stack antri = new Stack();
        int[] post;
        int a,b;
        
        for (int y = baris_awal; y < baris_akhir; y++) {
            if(imgOri[y][kolom] == 0) continue;
            
            antri.push(new int[] {y, kolom});
            imgLabel[y][kolom] = Variable_Label.numberingLabel;
            
            while (!antri.isEmpty()) {
                post = (int[]) antri.pop();
                a = post[0];
                b = post[1];

                try {
                    //Melakukan pengecekan 8 Connectivity ( 8-Neighbors)
                    if (checkIndexOfBounds(imgOri, a-1, b-1, 1) && checkIndexOfBounds(imgLabel, a-1, b-1, 0)) {
                        antri.push(new int[] {a-1, b-1});
                        imgLabel[a-1][b-1] = Variable_Label.numberingLabel;
                    }
                    if (checkIndexOfBounds(imgOri, a-1, b, 1) && checkIndexOfBounds(imgLabel, a-1, b, 0)) {
                        antri.push(new int[] {a-1, b});
                        imgLabel[a-1][b] = Variable_Label.numberingLabel;
                    }
                    if (checkIndexOfBounds(imgOri, a-1, b+1, 1) && checkIndexOfBounds(imgLabel, a-1, b+1, 0)) {
                        antri.push(new int[] {a-1, b+1});
                        imgLabel[a-1][b+1] = Variable_Label.numberingLabel;
                    }
                    if (checkIndexOfBounds(imgOri, a, b-1, 1) && checkIndexOfBounds(imgLabel, a, b-1, 0)) {
                        antri.push( new int[] {a, b-1} );    
                        imgLabel[a][b-1] = Variable_Label.numberingLabel;
                    } 
                    if (checkIndexOfBounds(imgOri, a, b+1, 1) && checkIndexOfBounds(imgLabel, a, b+1, 0)) {
                        antri.push( new int[] {a, b+1} );               
                        imgLabel[a][b+1] = Variable_Label.numberingLabel;
                    } 
                    if (checkIndexOfBounds(imgOri, a+1, b-1, 1) && checkIndexOfBounds(imgLabel, a+1, b-1, 0)) {
                        antri.push( new int[] {a+1, b-1} );              
                        imgLabel[a+1][b-1] = Variable_Label.numberingLabel;
                    } 
                    if (checkIndexOfBounds(imgOri, a+1, b, 1) && checkIndexOfBounds(imgLabel, a+1, b, 0)) {
                        antri.push( new int[] {a+1,b} );                
                        imgLabel[a+1][b] = Variable_Label.numberingLabel;
                    } 
                    if (checkIndexOfBounds(imgOri, a+1, b+1, 1) && checkIndexOfBounds(imgLabel, a+1, b+1, 0)) {
                        antri.push( new int[] {a+1,b+1} );                  
                        imgLabel[a+1][b+1] = Variable_Label.numberingLabel;
                    }  
                } catch (Exception labeling) {
                    labeling.printStackTrace();
                }
            }
        }
    }
    
    private static boolean checkIndexOfBounds(int[][] img, int y, int x, int val) {
        boolean value = false;
        try {
            value = img[y][x] == val;
        } catch(Exception e) {
            value = false;
        }
        
        return value;
    }
}
