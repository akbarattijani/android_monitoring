/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ip.signature.com.signatureapps.imageprocessing.Segmentation;

import java.util.Stack;

/**
 *
 * @author AKBAR
 */
public class SplitCharacter {
    
    private static final int COLOR_WHITE = 0;
    private static final int COLOR_BLACK = 1;
    private static final int COLOR_NEW   = 2;
    private static int count;
    private static Stack stack;
    
    public static int[][] dropFalling(int[][] img, boolean print, int baris, int kolom) {
         
        int[] n = new int[6];
        int sum_w;
        int[][] w = new int[baris][kolom];
        int[] wj = new int[5];
        int[] zj = new int[5];
        int[][] I = new int[baris][kolom];
        int alfa = 1;
        int V04, V05, T = 0;
        
        for(int i=0; i<baris; i++) {
            for(int j=0; j<kolom; j++) {
                
                if(img[i][j] == COLOR_WHITE) {
                    sum_w = 0;
                
                    // Get pixel n for case rule
                    n[0] = checkIndexOfBounds(img, i, j);
                    n[1] = checkIndexOfBounds(img, i+1, j-1);
                    n[2] = checkIndexOfBounds(img, i+1, j);
                    n[3] = checkIndexOfBounds(img, i+1, j+1);
                    n[4] = checkIndexOfBounds(img, i, j+1);
                    n[5] = checkIndexOfBounds(img, i, j-1);

                    V04 = n[0];
                    V05 = n[0];

                    // Get pixel get Zj
                    zj[0] = checkIndexOfBounds(img, i+1, j-1);
                    zj[1] = checkIndexOfBounds(img, i+1, j);
                    zj[2] = checkIndexOfBounds(img, i+1, j+1);
                    zj[3] = checkIndexOfBounds(img, i, j+1);
                    zj[4] = checkIndexOfBounds(img, i, j-1);

                    // Case 1 (n1)
                    if( n[1] == COLOR_WHITE &&
                        n[2] == COLOR_BLACK &&
                        n[3] == COLOR_BLACK && 
                        n[4] == COLOR_BLACK &&
                        n[5] == COLOR_BLACK ) {

                        sum_w += 1;
                    }
                    
                    // Case 2 (n2)
                    if( n[1] == COLOR_BLACK &&
                        n[2] == COLOR_WHITE ) {

                        sum_w += 2;
                    }
                    
                    // Case 3 (n3)
                    if( n[1] == COLOR_BLACK &&
                        n[2] == COLOR_BLACK &&
                        n[3] == COLOR_WHITE ) {

                        sum_w += 3;
                    }
                    
                    // Case 4 (n4)
                    if( n[1] == COLOR_BLACK &&
                        n[2] == COLOR_BLACK &&
                        n[3] == COLOR_BLACK && 
                        n[4] == COLOR_WHITE ) {

                        sum_w += 4;
                    }
                    
                    // Case 5 (n5)
                    if( n[1] == COLOR_BLACK &&
                        n[2] == COLOR_BLACK &&
                        n[3] == COLOR_BLACK && 
                        n[4] == COLOR_BLACK &&
                        n[5] == COLOR_WHITE) {

                        sum_w += 5;
                    }

                    // Count weight for get value Wj (Wj = 6 - j, j=1,2,3,4,5)
                    for(int weight=0; weight<wj.length; weight++) {
                        if(weight == 0) wj[weight] = 6 - (weight + 1);
                        else if(weight == 1) wj[weight] = 6 - (weight + 1);
                        else if(weight == 2) wj[weight] = 6 - (weight + 1);
                        else if(weight == 3) wj[weight] = 6 - (weight + 1);
                        else if(weight == 4) wj[weight] = 6 - (weight + 1);
                    }

                    /*
                        Wi -->  4                           if sum(Wi=0)
                                6                           if sum(Wi=15)
                                max(1-Zj)Wj, j=1,2,3,4,5    else
                    */
                    if(sum_w == 0) w[i][j] = 4;
                    else if(sum_w == 15) w[i][j] = 6;
                    else {
                        int sumWj = 0;
                        for(int s=0; s<5; s++) {
                            sumWj = Math.max(sumWj, (1-zj[s])*wj[s]);
                        }

                        w[i][j] = sumWj;
                    }

                    if(n[0] > checkIndexOfBounds(img, i-alfa, j-alfa) && checkIndexOfBounds(img, i-alfa, j-alfa) != -1) {
                        I[i][j] = V04;
                    } else if(n[0] < checkIndexOfBounds(img, i-alfa, j-alfa) && checkIndexOfBounds(img, i-alfa, j-alfa) != -1) {
                        I[i][j] = V05;
                    } else {
                        I[i][j] = 0;
                    }

                    // T(Xi+1, Yi+1) = f'(Xi, Yi, Wi,Ii)
                    if(w[i][j] == 1) T = checkIndexOfBounds(img, i, j-1);
                    else if(w[i][j] == 2) T = checkIndexOfBounds(img, i, j+1);
                    else if(w[i][j] == 3) T = checkIndexOfBounds(img, i+1, j+1);
                    else if(w[i][j] == 6) T = checkIndexOfBounds(img, i+1, j);
                    else if(w[i][j] == 5) T = checkIndexOfBounds(img, i+1, j-1);
                    else if(w[i][j] == 4 && I[i][j] == V04) T = checkIndexOfBounds(img, i+1, j+1);
                    else if(w[i][j] == 4 && I[i][j] == V05) T = checkIndexOfBounds(img, i+1, j);

                    if(checkIndexOfBounds(i+1, j+1, img)) {
                        if(T == 0)  img[i+1][j+1] = COLOR_WHITE;
                        else if(T == 1)  img[i+1][j+1] = COLOR_NEW;
                    }
                }
                
            }
        }
        
        for(int y=0; y<baris; y++) {
            for(int x=0; x<kolom; x++) {
                if(img[y][x] == COLOR_NEW)  img[y][x] = COLOR_WHITE;
            }
        }
        
        if(print==true) {
            for(int j=0; j<baris; j++) {
                for(int i=0; i<kolom; i++) {
                    System.out.print(img[j][i]+"\t");
                }

                System.out.println();
            }
        }
        
        return img;
    }
    
    public static int[][] DropFalling(int[][] img, int baris, int kolom, boolean print) {
        
        int[] n = new int[6];
        int[] mimic = new int[9];
        int[] pos;
        int i, j;
        
        for(int y=1; y<baris-1; y++) {
            for(int x=1; x<kolom-1; x++) {
                mimic[0] = checkIndexOfBounds(img, y, x-1);
                mimic[1] = checkIndexOfBounds(img, y, x);
                mimic[2] = checkIndexOfBounds(img, y, x+1);
                mimic[3] = checkIndexOfBounds(img, y+1, x-1);
                mimic[4] = checkIndexOfBounds(img, y+1, x);
                mimic[5] = checkIndexOfBounds(img, y+1, x+1);
                mimic[6] = checkIndexOfBounds(img, y+2, x-1);
                mimic[7] = checkIndexOfBounds(img, y+2, x);
                mimic[8] = checkIndexOfBounds(img, y+2, x+1);
                
                if( mimic[0] == COLOR_BLACK   &&
                    mimic[1] == COLOR_WHITE   &&
                    mimic[2] == COLOR_WHITE   &&
                    mimic[3] == COLOR_BLACK   &&
                    mimic[4] == COLOR_BLACK   &&
                    mimic[5] == COLOR_BLACK   &&
                    mimic[6] == COLOR_BLACK   &&
                    mimic[7] == COLOR_BLACK   &&
                    mimic[8] == COLOR_BLACK) {
                    
                    img[y+1][x] = COLOR_NEW;
                } else if(
                    mimic[0] == COLOR_WHITE   &&
                    mimic[1] == COLOR_WHITE   &&
                    mimic[2] == COLOR_BLACK   &&
                    mimic[3] == COLOR_BLACK   &&
                    mimic[4] == COLOR_BLACK   &&
                    mimic[5] == COLOR_BLACK   &&
                    mimic[6] == COLOR_BLACK   &&
                    mimic[7] == COLOR_BLACK   &&
                    mimic[8] == COLOR_BLACK) {
                    
                    img[y+1][x] = COLOR_NEW;
                }
            }
        }
        
//        for(int y=1; y<baris-1; y++) {
//            for(int x=1; x<kolom-1; x++) {
//                
//                if(img[y][x] == COLOR_WHITE) {
//                    stack = new Stack();
//                    stack.push(new int[] {y, x});
////                    count = 0;
//                    
//                    while(!stack.isEmpty()) {
//                        
//                        pos = (int[]) stack.pop();
//                        i = pos[0];
//                        j = pos[1];
//                        
//                        n[0] = checkIndexOfBounds(img, i, j);
//                        n[1] = checkIndexOfBounds(img, i+1, j-1);
//                        n[2] = checkIndexOfBounds(img, i+1, j);
//                        n[3] = checkIndexOfBounds(img, i+1, j+1);
//                        n[4] = checkIndexOfBounds(img, i, j+1);
//                        n[5] = checkIndexOfBounds(img, i, j-1);
//                        
//                        try {
//                            //Goto n2
//                            if(n[2] == COLOR_WHITE) {
//
//                                checkIndexOfBounds(img, i+1, j, COLOR_NEW);
//                                stack.push(new int[] {i+1, j});
//                            }
//
//                            //Goto n3
//                            else if( n[2] == COLOR_BLACK   &&
//                                n[3] == COLOR_WHITE
//                               ) {
//
//                                checkIndexOfBounds(img, i+1, j+1, COLOR_NEW);
//                                stack.push(new int[] {i+1, j+1});
//                            }
//
//                            //Goto n1
//                            else if( n[1] == COLOR_WHITE   &&
//                                     n[2] == COLOR_BLACK   && 
//                                     n[3] == COLOR_BLACK
//                                    ) {
//
//                                checkIndexOfBounds(img, i+1, j-1, COLOR_NEW);
//                                stack.push(new int[] {i+1, j-1});
//                            }
//
//                            //Goto n4
//                            else if(n[1] == COLOR_BLACK   &&
//                                    n[2] == COLOR_BLACK   &&
//                                    n[3] == COLOR_BLACK   &&
//                                    n[4] == COLOR_WHITE
//                                    ) {
//
//                                checkIndexOfBounds(img, i, j+1, COLOR_NEW);
//                                stack.push(new int[] {i, j+1});
//                            }
//
//                            //Goto n5
//                            else if(n[1] == COLOR_BLACK   &&
//                                    n[2] == COLOR_BLACK   &&
//                                    n[3] == COLOR_BLACK   &&
//                                    n[4] == COLOR_BLACK   &&
//                                    n[5] == COLOR_WHITE
//                                    ) {
//
//                                checkIndexOfBounds(img, i, j-1, COLOR_NEW);
//                                stack.push(new int[] {i, j-1});
//                            }
//
//                            //Goto n2 extra for "cut"
//                            else if(n[1] == COLOR_BLACK   &&
//                                    n[2] == COLOR_BLACK   &&
//                                    n[3] == COLOR_BLACK   &&
//                                    n[4] == COLOR_BLACK   &&
//                                    n[5] == COLOR_BLACK
//                                    ) {
//
//                                checkIndexOfBounds(img, i+1, j, COLOR_NEW);
//                                stack.push(new int[] {i+1, j});
//                            }
//                            
//                            //Goto n2 extra for "cut" 2
//                            else if(n[1] == COLOR_BLACK   &&
//                                    n[2] == COLOR_BLACK   &&
//                                    n[3] == COLOR_BLACK   &&
//                                    n[4] == COLOR_BLACK   &&
//                                    (n[5] == COLOR_BLACK || n[5] == COLOR_NEW)
//                                    ) {
//
//                                checkIndexOfBounds(img, i+1, j, COLOR_NEW);
//                                stack.push(new int[] {i+1, j});
//                            }
//                            
//                        } catch(Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    
//                }
//            }
//        }
        
        if(print==true) {
            for(j=0; j<baris; j++) {
                for(i=0; i<kolom; i++) {
                    System.out.print(img[j][i]+"\t");
                }

                System.out.println();
            }
        }
        
        return img;
    } 
    
    private static int checkIndexOfBounds(int[][] img, int row, int col) {
        int result = 0;
        
        try {
            result = img[row][col];
        } catch(Exception f) {
            result = -1;
        }
        
        return result;
    }
    
    private static boolean checkIndexOfBounds(int row, int col, int[][] img) {
        int result = 0;
        
        try {
            result = img[row][col];
        } catch(Exception f) {
            result = -1;
        }
        
        if(result != -1)    return true;
        else return false;
    }
    
    private static void checkIndexOfBounds(int[][] img, int row, int col, int COLOR) {
        try {
            img[row][col] = COLOR;
            
        } catch(Exception f) { 
            stack.clear();
        }
    }
}
 