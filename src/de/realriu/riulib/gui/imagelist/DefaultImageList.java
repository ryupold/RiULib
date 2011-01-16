
package de.realriu.riulib.gui.imagelist;

import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;


/**
 * Maintains a list of images with image objects, the images are not compressed.
 * @author riu
 * @version 1.1
 * 
 * @see AbstractImageList
 * @see #DefaultImageList(riulib.gui.imagelist.AbstractImageList.Alignment, boolean)
 * @see #addImage(java.awt.Image, java.lang.String)
 * @see #removeImage(int)
 * @see #getSelectedImage()
 * @see #getImage(int)
 */
public class DefaultImageList extends AbstractImageList<Image> implements MouseListener,
		MouseMotionListener, MouseWheelListener {

    /**
     * Creates a default image list
     * @param a scroll alignment
     * @param autoAlignment automatic scroll alignment
     */
    public DefaultImageList(Alignment a, boolean autoAlignment) {
        super(a, autoAlignment);
    }


    /**
     * Adds an image with a title
     * @param img image object
     * @param title title, null if no title should be shown
     */
    @Override
    public synchronized void addImage(Image img, String title) {
        images.add(img);
        titles.add(title==null?"":title);
        
        fireImageAdded(img);
    }


    /**
     * Removes an image on the specified position an retrives it.
     * @param pos Bildposition
     * @return removed image
     */
    @Override
    public synchronized Image removeImage(int pos) {
        Image i = images.remove(pos);
        titles.remove(pos);
        
        fireImageRemoved(i);
        return i;
    }


    /**
     * Replaces the image on the specified index with a new one.
     * @param pos position of the new image
     * @param newImage new image
     * @return old image or null if the index isn't valid
     */
    @Override
    public synchronized Image replaceImage(int pos, Image newImage) {
        if(newImage==null)
            throw new IllegalArgumentException("Nullpointer für das Bild übergeben!");

        if(pos>=0 && pos<images.size()){
            Image old = images.set(pos, newImage);

            repaint();
            fireImageReplaced(old, newImage, pos);

            return old;
        }

        return null;
    }

    /**
     * Returns the current selected image<br>
     * Returns <b>null</b> if no image is selected.
     * @return selected image
     */
    public Image getSelectedImage() {
        if(getSelectedImageIndex()<0 || getSelectedImageIndex()>=images.size())
                return null;
        return images.get(getSelectedImageIndex());
    }

    /**
     * Returns an image on the specified index
     * @param i index
     * @return image or <b>null</b> if index isn't valid
     */
    @Override
    public Image getImage(int i) {
        if(i<0 || i>=images.size())
                return null;
        return images.get(i);
    }

    /**
     * Clear the list
     */
    @Override
    public synchronized void clear() {
        super.clear();
        repaint();
    }

    

}
