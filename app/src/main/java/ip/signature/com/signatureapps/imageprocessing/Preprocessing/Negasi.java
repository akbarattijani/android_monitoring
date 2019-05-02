package ip.signature.com.signatureapps.imageprocessing.Preprocessing;

/**
 *
 * @author AKBAR
 */
public class Negasi {
    
    public static int[][] toBiner(int[][] bw, boolean print) {
        int white=0, black=0;

        for (int[] aBw1 : bw) {
            for (int anABw1 : aBw1) {
                if (anABw1 == 1)
                    black++;
                else
                    white++;
            }
        }
        
        if (black > white) {
            for (int j = 0; j < bw.length; j++) {
                for (int i = 0; i < bw[j].length; i++) {
                    if (bw[j][i] == 1) {
                        bw[j][i] = 0;
                    } else
                        bw[j][i] = 1;
                }
            }
        }
        
        if (print) {
            for (int[] aBw : bw) {
                for (int anABw : aBw) {
                    System.out.print(anABw + "\t");
                }
                System.out.println();
            }
        }
        
        return bw;
    }
}
