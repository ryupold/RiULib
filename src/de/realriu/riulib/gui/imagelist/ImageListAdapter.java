package de.realriu.riulib.gui.imagelist;

import java.awt.Color;
import java.awt.Font;

/**
 * This class is used to create listener objects in which one overrides only the methods you want to pay attention to their events.
 * @version 1.0
 * @author riu
 */
public abstract class ImageListAdapter<L> implements ImageListListener<L>, ImageListPropertyListener{

    /**
     * One image was selected through a click or -1 if no image was selected
     * @param pos position of the image or -1 if no one is selected
     */
    @Override
    public void imageSelected(int pos){}

    /**
     * An element which is represented by an image was inserted.
     * @param i image element
     */
    @Override
    public void imageAdded(L i){}

    /**
     * An element which is represented by an image was removed.
     * @param i image element
     */
    @Override
    public void imageRemoved(L i){}

    /**
     * An image get hovered
     * @param pos position of the image
     */
    @Override
    public void imageHovered(int pos){}

    /**
     * An image has been replaced in the position passed by another
     * @param Old 
     * @param New 
     * @param pos Position
     */
    @Override
    public void imageReplaced(L Old, L New, int pos){}


    /**
     * Is thrown when an image is set for a new title.
     * @param Old
     * @param New
     * @param pos image position
     * @see AbstractImageList#setTitle(int, java.lang.String)
     */
    @Override
    public void titleChanged(String Old, String New, int pos){}

    /**
     * Is executed when the text font is changed.
     * @param oldFont
     * @param newFont
     */
    @Override
    public void textFontChanged(Font oldFont, Font newFont){}

    /**
     * Runs when the title font is changed.
     * @param oldFont
     * @param newFont
     */
    @Override
    public void titleFontChanged(Font oldFont, Font newFont){}

    /**
     * Runs when the space between the images was changed.
     * @param oldSpace
     * @param newSpace
     */
    @Override
    public void spaceBetweenPicturesChanged(int oldSpace, int newSpace){}

    /**
     * Runs when the color of the selection marquee was changed
     * @param oldColor
     * @param newColor
     */
    @Override
    public void selectionColorChanged(Color oldColor, Color newColor){}

    /**
     * Runs when the color of the hover rectangle has changed.
     * @param oldColor
     * @param newColor
     */
    @Override
    public void hoverColorChanged(Color oldColor, Color newColor){}

    /**
     * Is executed when the text color was changed.
     * @param oldColor
     * @param newColor
     */
    @Override
    public void textColorChanged(Color oldColor, Color newColor){}

    /**
     * Runs when the title text color was changed.
     * @param oldColor
     * @param newColor
     */
    @Override
    public void titleColorChanged(Color oldColor, Color newColor){}

    /**
     * Runs when the scroll alignment has changed.
     * @param oldAlignment
     * @param newAlignment
     */
    @Override
    public void alignmentChanged(AbstractImageList.Alignment oldAlignment, AbstractImageList.Alignment newAlignment){}

    /**
     * Is executed when the automatic orientation adjustment was to go off or on.
     * @param newValue
     */
    @Override
    public void autoAlignmentChanged(boolean newValue){}

    /**
     * Wird ausgeführt, wenn sich der Text verändert hat.
     * @param oldText
     * @param newText
     */
    @Override
    public void textChanged(String oldText, String newText){}

    /**
     * Is executed when the text has changed.
     * @param imageIndex Bildindex
     */
    @Override
    public void centeringStart(int imageIndex){}

    /**
     * Is performed when an image centering is completed or aborted.
     * @param imageIndex Bildindex
     */
    @Override
    public void centeringEnd(int imageIndex){}

    /**
     * Runs when the mouse wheel scroll intensitivy was changed.
     * @param oldSens
     * @param newSens
     */
    @Override
    public void mouseWheelSensivityChanged(int oldSens, int newSens){}

    /**
     * Runs when the 'image-back-sliding' was changed on or off.
     * @param newValue
     */
    @Override
    public void slideAfterDragChanged(boolean newValue){}

    /**
     * Runs when allowed or unallowed dragging.
     * @param newValue
     */
    @Override
    public void dragAllowingChanged(boolean newValue){}

    /**
     * Runs if hovering was enabled or disabled
     * @param newValue
     */
    @Override
    public void hoveringAllowingChanged(boolean newValue){}

    /**
     * Runs when selection is enabled or disabled
     * @param newValue
     */
    @Override
    public void selectionAllowingChanged(boolean newValue){}

    /**
     * Runs on vertical title position change
     * @param oldValue
     * @param newValue
     */
    @Override
    public void vertikalTitlePositionChanged(AbstractImageList.VertikalTitlePosition oldValue, AbstractImageList.VertikalTitlePosition newValue){}

    /**
     * Runs when margin changed
     * @param oldSpaces
     * @param newSpaces
     */
    @Override
    public void marginChanged(int[] oldSpaces, int[] newSpaces){}

    /**
     * Runs when padding changed
     * @param oldSpaces
     * @param newSpaces
     */
    @Override
    public void paddingChanged(int[] oldSpaces, int[] newSpaces){}


    /**
     * Is thrown when the square or rectangular images display is switched on margin change.
     * @param newValue
     */
    @Override
    public void forceSquaredImagesChanged(boolean newValue){}

}
