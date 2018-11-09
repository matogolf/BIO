package wsq.code;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * This class inherits from MyShape and is responsible for drawing a line.
 */
public class MyMultiLine extends MyShape
{  
    /**
     * No parameter constructor which calls the no parameter constructor in MyShape
     */
    private LinkedList<MyShape> myLines;
    private int currentX, currentY;
    private MyShape currentLine;
    public MyMultiLine()
    {
        super();
        myLines = new LinkedList<MyShape>();
        currentLine = null;
    }
    
    /** 
     * Overloaded constructor that takes coordinates and color. It passes them to the constructor in MyShape
     */
    public MyMultiLine( int x1, int y1, int x2, int y2, Color color )
    {
        super(x1, y1, x2, y2, color);
        myLines = new LinkedList<MyShape>();
        currentX = x1;
        currentY = y1;
    } 
    
    public void addLine(int x, int y) {
        currentLine= new MyLine( currentX, currentY, x, y, super.getColor());
        myLines.addFront(currentLine);
        currentX = x;
        currentY = y;
    }
     
    /**
     * Overrides the draw method in Myshape. It sets the gets the color from Myshape
     * and the coordinates it needs to draw from MyShape as well.
     */
    @Override
    public void draw( Graphics g )
    {
        g.setColor( getColor() ); //sets the color
        ArrayList<MyShape> shapeArray=myLines.getArray();
        for ( int counter=shapeArray.size()-1; counter>=0; counter-- )
           shapeArray.get(counter).draw(g);
    } 
} // end class MyLine