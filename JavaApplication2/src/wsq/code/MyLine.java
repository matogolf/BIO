package wsq.code;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * This class inherits from MyShape and is responsible for drawing a line.
 */
public class MyLine extends MyShape
{  
    /**
     * No parameter constructor which calls the no parameter constructor in MyShape
     */
    public MyLine()
    {
        super();
    }
    
    /** 
     * Overloaded constructor that takes coordinates and color. It passes them to the constructor in MyShape
     */
    public MyLine( int x1, int y1, int x2, int y2, Color color )
    {
        super(x1, y1, x2, y2, color);
    } 
     
    /**
     * Overrides the draw method in Myshape. It sets the gets the color from Myshape
     * and the coordinates it needs to draw from MyShape as well.
     */
    @Override
    public void draw( Graphics g )
    {
        g.setColor( getColor() ); //sets the color
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3.0F));
        g2.drawLine( getX1(), getY1(), getX2(), getY2() ); //draws the line
    } 
} // end class MyLine