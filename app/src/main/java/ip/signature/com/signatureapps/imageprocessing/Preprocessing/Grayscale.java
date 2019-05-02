package ip.signature.com.signatureapps.imageprocessing.Preprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Grayscale {
    
//    public static BufferedImage ToGray(BufferedImage citra, boolean print, boolean createFile, JProgressBar progressBar) {
//
//        int c, r, g, b;
//        BufferedImage result = new BufferedImage(citra.getWidth(), citra.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//        WritableRaster raster = result.getRaster();
//
//        if(progressBar != null ) {
//            progressBar.setValue(0);
//            progressBar.setMaximum(100);
//        }
//
//        for(int j=0; j<citra.getHeight(); j++) {
//
//            if(progressBar != null) {
//                progressBar.setValue(j*100/citra.getHeight()+1);
//            }
//
//            for(int i=0; i<citra.getWidth(); i++) {
//                try {
//                    c =  citra.getRGB(i,j);
//                    r = (c & 0x00ff0000) >> 16;
//                    g = (c & 0x0000ff00) >> 8;
//                    b = c & 0x000000ff;
//
//                    float gray = (float) (0.299 * r + 0.587 * g + 0.114 * b);
//
//                    //Konversi ke Grayscale
//                    /*
//                        Rumus Konversi RGB ke Grayscale :
//
//                        Gray = (Red * 0.299) + (Green * 0.587) + (Blue * 0.114)
//                    */
//
//                    raster.setSample(i, j, 0, gray);
//                }
//                catch(Exception graycatch)
//                {
//                    System.out.println("Error konversi gray : "+graycatch.getMessage());
//                }
//            }
//        }
//
//        if(createFile) {
//            try {
//                ImageIO.write(result, "jpg", new File("gray_result.jpg"));
//            } catch (IOException ex) {
//                Logger.getLogger(Grayscale.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        if(print==true)
//        {
//            for(int j=0; j<result.getHeight(); j++){
//                for(int i=0; i<result.getWidth(); i++){
//                    System.out.print(result.getRGB(i, j)+"\t");
//                }
//
//                System.out.println();
//            }
//        }
//
//        return result;
//    }
    
    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }

    public static int[][] toGray(int[][] gray, boolean print) {
        for (int j = 0; j < gray.length; j++) {
            for(int i = 0; i < gray[0].length; i++) {
                try {
                    int color = gray[i][j];
                    int red = (int) (Color.red(color) * 0.213);
                    int green = (int) (Color.green(color) * 0.715);
                    int blue = (int) (Color.blue(color) * 0.072);

                    //Konversi ke Grayscale
                    /*
                        Rumus Konversi RGB ke Grayscale :

                        Gray = (Red * 0.299) + (Green * 0.587) + (Blue * 0.114)
                    */
                    gray[j][i]= red + green + blue;

                } catch (Exception graycatch) {
                    System.out.println("Error konversi gray : "+graycatch.getMessage());
                }
            }
        }

        if (print) {
            for (int[] aGray : gray) {
                for (int anAGray : aGray) {
                    System.out.print(anAGray + "\t");
                }
                System.out.println();
            }
        }

        return gray;
    }
    
    public static int[][] toGray(Bitmap citra, boolean print) {
        int[][] gray = new int[citra.getHeight()][citra.getWidth()];
        
        for (int j = 0; j < citra.getHeight(); j++) {
            for(int i = 0; i < citra.getWidth(); i++) {
                try {
                    int color = citra.getPixel(i, j);
                    int red = (int) (Color.red(color) * 0.213);
                    int green = (int) (Color.green(color) * 0.715);
                    int blue = (int) (Color.blue(color) * 0.072);

                    //Konversi ke Grayscale
                    /*
                        Rumus Konversi RGB ke Grayscale :

                        Gray = (Red * 0.299) + (Green * 0.587) + (Blue * 0.114) 
                    */
                    gray[j][i]= red + green + blue;

                } catch (Exception graycatch) {
                    System.out.println("Error konversi gray : "+graycatch.getMessage());
                }
            }
        }
        
        if (print) {
            for (int[] aGray : gray) {
                for (int anAGray : aGray) {
                    System.out.print(anAGray + "\t");
                }
                System.out.println();
            }
        }

        return gray;
    }
}