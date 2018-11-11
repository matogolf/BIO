/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsq.code;

import com.sun.org.glassfish.external.statistics.annotations.Reset;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;
import sun.net.www.content.audio.x_aiff;

/**
 *
 * @author matogolf
 */


public class ImageTools {
    
    
    
    // zmeni velkost obrazka pre vykreslovanie
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }  
    
    // zoberie strany obrazka a hranice obrazovky
    // vrati nove velkosti stran zo zachovanym pomerom
    public static Dimension getScaledDimension(int Xoriginal, int Yoriginal, int xBound, int yBound) {

        int new_width = Xoriginal;
        int new_height = Yoriginal;

        // first check if we need to scale width
        if (Xoriginal > xBound) {
            //scale width to fit
            new_width = xBound;
            //scale height to maintain aspect ratio
            new_height = (new_width * Yoriginal) / Xoriginal;
        }

        // then check if we need to scale even with the new height
        if (new_height > yBound) {
            //scale height to fit instead
            new_height = yBound;
            //scale width to maintain aspect ratio
            new_width = (new_height * Xoriginal) / Yoriginal;
        }

        return new Dimension(new_width, new_height);
    }
   
    public static Dimension getWindowSize(int x, int y, int xBound, int yBound) {
        int newX = x;
        int newY = y;
        if (x < xBound) {
            x = x + 150;
        }
        else {
            x = -1;
        }
        
        if (y < yBound && y > 600) {
            y = y + 100;
        }
        else if (y < yBound && y <= 600){
            y = 600;
        }
        else {
            y = -1;
        }
     
        return new Dimension(x, y);
    }
    
    
    public static BufferedImage changeBrightness(BufferedImage picture1, int factor) {
        BufferedImage picture2 = new BufferedImage(picture1.getWidth(), picture1.getHeight(),BufferedImage.TYPE_INT_RGB);      
        
        int width  = picture1.getWidth();
        int height = picture1.getHeight();
        factor = 255 * factor/100;

        for (int y = 0; y < height ; y++) {//loops for image matrix
            for (int x = 0; x < width ; x++) {

            Color c=new Color(picture1.getRGB(x,y));
            
            //adding factor to rgb values
            int r=c.getRed()+factor;
            int b=c.getBlue()+factor;
            int g=c.getGreen()+factor;
            if (r >= 256) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            if (g >= 256) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b >= 256) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
        
            picture2.setRGB(x, y,new Color(r,g,b).getRGB());
       
            }
        }
    return picture2;
    }

    static BufferedImage changeContrast(BufferedImage picture1, int contrast) {
         BufferedImage picture2 = new BufferedImage(picture1.getWidth(), picture1.getHeight(),BufferedImage.TYPE_INT_RGB);      
        
        int width  = picture1.getWidth();
        int height = picture1.getHeight();

        for (int y = 0; y < height ; y++) {//loops for image matrix
            for (int x = 0; x < width ; x++) {

            Color c=new Color(picture1.getRGB(x,y));
            
            int r = c.getRed();   
            int b = c.getBlue();   
            int g = c.getGreen();
            //adding factor to rgb values
            
            if (contrast >=0) {
                if (c.getRed() > 127) {
                    r = c.getRed() + contrast;   
                    b = c.getBlue() + contrast;   
                    g = c.getGreen() + contrast;   
                } else {
                    r = c.getRed() - contrast;   
                    b = c.getBlue() - contrast;   
                    g = c.getGreen() - contrast;  
                }
            } else {
                if (c.getRed() > 127) {
                    r = c.getRed() + contrast;   
                    b = c.getBlue() + contrast;   
                    g = c.getGreen() + contrast;   
                    if (r < 127) 
                        r = b = g = 127; //we dont want inverted colors...
                } else {
                    r = c.getRed() - contrast;   
                    b = c.getBlue() - contrast;   
                    g = c.getGreen() - contrast;  
                    if (r > 127)
                        r = b = g = 127;
                }
            }
 
            if (r >= 256) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            if (g >= 256) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b >= 256) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
        
            picture2.setRGB(x, y,new Color(r,g,b).getRGB());
       
            }
        }
    return picture2;
    }

    
    static BufferedImage rotate(BufferedImage img)
    {
        int         width  = img.getWidth();
        int         height = img.getHeight();
        BufferedImage   newImage = new BufferedImage( height, width, img.getType() );
 
        for( int i=0 ; i < width ; i++ )
            for( int j=0 ; j < height ; j++ )
                newImage.setRGB( height-1-j, i, img.getRGB(i,j) );
 
            return newImage;
    }

    static BufferedImage maximumDispaly(BufferedImage img) {
            Dimension newImgSize = getScaledDimension(img.getWidth(), img.getHeight(), 230, 130);
            
            int x = (int) newImgSize.getWidth();
            int y = (int) newImgSize.getHeight();
            
            img = ImageTools.resize(img, x, y);
            return img;
    }
            
    
}
