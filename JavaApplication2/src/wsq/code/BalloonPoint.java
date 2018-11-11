/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsq.code;

import java.awt.Color;
import javax.swing.JLabel;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.EdgedBalloonStyle;

/**
 *
 * @author John
 */
public class BalloonPoint {
    
    private int x, y; //coordinates
    private JLabel point;
    private BalloonTip myBalloonTip; 
    private boolean inputEnabled;
    private String bubbleText;
    private boolean isVisible;
    
    
    public BalloonPoint(int x, int y) {
        
        point = new JLabel("Test label");  
        this.x = x;
        this.y = y;
        point.setLocation(x - 5, y - 5);
        point.setOpaque(true);
        point.setBackground(Color.blue);
        point.setSize(10, 10);
        point.setForeground(Color.blue);
        BalloonTipStyle edgedLook = new EdgedBalloonStyle(Color.WHITE, Color.BLUE);
        myBalloonTip = new BalloonTip(point, "", edgedLook, true);
        bubbleText = "";
        isVisible = true;
    }
    
    public JLabel getLabel() {
        return point;
    }
    
    public BalloonTip getBalloonTop() {
        return myBalloonTip;
    }
    
    public void addInput(char letter) {
        if (inputEnabled) {
            bubbleText = bubbleText + letter;
            myBalloonTip.setTextContents(bubbleText);
        }
    }
    
    public void removeInput() {
        if (inputEnabled) {
           bubbleText = bubbleText.substring(0, bubbleText.length() - 1); 
           myBalloonTip.setTextContents(bubbleText);
        }
    }
    
    public void disableInput() {
        inputEnabled = false;
        myBalloonTip.setVisible(false);
        isVisible = false;
    }
    
    public void enableInput() {
        inputEnabled = true;
        myBalloonTip.setVisible(true);
        isVisible = true;
    }
    
    public void setVisible() { //only relevant if input is disabled
        if (!inputEnabled) {
            myBalloonTip.setVisible(true);
            isVisible = true;
        }
    }
    
    public void setInvisible() {
        if (!inputEnabled) {
            myBalloonTip.setVisible(false);
            isVisible = false;
        }
    }
    
    public boolean isInputEnabled() {
        return inputEnabled;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
}
