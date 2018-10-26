package wsq.code;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jnbis.api.Jnbis;


/**
 * Provides the GUI and encapsulates the DrawPanel
 * It creates 3 buttons undo, redo and clear.
 * It creates 2 combobox colors and shapes.
 * It creates 1 checkbox filled to select whether shape is filled or not.
 * Has two private inner classes 
 * One for handling button events
 * Another for handling both combo box events and checkbox events
 */
public class DrawFrame extends JFrame
{
    private static String WSQ_FILE_NAME;
    private JLabel stausLabel; //label display mouse coordinates
    private DrawPanel panel; //draw panel for the shapes
    private int screenWidth;            //velkost obrazovky uzivatela
    private int screenHeight;           //velkost obrazovky uzivatela
    public int xBoundery;               // orezana plocha na zobrazovanie obrazka
    public int yBoundery;               // orezana plocha na zobrazovanie obrazka
    private int xBorder = 230;          // o kolko sa oreazava zobrazovacia plocha
    private int yBorder = 130;          // o kolko sa oreazava zobrazovacia plocha
    
    
    private JButton undo; // button to undo last drawn shape
    private JButton redo; // button to redo an undo
    private JButton clear; // button to clear panel
    
    private BufferedImage img = null;   // obrazok ktory sa zobrazuje
    
    private JComboBox colors; //combobox with color options
    
    //array of strings containing color options for JComboBox colors
    private String colorOptions[]=
    {"Black","Blue","Cyan","Dark Gray","Gray","Green","Light Gray",
        "Magenta","Orange","Pink","Red","White","Yellow"};
    
    //aray of Color objects contating Color constants
    private Color colorArray[]=
    {Color.BLACK , Color.BLUE , Color.CYAN , Color.darkGray , Color.GRAY , 
        Color.GREEN, Color.lightGray , Color.MAGENTA , Color.ORANGE , 
    Color.PINK , Color.RED , Color.WHITE , Color.YELLOW};
    
    private JComboBox shapes; //combobox with shape options
    
    //array of strings containing shape options for JComboBox shapes
    private String shapeOptions[]=
    {"Line","Rectangle","Oval"};
    
    private JCheckBox filled; //checkbox to select whether shape is filled or not
        
    private JPanel widgetJPanel; //holds the widgets: buttons, comboboxes and checkbox
    private JPanel widgetPadder; //encapsulates widgetJPanel and adds padding around the edges 
    
    /**
     * This constructor sets the name of the JFrame.
     * It also creates a DrawPanel object that extends JPanel for drawing the shapes and contains
     * a statuslabel for mouse position.
     * Initializes widgets for buttons, comboboxes and checkbox
     * It also adds event handlers for the widgets
     */
    public DrawFrame()
    {
        super("SuperPaint Application v2.0!"); //sets the name of DrawFrame
        
        JLabel statusLabel = new JLabel( "" ); //create JLabel object to pass into DrawPanel
            
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();
        xBoundery = this.screenWidth - xBorder;
        yBoundery = this.screenHeight - yBorder;
        
        panel = new DrawPanel(statusLabel, this); //create draw panel and pass in JLabel
        
        //create buttons
        undo = new JButton();
        redo = new JButton( "Redo" );
        clear = new JButton( "Clear" );
        
        undo.setBorderPainted(false);
        undo.setBorder(null);
        //button.setFocusable(false);
        undo.setMargin(new Insets(0, 0, 0, 0));
        undo.setContentAreaFilled(false);
        undo.setIcon(new ImageIcon(getClass().getResource("crop.png")));
        //undo.setPreferredSize(new Dimension(40, 40));
        
        //create comboboxes
        colors = new JComboBox( colorOptions );
        shapes = new JComboBox( shapeOptions );
        
        //create checkbox
        filled = new JCheckBox( "Filled" );
        
        //JPanel object, widgetJPanel, with grid layout for widgets
        widgetJPanel = new JPanel();
        widgetJPanel.setLayout( new GridLayout( 6, 1, 0, 5 ) ); //sets padding between widgets in gridlayout
        
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Veci");
        JMenuItem importWsq = new JMenuItem("import wsq file");
        importWsq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importWsqActionPerformed(evt);
            }
        });
        menu.add(importWsq);
        menubar.add(menu);
        this.setJMenuBar(menubar);
        
        //JPanel object, widgetPadder, with flowlayout to encapsulate and pad the widgetJPanel
        widgetPadder = new JPanel();
        widgetPadder.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 5)); //sets padding around the edges
            
        // add widgets to widgetJPanel
        widgetJPanel.add( undo );
        widgetJPanel.add( redo );
        widgetJPanel.add( clear );
        //widgetJPanel.add( colors );
        //widgetJPanel.add( shapes );                 
        //widgetJPanel.add( filled );
        // add widgetJPanel to widgetPadder
        widgetPadder.add( widgetJPanel );
        
        //add widgetPadder and panel to JFrame
        add( widgetPadder, BorderLayout.WEST);
        add( panel, BorderLayout.CENTER);
        
        // create new ButtonHandler for button event handling
        ButtonHandler buttonHandler = new ButtonHandler();
        undo.addActionListener( buttonHandler );
        redo.addActionListener( buttonHandler );
        clear.addActionListener( buttonHandler );
        
        //create handlers for combobox and checkbox
        ItemListenerHandler handler = new ItemListenerHandler();
        colors.addItemListener( handler );
        shapes.addItemListener( handler );
        filled.addItemListener( handler );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( 500, 500 );
        setVisible( true );
        
    } // end DrawFrame constructor
    
    /**
     * private inner class for button event handling
     */
    private class ButtonHandler implements ActionListener
    {
        // handles button events
        public void actionPerformed( ActionEvent event )
        {
            if (event.getSource() == undo){
                panel.clearLastShape();
            }
            else if (event.getActionCommand().equals("Redo")){
                panel.redoLastShape();
            }
            else if (event.getActionCommand().equals("Clear")){
                panel.clearDrawing();
            }
             
        } // end method actionPerformed
    } // end private inner class ButtonHandler
    
    /**
     * private inner class for checkbox and combobox event handling
     */
    private class ItemListenerHandler implements ItemListener
    {
        public void itemStateChanged( ItemEvent event )
        {
            // process filled checkbox events
            if ( event.getSource() == filled )
            {
                boolean checkFill=filled.isSelected() ? true : false; //
                panel.setCurrentShapeFilled(checkFill);
            }
            
            // determine whether combo box selected
            if ( event.getStateChange() == ItemEvent.SELECTED )
            {
                //if event source is combo box colors pass in colorArray at index selected.
                if ( event.getSource() == colors)
                {
                    panel.setCurrentShapeColor
                        (colorArray[colors.getSelectedIndex()]);
                }
                
                //else if event source is combo box shapes pass in index selected
                else if ( event.getSource() == shapes)
                {
                    panel.setCurrentShapeType(shapes.getSelectedIndex());
                }
            }
            
        } // end method itemStateChanged
    }
    
        private void importWsqActionPerformed(java.awt.event.ActionEvent evt) {                                           
        FileDialog f = new FileDialog(this, "Open WSQ File ", FileDialog.LOAD);
        String directory = null;
        f.setDirectory(directory);       // set the default directory
        f.show();
        directory = f.getDirectory();
        String filepath = directory+f.getFile();
        // display the dialog and wait for the user's response
        System.out.println(filepath);    

        WSQ_FILE_NAME = filepath;//("/home/ormos/Downloads/a001.wsq");
        byte[] jpgArray;
        jpgArray = Jnbis.wsq().decode(WSQ_FILE_NAME).toJpg().asByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(jpgArray);
        try {
            img = ImageIO.read(bais);
            Dimension newImgSize = ImageTools.getScaledDimension(img.getWidth(), img.getHeight(), xBoundery, yBoundery);
            int x = (int) newImgSize.getWidth();
            int y = (int) newImgSize.getHeight();
            img = ImageTools.resize(img, x, y);
            
            /*if (ImageTools.getWindowSize(x, y, xBoundery, yBoundery).getWidth() < 0 ||
                ImageTools.getWindowSize(x, y, xBoundery, yBoundery).getHeight() < 0) {
                this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            }
            else {
                this.setSize(ImageTools.getWindowSize(x, y, xBoundery, yBoundery));
            }*/
            
            //this.setSize( 700,700 );
                            
            //imgViewerLabel.setIcon(new javax.swing.ImageIcon(img)); 
            //panel.printImage(img);
            panel.importImage(new javax.swing.ImageIcon(img).getImage());            
            validate();
            repaint();
        
        } catch (IOException ex) {
            Logger.getLogger(DrawFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
} // end class DrawFrame