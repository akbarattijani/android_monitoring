package ip.signature.com.signatureapps.imageprocessing.Enhancement;

/**
 *
 * @author AKBAR
 */

public class Sharpening {
    public static int[][] unsharpMask(  int[][] origPixels,
                                        int[][] blurredPixels,
                                        float amount,
                                        int threshold,
                                        boolean print) {

        int[][] result = new int[origPixels.length][origPixels[0].length];
        int orgRed = 0,
            orgGreen = 0,
            orgBlue = 0;
        int blurredRed = 0,
            blurredGreen = 0,
            blurredBlue = 0;
        int usmPixel = 0;
        int alpha = 0xFF000000; //transperency is not considered and always zero

        for (int i = 0; i < origPixels.length; i++) {
            for (int j = 0; j < origPixels[0].length; j++) {
                int origPixel = origPixels[i][j],
                    blurredPixel = blurredPixels[i][j];

                //seperate RGB values of original and blurred pixels into seperate R,G and B values
                orgRed = ((origPixel >> 16) & 0xff);
                orgGreen = ((origPixel >> 8 ) & 0xff);
                orgBlue = (origPixel & 0xff);
                blurredRed = ((blurredPixel >> 16) & 0xff);
                blurredGreen = ((blurredPixel >> 8 ) & 0xff);
                blurredBlue = (blurredPixel & 0xff);

                //If the absolute val. of difference between original and blurred
                //values are greater than the given threshold add weighed difference
                //back to the original pixel. If the result is outside (0-255),
                //change it back to the corresponding margin 0 or 255
                if (Math.abs(orgRed - blurredRed) >= threshold) {
                    orgRed = (int) (amount * (orgRed - blurredRed) + orgRed);
                    orgRed = orgRed > 255 ? 255 : orgRed < 0 ? 0 : orgRed;
                }

                if (Math.abs(orgGreen - blurredGreen) >= threshold) {
                    orgGreen = (int) (amount * (orgGreen - blurredGreen) + orgGreen);
                    orgGreen = orgGreen > 255 ? 255 : orgGreen < 0 ? 0 : orgGreen;
                }

                if (Math.abs(orgBlue - blurredBlue) >= threshold) {
                    orgBlue = (int) (amount * (orgBlue - blurredBlue) + orgBlue);
                    orgBlue = orgBlue > 255 ? 255 : orgBlue < 0 ? 0 : orgBlue;
                }

                usmPixel = (alpha | (orgRed << 16) | (orgGreen << 8 ) | orgBlue);
                result[i][j] = usmPixel;
            }
        }

        if (print) {
            for (int[] aResult : result) {
                for (int j = 0; j < result[0].length; j++) {
                    System.out.print(aResult[j] + "\t");
                }

                System.out.println();
            }
        }

        return result;
    }
}
