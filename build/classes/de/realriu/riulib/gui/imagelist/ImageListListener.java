package de.realriu.riulib.gui.imagelist;



/**
 * Listener für die Events von ImageListen.
 * @author riu
 * @version 1.1
 */
public interface ImageListListener<L> {

        /**
     * One image was selected through a click or -1 if no image was selected
     * @param pos position of the image or -1 if no one is selected
     */
    public void imageSelected(int pos);

    /**
     * An element which is represented by an image was inserted.
     * @param i image element
     */
    public void imageAdded(L i);

    /**
     * An element which is represented by an image was removed.
     * @param i image element
     */
    public void imageRemoved(L i);

    /**
     * An image get hovered
     * @param pos position of the image
     */
    public void imageHovered(int pos);

    /**
     * An image has been replaced in the position passed by another
     * @param Old
     * @param New
     * @param pos Position
     */
    public void imageReplaced(L Old, L New, int pos);


    /**
     * Is thrown when an image is set for a new title.
     * @param Old
     * @param New
     * @param pos image position
     * @see AbstractImageList#setTitle(int, java.lang.String)
     */
    void titleChanged(String Old, String New, int pos);
}
