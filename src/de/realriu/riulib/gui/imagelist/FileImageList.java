
package de.realriu.riulib.gui.imagelist;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.imageio.ImageIO;
import de.realriu.riulib.helpers.ScaleImage;


/**
 * Imagelist that is working with the file references<br>
 * The image files are loaded and on the component size down scaled to use less Ram.<br>
 
 * @author riu
 * @version 1.1
 *
 * @see AbstractImageList
 * @see #refreshSize()
 * @see #addImage(java.io.File, java.lang.String) 
 * @see #removeImage(int) 
 * @see #getSelectedImage()
 * @see #getImage(int) 
 */
public class FileImageList extends AbstractImageList<File> implements MouseListener,
		MouseMotionListener, MouseWheelListener {

    protected List<File> files = new ArrayList<File>();
    


    /**
     * Creates a new FileImageList.
     * @param a scroll alignment
     * @param autoAlignment automatic scroll alignment
     */
    public FileImageList(Alignment a, boolean autoAlignment) {
        super(a, autoAlignment);
        

    }


    /**
     * Adds a file, the image is loaded and then is scaled to the size of the component.<br/>
     *
     * @param f image file
     * @param title title
     * @throws RuntimeException Using ImageIO.read(f) to load the file, if an error occurs the file wont be in the list
     */
    @Override
    public synchronized void addImage(File f, String title) {
        if(f!=null && f.exists() && f.isFile()){
            try {
                repaint();

                if(fotoHeigth>0 && fotoWidth>0){
                    BufferedImage img = ImageIO.read(f);
                    ScaleImage.Rectangle preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, fotoWidth, fotoHeigth), img);
                    images.add(ScaleImage.scale(img, preferedSize.width, preferedSize.heigth));
                }

                titles.add(title==null?"":title);
                files.add(f);

                fireImageAdded(f);
                System.gc();
                repaint();
                
            } catch (IOException ex) {throw new RuntimeException(ex);}
        }
    }

    /**
     * Removes the image file and the corresponding image from the list
     * @param pos Index
     * @return removed file
     */
    @Override
    public synchronized File removeImage(int pos) {
        images.remove(pos);
        titles.remove(pos);
        File f = files.remove(pos);
        
        fireImageRemoved(f);
        return f;
    }


    /**
     * Replaces the image file on the specified index with a new one.
     * @param pos index
     * @param newImage new image
     * @return old image or <b>null</b> if index wasn't valid
     */
    @Override
    public synchronized File replaceImage(int pos, File newImage) {
        if(newImage==null)
            throw new IllegalArgumentException("Nullpointer für das Bild übergeben!");

        if(pos>=0 && pos<files.size()){
            File old = files.set(pos, newImage);

            if(fotoHeigth>0 && fotoWidth>0){
                try{
                    BufferedImage img = ImageIO.read(newImage);
                    ScaleImage.Rectangle preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, fotoWidth, fotoHeigth), img);
                    images.set(pos, ScaleImage.scale(img, preferedSize.width, preferedSize.heigth));
                }catch(Exception e){}
            }

            repaint();
            fireImageReplaced(old, newImage, pos);

            return old;
        }

        return null;
    }

    /**
     * Returns the file for the selected image
     * @return image file - <b>null</b> if no image is selected
     */
    public File getSelectedImage() {
        if(getSelectedImageIndex()<0 || getSelectedImageIndex()>=files.size())
                return null;
        return files.get(getSelectedImageIndex());
    }

    /**
     * Returns the file of the specified index<br/>
     * @param i Index
     * @return image file or <b>null</b> if index isn't valid
     */
    @Override
    public File getImage(int i) {
        if(i<0 || i>=files.size())
                return null;
        return files.get(i);
    }

    /**
     * Loads the images from the files again in order to adapt to the new component size.
     */
    public synchronized void refreshSize(){
        images.clear();
        ArrayList<File> files2 = new ArrayList<File>();
        for(int i=0; i<files.size(); i++){
            try {
                System.gc();
                BufferedImage img = ImageIO.read(files.get(i));
                ScaleImage.Rectangle preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, fotoWidth, fotoHeigth), img);
                images.add(ScaleImage.scale(img, preferedSize.width, preferedSize.heigth));
                files2.add(files.get(i));
            } catch (IOException ex) {}
        }
        files = files2;
        repaint();
    }

    /**
     * Clears the lists
     */
    @Override
    public synchronized void clear() {
        files.clear();
        super.clear();
        titles.clear();
        repaint();
    }
    
}
