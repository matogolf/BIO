package wsq.code;

import java.awt.BasicStroke;
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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
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
//    public int brightnessLevel = 0;     // uroven jasu
//    public int contrastLevel = 0;       // uroven kontrastu
    public boolean brightnessOn = false; 
    public boolean contrastOn = false;
    public boolean cropOn = false;
    
    private JButton undo; // button to undo last drawn shape
    private JButton redo; // button to redo an undo
    private JButton clear; // button to clear panel
    
    private BufferedImage img = null;   // obrazok ktory sa zobrazuje
    private BufferedImage upImg = null; // updateovany obrazok ktory sa zobrazuje pri live nahlaade
    private BufferedImage defaultImg = null; // pre clear button - povodny obrazok

    
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
    private JButton brightnessButtonOK;
    private javax.swing.JButton contrastButton;
    private javax.swing.JButton cropButton;
    private JButton cropButtonOK; 
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
    private javax.swing.JButton ovalButton;
    private javax.swing.JButton triangleButton;
    private javax.swing.JButton rotateButton;
    private javax.swing.JButton textButton;
    private javax.swing.JButton zoomMinusButton;
    private javax.swing.JButton zoomPlusButton;
    private javax.swing.JButton drawingButton;
    private javax.swing.JSlider brightnessSlider;
    private javax.swing.JLabel brightnessSliderText;
    //spinnery omg
    private javax.swing.JSpinner spinnerTop;
    private javax.swing.JSpinner spinnerBottom;
    private javax.swing.JSpinner spinnerLeft;
    private javax.swing.JSpinner spinnerRight;
    private javax.swing.SpinnerModel modelTop;
    private javax.swing.SpinnerModel modelBottom;
    private javax.swing.SpinnerModel modelLeft;
    private javax.swing.SpinnerModel modelRight;
    private javax.swing.JLabel lTop;
    private javax.swing.JLabel lBottom;
    private javax.swing.JLabel lLeft;
    private javax.swing.JLabel lRight;
    private javax.swing.JComponent centerTop;
    private javax.swing.JComponent centerBottom;
    private javax.swing.JComponent centerLeft;
    private javax.swing.JComponent centerRight;
    
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
        panel.setLayout(null);
        
        //create buttons
        undo = new JButton( " Undo" );
        redo = new JButton( " Redo" );
        clear = new JButton( " Clear" );
        
        
        importImg = new javax.swing.JLabel();
        brightnessButton = new JButton();
        brightnessButtonOK = new JButton("Apply");
        cropButtonOK = new  JButton("Apply");
        contrastButton = new JButton();
        ovalButton = new JButton();
        triangleButton = new JButton();
        rotateButton = new JButton();
        cropButton = new JButton();
        textButton = new JButton();
        zoomPlusButton = new JButton();
        zoomMinusButton = new JButton();
        drawingButton = new JButton();
        
        brightnessSliderText = new JLabel();
        brightnessSlider = new JSlider();
        
        
        modelTop = new SpinnerNumberModel(0, 0, 100, 1);
        modelBottom = new SpinnerNumberModel(0, 0, 100, 1);
        modelLeft = new SpinnerNumberModel(0, 0, 100, 1);
        modelRight = new SpinnerNumberModel(0, 0, 100, 1);
//        spinnerTop = addLabeledSpinner(widgetJPanel,"Kek",modelTop);
        spinnerTop = new JSpinner(modelTop);
        spinnerBottom = new JSpinner(modelBottom);
        spinnerLeft = new JSpinner(modelLeft);
        spinnerRight = new JSpinner(modelRight);
        lTop =      new JLabel("    Top:");
        lBottom =   new JLabel("  Bottom:");
        lLeft =     new JLabel("    Left:");
        lRight =    new JLabel("    Right:");
        centerTop = spinnerTop.getEditor();
        centerBottom = spinnerBottom.getEditor();
        centerLeft = spinnerLeft.getEditor();
        centerRight = spinnerRight.getEditor();
        JSpinner.DefaultEditor spinnerEditor1 = (JSpinner.DefaultEditor)centerTop;
        JSpinner.DefaultEditor spinnerEditor2 = (JSpinner.DefaultEditor)centerBottom;
        JSpinner.DefaultEditor spinnerEditor3 = (JSpinner.DefaultEditor)centerLeft;
        JSpinner.DefaultEditor spinnerEditor4 = (JSpinner.DefaultEditor)centerRight;
        spinnerEditor1.getTextField().setHorizontalAlignment(JTextField.CENTER);
        spinnerEditor2.getTextField().setHorizontalAlignment(JTextField.CENTER);
        spinnerEditor3.getTextField().setHorizontalAlignment(JTextField.CENTER);
        spinnerEditor4.getTextField().setHorizontalAlignment(JTextField.CENTER);
        

        undo.setBorderPainted(false);
        redo.setBorderPainted(false);
        clear.setBorderPainted(false);
        brightnessButton.setBorderPainted(false);

        contrastButton.setBorderPainted(false);
        ovalButton.setBorderPainted(false);
        triangleButton.setBorderPainted(false);
        rotateButton.setBorderPainted(false);
        cropButton.setBorderPainted(false);
        textButton.setBorderPainted(false);
        zoomPlusButton.setBorderPainted(false);
        zoomMinusButton.setBorderPainted(false);
        drawingButton.setBorderPainted(false);
                
        undo.setFocusable(false);
        redo.setFocusable(false);
        clear.setFocusable(false);
        brightnessButton.setFocusable(false);
        contrastButton.setFocusable(false);
        ovalButton.setFocusable(false);
        triangleButton.setFocusable(false);
        rotateButton.setFocusable(false);
        cropButton.setFocusable(false);
        textButton.setFocusable(false);
        zoomPlusButton.setFocusable(false);
        zoomMinusButton.setFocusable(false);
        drawingButton.setFocusable(false);
        
        
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
//      widgetJPanel.setLayout( new GridLayout( 12, 1 ) ); //sets padding between widgets in gridlayout
        widgetJPanel.setLayout(new BoxLayout(widgetJPanel, BoxLayout.PAGE_AXIS));
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
                panel.setTextBoxesInvisible();
                importWsqActionPerformed(evt);
            }
        });
        menu1.add(importWsq);
        
        JMenuItem importBitmap = new JMenuItem("Import Bitmap file...");
        importBitmap.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        importBitmap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                importBitmapActionPerformed(evt);
            }
        });
        menu1.add(importBitmap);
        
        JMenuItem saveAs = new JMenuItem("Save as...");
        saveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        saveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                saveFileAsPerformed(evt);                
            }
        });
        menu1.add(saveAs);
                
        
        JMenuItem quit = new JMenuItem("Quit");
        quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
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
        ImporttoFBI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        ImporttoFBI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                ImporttoFBIPerformed(evt);
            }
        });
        menu2.add(ImporttoFBI);
    
        JMenuItem CallThePolice = new JMenuItem("Call the police...");
        CallThePolice.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        CallThePolice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
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
                panel.setTextBoxesInvisible();
                ConvertWSQtoJPGPerformed(evt);
            }
        });
        menu3.add(ConvertWSQtoJPG); 
        
        JMenuItem ConvertWSQtoGIF = new JMenuItem("Convert WSQ to GIF...");
        ConvertWSQtoGIF.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        ConvertWSQtoGIF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                ConvertWSQtoGIFPerformed(evt);
            }
        });
        menu3.add(ConvertWSQtoGIF);
    
        JMenuItem ConvertWSQtoPNG = new JMenuItem("Convert WSQ to PNG...");
        ConvertWSQtoPNG.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        ConvertWSQtoPNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
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
        undo.setToolTipText("Undo: Reverse last action");
        widgetJPanel.add( redo );
        redo.setToolTipText("Redo: Reverse last Undo");
        widgetJPanel.add( clear );
        clear.setToolTipText("Clear: Reset all changes");
        widgetJPanel.add( brightnessButton );
        brightnessButton.setToolTipText("Brightness: Set brightness of image");
        widgetJPanel.add( contrastButton );
        contrastButton.setToolTipText("Contrast: Set contrast of image");
        widgetJPanel.add( triangleButton );
        triangleButton.setToolTipText("Triangle: Draw triangle for minutiae points");
        widgetJPanel.add( ovalButton );
        ovalButton.setToolTipText("Ellipse: Draw ellipse for minutiae points");
        widgetJPanel.add( drawingButton );
        drawingButton.setToolTipText("Pen: Draw lines for minutiae points or papillary lines");
        widgetJPanel.add( rotateButton );
        rotateButton.setToolTipText("Rotate: Rotate image 90 degress to the right");
        widgetJPanel.add( cropButton );
        cropButton.setToolTipText("Crop: Crop image");
        widgetJPanel.add( textButton );
        textButton.setToolTipText("Notes: Add notes to the image");
        widgetJPanel.add( zoomPlusButton );
        zoomPlusButton.setToolTipText("Zoom In: Adjust zoom level");
        widgetJPanel.add( zoomMinusButton );
        zoomMinusButton.setToolTipText("Zoom Out: Adjust zoom level");
        
        
        widgetJPanel.add ( brightnessSliderText );       
        widgetJPanel.add( brightnessSlider );
        widgetJPanel.add( brightnessButtonOK );
        brightnessSlider.setSize(1000000, 10);
        brightnessSlider.setPaintTrack(true); 
        brightnessSlider.setPaintTicks(true); 
        brightnessSlider.setPaintLabels(true);
        brightnessSlider.setMajorTickSpacing(50); 
        brightnessSlider.setMinorTickSpacing(5); 
        brightnessSlider.setMinimum(-100);
        brightnessSlider.setMaximum(100);
        brightnessSlider.setValue(0);
        brightnessSlider.setOrientation(JSlider.VERTICAL);
        brightnessSlider.setAlignmentX(LEFT_ALIGNMENT);

        brightnessSliderText.setText("Value: " + brightnessSlider.getValue());
        
        brightnessSlider.show(false);
        brightnessSliderText.show(false);
        brightnessButtonOK.show(false);
        
        brightnessSlider.addChangeListener(e -> brightnessSliderChanged() );
        
        Dimension d = new Dimension(75, 30);
        spinnerTop.setPreferredSize(d);spinnerTop.setMaximumSize(d);
        spinnerBottom.setPreferredSize(d);spinnerBottom.setMaximumSize(d);
        spinnerLeft.setPreferredSize(d);spinnerLeft.setMaximumSize(d);
        spinnerRight.setPreferredSize(d);spinnerRight.setMaximumSize(d);

        spinnerTop.setAlignmentX(TOP_ALIGNMENT);
        spinnerBottom.setAlignmentX(TOP_ALIGNMENT);
        spinnerLeft.setAlignmentX(TOP_ALIGNMENT);
        spinnerRight.setAlignmentX(TOP_ALIGNMENT);
        widgetJPanel.add(lTop);
        widgetJPanel.add(spinnerTop);
        widgetJPanel.add(lBottom);
        widgetJPanel.add(spinnerBottom);
        widgetJPanel.add(lLeft);
        widgetJPanel.add(spinnerLeft);
        widgetJPanel.add(lRight);
        widgetJPanel.add(spinnerRight);
        spinnerTop.show(false);spinnerBottom.show(false);
        spinnerLeft.show(false);spinnerRight.show(false);
        lTop.show(false);lBottom.show(false);
        lLeft.show(false);lRight.show(false);
        widgetJPanel.add(cropButtonOK);
        cropButtonOK.show(false);
        

        

//        slider.show(false);

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
                panel.setTextBoxesInvisible();
                brightnessButtonActionPerformed(evt);
            }
        });
        brightnessButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                brightnessButtonOKActionPerformed(evt);
            }
        });
        cropButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                croppButtonOKActionPerformed(evt);
            }
        });
        
        contrastButton.setIcon(new ImageIcon(getClass().getResource("/resources/contrast.png")));
        contrastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                contrastButtonActionPerformed(evt);
            }
        });
        
        ovalButton.setIcon(new ImageIcon(getClass().getResource("/resources/markantEllipse.png")));
        ovalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                ovalButtonActionPerformed(evt);
            }
        });
        
        triangleButton.setIcon(new ImageIcon(getClass().getResource("/resources/markantTriangle.png")));
        triangleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                triangleButtonActionPerformed(evt);
            }
        });
                
        drawingButton.setIcon(new ImageIcon(getClass().getResource("/resources/markantLine.png")));
        drawingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                drawingButtonActionPerformed(evt);
            }
        });
        
        rotateButton.setIcon(new ImageIcon(getClass().getResource("/resources/rotate.PNG")));
        rotateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                rotateButtonActionPerformed(evt);
            }
        });
        
        cropButton.setIcon(new ImageIcon(getClass().getResource("/resources/crop.png")));
        cropButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                cropButtonActionPerformed(evt);
            }
        });
        
        textButton.setIcon(new ImageIcon(getClass().getResource("/resources/text.png")));
        textButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                textButtonActionPerformed(evt);
            }
        });
        
        zoomPlusButton.setIcon(new ImageIcon(getClass().getResource("/resources/zoomPlus.PNG")));
        zoomPlusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                zoomPlusButtonActionPerformed(evt);
            }
        });
        
        zoomMinusButton.setIcon(new ImageIcon(getClass().getResource("/resources/zoomMinus.PNG")));
        zoomMinusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                panel.setTextBoxesInvisible();
                zoomMinusButtonActionPerformed(evt);
            }
        });
        
        
       brightnessSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent event) {
            int value = brightnessSlider.getValue();
            brightnessSliderText.setText("Value: " + value);
        }
       });
       
       
        spinnerTop.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                int x = img.getWidth();
                int y = img.getHeight();
                int top = (y * ((Integer)spinnerTop.getValue())/100);
                int bottom = (y * ((Integer)spinnerBottom.getValue())/100);
                int left = (x * ((Integer)spinnerLeft.getValue())/100);
                int right = (x * ((Integer)spinnerRight.getValue())/100);
                upImg = deepCopy(img);
                ImageTools.cropView(upImg, top, bottom, left, right);
                panel.importImage(new ImageIcon(upImg).getImage());
                validate();
                repaint();
            }
        });
        spinnerBottom.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                int x = img.getWidth();
                int y = img.getHeight();
                int top = (y * ((Integer)spinnerTop.getValue())/100);
                int bottom = (y * ((Integer)spinnerBottom.getValue())/100);
                int left = (x * ((Integer)spinnerLeft.getValue())/100);
                int right = (x * ((Integer)spinnerRight.getValue())/100);
                upImg = deepCopy(img);
                ImageTools.cropView(upImg, top, bottom, left, right);
                panel.importImage(new ImageIcon(upImg).getImage());
                validate();
                repaint();
            }
        });
        spinnerLeft.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                int x = img.getWidth();
                int y = img.getHeight();
                int top = (y * ((Integer)spinnerTop.getValue())/100);
                int bottom = (y * ((Integer)spinnerBottom.getValue())/100);
                int left = (x * ((Integer)spinnerLeft.getValue())/100);
                int right = (x * ((Integer)spinnerRight.getValue())/100);
                upImg = deepCopy(img);
                ImageTools.cropView(upImg, top, bottom, left, right);
                panel.importImage(new ImageIcon(upImg).getImage());
                validate();
                repaint();
            }
        });
        spinnerRight.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                int x = img.getWidth();
                int y = img.getHeight();
                int top = (y * ((Integer)spinnerTop.getValue())/100);
                int bottom = (y * ((Integer)spinnerBottom.getValue())/100);
                int left = (x * ((Integer)spinnerLeft.getValue())/100);
                int right = (x * ((Integer)spinnerRight.getValue())/100);
                upImg = deepCopy(img);
                ImageTools.cropView(upImg, top, bottom, left, right);
                panel.importImage(new ImageIcon(upImg).getImage());
                validate();
                repaint();
            }
        });
       
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( 700, 880 );
        setMinimumSize(new Dimension(700, 880));
        setVisible( true );
        
    this.addWindowFocusListener(new WindowAdapter() {
        public void windowGainedFocus(WindowEvent e) {
        panel.requestFocusInWindow();
        }
    });
        
    } // end DrawFrame constructor
    
    private void brightnessSliderChanged() {
        if (img != null) {
            if (brightnessOn) {
              upImg = ImageTools.changeBrightness(img, brightnessSlider.getValue());  
            }
            if (contrastOn) {
              upImg = ImageTools.changeContrast(img, brightnessSlider.getValue());  
            }
        }

        
        if (upImg != null)
            panel.importImage(new ImageIcon(upImg).getImage());            
        validate();
        repaint();
        
        
    }
    
    private void brightnessButtonActionPerformed(java.awt.event.ActionEvent evt) { 


           if (!brightnessSlider.isShowing() || contrastOn || cropOn) {
            turnOffCropLabels();
            brightnessSlider.setMinimum(-100);
            brightnessSlider.setMaximum(100);
            brightnessSlider.setValue(0);
            brightnessSlider.setVisible(true);
            brightnessSliderText.setVisible(true);
            brightnessButtonOK.setVisible(true);              
            brightnessOn = true;
            contrastOn = false;
            cropOn = false;
          }
          else { 
            brightnessSlider.setVisible(false);
            brightnessSliderText.setVisible(false);
            brightnessButtonOK.setVisible(false);  
            brightnessSlider.setValue(0);
            brightnessOn = false;
            contrastOn = false;
            //panel.importImage(new ImageIcon(img).getImage());            
            validate();
            repaint();
            
          }
    }     
    private void brightnessButtonOKActionPerformed(java.awt.event.ActionEvent evt) { 
        brightnessSlider.setVisible(false);
        brightnessSliderText.setVisible(false);
        brightnessButtonOK.setVisible(false);     
        brightnessOn = false;
        contrastOn = false;
        brightnessSlider.setValue(0);
        img = upImg;
    }
    
    
    private void contrastButtonActionPerformed(java.awt.event.ActionEvent evt) {   
        
           if (!brightnessSlider.isShowing() || brightnessOn || cropOn) {
            turnOffCropLabels();
            brightnessSlider.setMinimum(-100);
            brightnessSlider.setMaximum(100);
            brightnessSlider.setValue(0);
            brightnessSlider.setVisible(true);
            brightnessSliderText.setVisible(true);
            brightnessButtonOK.setVisible(true);              
            contrastOn = true;
            brightnessOn = false;
            cropOn = false;
          }
          else { 
            brightnessSlider.setVisible(false);
            brightnessSliderText.setVisible(false);
            brightnessButtonOK.setVisible(false);  
            brightnessSlider.setValue(0);
            contrastOn = false;
            brightnessOn = false;
            validate();
            repaint();
            
          }
    }  
    
    private void ovalButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        panel.setCurrentShapeType(2);
    }
    
    private void triangleButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        panel.setCurrentShapeType(3);         
    }
    
    private void rotateButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
//        img = ImageTools.rotate(img);
        if (img != null) {
            img = ImageTools.rotate(img);
            
            panel.importImage(new ImageIcon(img).getImage());  
            
            panel.redrawTextPoints();
            validate();
            repaint();
        }
    }  
    
    private void cropButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (cropOn) {
            cropOn = false;
            turnOffCropLabels();
        }
        else if (!cropOn || brightnessOn || contrastOn) {
            brightnessOn = false;
            brightnessSlider.setVisible(false);
            brightnessSliderText.setVisible(false);
            brightnessButtonOK.setVisible(false);  
            contrastOn = false;
            turnOnCropLabels();
            cropOn = true;
        }
    }
    private void croppButtonOKActionPerformed(ActionEvent evt) {
        int x = img.getWidth();
        int y = img.getHeight();
        int top = (y * ((Integer)spinnerTop.getValue())/100);
        int bottom = (y * ((Integer)spinnerBottom.getValue())/100);
        int left = (x * ((Integer)spinnerLeft.getValue())/100);
        int right = (x * ((Integer)spinnerRight.getValue())/100);
        
        if ((x-left-right < 0) && (y-top-bottom < 0))
            img = img.getSubimage(left+(x-left-right), top+(y-top-bottom), abs(x-left-right), abs(y-top-bottom));
        else if (x-left-right < 0)
            img = img.getSubimage(left+(x-left-right), top, abs(x-left-right), y-top-bottom);
        else if (y-top-bottom < 0)
            img = img.getSubimage(left, top+(y-top-bottom), x-left-right, abs(y-top-bottom));
        else
           img = img.getSubimage(left, top, x-left-right, y-top-bottom); 
        panel.importImage(new ImageIcon(img).getImage());
        validate();
        repaint();
        turnOffCropLabels();
        cropOn = false;
        
    }
    
    private void textButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        panel.setCurrentShapeType(4);
    }
    
    private void zoomPlusButtonActionPerformed(java.awt.event.ActionEvent evt) { 
        if (img != null) {
            img = ImageTools.zoomIn(img);
            
            panel.importImage(new ImageIcon(img).getImage());  
            
            panel.redrawTextPoints();
            validate();
            repaint();
        }            
    }
    
    private void zoomMinusButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        if (img != null) {
            img = ImageTools.zoomOut(img);
            
            panel.importImage(new ImageIcon(img).getImage());  
            
            panel.redrawTextPoints();
            validate();
            repaint();
        } 
    }
    
    private void drawingButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        panel.setCurrentShapeType(1);
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
            defaultImg = img;
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
    
    private void saveFileAsPerformed(ActionEvent evt) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (jfc.getSelectedFile().isDirectory()) {
                        System.out.println("You selected the directory: " + jfc.getSelectedFile());
                }
        }
        
        try {
//            Image img_new = panel.getImage();
//            System.out.println(img_new);
            System.out.println(img);            
            File outputfile = new File(jfc.getSelectedFile()+"/saved.jpg");
            ImageIO.write((RenderedImage) img, "jpg", outputfile);
        } catch (IOException e) {
            // handle exception
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
            else if (event.getSource() == redo){
                panel.redoLastShape();
            }
            // clear button - reset img
            else if (event.getSource() == clear){
                panel.clearDrawing();
                Dimension newImgSize = ImageTools.getScaledDimension(defaultImg.getWidth(), defaultImg.getHeight(), xBoundery, yBoundery);
                int x = (int) newImgSize.getWidth();
                int y = (int) newImgSize.getHeight();
                
                defaultImg = ImageTools.resize(defaultImg, x, y);
                panel.importImage(new javax.swing.ImageIcon(defaultImg).getImage());            
                validate();
                repaint();
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
            defaultImg = img;
            Dimension newImgSize = ImageTools.getScaledDimension(img.getWidth(), img.getHeight(), xBoundery, yBoundery);
            int x = (int) newImgSize.getWidth();
            int y = (int) newImgSize.getHeight();
            img = ImageTools.resize(img, x, y);
            
            panel.setImageSize(x, y);
            
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

        String newFilePath = WSQ_FILE_NAME.substring(0, WSQ_FILE_NAME.length()-".wsq".length())+".jpg";
        
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
    
    public void addLabel(JLabel label) {
        panel.add(label);
    }
    
    static protected JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
        JLabel l = new JLabel(label);
        c.add(l);

        JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);

        return spinner;
    }
    public void turnOnCropLabels() {
        spinnerTop.setValue(new Integer(0));
        spinnerBottom.setValue(new Integer(0));
        spinnerLeft.setValue(new Integer(0));
        spinnerRight.setValue(new Integer(0));
        spinnerTop.setVisible(true);spinnerBottom.setVisible(true);
        spinnerLeft.setVisible(true);spinnerRight.setVisible(true);
        lTop.setVisible(true);lBottom.setVisible(true);
        lLeft.setVisible(true);lRight.setVisible(true);
        cropButtonOK.setVisible(true);
    }
    public void turnOffCropLabels() {
        
        spinnerTop.setVisible(false);spinnerBottom.setVisible(false);
        spinnerLeft.setVisible(false);spinnerRight.setVisible(false);
        lTop.setVisible(false);lBottom.setVisible(false);
        lLeft.setVisible(false);lRight.setVisible(false);
        cropButtonOK.setVisible(false);
        panel.importImage(new ImageIcon(img).getImage());
        validate();
        repaint();
    }
    
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
}
    
} // end class DrawFrame