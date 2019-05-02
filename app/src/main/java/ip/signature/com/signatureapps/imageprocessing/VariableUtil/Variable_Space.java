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
public class Variable_Space {
    public static int[] space = new int[1000];
    public static int[] condition = new int[2];
    public static int stack_in=0;
    
    public static void increaseStackArraySpace() {
        stack_in++;
    }
}
