package wsq.code;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class contains int coordinates and a Color color. It has accessor and mutator methods for them.
 */
abstract class MyShape
{
    private int x1,y1,x2,y2, x3, y3; //coordinates of shape
    private Color color; // color of shape
    private int clickCount;
    
    /**
    * public constructor which takes no variables and
    * sets coordinates to zero and color to black
    */
    public MyShape()
    {
        x1=0;
        y1=0;
        x2=0;
        y2=0;
        x3=0;
        y3=0;
        color=Color.BLACK;
        clickCount=0;
    }
    
    /**
    * overloaded constructor which takes variables for coordinates and 
    * color assigning them to the instance variables in the class
    */
    public MyShape(int x1, int y1, int x2, int y2, Color color)
    {
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.color=color;
        clickCount=0;
    }
    
    public MyShape(int x1, int y1, int x2, int y2, int x3, int y3, Color color)
    {
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.x3=x3;
        this.y3=y3;
        this.color=color;
        clickCount=0;
    }
    
    
    //Mutator methods
    
    /**
     * Mutator method for x1
     */
    public void setX1(int x1)
    {
        this.x1=x1;
    }   
    
    /**
     * Mutator method for y1
     */
    public void setY1(int y1)
    {
        this.y1=y1;
    }   
    
    /**
     * Mutator method for x2
     */
    public void setX2(int x2)
    {
        this.x2=x2;
    }   
    
    /**
     * Mutator method for y2
     */
    public void setY2(int y2)
    {
        this.y2=y2;
    }   
    
    public void setX3(int x3)
    {
        this.x3=x3;
    }   
    
    /**
     * Mutator method for y2
     */
    public void setY3(int y3)
    {
        this.y3=y3;
    }   
    
    /**
     * Mutator method for color
     */
    public void setColor(Color color)
    {
        this.color=color;
    }
    
    public void setClickCount(int clicks) {
        this.clickCount = clicks;
    }
    //Accessor methods
    
    /**
     * Accessor method for x1
     */
    public int getX1()
    {
        return x1;
    }
    
    /**
     * Accessor method for y1
     */
    public int getY1()
    {
        return y1;
    }
    
    /**
     * Accessor method for x2
     */
    public int getX2()
    {
        return x2;
    }
    
    /**
     * Accessor method for y2
     */
    public int getY2()
    {
        return y2;
    }
    
    public int getX3()
    {
        return x3;
    }
    
    /**
     * Accessor method for y2
     */
    public int getY3()
    {
        return y3;
    }
    
    /**
     * Accessor method for color
     */
    public Color getColor()
    {
        return color;
    }
    
    public int getClickcount(){
        return clickCount;
    }
    
    public void addLine(int x, int y) {
        
    }
    
    /**
     * Abstract method for drawing the shape that must be overriden
     */
    abstract public void draw( Graphics g );

}