package ip.signature.com.signatureapps.util;

/**
 *
 * @author AKBAR
 */
public class ConvertArray {
    private int[] biner;

    public static int[] twoDimensionToOneDimension(int[][] arr, boolean print) {
        int[] result = new int[arr.length * arr[0].length];

        int index = 0;
        for (int[] anArr1 : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                result[index] = anArr1[j];
                index++;
            }
        }

        if (print) {
            for (int value : result) {
                System.out.print(value + "\t");
            }

            System.out.println();
        }

        return result;
    }

    public ConvertArray twoDimensionToOneDimension(int[][] arr) {
        biner = new int[arr.length * arr[0].length];

        int index = 0;
        for (int[] anArr1 : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                biner[index] = anArr1[j];
                index++;
            }
        }

        return this;
    }

    public String asString() {
        String result = "";
        for (int value : biner) {
            result += String.valueOf(value) + " ";
        }

        return result;
    }
}
