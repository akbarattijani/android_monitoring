package ip.signature.com.signatureapps.imageprocessing.VariableUtil;

import java.util.Stack;


/**
 *
 * @author AKBAR
 */
public class Stack_Char {
    public static int[] getRow(int[][] imge, int r, int c, boolean print) {
        int awal=0, 
            batas_bawah=0, 
            hitung_baris= 1,
            baris_mulai=0,
            awal_x,
            baris_mulai_x,
            hasil;
        
        int[] scan_baris = new int[1000];
        boolean status;
        scan_baris[0] = 1;
        
        try {
            for (int i = 0; i < r; i++) {
                status = false;
                
                for (int j = 0; j < c; j++) {
                    if (imge[i][j] == 1) {
                        batas_bawah = i;
                        if (awal == 0) {
                            awal = i;
                        }   
                        
                        status = true;
                        break;
                    }
                }
                
                if (status) {
                    baris_mulai = batas_bawah;
                } else if (!status && awal != 0) {
                    try {
                        if (scan_baris[hitung_baris-1] != scan_baris[hitung_baris] && baris_mulai!=0) {
                            awal_x=awal; 
                            baris_mulai_x= baris_mulai; 
                            hasil=0;
                            Variable_Segmen.row_start[Variable_Segmen.index_in] = awal_x;
                            Variable_Segmen.row_end[Variable_Segmen.index_in] = baris_mulai_x;
                            Variable_Segmen.increaseIndex();
                            
                            for (int x = awal; x <= baris_mulai; x++) {
                                if (awal_x == baris_mulai_x)
                                    hasil = baris_mulai_x;
                                else {
                                    awal_x++;
                                    if (awal_x == baris_mulai_x)
                                        hasil = baris_mulai_x;
                                    baris_mulai_x--;
                                }
                            }
                            
                            scan_baris[hitung_baris] = hasil;
                            hitung_baris++;
                            awal = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception h) {
            h.printStackTrace();
        }
        
        if(print) {
            for (int scan : scan_baris) {
                System.out.println(scan);
            }
        }
        
        return scan_baris;
    }
    
    public static int[][] findPoint(int[][] imgLabel, int[][] imgOri, int ro, int col) {
        Stack stack = new Stack();
        int i,j, index_tanda = 1;
        int[] pos;

        for (int jum = 0; jum < Variable_Segmen.row_end.length; jum++) {
            if (Variable_Segmen.row_end[jum] != 0) {
                for (int r = Variable_Segmen.row_start[jum]+18; r < Variable_Segmen.row_end[jum]; r++) {
                    for (int k = 0; k < col; k++){
                        if (imgOri[r][k] == 1 && imgLabel[r][k] == 0) {
                            stack.push(new int[] {r, k});

                            while (!stack.isEmpty()) {
                                pos = (int[]) stack.pop();
                                i = pos[0];
                                j = pos[1];

                                try {
                                    if(imgOri[i-1][j-1] == 1 && imgLabel[i-1][j-1] == 0) {
                                        stack.push(new int[] {i-1, j-1}); 
                                        imgLabel[i-1][j-1] = Variable_Label2.lab2;
                                    }
                                    if(imgOri[i-1][j] == 1 && imgLabel[i-1][j] == 0) {
                                        stack.push(new int[] {i-1, j});
                                        imgLabel[i-1][j] = Variable_Label2.lab2;
                                    } 
                                    if(imgOri[i-1][j+1] == 1 && imgLabel[i-1][j+1] == 0) {
                                        stack.push(new int[] {i-1, j+1});
                                        imgLabel[i-1][j+1] = Variable_Label2.lab2;
                                    }
                                    if(imgOri[i][j-1] == 1 && imgLabel[i][j-1] == 0) {
                                        stack.push( new int[] {i, j-1} );    
                                        imgLabel[i][j-1] = Variable_Label2.lab2;    
                                    } 
                                    if(imgOri[i][j+1] == 1 && imgLabel[i][j+1] == 0) {
                                        stack.push( new int[] {i, j+1} );
                                        imgLabel[i][j+1] = Variable_Label2.lab2; 
                                    } 
                                    if(imgOri[i+1][j-1] == 1 && imgLabel[i+1][j-1] == 0) {         
                                        stack.push( new int[] {i+1, j-1} );              
                                        imgLabel[i+1][j-1] = Variable_Label2.lab2;   
                                    } 
                                    if(imgOri[i+1][j] == 1 && imgLabel[i+1][j] == 0) {
                                        stack.push( new int[] {i+1,j} );
                                        imgLabel[i+1][j] = Variable_Label2.lab2;      
                                    } 
                                    if (imgOri[i+1][j+1] == 1 && imgLabel[i+1][j+1] == 0) {  
                                        stack.push( new int[] {i+1,j+1} ); 
                                        imgLabel[i+1][j+1] = Variable_Label2.lab2;      
                                    }
                                } catch (Exception imgLabeling) {
                                    System.out.println("Error imgLabel posisi : "+imgLabeling.getMessage());
                                }
                            } 

                            Variable_Label2.decreaseLabel();
                        }
                    }
                    
                    Variable_Mark_Row.enterMark[jum+1] = Variable_Label2.lab2+1;
                }
            }
        }
        
        return imgLabel;
    }
}
