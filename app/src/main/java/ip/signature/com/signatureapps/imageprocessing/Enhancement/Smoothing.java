package ip.signature.com.signatureapps.imageprocessing.Enhancement;

/**
 *
 * @author AKBAR
 */
public class Smoothing {
    
    private static int[] karnel;
    private static int  divine = 9;
    private static int  red = 0,
                        green = 0,
                        blue = 0;
    
    public static int[][] averageFilter(int[][] gray, boolean printing) {
        int average;
        
        for (int i = 0; i < gray.length; i++) {
            for(int j = 0; j < gray[0].length; j++) {
                try {
                    average = 0;
                    divine = 9;
                    
                    average = checkIndexOfBounds(average, gray, i-1, j-1);
                    average = checkIndexOfBounds(average, gray, i-1, j);
                    average = checkIndexOfBounds(average, gray, i-1, j+1);
                    average = checkIndexOfBounds(average, gray, i, j-1);
                    average = checkIndexOfBounds(average, gray, i, j);
                    average = checkIndexOfBounds(average, gray, i, j+1);
                    average = checkIndexOfBounds(average, gray, i+1, j-1);
                    average = checkIndexOfBounds(average, gray, i+1, j);
                    average = checkIndexOfBounds(average, gray, i+1, j+1);
                    
                    average = (average/divine);
                    
                    gray[i][j] = average;

                } catch (Exception modu) {
                    System.out.println("Error Average :"+modu.getMessage());
                }
            }
        }
        
        if (printing) {
            for (int i = 0; i < gray.length; i++) {
                for (int j = 0; j < gray[i].length; j++) {
                    System.out.print(gray[i][j] + "\t");
                }
                System.out.println();
            }
        }
        
        return gray;
    }
    
//    public static BufferedImage AverageFilterImageResult(   BufferedImage gray,
//                                                            boolean printing,
//                                                            boolean createFile,
//                                                            JProgressBar progressBar) {
//        int alpha = 0xff000000;
//        WritableRaster raster = gray.getRaster();
//
//
//        if(progressBar != null ) {
//            progressBar.setValue(0);
//            progressBar.setMaximum(100);
//        }
//
//        for(int i=0; i<gray.getHeight(); i++) {
//
//            if(progressBar != null) {
//                progressBar.setValue(i*100/gray.getHeight()+1);
//            }
//
//            for(int j=0; j<gray.getWidth(); j++) {
//                try {
//                    red = 0;
//                    green = 0;
//                    blue = 0;
//                    divine = 9;
//
//                    checkIndexOfBounds(gray, j-1, i-1);
//                    checkIndexOfBounds(gray, j, i-1);
//                    checkIndexOfBounds(gray, j+1, i-1);
//                    checkIndexOfBounds(gray, j-1, i);
//                    checkIndexOfBounds(gray, j, i);
//                    checkIndexOfBounds(gray, j+1, i);
//                    checkIndexOfBounds(gray, j-1, i+1);
//                    checkIndexOfBounds(gray, j, i+1);
//                    checkIndexOfBounds(gray, j+1, i+1);
//
//                    red /= divine;
//                    green /= divine;
//                    blue /= divine;
//
//                    raster.setSample(j, i, 0, (alpha | (red << 16) | (green << 8 ) | blue));
//
//                } catch(Exception modu) {
//                    System.out.println("Error Average :"+modu.getMessage());
//                }
//            }
//        }
//
//        if(createFile) {
//            try {
//                ImageIO.write(gray, "jpg", new File("avergeFilter_contoh.jpg"));
//            } catch (IOException ex) {
//                Logger.getLogger(Smoothing.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        if(printing==true)
//        {
//            Body.echo(gray);
//        }
//
//        return gray;
//    }
//
//    private static void checkIndexOfBounds(BufferedImage img, int x, int y) {
//        try {
//            int pixel = img.getRGB(x, y);
//            red += ((pixel >> 16) & 0xff);
//            green += ((pixel >> 8 ) & 0xff);
//            blue += ((pixel) & 0xff);
//        } catch(Exception e) {
//            red += 0;
//            green += 0;
//            blue += 0;
//            divine--;
//        }
//    }
    
    private static int checkIndexOfBounds(int value, int[][] img, int y, int x) {
        try {
            value += img[y][x];
        } catch (Exception e) {
            value += 0;
            divine--;
        }
        
        return value;
    }
    
    private static int checkIndexOfBounds(int[][] img, int y, int x) {
        int value = 0;
        try {
            value = img[y][x];
        } catch (Exception e) {
            value = 0;
        }
        
        return value;
    }
}
