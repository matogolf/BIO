/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsq.editor;

import com.sun.org.glassfish.external.statistics.annotations.Reset;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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
    

}
