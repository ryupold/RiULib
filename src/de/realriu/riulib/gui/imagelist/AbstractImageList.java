package de.realriu.riulib.gui.imagelist;

import de.realriu.riulib.helpers.ScaleImage;
import de.realriu.riulib.helpers.ScaleImage.Rectangle;
import java.awt.Color;
import java.awt.Font;


import java.awt.Graphics;

import java.awt.Image;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JPanel;
import javax.swing.border.Border;


/**
 * Abstract ImageList class<br>
 * Contains the rendering and eventhandling functionalitys.
 * @author riu
 * @version 2.0
 */
public abstract class AbstractImageList<T> extends JPanel implements MouseListener,
        MouseMotionListener, MouseWheelListener, ComponentListener {

    /**
     * Current position from which the images are drawn.<br/>
     * Default: 0
     */
    protected int position = 0;
    /**
     * Index of image where the mouse is over<br/>
     * -1 if the mouse isn't over a image<br/>
     * Default: -1
     */
    protected int mouseOverPicNumber = -1;
    /**
     * Index of selected Image<br/>
     * -1 if no image is selected<br/>
     * Default: -1
     */
    protected int selectedImageIndex = -1;
    /**
     * List of ImageListListeners  &lt;T&gt;
     */
    protected List<ImageListListener<T>> ills = new ArrayList<ImageListListener<T>>();
    /**
     * List of ImageListPropertyListeners
     */
    protected List<ImageListPropertyListener> ilpls = new ArrayList<ImageListPropertyListener>();
    /**
     * List with the images that should be drawn.
     */
    protected final List<Image> images = new ArrayList<Image>();
    /**
     * List with the image titles
     */
    protected final List<String> titles = new ArrayList<String>();
    /**
     * Current image width<br/>
     * Default: 0
     */
    protected int fotoWidth;
    /**
     * Current Image height<br/>
     * Default: 0
     */
    protected int fotoHeigth;
    /**
     * Space in Pixels between the images<br/>
     * Default: 10
     */
    protected int spaceBetweenPics = 10;
    /**
     * Selection border color<br/>
     * Default: Color.BLUE
     * @see Color
     * @see Color#BLUE
     */
    protected Color selectColor = Color.BLUE;
    /**
     * Hover border color<br/>
     * Default: Color.ORANGE
     */
    protected Color hoverColor = Color.ORANGE;
    /**
     * Text color<br/>
     * Default: null
     */
    protected Color textColor = null;
    /**
     * Title text color
     * Default: Color.GREEN
     */
    protected Color titleColor = Color.GREEN;
    /**
     * Scroll alignment<br/>
     * Default: Alignment.Horizontal
     */
    protected Alignment align = Alignment.Horizontal;
    /**
     * If true, the align variable will change on resize of the ImageList<br/>
     * Alignment.Horizontal if width>height<br/>
     * Alignment.Vertical if height>width<br/>
     * Default: false
     */
    protected boolean autoAlignment = false;
    /**
     * Text which is showing under the images, when the ImageList has Horizontal alignment.<br/>
     * Default: null
     */
    protected String text = null;
    /**
     * Index of the currently centering image<br/>
     * Default: -1
     */
    protected int centeringImage = -1;
    /**
     * Specifies how much can be scrolled up per mouse wheel rotations.<br/>
     * if mouseWheelSensivity is less or equal to 0, the mouse wheel scrolling is disabled.<br/>
     * Default: 6
     */
    protected int mouseWheelSensivity = 6;
    /**
     * Picking the ImageList with the mouse to scroll.<br/>
     * Default: true
     */
    protected boolean draggingAllowed = true;
    /**
     * Allowing to select images.
     */
    protected boolean selectingAllowed = true;
    /**
     * if false, images can not be hovered.
     */
    protected boolean hoveringAllowed = true;
    /**
     * This variable indicates whether the images are pushed out of the ImageList bounds they will be slided back in the visible area.<br/>
     * Default: true
     */
    protected boolean slideAfterDrag = true;
    /**
     * Spacing of the images from the left, right, top and bottom edge to edge components (margin).<br/>
     * margin = {left, right, top, bottom}<br/>
     * Default: {0,0,0,0}
     * 
     */
    protected final int[] margin = new int[4];

    /**
     * Distance of the images to its border (padding).<br/>
     * padding = {left, right, top, bottom}<br/>
     * Default: {0,0,0,0}
     */
    protected final int[] padding = new int[4];

    /**
     * Vertical title position<br/>
     * Default: VertikalTitlePosition.AtBottom
     */
    protected VertikalTitlePosition titleYPosition = VertikalTitlePosition.AtBottom;

    /**
     * Font of the titles<br/>
     * Default: new Font(null, Font.PLAIN, 12)
     */
    protected Font titleFont = new Font(null, Font.PLAIN, 12);

    /**
     * Font of the Text at the bottom<br/>
     * Default: new Font(null, Font.PLAIN, 12)
     */
    protected Font textFont = new Font(null, Font.PLAIN, 12);

    /**
     * Force image borders to be squares even if the margin changes.<br/>
     * Default: true
     */
    protected boolean forceSqaueredImages = false;

    private Integer dragStart = 0;
    private int oldpos = 0;
    private int wheel;
    private Thread t = null;
    private boolean wannaDrag = false;
    private boolean isMoving = false;
    private int currentWheelMovingIsAllowed = mouseWheelSensivity;

    /**
     * Directions in which the ImageList is scrollable.
     * @see Alignment#Horizontal
     * @see Alignment#Vertical
     */
    public static enum Alignment {

        /**
         * Vertical scrollbar
         */
        Vertical,
        /**
         * Horizontal scrollbar
         */
        Horizontal;
    }

    public AbstractImageList(Alignment a, boolean autoAlignment) {
        align = a == null ? Alignment.Horizontal : a;
        this.autoAlignment = autoAlignment;


        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addComponentListener(this);
    }
    
    /**
     * Set Force image borders to be squares even if the margin changes.<br/>
     * Default: true 
     * @param squared
     */
    public void setForceSquaredImages(boolean squared){
        forceSqaueredImages = squared;

        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.forceSquaredImagesChanged(squared);
        }
    }

    /**
     * Returns whether the images appear square or rectangular when you squeezed them together by margin changes.<br/>
     * Default: true
     * @return true = Sqaured<br/>false = Rectangular
     */
    public boolean isForcedSquareImages(){
        return forceSqaueredImages;
    }

    /**
     * Sets the title font
     * @param font title font<br/>null => titleFont = new Font(null, Font.PLAIN, 12)
     */
    public synchronized void setTitleFont(Font font){
        Font old = titleFont;

        if(font!=null)
            titleFont = font;
        else
            titleFont = new Font(null, Font.PLAIN, 12);

        repaint();
        
        
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.titleFontChanged(old, titleFont);
        }
    }

    /**
     * Gets the Title font.
     * @return title font
     */
    public Font getTitleFont(){
        return titleFont;
    }

    /**
     * Sets the font of the text at bottom
     * @param font Textschriftart<br/>null lässt die Schriftart auf new Font(null, Font.PLAIN, 12) gesetzt.
     */
    public synchronized void setTextFont(Font font){
        Font old = textFont;
        
        if(font!=null)
            textFont = font;
        else
            textFont = new Font(null, Font.PLAIN, 12);

        repaint();
        
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.textFontChanged(old, textFont);
        }
    }

    /**
     * Get font of the text under the images
     * @return text font
     */
    public Font getTextFont(){
        return textFont;
    }

    /**
     * Sets the distance of the images from the left, right, top and bottom of the screen to the component edge (margin).<br/>
     * If the alignment is <u>horizontal</u> the <b>left</b> and <b>right</b> distances are ignored.<br/>
     * If the alignment is <u>vertical</u> the <b>up</b> and <b>down</b> distances are ignored.<br/>
     * Default: {0,0,0,0}
     * @param left Linker Abstand
     * @param right Rechter Abstand
     * @param top Oberer Abstand
     * @param bottom Unterer Abstand
     */
    public void setMargin(int left, int right, int top, int bottom) {
        int[] old = {margin[0], margin[1], margin[2], margin[3]};
        margin[0] = left;
        margin[1] = right;
        margin[2] = top;
        margin[3] = bottom;

        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.marginChanged(old, margin);
        }
    }

    /**
     * Returns the distances of the images from the left, right, top and bottom edge to edge components (margin).<br/>
     * Indexbelegung:<br/>
     * 0 => left<br/>
     * 1 => right<br/>
     * 2 => top<br/>
     * 3 => bottom
     * @return Randabstände
     */
    public int[] getMargin() {
        return margin;
    }

    /**
     * Sets the distance of the images to the left, right, top and bottom of the image border (padding).<br/>
     * Default: {0,0,0,0}
     * @param left left distance
     * @param right right distance
     * @param top top distance
     * @param bottom bottom distance
     */
    public void setPadding(int left, int right, int top, int bottom) {
        int[] old = {padding[0], padding[1], padding[2], padding[3]};
        padding[0] = left;
        padding[1] = right;
        padding[2] = top;
        padding[3] = bottom;

        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.paddingChanged(old, margin);
        }
    }

    /**
     * Returns the distances of the images to the left, right, top and bottom of the screen (padding).<br/>
     * Index Assignment:<br/>
     * 0 => left<br/>
     * 1 => right<br/>
     * 2 => top<br/>
     * 3 => bottom
     * @return Randabstände
     */
    public int[] getPadding() {
        return padding;
    }

    /**
     * Set the vertical Position of the Image titles.
     * @param position vertical position - position==null has no effect
     */
    public void setVertikalTitlePosition(VertikalTitlePosition position) {
        if (position != null) {
            VertikalTitlePosition old = titleYPosition;
            titleYPosition = position;
            for (ImageListPropertyListener ilpl : ilpls) {
                ilpl.vertikalTitlePositionChanged(old, position);
            }
        }
    }

    /**
     * Returns the vertical Position if the image titles
     * @return vertical position
     */
    public VertikalTitlePosition getVertikalTitlePosition() {
        return titleYPosition;
    }

    /**
     * Sets the space between the images in pixel.<br/>
     * Default: 10
     * @param space
     */
    public void setSpaceBetweenPictures(int space) {
        int old = spaceBetweenPics;
        spaceBetweenPics = space;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.spaceBetweenPicturesChanged(old, space);
        }
    }

    /**
     * Returns the space between the images in pixel<br/>
     * Default: 10
     * @return Bilderabstand
     */
    public int getSpaceBetweenPics() {
        return spaceBetweenPics;
    }

    /**
     * If true the Imagelist automatically decides the scrolldirection<br/>
     * if(width&lt;height) then the imageList will be vertical else vertical<br/>
     * Default: false
     * @param autoAlignment
     */
    public void setAutoAlignment(boolean autoAlignment) {
        this.autoAlignment = autoAlignment;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.autoAlignmentChanged(autoAlignment);
        }
    }

    /**
     * Specifies whether to automatically adjust scroll alignment based on the height and width ratio of the alignment.<br/>
     * Default: false
     * @return autoDecide
     */
    public boolean isAutoAlignment() {
        return autoAlignment;
    }

    /**
     * Sets the maximum intensity of the scrolling when the mouse wheel is turned violent.<br/>
     * <b>Values less than or equal to 0 cause a prevention of the scrolling.</b><br/>
     * (Ich hoffe man versteht was ich mein xD, je stärker man das Rad dreht desto weiter scrollt man...)
     * Default: 6
     * @param mouseWheelSensivity steps
     */
    public void setMouseWheelSensivity(int mouseWheelSensivity) {
        int old = this.mouseWheelSensivity;
        this.mouseWheelSensivity = mouseWheelSensivity;
        currentWheelMovingIsAllowed = mouseWheelSensivity;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.mouseWheelSensivityChanged(old, mouseWheelSensivity);
        }
    }

    /**
     * Returns the maximum intensity of the scrolling when the mouse wheel is turned violent.<br/>
     * Default: 6
     * @return Sensitivity step
     */
    public int getMouseWheelSensivity() {
        return mouseWheelSensivity;
    }

    /**
     * Changes the behavior to be pushed back if the images are pushed completely out of the picture.<br/>
     * Default: true
     * @param slideAfterDrag
     */
    public void setSlideAfterDrag(boolean slideAfterDrag) {
        this.slideAfterDrag = slideAfterDrag;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.slideAfterDragChanged(slideAfterDrag);
        }
    }

    /**
     * Returns the behavior to be pushed back if the images are pushed completely out of the picture.<br/>
     * Default: true
     * @return Zurücksliden
     */
    public boolean doesSliedeAfterDrag() {
        return slideAfterDrag;
    }

    /**
     * Set dragging to scroll enabled<br/>
     * Default: true
     * @param allowed
     */
    public void setDraggingAllowed(boolean allowed) {
        draggingAllowed = allowed;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.dragAllowingChanged(allowed);
        }
    }

    /**
     * Returns if drag to scroll is enabled<br/>
     * Default: true
     * @return draggingAllowed
     */
    public boolean isDraggingAllowed() {
        return draggingAllowed;
    }

    /**
     * Make images selectable<br/>
     * Default: true
     * @param selectingAllowed
     */
    public void setSelectingAllowed(boolean selectingAllowed) {
        this.selectingAllowed = selectingAllowed;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.selectionAllowingChanged(selectingAllowed);
        }
    }

    /**
     * Returns if images are selectable<br/>
     * Default: true
     * @return
     */
    public boolean isSelectingAllowed() {
        return selectingAllowed;
    }

    /**
     * Sets whether images are highlighted when you move your mouse over it.<br/>
     * <b>Note: If the images can't be highlighted, they also can't be selected!<br/>
     * To guard against this, you set with 'setHoverColor' the color to 'null', then the highlight rectangle no longer visible but you can still select.</b><br/>
     * Default: true
     * @param hoveringAllowed
     * @see AbstractImageList#setHoverColor(java.awt.Color) 
     */
    public void setHoveringAllowed(boolean hoveringAllowed) {
        this.hoveringAllowed = hoveringAllowed;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.hoveringAllowingChanged(hoveringAllowed);
        }
    }

    /**
     * Returns if the images get highlightet when you mouseover them.<br/>
     * Default: true
     * @return
     */
    public boolean isHoveringAllowed() {
        return hoveringAllowed;
    }

    /**
     * Sets the Scrollalignment
     * @see Alignment
     * @see Alignment#Horizontal
     * @see Alignment#Vertical
     * @param a alignment
     */
    public void setScrollAlignment(Alignment a) {
        Alignment old = align;
        align = a;
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.alignmentChanged(old, align);
        }
    }

    /**
     * Returns the direction in which you can scroll the imagelist<br/>
     * Default: Horizontal
     * @return scroll direction
     */
    public Alignment getScrollAlignment() {
        return align;
    }

    /**
     * Gets the index of the selcted image<br/>
     * @return selected image index<br/>-1 if no image is selected.
     */
    public int getSelectedImageIndex() {
        return selectedImageIndex;
    }

    /**
     * Select an image
     * @param index if index is not valid no image will be selected.
     */
    public void selectImage(int index) {
        if (index >= 0 && index < images.size()) {
            selectedImageIndex = index;
        } else {
            selectedImageIndex = -1;
        }

        fireImageSelected(selectedImageIndex);
        repaint();
    }

    /**
     * Abstract method to add an image to the list.<br>
     * @param img Representation of an image
     * @param title image title
     */
    public abstract void addImage(T img, String title);

    /**
     * Abstract method to add an image to the list.
     * @param img Representation of an image
     */
    public void addImage(T img) {
        addImage(img, "");
    }

    /**
     * Abstract method to remove an image from the list.
     * @param pos Index
     * @return Representation of an image
     */
    public abstract T removeImage(int pos);

    /**
     * Sets a new image to the specified location and returns the previous one, if exist.
     * @param pos Position of the image to replace 0&lt;=pos&lt;count()
     * @param newImage new image
     * @return old image or null
     */
    public abstract T replaceImage(int pos, T newImage);

    /**
     * Returns the number of images
     * @return image count
     */
    public int count() {
        return images.size();
    }

    /**
     * Sets the color of the border that is drawn around a selected image.<br/>
     * <br/>Default value: Color.BLUE
     * @param color border color - if <b>null</b> no border is drawn
     */
    public void setSelectionColor(Color color) {
        Color old = selectColor;
        selectColor = color;
        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.selectionColorChanged(old, selectColor);
        }
    }

    /**
     * Returns the color of the selection border<br/>
     * Default: Color.BLUE
     * @return color
     */
    public Color getSelectionColor() {
        return selectColor;
    }

    /**
     * Sets the hover border color.
     * <br>Default value: Color.ORANGE
     * @param color border color - if <b>null</b> no border is drawn
     */
    public void setHoverColor(Color color) {
        Color old = hoverColor;
        hoverColor = color;
        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.hoverColorChanged(old, hoverColor);
        }
    }

    /**
     * Returns the hover border color<br/>
     * Default: Color.ORANGE
     * @return color
     */
    public Color getHoverColor() {
        return hoverColor;
    }

    /**
     * Sets the text to appear below.<br/>
     * <u>Currently, the text appears only in a horizontal orientation.</u><br/>
     * Default: null
     * @param text if <b>null</b> no text is drawn
     */
    public void setText(String text) {
        String old = this.text;
        this.text = text;
        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.textChanged(old, text);
        }
    }

    /**
     * Returns the text, appear below<br/>
     * <u>Currently, the text appears only in a horizontal orientation.</u><br/>
     * Default: null
     * @return Text or null if no text is set
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the color of the text appear below
     * <br>Defaultwert: null
     * @param color text color - if <b>null</b> no text is showing
     */
    public void setTextColor(Color color) {
        Color old = textColor;
        textColor = color;
        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.textColorChanged(old, textColor);
        }
    }

    /**
     * Returns the color of the text that is shown at the bottom of the component<br/>
     * Default: null
     * @return text color
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Sets the title color
     * <br>Default value: Color.GREEN
     * @param color title color - if <b>null</b> no titles are drawn.
     */
    public void setTitleColor(Color color) {
        Color old = titleColor;
        titleColor = color;
        repaint();
        for (ImageListPropertyListener ilpl : ilpls) {
            ilpl.titleColorChanged(old, titleColor);
        }
    }

    /**
     * Returns the color of the image titles<br/>
     * Default: Color.GREEN
     * @return Titlefarbe
     */
    public Color getTitleColor() {
        return titleColor;
    }

    /**
     * Abstract method to get an image representation on a specified index.
     * @param i index
     * @return image representation
     */
    public abstract T getImage(int i);

    /**
     * Returns the title of the image with the specified index.
     * @param i image index
     * @return Titel
     */
    public String getImageTitle(int i) {
        if (i < 0 || i >= titles.size()) {
            return null;
        }
        return titles.get(i);
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);

        Alignment align = this.align == Alignment.Horizontal || this.align == null ? Alignment.Horizontal : Alignment.Vertical;
        
        if (textColor != null && text != null && align == Alignment.Horizontal) {
            g.setFont(textFont);
            g.setColor(textColor);
            g.drawString(text, this.getWidth() / 2 - g.getFontMetrics().stringWidth(text) / 2,
                    this.getHeight() - g.getFont().getSize() / 2);
        }

        if (align == Alignment.Horizontal) {
            fotoHeigth = this.getVisibleRect().height - 3;

            //margin
            fotoHeigth -= margin[2] + margin[3];
            fotoWidth = fotoHeigth;
            fotoWidth += (forceSqaueredImages ? 0 : (margin[2] + margin[3]));
            
        } else { // Vertikal
            fotoWidth = this.getVisibleRect().width - 3;

            //margin
            fotoWidth -= margin[0] + margin[1];
            fotoHeigth = fotoWidth;
            fotoHeigth += (forceSqaueredImages ? 0 : (margin[0] + margin[1]));
        }


        for (int i = 0; i < images.size(); i++) {

            if(align==Alignment.Horizontal && (getImageXPosition(i)+fotoWidth < 0 || getImageXPosition(i)>getWidth())){
                continue;
            }else if(align==Alignment.Vertical && (getImageYPosition(i)+fotoHeigth < 0 || getImageYPosition(i)>getHeight())){
                continue;
            }

            Rectangle prefered = new Rectangle(getImageXPosition(i), getImageYPosition(i), fotoWidth, fotoHeigth);
            Rectangle paddingRect = new Rectangle(prefered.x+padding[0], prefered.y+padding[2], prefered.width-(padding[0]+padding[1]), prefered.heigth-(padding[2]+padding[3]));
            if(!forceSqaueredImages){
                paddingRect = ScaleImage.fitToRect(paddingRect, (BufferedImage) images.get(i));
            }
            Rectangle imageRect = ScaleImage.fitToRect(prefered, (BufferedImage) images.get(i));

            if (i != swapPositions[0][0] && i != swapPositions[1][0]) {
                g.drawImage(images.get(i), paddingRect.x, paddingRect.y, paddingRect.width, paddingRect.heigth, this);
            } else {
                g.setColor(Color.WHITE);
                g.drawRect(getImageXPosition(i), getImageYPosition(i), fotoWidth, fotoHeigth);
            }


            if (selectedImageIndex == i && selectColor != null) {
                g.setColor(selectColor);
                g.drawRect(prefered.x, prefered.y, prefered.width, prefered.heigth);

            } else if (mouseOverPicNumber == i && hoverColor != null) {
                g.setColor(hoverColor);
                g.drawRect(prefered.x, prefered.y, prefered.width, prefered.heigth);
            }//TODO border



            if (titleColor != null) {
                g.setFont(titleFont);
                g.setColor(titleColor);
                String title = titles.get(i);
                int textintend = fotoWidth / 2 - (g.getFontMetrics().stringWidth(title)) / 2;
                while (textintend < 0 && title.length() > 4) {
                    title = title.substring(0, title.length() - 4) + "...";
                    textintend = fotoWidth / 2 - (g.getFontMetrics().stringWidth(title)) / 2;
                }

                int titleYPos;
                switch (titleYPosition) {
                    case AtBottom:
                        titleYPos = fotoHeigth - g.getFont().getSize();
                        break;
                    case Center:
                        titleYPos = fotoHeigth / 2 + g.getFont().getSize() / 2 - g.getFont().getSize();
                        break;
                    case UnderBottom:
                        titleYPos = fotoHeigth;
                        break;
                    case UnderTop:
                        titleYPos = 0;
                        break;
                    default:
                        titleYPos = titleYPosition.getPosition();
                        break;
                }

                g.drawString(title, getImageXPosition(i) + textintend, getImageYPosition(i) + titleYPos + g.getFont().getSize());
            }


            if (i == swapPositions[0][0]) {
                if (align == Alignment.Horizontal) {
                    //g.drawImage(images.get(i), imageRect.x, imageRect.y, imageRect.width, imageRect.heigth, this);
                    g.drawImage(images.get(i), swapPositions[0][1] + getImageXPosition(i) - imageRect.x, imageRect.y, imageRect.width, imageRect.heigth, this);
                } else {
                    g.drawImage(images.get(i), imageRect.x, swapPositions[0][1] + getImageYPosition(i) - imageRect.y, fotoWidth, fotoHeigth, this);
                }
            } else if (i == swapPositions[1][0]) {
                if (align == Alignment.Horizontal) {
                    g.drawImage(images.get(i), swapPositions[1][1] + getImageXPosition(i) - imageRect.x, imageRect.y, imageRect.width, imageRect.heigth, this);
                } else {
                    g.drawImage(images.get(i), imageRect.x, swapPositions[1][1] + getImageYPosition(i) - imageRect.y, fotoWidth, fotoHeigth, this);
                }
            }

        }

    }

    /**
     * Clear the image list<br>
     * <b>Bei überschreiben unbedingt repaint() nicht vergessen!</b>
     */
    public synchronized void clear() {
        images.clear();
        titles.clear();
        position = 0;
        dragStart = 0;
        oldpos = 0;
        wheel = 0;
        t = null;
        wannaDrag = false;
        isMoving = false;
        mouseOverPicNumber = -1;
        centeringImage = -1;
        selectedImageIndex = -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mouseOverPicNumber >= 0 && selectingAllowed) {
            selectedImageIndex = mouseOverPicNumber;
            repaint();
        } else {
            selectedImageIndex = -1;
            repaint();
        }

        fireImageSelected(selectedImageIndex);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!draggingAllowed) {
            return;
        }

        wannaDrag = true;
        currentWheelMovingIsAllowed = mouseWheelSensivity;
        centeringImage = -1;

        if (align == Alignment.Horizontal || align == null) {
            dragStart = e.getX();
        } else {
            dragStart = e.getY();
        }
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        wannaDrag = false;
        Alignment align = this.align == Alignment.Horizontal || this.align == null ? Alignment.Horizontal : Alignment.Vertical;

        //HORIZONTAL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (align == Alignment.Horizontal) {
            if (images.size() > 0
                    && getImageXPosition(images.size() - 1) < getAlignmentX()
                    + spaceBetweenPics && slideAfterDrag) {
                
                new Thread() {

                    @Override
                    public void run() {
                        isMoving = true;

                        int sollPosition = -(images.size() - 1) * (fotoWidth + spaceBetweenPics);
                        int strecke = Math.abs(position - sollPosition);

                        while (!wannaDrag && position < sollPosition) {

                            strecke = Math.abs(position - sollPosition);

                            position += strecke / (fotoWidth / 10 + 1);

                            try {
                                sleep(20);
                            } catch (InterruptedException ex) {
                            }
                            repaint();


                        }

                        oldpos = position;
                        isMoving = false;
                    }
                }.start();
            } else if (images.size() > 0
                    && getImageXPosition(0) > getAlignmentX() + getWidth()
                    - fotoWidth - spaceBetweenPics && slideAfterDrag) {

                new Thread() {

                    @Override
                    public void run() {
                        isMoving = true;

                        int sollPosition = AbstractImageList.this.getWidth() - (fotoWidth + spaceBetweenPics);
                        int strecke = Math.abs(sollPosition - position);

                        while (!wannaDrag && position > sollPosition) {

                            strecke = Math.abs(position - sollPosition);

                            position -= strecke / (fotoWidth / 10) + 1;

                            try {
                                sleep(20);
                            } catch (InterruptedException ex) {
                            }
                            repaint();
                        }
                        oldpos = position;
                        isMoving = false;
                    }
                }.start();
            }

            oldpos = position;

            repaint();
        } //VERTIKAL !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        else {
            if (images.size() > 0
                    && getImageYPosition(images.size() - 1) < getAlignmentY()
                    + spaceBetweenPics && slideAfterDrag) {
                new Thread() {

                    @Override
                    public void run() {
                        isMoving = true;

                        int sollPosition = -(images.size() - 1) * (fotoHeigth + spaceBetweenPics);
                        int strecke = Math.abs(position - sollPosition);

                        while (!wannaDrag && position < sollPosition) {

                            strecke = Math.abs(position - sollPosition);

                            position += strecke / (fotoWidth / 10 + 1);

                            try {
                                sleep(20);
                            } catch (InterruptedException ex) {
                            }
                            repaint();
                        }
                        oldpos = position;
                        isMoving = false;
                    }
                }.start();
            } else if (images.size() > 0
                    && getImageYPosition(0) > getAlignmentY() + getHeight()
                    - fotoHeigth - spaceBetweenPics && slideAfterDrag) {
                
                new Thread() {

                    @Override
                    public void run() {
                        isMoving = true;

                        int sollPosition = AbstractImageList.this.getHeight() - (fotoWidth + spaceBetweenPics);
                        int strecke = Math.abs(sollPosition - position);

                        while (!wannaDrag && position > sollPosition) {

                            strecke = Math.abs(position - sollPosition);

                            position -= strecke / (fotoWidth / 10) + 1;

                            try {
                                sleep(20);
                            } catch (InterruptedException ex) {
                            }
                            repaint();
                        }
                        oldpos = position;
                        isMoving = false;
                    }
                }.start();
            }

            oldpos = position;

            repaint();
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOverPicNumber = -1;
        repaint();
        fireImageHovered(mouseOverPicNumber);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!draggingAllowed) {
            return;
        }

        //HORIZONTAL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (align == Alignment.Horizontal || align == null) {
            if (images.size() > 0
                    && getImageXPosition(images.size() - 1) < getAlignmentX()
                    + spaceBetweenPics) {
                if (e.getX() + oldpos - dragStart > position) {
                    position = e.getX() + oldpos - dragStart;
                }

            } else if (images.size() > 0
                    && getImageXPosition(0) > getAlignmentX() + getWidth()
                    - fotoWidth - spaceBetweenPics) {
                if (e.getX() + oldpos - dragStart < position) {
                    position = e.getX() + oldpos - dragStart;
                }

            } else {
                position = e.getX() + oldpos - dragStart;
            }

            repaint();
        } //VERTIKAL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        else {

            if (images.size() > 0
                    && getImageYPosition(images.size() - 1) < getAlignmentY()
                    + spaceBetweenPics) {
                if (e.getY() + oldpos - dragStart > position) {
                    position = e.getY() + oldpos - dragStart;
                }

            } else if (images.size() > 0
                    && getImageYPosition(0) > getAlignmentY() + getHeight()
                    - fotoHeigth - spaceBetweenPics) {
                if (e.getY() + oldpos - dragStart < position) {
                    position = e.getY() + oldpos - dragStart;
                }

            } else {
                position = e.getY() + oldpos - dragStart;
            }

            repaint();

        }
    }

    @Override
    public synchronized void mouseMoved(final MouseEvent e) {
        if (!hoveringAllowed) {
            mouseOverPicNumber = -1;
            return;
        }



        final Alignment align = (this.align == Alignment.Horizontal || this.align == null) ? Alignment.Horizontal : Alignment.Vertical;
        boolean needRepaint = false;
        int relativeMousePosition = align == Alignment.Horizontal ? e.getX() : e.getY();
        relativeMousePosition -= position + spaceBetweenPics + 3;
        int i = relativeMousePosition / ((align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics);
        i = (relativeMousePosition % ((align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics) >= (align == Alignment.Horizontal ? fotoWidth : fotoHeigth)) ? -1 : i;

        if (images.isEmpty() || relativeMousePosition < 0 && i == 0 || i >= images.size() || i < 0) {
            i = -1;
        }

        if (i != -1 && align == Alignment.Horizontal && (e.getY() < getImageYPosition(i) || e.getY() > fotoHeigth + getImageYPosition(i))) {
            i = -1;
        } else if (i != -1 && align == Alignment.Vertical && (e.getX() < getImageXPosition(i) || e.getX() > fotoWidth + getImageXPosition(i))) {
            i = -1;
        }


        needRepaint = mouseOverPicNumber != i;
        mouseOverPicNumber = i;

        if (needRepaint) {
            repaint();
            fireImageHovered(mouseOverPicNumber);
        }
    }

    @Override
    public synchronized void mouseWheelMoved(final MouseWheelEvent e) {
        wheel = e.getWheelRotation();
        final int wheelDirection = wheel > 0 ? 1 : -1;
        centeringImage = -1;
        final Alignment align = (this.align == Alignment.Horizontal || this.align == null) ? Alignment.Horizontal : Alignment.Vertical;


        if (currentWheelMovingIsAllowed > 0) {

            currentWheelMovingIsAllowed--;
            t = new Thread() {

                @Override
                public void run() {
                    try {

                        isMoving = true;

                        boolean scrollCondition;

                        if (align == Alignment.Horizontal) {
                            scrollCondition = !(images.size() > 0 && getImageXPosition(images.size() - 1) < getAlignmentX()
                                    + spaceBetweenPics)
                                    && !(images.size() > 0 && getImageXPosition(0) > getAlignmentX()
                                    + getWidth()
                                    - fotoWidth
                                    - spaceBetweenPics);
                        } else {
                            scrollCondition = !(images.size() > 0 && getImageYPosition(images.size() - 1) < getAlignmentY()
                                    + spaceBetweenPics)
                                    && !(images.size() > 0 && getImageYPosition(0) > getAlignmentY()
                                    + getHeight()
                                    - fotoHeigth
                                    - spaceBetweenPics);
                        }


                        
                        int t = 400;
                        int s = ((align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics) * wheelDirection;
                        double last = s * (1 - Math.pow(Math.E, -0.08 * 0));
                        double next = last;
                        



                        for (int i = 1; !wannaDrag && i < 35 && scrollCondition; i++) {

                            if (align == Alignment.Horizontal) {
                                scrollCondition = !(images.size() > 0 && getImageXPosition(
                                        images.size() - 1) < getAlignmentX()
                                        + spaceBetweenPics)
                                        && !(images.size() > 0 && getImageXPosition(0) > getAlignmentX()
                                        + getWidth()
                                        - fotoWidth
                                        - spaceBetweenPics);
                            } else {
                                scrollCondition = !(images.size() > 0 && getImageYPosition(images.size() - 1) < getAlignmentY()
                                        + spaceBetweenPics)
                                        && !(images.size() > 0 && getImageYPosition(0) > getAlignmentY()
                                        + getHeight()
                                        - fotoHeigth
                                        - spaceBetweenPics);
                            }
                            
                            
                            position += next - last;


                            last = next;
                            next = s * (1 - Math.pow(Math.E, -0.08 * i));

                            try {
                                sleep(20);
                            } catch (InterruptedException ex) {
                            }
                            repaint();

                        }

                        final boolean smallSlowCondition;
                        final boolean bigSlowCondition;

                        if (align == Alignment.Horizontal) {
                            smallSlowCondition = images.size() > 0
                                    && getImageXPosition(images.size() - 1) < getAlignmentX()
                                    + spaceBetweenPics; //unterer Rand
                            bigSlowCondition = images.size() > 0
                                    && getImageXPosition(0) > getAlignmentX() + getWidth()
                                    - fotoWidth - spaceBetweenPics; // oberer Rand
                        } else {
                            smallSlowCondition = images.size() > 0
                                    && getImageYPosition(images.size() - 1) < getAlignmentY()
                                    + spaceBetweenPics;

                            bigSlowCondition = images.size() > 0
                                    && getImageYPosition(0) > getAlignmentY() + getHeight()
                                    - fotoHeigth - spaceBetweenPics;
                        }


                        

                        // Abbremsen 
                        if (smallSlowCondition || bigSlowCondition) {
                            
                            isMoving = true;

                            
                            for (int i = 15; !wannaDrag && i > 0; i--) {

                                position += (smallSlowCondition ? 1 : -1) * i/10;

                                try {
                                    sleep(20);
                                } catch (InterruptedException ex) {
                                }
                                repaint();

                            }
                        }

                        oldpos = position;
                        isMoving = false;

                        repaint();

                    } catch (NullPointerException e) {
                    } catch (IndexOutOfBoundsException e) {
                    }

                    mouseMoved(e);

                    currentWheelMovingIsAllowed++;
                }
            };
            t.start();
            t = null;
        }
    }

    /**
     * Adds an imagelistlistener
     * @param ill ImageListListener
     */
    public void addImageListListener(ImageListListener<T> ill) {
        if (ill != null) {
            ills.add(ill);
        }
    }

    /**
     * Removes an imagelistlistener
     * @param ill ImageListListener
     */
    public void removeImageListListener(ImageListListener<T> ill) {
        ills.remove(ill);
    }

    /**
     * adds an Propertylistener
     * @param listener
     */
    public void addImageListePropertyListener(ImageListPropertyListener listener) {
        if (listener != null) {
            ilpls.add(listener);
        }
    }

    protected void fireImageSelected(int pos) {
        for (int i = 0; i < ills.size(); i++) {
            if (ills.get(i) != null) {
                ills.get(i).imageSelected(pos);
            }
        }
    }

    protected void fireImageAdded(T img) {
        repaint();
        for (ImageListListener<T> l : ills) {
            l.imageAdded(img);
        }
    }

    protected void fireImageRemoved(T img) {
        repaint();
        for (ImageListListener<T> l : ills) {
            l.imageRemoved(img);
        }
    }

    protected void fireImageHovered(int pos) {
        for (ImageListListener<T> l : ills) {
            l.imageHovered(pos);
        }
    }

    protected void fireImageReplaced(T Old, T New, int pos) {
        for (ImageListListener<T> l : ills) {
            l.imageReplaced(Old, New, pos);
        }
    }

    /**
     * Returns the relative image position on x-axis
     * @param index image index
     * @return relative Position in x direction
     */
    protected int getImageXPosition(int index) {
        if (index < 0 || index > images.size()) {
            throw new IllegalArgumentException("Ungültiger index angegeben! (" + index + ") (Size: " + images.size() + ")");
        }

        Alignment a = align == null ? Alignment.Horizontal : align;

        return a == Alignment.Horizontal ? (index * ((fotoWidth) + spaceBetweenPics) + spaceBetweenPics + position) : margin[0];
    }

    /**
     * Returns the relative image position on y-axis
     * @param index image index
     * @return relative Position in y direction
     */
    protected int getImageYPosition(int index) {
        if (index < 0 || index > images.size()) {
            throw new IllegalArgumentException("Ungültiger index angegeben! (" + index + ") (Size: " + images.size() + ")");
        }

        Alignment a = align == null ? Alignment.Horizontal : align;

        return a == Alignment.Vertical ? (index * ((fotoHeigth) + spaceBetweenPics) + spaceBetweenPics + position) : margin[2];
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Alignment old = align;
        if (this.getWidth() < this.getHeight() && autoAlignment) {
            align = Alignment.Vertical;
        } else if (this.getWidth() > this.getHeight() && autoAlignment) {
            align = Alignment.Horizontal;
        }

        mouseReleased(null);

        if (old != align) {
            for (ImageListPropertyListener ilpl : ilpls) {
                ilpl.alignmentChanged(old, align);
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    /**
     * Centers an image in the center of the component
     * @param index image index
     * @param speed speed of the animation <b>1-99</b>
     */
    public void centerImage(final int index, final int speed) {
        if (index >= 0 && index < images.size() && speed > 0 && speed < 100) {

            final int smallestCenter = (align == null || align == Alignment.Horizontal ? getWidth() : getHeight()) / 2 - (align == null || align == Alignment.Horizontal ? fotoWidth : fotoHeigth) / 2 - spaceBetweenPics;
            final int newPosition = smallestCenter - index * ((align == null || align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics);

            final int direction = newPosition > position ? 1 : (newPosition == position ? 0 : -1);

            centeringImage = index;


            new Thread() {

                @Override
                public void run() {
                    while (centeringImage == index && direction != 0 && ((direction == 1 && position < newPosition) || (direction == -1 && newPosition < position))) {
                        int strecke = direction == 1 ? newPosition - position : position - newPosition;
                        long time = 2;
                        position += direction * ((strecke / (100 - speed)) + 1);
                        oldpos = position;
                        try {
                            sleep(10);
                        } catch (Exception e) {
                        }

                        //if (strecke < fotoWidth / 2) {
//                            int x = MouseInfo.getPointerInfo().getLocation().x;
//                            int y = MouseInfo.getPointerInfo().getLocation().y;
//                            mouseMoved(new MouseEvent(AbstractImageList.this, index, time, position, x, y, direction, false));
//
//                        }
                        repaint();
                    }

                    //position = newPosition;

                    repaint();

                    for (ImageListPropertyListener ilpl : ilpls) {
                        ilpl.centeringEnd(index);
                    }

                    centeringImage = -1;
                }
            }.start();

            for (ImageListPropertyListener ilpl : ilpls) {
                ilpl.centeringStart(index);
            }

        }
    }

    private class Swapping {

        public final int pos1;
        public final int pos2;

        public Swapping(int p1, int p2) {
            pos1 = p1;
            pos2 = p2;
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && pos1 == ((Swapping) obj).pos1 && ((Swapping) obj).pos2 == pos2;
        }

        @Override
        public int hashCode() {
            int stellen = 1;
            int p = this.pos1;
            while (p > 1) {
                stellen *= 10;
                p /= 10;
            }

            return pos1 * stellen + pos2;
        }
    }
    private ArrayList<Swapping> swaps = new ArrayList<Swapping>(20);

    /**
     * Swaps 2 images
     * @param pos1 first image
     * @param pos2 second image
     * @param animated animated swap
     * @param speed speed of the animation 1-99
     * @return Thread, which handles the animation.<br>returns null if the images can't swap.
     */
    public synchronized Thread swap(final int pos1, final int pos2, final boolean animated, final int speed) {
        if (!(pos1 >= 0 && pos1 < images.size() && pos2 >= 0 && pos2 < images.size())) {
            return null;
        }

        final Swapping sw = new Swapping(pos1, pos2);
        swaps.add(sw);
        final boolean[] success = new boolean[]{true};

        Thread th = new Thread() {

            @Override
            public void run() {
                if (!swap(pos1, pos2, animated, speed, sw)) {
                    swaps.remove(sw);
                    success[0] = false;
                }
            }
        };
        th.start();
        return success[0] ? th : null;
    }

    private synchronized boolean swap(final int pos1, final int pos2, final boolean animated, final int speed, final Swapping sw) {

        if (pos1 >= 0 && pos1 < images.size() && pos2 >= 0 && pos2 < images.size() && pos1 != pos2 && speed > 0 && speed < 100) {

            if (animated) {

                while (swaps.get(0) != (sw)) {
                    try {
                        wait(5);
                    } catch (InterruptedException ex) {
                        System.out.println("Error beim wait(5);");
                    }
                }

                swapPositions[0][0] = pos1;
                swapPositions[1][0] = pos2;


//                final int newPositionOf1 = spaceBetweenPics + position + pos2 * ((align == null || align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics);
//                final int newPositionOf2 = spaceBetweenPics + position + pos1 * ((align == null || align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics);
                Rectangle pos1Rect = ScaleImage.fitToRect(new Rectangle(getImageXPosition(pos1), getImageYPosition(pos1), fotoWidth, fotoHeigth), (BufferedImage) images.get(pos1));
                Rectangle pos2Rect = ScaleImage.fitToRect(new Rectangle(getImageXPosition(pos2), getImageYPosition(pos2), fotoWidth, fotoHeigth), (BufferedImage) images.get(pos2));

                final int newPositionOf1 = align == Alignment.Vertical ? pos2Rect.y : pos2Rect.x;
                final int newPositionOf2 = align == Alignment.Vertical ? pos1Rect.y : pos1Rect.x;

                final int direction1 = newPositionOf2 < newPositionOf1 ? 1 : -1;


                final Thread t1 = new Thread() {

                    int oldPositionOf1 = spaceBetweenPics + position + pos1 * ((align == null || align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics);
                    int oldPositionOf2 = spaceBetweenPics + position + pos2 * ((align == null || align == Alignment.Horizontal ? fotoWidth : fotoHeigth) + spaceBetweenPics);

                    @Override
                    public void run() {
                        int distance1 = Math.abs(newPositionOf1 - oldPositionOf1);
                        int distance2 = Math.abs(newPositionOf2 - oldPositionOf2);

                        while ((direction1 == 1 && newPositionOf1 > oldPositionOf1 || direction1 == -1 && oldPositionOf1 > newPositionOf1)
                                || (direction1 == -1 && newPositionOf2 > oldPositionOf2 || direction1 == 1 && oldPositionOf2 > newPositionOf2)) {


                            distance1 = Math.abs(newPositionOf1 - oldPositionOf1);
                            distance2 = Math.abs(newPositionOf2 - oldPositionOf2);


                            oldPositionOf1 += direction1 * ((distance1 / (100 - speed)) + 1);
                            oldPositionOf2 -= direction1 * ((distance2 / (100 - speed)) + 1);


                            if ((direction1 == 1 && newPositionOf1 > oldPositionOf1 || direction1 == -1 && oldPositionOf1 > newPositionOf1)) {
                                swapPositions[0][1] = (oldPositionOf1);
                            }

                            if ((direction1 == -1 && newPositionOf2 > oldPositionOf2 || direction1 == 1 && oldPositionOf2 > newPositionOf2)) {
                                swapPositions[1][1] = (oldPositionOf2);
                            }

                            repaint();
                            try {
                                sleep(5);
                            } catch (Exception e) {
                                System.out.println("Error beim Swap von: " + pos1 + " und " + pos2);
                            }
                        }

                        Image i = images.get(pos2);
                        images.set(pos2, images.get(pos1));
                        images.set(pos1, i);

                        String s = titles.get(pos2);
                        titles.set(pos2, titles.get(pos1));
                        titles.set(pos1, s);

                        swapPositions[0][0] = -1;
                        swapPositions[1][0] = -1;
                        repaint();
                        swaps.remove(0);

                    }
                };

                t1.start();


            } else {
                Image i = images.get(pos2);
                images.set(pos2, images.get(pos1));
                images.set(pos1, i);

                String s = titles.get(pos2);
                titles.set(pos2, titles.get(pos1));
                titles.set(pos1, s);
            }


            notifyAll();
            repaint();
            return true;
        }


        return false;
    }
    private int[][] swapPositions = new int[][]{{-1, 0}, {-1, 0}};

    /**
     * Vertical position of the image titles
     */
    public static enum VertikalTitlePosition {

        /**
         * Custom position.<br/>
         * VertikalTitlePosition.Custom.position = [custom value in pixels]
         * @see #position
         */
        Custom,
        /**
         * The title is above the lower edge of the picture.<br/>
         * <b>Default position</b>
         */
        AtBottom,
        /**
         * The title is located directly below the image.
         */
        UnderBottom,
        /**
         * The title is located just below the top of the image.
         */
        UnderTop,
        /**
         * The title is right in the middle of the image.
         */
        Center;

        /**
         * Value of Custom<br/>
         * 0 is UnderTop<br/>
         * Default: 0
         * @see #UnderTop
         */
        public int getPosition() {
            return position;
        }
        private int position = 0;

        /**
         * Sets the custom value<br/>
         * 0 is UnderTop
         * @param pos Vertical Position
         * @return Custom with custom value
         */
        public VertikalTitlePosition setPosition(int pos) {
            if (this.equals(Custom)) {
                VertikalTitlePosition c = Custom;
                c.position = pos;
                return this;
            }

            return this;
        }
    }

    /**
     * Sets the image title of the image with the specified index<br/>
     * <br/>
     * if(title==null)<br/>
            title = "";
     * @param index image index
     * @param title new title
     * @see AbstractImageList#getTitle(int) 
     */
    public synchronized void setTitle(int index, String title){
        if(title==null)
            title = "";

        if(index<0 || index>=titles.size()){
            throw new IllegalArgumentException("Ungültiger Index übergeben: index="+index);
        }

        String old = titles.set(index, title);
        repaint();

        for (ImageListListener ill : ills) {
            ill.titleChanged(old, title, index);
        }
        
    }

    /**
     * Returns the title of the image with the specified index
     * @param index image index
     * @return image title
     * @see AbstractImageList#setTitle(int, java.lang.String)
     */
    public String getTitle(int index){
        if(index<0 || index>=titles.size()){
            throw new IllegalArgumentException("Ungültiger Index übergeben: index="+index);
        }

        return titles.get(index);
    } 
    
    /**
     * @deprecated not implemented yet
     * @param border
     */
    @Override
    public void setBorder(Border border) {
        
    }

    private Image doubleImg = null;
    private Graphics doubleBuff = null;
    @Override
    public void update(Graphics g) {

        if(doubleImg == null){
            doubleImg = createImage(this.getWidth(), this.getHeight());
            doubleBuff = doubleImg.getGraphics();
        }

        doubleBuff.setColor (getBackground ());
        doubleBuff.fillRect (0, 0, this.getWidth(), this.getHeight());

        paint(doubleBuff);

        g.drawImage (doubleImg, 0, 0, this);
    }




}
