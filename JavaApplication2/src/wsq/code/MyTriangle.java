package wsq.code;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * This class inherits from MyBoundedShape and is responsible for drawing a oval.
 */
public class MyTriangle extends MyShape
{ 
    /**
     * No parameter constructor which calls the no parameter constructor in MyBoundedShape.
     */
    public MyTriangle()
    {
        super();
    }
    
    /** 
     * Overloaded constructor that takes coordinates, color and fill. 
     * It passes them into MyBoundedShape's constructor.
     */
    public MyTriangle( int x1, int y1, int x2, int y2, int x3, int y3, Color color)
    {
        super(x1, y1, x2, y2, x3, y3, color);
        setClickCount(1);
    }
    
     
    /**
     * Overrides the draw method in MyBoundedShape. It sets the gets the color from MyBoundedShape
     * to set the color and the values it needs to draw from MyBoundedShape as well.
     */
    @Override
    public void draw( Graphics g )
    {
        g.setColor( getColor() ); //sets the color
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3.0F));
        if (getClickcount() == 1) {
            g2.drawLine( getX1(), getY1(), getX2(), getY2() ); //draws the line
        } else if (getClickcount() == 2 || getClickcount() == 3) {
            g2.drawLine( getX1(), getY1(), getX2(), getY2() ); //draws the line
            g2.drawLine( getX1(), getY1(), getX3(), getY3() ); //draws the line
            g2.drawLine( getX2(), getY2(), getX3(), getY3() ); //draws the line
        }
        
    }
    
} // end class MyOval