
package ip.signature.com.signatureapps.imageprocessing.VariableUtil;


/**
 *
 * @author AKBAR
 */
public class PixelSpace extends Variable_Space {
    public static void getSpace(int column_end_1, int column_start_2, int label_position) {
        if (Variable_Space.condition[0] == 0)
            Variable_Space.condition[0] = column_end_1;
        else                                    
            Variable_Space.condition[1] = column_start_2;
        
        int hasil_nya = (Variable_Space.condition[1] - Variable_Space.condition[0]);
        
        if (hasil_nya > 12) {
            Variable_Space.space[Variable_Space.stack_in] = label_position;
            Variable_Space.increaseStackArraySpace();
            Variable_Space.condition[0] = column_end_1;
        } else if (label_position != 1)
            Variable_Space.condition[0] = column_end_1;
    }
}
