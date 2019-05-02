/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ip.signature.com.signatureapps.imageprocessing.VariableUtil;

/**
 *
 * @author AKBAR
 */
public class Variable_Segmen {
    public static int[] row_start  = new int[100];
    public static int[] row_end = new int[100];
    public static int index_in = 0;
    
    public static void increaseIndex()
    {
        index_in++;
    }
}
