package wsq.code;


import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.EdgedBalloonStyle;

/**
 * This class handles mouse events and uses them to draw shapes.
 * It contains a dynamic stack myShapes which is the shapes drawn on the panel.
 * It contains a dynamic stack clearedShape which is the shapes cleared from the panel.
 * It has many variables for the current shape [type, variable to store shape object, color, fill].
 * It contains a JLabel called statusLabel for the mouse coordinates
 * It has mutator methods for currentShapeType, currentShapeColor and currentShapeFilled.
 * It has methods for undoing, redoing and clearing shapes.
 * It has a private inner class MouseHandler which extends MouseAdapter and 
 * handles mouse and mouse motion events used for drawing the current shape.
 */

public class DrawPanel extends JPanel
{
    private LinkedList<MyShape> myShapes; //dynamic stack of shapes
    private LinkedList<MyShape> clearedShapes; //dynamic stack of cleared shapes from undo
    
    //current Shape variables
    private int currentShapeType; // 1 for line, 2 for oval, 3 for triangle, 4 for text
    private MyShape currentShapeObject; //stores the current shape object
    private Color currentShapeColor; //current shape color
    private boolean currentShapeFilled; //determine whether shape is filled or not
    private DrawFrame myFrame;
    private Image img;
    private BufferedImage img2;
    private boolean paintNow = false;
    private int oldX, oldY, nowX, nowY;
    private int imageHeight = 0, imageWidth = 0;
    private List<BalloonPoint> textBoxes;
    private float scale = 1;
    private double zoom = 1.0;  // zoom factor 
    private double zoom_x = 0;
    private double zoom_y = 0;
    
    JLabel statusLabel; //status label for mouse coordinates
    
    /**
     * This constructor initializes the dynamic stack for myShapes and clearedShapes.
     * It sets the current shape variables to default values.
     * It initalizes statusLabel from JLabel passed in.
     * Sets up the panel and adds event handling for mouse events.
     */
    public DrawPanel(JLabel statusLabel, DrawFrame myFrame){
        this.textBoxes = new ArrayList<BalloonPoint>();
        
        this.myFrame = myFrame;
        myShapes = new LinkedList<MyShape>(); //initialize myShapes dynamic stack
        clearedShapes = new LinkedList<MyShape>(); //initialize clearedShapes dynamic stack
        textBoxes = new ArrayList<BalloonPoint>();
        
        //Initialize current Shape variables
        currentShapeType=-1;
        currentShapeObject=null;
        currentShapeColor=Color.BLACK;
        currentShapeFilled=false;
        
        this.statusLabel = statusLabel; //Initialize statusLabel
        
        setLayout(new BorderLayout()); //sets layout to border layout; default is flow layout
        setBackground( Color.WHITE ); //sets background color of panel to white
        add( statusLabel, BorderLayout.SOUTH );  //adds a statuslabel to the south border
        
        // event handling for mouse and mouse motion events
        MouseHandler handler = new MouseHandler();                                    
        addMouseWheelListener( handler );        
        addMouseListener( handler );
        addMouseMotionListener( handler ); 
        MyKeyListener keyListener = new MyKeyListener();
        addKeyListener(keyListener);
        
    }
    
    /**
     * Calls the draw method for the existing shapes.
     */
    public void paintComponent( Graphics g )
    {
        
        Graphics2D g2 = null;
        super.paintComponent( g );
        
        if (img != null) {
           g2 = (Graphics2D) img.getGraphics();
        } 
                
        g.drawImage(img, 0, 0, null); 

        if (currentShapeObject!=null)
            currentShapeObject.draw(g2);
        
        Graphics2D g2d = null;
            super.paintComponent(g);
        if (img != null) {
           g2d = (Graphics2D) img.getGraphics();
        }             
            g2d = (Graphics2D) g.create();
           
            double width = zoom_x;//getWidth();
            double height = zoom_y;//getHeight();

            double zoomWidth = width * zoom;
            double zoomHeight = height * zoom;

            double anchorx = (width - zoomWidth) / 1.0;
            double anchory = (height - zoomHeight) / 1.0;

            System.out.println("======");
            System.out.println(width);
            System.out.println(height);
            System.out.println(zoomWidth);
            System.out.println(zoomHeight);
            System.out.println("======");

            AffineTransform at = new AffineTransform();
            at.translate(anchorx, anchory);
            at.scale(zoom, zoom);
            at.translate(1, 1);

            g2d.setTransform(at);
            g2d.drawImage(img, 0, 0, null);
            
    }
        
    public Image getImage() {
        return img;
    }
    //Mutator methods for currentShapeType, currentShapeColor and currentShapeFilled
    
    /**
     * Sets the currentShapeType to type (0 for line, 1 for rect, 2 for oval) passed in.
     */
    public void setCurrentShapeType(int type)
    {
        if (type == -1) {

            currentShapeType=-1;
            currentShapeObject=null;
            currentShapeColor=Color.BLACK;
            currentShapeFilled=false;
        }
        else {
            currentShapeType=type;
        }
    }
    
    /**
     * Sets the currentShapeColor to the Color object passed in.
     * The Color object contains the color for the current shape.
     */
    public void setCurrentShapeColor(Color color)
    {
        currentShapeColor=color;
    }
    
    /**
     * Sets the boolean currentShapeFilled to boolean filled passed in. 
     * If filled=true, current shape is filled. 
     * If filled=false, current shape is not filled.
     */
    public void setCurrentShapeFilled(boolean filled)
    {
        currentShapeFilled=filled;
    }
    
    
    /**
     * Clear the last shape drawn and calls repaint() to redraw the panel if clearedShapes is not empty
     */
    public void clearLastShape()
    {
        if (! myShapes.isEmpty())
        {
            clearedShapes.addFront(myShapes.removeFront());
            repaint();
        }
    }
    
    /**
     * Redo the last shape cleared if clearedShapes is not empty
     * It calls repaint() to redraw the panel.
     */
    public void redoLastShape()
    {
        if (! clearedShapes.isEmpty())
        {
            myShapes.addFront(clearedShapes.removeFront());
            repaint();
        }
    }
    
    /**
     * Remove all shapes in current drawing. Also makes clearedShapes empty since you cannot redo after clear.
     * It called repaint() to redraw the panel.
     */
    public void clearDrawing()
    {
        myFrame.setSize( 700,700 );
        myShapes.makeEmpty();
        clearedShapes.makeEmpty();
        
        repaint();
    }

    void importImage(Image img) {
        this.img=img;
        Dimension size = new Dimension(img.getWidth(null) + 82, img.getHeight(null) + 70);
        myFrame.setSize(size);
        this.paintNow = true;
    }

    void setUndecorated(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setImageSize(int imageWidth, int imageHeight) {
        
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    
    
    /**
     * Private inner class that implements MouseAdapter and does event handling for mouse events.
     */
    private class MouseHandler extends MouseAdapter 
    {
        /**
         * When mouse is pressed draw a shape object based on type, color and filled.
         * X1,Y1 & X2,Y2 coordinate for the drawn shape are both set to the same X & Y mouse position.
         */
        public void mousePressed( MouseEvent event )
        {
            System.out.println("Mouse pressssssssssssed");

            setTextBoxesInvisible();

            if (currentShapeType == 1) 
                currentShapeObject= new MyMultiLine( event.getX(), event.getY(), event.getX(), event.getY(), Color.red);
            else if (currentShapeType == 2)
                currentShapeObject= new MyOval( event.getX(), event.getY(), event.getX(), event.getY(), Color.green, false);
            else if (currentShapeType == 3){
                if (currentShapeObject == null) {
                    currentShapeObject= new MyTriangle( event.getX(), event.getY(), event.getX(), event.getY(), event.getX(), event.getY(), Color.green);
                    currentShapeObject.setClickCount(1);
                } else if (currentShapeObject.getClickcount() == 1) {
                    currentShapeObject.setX2(event.getX());
                    currentShapeObject.setY2(event.getY());
                    currentShapeObject.setClickCount(2);
                } else if (currentShapeObject.getClickcount() == 2) {
                    currentShapeObject.setX3(event.getX());
                    currentShapeObject.setY3(event.getY());
                    currentShapeObject.setClickCount(3);
                    myShapes.addFront(currentShapeObject); //addFront currentShapeObject onto myShapes
                    
                    currentShapeObject=null; //sets currentShapeObject to null
                    clearedShapes.makeEmpty(); //clears clearedShapes
                    repaint();
                }
            } else if (currentShapeType == 4) {
                
                int x = event.getX();
                int y = event.getY();
                int bubbleX, bubbleY;
                
                BalloonPoint textBox = null;
                for ( int counter=textBoxes.size()-1; counter>=0; counter-- )  {
                    textBox = textBoxes.get(counter);
                    bubbleX = textBox.getX();
                    bubbleY = textBox.getY();
                    if (x >= (bubbleX - 5) && x <= (bubbleX + 5) && y >= (bubbleY - 5) && y <= (bubbleY + 5) && !textBox.isVisible()) { //cursor is within bubble point
                        textBox.enableInput();
                        return;
                    }

                } 
                
                textBox = new BalloonPoint(event.getX(), event.getY(), imageWidth, imageHeight);
                myFrame.addLabel(textBox.getLabel());
                textBox.enableInput();
                textBoxes.add(textBox);
            }
                
        } // end method mousePressed
        
        /**
         * When mouse is released set currentShapeObject's x2 & y2 to mouse pos.
         * Then addFront currentShapeObject onto the myShapes dynamic Stack 
         * and set currentShapeObject to null [clearing current shape object since it has been drawn].
         * Lastly, it clears all shape objects in clearedShapes [because you cannot redo after a new drawing]
         * and calls repaint() to redraw panel.
         */
        public void mouseReleased( MouseEvent event )
        {
            //sets currentShapeObject x2 & Y2
            if (currentShapeType == 1) {
                currentShapeObject.setX2(event.getX());
                currentShapeObject.setY2(event.getY());
            
                myShapes.addFront(currentShapeObject); //addFront currentShapeObject onto myShapes
            }
            else if (currentShapeType == 2) {
                currentShapeObject.setX2(event.getX());
                currentShapeObject.setY2(event.getY());
            
                myShapes.addFront(currentShapeObject); //addFront currentShapeObject onto myShapes
            }
            
            if (currentShapeType != 3)
            currentShapeObject=null; //sets currentShapeObject to null
            clearedShapes.makeEmpty(); //clears clearedShapes
            repaint();
            
        } // end method mouseReleased
        
     
        /**
         * This method gets the mouse pos when it is moving and sets it to statusLabel.
         */
        public void mouseMoved( MouseEvent event )
        {
            
            int x = event.getX();
            int y = event.getY();
            zoom_x = x;
            zoom_y = y;
            
            if (currentShapeType == 3 && currentShapeObject != null){
                if (currentShapeObject.getClickcount() == 1) {
                    currentShapeObject.setX2(x);
                    currentShapeObject.setY2(y);
                } else if (currentShapeObject.getClickcount() == 2) {
                    currentShapeObject.setX3(x);
                    currentShapeObject.setY3(y);
                }
            }
            int bubbleX, bubbleY;
            
            BalloonPoint textBox = null;
            for ( int counter=textBoxes.size()-1; counter>=0; counter-- )  {
                textBox = textBoxes.get(counter);
                bubbleX = textBox.getX();
                bubbleY = textBox.getY();
                if (x >= (bubbleX - 5) && x <= (bubbleX + 5) && y >= (bubbleY - 5) && y <= (bubbleY + 5)) { //cursor is within bubble point
                    textBox.setVisible();
                } else {
                    textBox.setInvisible();
                }
                
            }           

            statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d",event.getX(),event.getY()));
            
            repaint();
        } // end method mouseMoved
        
        /**
         * This method gets the mouse position when it is dragging and sets x2 & y2 of current shape to the mouse pos
         * It also gets the mouse position when it is dragging and sets it to statusLabel
         * Then it calls repaint() to redraw the panel
         */
        public void mouseDragged( MouseEvent event )
        {
            //sets currentShapeObject x2 & Y2
            if (currentShapeType == 1) 
                currentShapeObject.addLine(event.getX(), event.getY());
            else if (currentShapeType == 2) {
                currentShapeObject.setX2(event.getX());
                currentShapeObject.setY2(event.getY());
            }
            
            //sets statusLabel to current mouse position
            statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d",event.getX(),event.getY()));
            
            repaint();
            
        } // end method mouseDragged
        
        @Override        
        public void mouseWheelMoved(MouseWheelEvent e) {
                    System.out.println("mouseeeeeeeeeeeee");
                int notches = e.getWheelRotation();
                double temp = zoom - (notches * 0.2);
                // minimum zoom factor is 1.0
                temp = Math.max(temp, 1.0);
                if (temp != zoom) {
                    zoom = temp;
                    resizeImage();
                }                    
                repaint();
        }     
        
    }// end MouseHandler
       
    public void resizeImage() {
           System.out.println(zoom);
           System.out.println(zoom_x);
           System.out.println(zoom_y);
        }    
    
    public class MyKeyListener implements KeyListener {
        
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            char keyChar = e.getKeyChar();
            BalloonPoint textBox = null;

            for ( int counter=textBoxes.size()-1; counter>=0; counter-- ) {
                textBox = textBoxes.get(counter);
                if (textBox.isInputEnabled()) {
                    if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                        textBox.disableInput();
                    } else if (key == KeyEvent.VK_BACK_SPACE) {
                        textBox.removeInput();
                    } else if (key == KeyEvent.VK_DELETE) {
                        DrawPanel.this.remove(textBox.getLabel());
                        textBox.disableInput();
                        textBoxes.remove(counter);
                        textBox = null;
                    } else if (keyChar != KeyEvent.VK_UNDEFINED) {
                        textBox.addInput(keyChar);
                    }
                    break; //only 1 textbox should be enabled so break
                }
            }

            repaint();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            ;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            ;
        }
    }
    
    public void setTextBoxesInvisible() {
        
        BalloonPoint textBox = null;
        
        for ( int counter=textBoxes.size()-1; counter>=0; counter-- )  {
            
            textBox = textBoxes.get(counter);
            textBox.disableInput();
        }           

        repaint();
        
    }
    
    public void redrawTextPoints() {
        
        BalloonPoint textBox = null;
        for ( int counter=textBoxes.size()-1; counter>=0; counter-- )  {
            textBox = textBoxes.get(counter);
            textBox.rotatePoint();
        }       
    }
} // end class DrawPanel