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
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
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
    
    private JButton brightnessButton;
    private javax.swing.JButton contrastButton;
    private javax.swing.JButton cropButton;
    public javax.swing.JLabel imgViewerLabel;
    private javax.swing.JLabel importImg;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JButton markantButton;
    private javax.swing.JButton rotateButton;
    private javax.swing.JButton textButton;
    private javax.swing.JButton zoomMinusButton;
    private javax.swing.JButton zoomPlusButton;
    
    /**
     * This constructor sets the name of the JFrame.
     * It also creates a DrawPanel object that extends JPanel for drawing the shapes and contains
     * a statuslabel for mouse position.
     * Initializes widgets for buttons, comboboxes and checkbox
     * It also adds event handlers for the widgets
     */
    public DrawFrame()
    {
        super("Brutal BIO WSQ Editor v2.0.1!"); //sets the name of DrawFrame
        
        JLabel statusLabel = new JLabel( "" ); //create JLabel object to pass into DrawPanel
        
            
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();
        xBoundery = this.screenWidth - xBorder;
        yBoundery = this.screenHeight - yBorder;
        
        panel = new DrawPanel(statusLabel, this); //create draw panel and pass in JLabel
        
        //create buttons
        undo = new JButton( "Undo" );
        redo = new JButton( "Redo" );
        clear = new JButton( "Clear" );
        
        importImg = new javax.swing.JLabel();
        brightnessButton = new JButton();
        contrastButton = new JButton();
        markantButton = new JButton();
        rotateButton = new JButton();
        cropButton = new JButton();
        textButton = new JButton();
        zoomPlusButton = new JButton();
        zoomMinusButton = new JButton();

        undo.setBorderPainted(false);
        redo.setBorderPainted(false);
        clear.setBorderPainted(false);
        brightnessButton.setBorderPainted(false);        brightnessButton.setBounds(100, 100, 100, 80);

        contrastButton.setBorderPainted(false);
        markantButton.setBorderPainted(false);
        rotateButton.setBorderPainted(false);
        cropButton.setBorderPainted(false);
        textButton.setBorderPainted(false);
        zoomPlusButton.setBorderPainted(false);
        zoomMinusButton.setBorderPainted(false);
                
        undo.setFocusable(false);
        redo.setFocusable(false);
        clear.setFocusable(false);
        brightnessButton.setFocusable(false);
        contrastButton.setFocusable(false);
        markantButton.setFocusable(false);
        rotateButton.setFocusable(false);
        cropButton.setFocusable(false);
        textButton.setFocusable(false);
        zoomPlusButton.setFocusable(false);
        zoomMinusButton.setFocusable(false);
        
        
//        undo.setMargin(new Insets(0, 0, 0, 0));
//        undo.setContentAreaFilled(false);
        //undo.setPreferredSize(new Dimension(40, 40));
        
        //create comboboxes
        colors = new JComboBox( colorOptions );
        shapes = new JComboBox( shapeOptions );
        
        //create checkbox
        filled = new JCheckBox( "Filled" );
        
        //JPanel object, widgetJPanel, with grid layout for widgets
        widgetJPanel = new JPanel();
        widgetJPanel.setLayout( new GridLayout( 12, 1 ) ); //sets padding between widgets in gridlayout
//      widgetJPanel.setLayout( new GridLayout( 6, 1, 0, 5 ) ); //sets padding between widgets in gridlayout
        
      
        /**
         * First File menu
         */
        JMenuBar menubar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        
        JMenuItem importWsq = new JMenuItem("Import WSQ file...");
        importWsq.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        importWsq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importWsqActionPerformed(evt);
            }
        });
        menu1.add(importWsq);
        
        JMenuItem importBitmap = new JMenuItem("Import Bitmap file...");
        importBitmap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        importBitmap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importBitmapActionPerformed(evt);
            }
        });
        menu1.add(importBitmap);
        
        JMenuItem quit = new JMenuItem("Quit");
        quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitPerformed(evt);
            }
        });
        menu1.add(quit);
        
        /**
         * Second Tools menu
         */
        JMenu menu2 = new JMenu("Tools");
        
        JMenu menu3 = new JMenu("Convert WSQ to other formats...");
        JMenuItem ConvertWSQtoOther = new JMenuItem("Convert WSQ to other formats...");
        ConvertWSQtoOther.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        menu2.add(menu3); 
        
        JMenuItem ImporttoFBI = new JMenuItem("Import to FBI Database...");
        ImporttoFBI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        ImporttoFBI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImporttoFBIPerformed(evt);
            }
        });
        menu2.add(ImporttoFBI);
    
        JMenuItem CallThePolice = new JMenuItem("Call the police...");
        CallThePolice.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        CallThePolice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CallThePolicePerformed(evt);
            }
        });
        menu2.add(CallThePolice);        
        
        /**
         * Second Tools sub menu
         */        
        JMenuItem ConvertWSQtoJPG = new JMenuItem("Convert WSQ to JPG...");
        ConvertWSQtoJPG.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        ConvertWSQtoJPG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConvertWSQtoJPGPerformed(evt);
            }
        });
        menu3.add(ConvertWSQtoJPG); 
        
        JMenuItem ConvertWSQtoGIF = new JMenuItem("Convert WSQ to GIF...");
        ConvertWSQtoGIF.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        ConvertWSQtoGIF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConvertWSQtoGIFPerformed(evt);
            }
        });
        menu3.add(ConvertWSQtoGIF);
    
        JMenuItem ConvertWSQtoPNG = new JMenuItem("Convert WSQ to PNG...");
        ConvertWSQtoPNG.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        ConvertWSQtoPNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConvertWSQtoPNGPerformed(evt);
            }
        });
        menu3.add(ConvertWSQtoPNG);    
        
        
        menubar.add(menu1);
        menubar.add(menu2);
        this.setJMenuBar(menubar);
        
        //JPanel object, widgetPadder, with flowlayout to encapsulate and pad the widgetJPanel
        widgetPadder = new JPanel();
        widgetPadder.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 5)); //sets padding around the edges
            
        // add widgets to widgetJPanel
        widgetJPanel.add( undo );
        widgetJPanel.add( redo );
        widgetJPanel.add( clear );
        widgetJPanel.add( brightnessButton );
        widgetJPanel.add( contrastButton );
        widgetJPanel.add( markantButton );
        widgetJPanel.add( rotateButton );
        widgetJPanel.add( cropButton );
        widgetJPanel.add( textButton );
        widgetJPanel.add( zoomPlusButton );
        widgetJPanel.add( zoomMinusButton );
        

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
        
        
        brightnessButton.setIcon(new ImageIcon(getClass().getResource("/resources/brightness.png")));
        brightnessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brightnessButtonActionPerformed(evt);
            }
        });
        
        contrastButton.setIcon(new ImageIcon(getClass().getResource("/resources/contrast.png")));
        contrastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contrastButtonActionPerformed(evt);
            }
        });
        
        markantButton.setIcon(new ImageIcon(getClass().getResource("/resources/markant.png")));
        markantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markantButtonActionPerformed(evt);
            }
        });
        
        rotateButton.setIcon(new ImageIcon(getClass().getResource("/resources/rotate.PNG")));
        rotateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateButtonActionPerformed(evt);
            }
        });
        
        cropButton.setIcon(new ImageIcon(getClass().getResource("/resources/crop.png")));
        cropButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cropButtonActionPerformed(evt);
            }
        });
        
        textButton.setIcon(new ImageIcon(getClass().getResource("/resources/text.png")));
        textButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textButtonActionPerformed(evt);
            }
        });
        
        zoomPlusButton.setIcon(new ImageIcon(getClass().getResource("/resources/zoomPlus.PNG")));
        zoomPlusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomPlusButtonActionPerformed(evt);
            }
        });
        
        zoomMinusButton.setIcon(new ImageIcon(getClass().getResource("/resources/zoomMinus.PNG")));
        zoomMinusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomMinusButtonActionPerformed(evt);
            }
        });
        

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( 700, 610 );
        setVisible( true );
        
    } // end DrawFrame constructor
    
    private void brightnessButtonActionPerformed(java.awt.event.ActionEvent evt) { 
//        JSlider slider = new JSlider(JSlider.HORIZONTAL);
//        slider.setSize(50,50);
//        
//        widgetJPanel.add(slider);
        
        img = ImageTools.changeBrightness(img, 20);
        panel.importImage(new ImageIcon(img).getImage());            
        validate();
        repaint();
    }      
    
    private void contrastButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }  
    
    private void markantButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }
    
    private void rotateButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }  
    
    private void cropButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }
    
    private void textButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }
    
    private void zoomPlusButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }
    
    private void zoomMinusButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }
    
    private void importBitmapActionPerformed(ActionEvent evt) {
        FileDialog f = new FileDialog(this, "Open bitmap File ", FileDialog.LOAD);
        String directory = null;
        f.setDirectory(directory);       // set the default directory
        f.show();
        directory = f.getDirectory();
        String filepath = directory+f.getFile();
        
        try {
            img = ImageIO.read(new File(filepath));
            Dimension newImgSize = ImageTools.getScaledDimension(img.getWidth(), img.getHeight(), xBoundery, yBoundery);
            
            int x = (int) newImgSize.getWidth();
            int y = (int) newImgSize.getHeight();
            
            img = ImageTools.resize(img, x, y);
            panel.importImage(new javax.swing.ImageIcon(img).getImage());            
            validate();
            repaint();
        } catch (IOException ex) {
            Logger.getLogger(DrawFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void quitPerformed(ActionEvent evt) {
        System.exit(0);
    }
    
    private void ImporttoFBIPerformed(ActionEvent evt) {
        //custom title, warning icon
        JOptionPane.showMessageDialog(panel,
            "Thank you for you tip. Please stay where you are. We will visit you shortly",
            "FBI warning",
            JOptionPane.WARNING_MESSAGE);
    }

    private void CallThePolicePerformed(ActionEvent evt) {
        //custom title, warning icon
        JOptionPane.showMessageDialog(panel,
            "As soon as we finish our donuts we will be there",
            "Inane error",
            JOptionPane.ERROR_MESSAGE);
    }    
    
   
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

        WSQ_FILE_NAME = filepath;
        byte[] jpgArray;
        jpgArray = Jnbis.wsq().decode(WSQ_FILE_NAME).toJpg().asByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(jpgArray);
        try {
            img = ImageIO.read(bais);
            Dimension newImgSize = ImageTools.getScaledDimension(img.getWidth(), img.getHeight(), xBoundery, yBoundery);
            int x = (int) newImgSize.getWidth();
            int y = (int) newImgSize.getHeight();
            img = ImageTools.resize(img, x, y);
            
            panel.importImage(new javax.swing.ImageIcon(img).getImage());            
            validate();
            repaint();
        
        } catch (IOException ex) {
            Logger.getLogger(DrawFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void ConvertWSQtoPNGPerformed(ActionEvent evt) {
        FileDialog f = new FileDialog(this, "Choose WSQ File ", FileDialog.LOAD);
        String directory = null;
        f.setDirectory(directory);
        f.show();
        directory = f.getDirectory();
        String filepath = directory+f.getFile();

        WSQ_FILE_NAME = filepath;

        String newFilePath = WSQ_FILE_NAME.substring(0, WSQ_FILE_NAME.length()-".wsq".length())+".png";
        
        File png = Jnbis.wsq()
                .decode(WSQ_FILE_NAME)
                .toPng()
                .asFile(newFilePath);             
    }    
    
    private void ConvertWSQtoJPGPerformed(ActionEvent evt) {
        FileDialog f = new FileDialog(this, "Choose WSQ File ", FileDialog.LOAD);
        String directory = null;
        f.setDirectory(directory);
        f.show();
        directory = f.getDirectory();
        String filepath = directory+f.getFile();

        WSQ_FILE_NAME = filepath;

        String newFilePath = WSQ_FILE_NAME.substring(0, WSQ_FILE_NAME.length()-".wsq".length())+".jpg   ";
        
        File jpg = Jnbis.wsq()
                .decode(WSQ_FILE_NAME)
                .toJpg()
                .asFile(newFilePath);             
    } 

    private void ConvertWSQtoGIFPerformed(ActionEvent evt) {
        FileDialog f = new FileDialog(this, "Choose WSQ File ", FileDialog.LOAD);
        String directory = null;
        f.setDirectory(directory);
        f.show();
        directory = f.getDirectory();
        String filepath = directory+f.getFile();

        WSQ_FILE_NAME = filepath;

        String newFilePath = WSQ_FILE_NAME.substring(0, WSQ_FILE_NAME.length()-".wsq".length())+".gif";
        
        File gif = Jnbis.wsq()
                .decode(WSQ_FILE_NAME)
                .toGif()
                .asFile(newFilePath);             
    }     
    
} // end class DrawFrame