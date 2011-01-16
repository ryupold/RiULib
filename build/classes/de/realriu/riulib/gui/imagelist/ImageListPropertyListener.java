package de.realriu.riulib.gui.imagelist;

import java.awt.Color;
import java.awt.Font;

/**
 * Hört auf die Events, die geschmissen werden wenn Eigenschaften der ImageList geändert werden.
 * 
 * @version 1.0.1
 * @author riu
 */
public interface ImageListPropertyListener {

    /**
     * Is executed when the text font is changed.
     * @param oldFont
     * @param newFont
     */
    public void textFontChanged(Font oldFont, Font newFont);

    /**
     * Runs when the title font is changed.
     * @param oldFont
     * @param newFont
     */

    public void titleFontChanged(Font oldFont, Font newFont);

    /**
     * Runs when the space between the images was changed.
     * @param oldSpace
     * @param newSpace
     */

    public void spaceBetweenPicturesChanged(int oldSpace, int newSpace);

    /**
     * Runs when the color of the selection marquee was changed
     * @param oldColor
     * @param newColor
     */

    public void selectionColorChanged(Color oldColor, Color newColor);

    /**
     * Runs when the color of the hover rectangle has changed.
     * @param oldColor
     * @param newColor
     */

    public void hoverColorChanged(Color oldColor, Color newColor);

    /**
     * Is executed when the text color was changed.
     * @param oldColor
     * @param newColor
     */

    public void textColorChanged(Color oldColor, Color newColor);

    /**
     * Runs when the title text color was changed.
     * @param oldColor
     * @param newColor
     */

    public void titleColorChanged(Color oldColor, Color newColor);

    /**
     * Runs when the scroll alignment has changed.
     * @param oldAlignment
     * @param newAlignment
     */

    public void alignmentChanged(AbstractImageList.Alignment oldAlignment, AbstractImageList.Alignment newAlignment);

    /**
     * Is executed when the automatic orientation adjustment was to go off or on.
     * @param newValue
     */

    public void autoAlignmentChanged(boolean newValue);

    /**
     * Wird ausgeführt, wenn sich der Text verändert hat.
     * @param oldText
     * @param newText
     */

    public void textChanged(String oldText, String newText);

    /**
     * Is executed when the text has changed.
     * @param imageIndex Bildindex
     */

    public void centeringStart(int imageIndex);

    /**
     * Is performed when an image centering is completed or aborted.
     * @param imageIndex Bildindex
     */

    public void centeringEnd(int imageIndex);

    /**
     * Runs when the mouse wheel scroll intensitivy was changed.
     * @param oldSens
     * @param newSens
     */

    public void mouseWheelSensivityChanged(int oldSens, int newSens);

    /**
     * Runs when the 'image-back-sliding' was changed on or off.
     * @param newValue
     */

    public void slideAfterDragChanged(boolean newValue);

    /**
     * Runs when allowed or unallowed dragging.
     * @param newValue
     */

    public void dragAllowingChanged(boolean newValue);

    /**
     * Runs if hovering was enabled or disabled
     * @param newValue
     */

    public void hoveringAllowingChanged(boolean newValue);

    /**
     * Runs when selection is enabled or disabled
     * @param newValue
     */

    public void selectionAllowingChanged(boolean newValue);

    /**
     * Runs on vertical title position change
     * @param oldValue
     * @param newValue
     */

    public void vertikalTitlePositionChanged(AbstractImageList.VertikalTitlePosition oldValue, AbstractImageList.VertikalTitlePosition newValue);

    /**
     * Runs when margin changed
     * @param oldSpaces
     * @param newSpaces
     */

    public void marginChanged(int[] oldSpaces, int[] newSpaces);

    /**
     * Runs when padding changed
     * @param oldSpaces
     * @param newSpaces
     */

    public void paddingChanged(int[] oldSpaces, int[] newSpaces);


    /**
     * Is thrown when the square or rectangular images display is switched on margin change.
     * @param newValue
     */
    
    public void forceSquaredImagesChanged(boolean newValue);
}
