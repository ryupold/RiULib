package de.realriu.riulib.gui.imagelist;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * This is a special type of the imageList it handles object references for each image in it.
 * @author riu
 * @version 1.1
 *
 * @see AbstractImageList
 * @see #getSelectedImage()
 * @see #getSelectedImageReferenz()
 * @see #getReferenz(int)
 * @see #addImage(java.awt.Image, java.lang.String)
 * @see #addImage(java.awt.Image, java.lang.String, java.lang.Object)
 * @see #addImage(java.awt.Image, java.lang.Object)
 * @see #removeImage(int)
 * @see #addReferenceImageListListener(riulib.gui.imagelist.ImageListListener)
 * @see #getImage(int)
 * @see #containsReferenz(java.lang.Object)
 * @see #indexOf(java.lang.Object)
 * @see #sort(java.util.Comparator, boolean, int, boolean) 
 */
public class ReferenzImageList<T> extends AbstractImageList<Image> {

    protected final List<T> referenzes = new ArrayList<T>();
    protected List<ImageListListener<T>> refL = new ArrayList<ImageListListener<T>>();
    
    /**
     * Creates a new reference image list with the given scrolling direction
     * @param a scrolling direction
     * @param autoAlignment automatic scrolling direction change
     * @see Alignment
     */
    public ReferenzImageList(Alignment a, boolean autoAlignment) {
        super(a, autoAlignment);
    }

    /**
     * True if the given reference is in the list.
     * @param reference  <b>null</b> returns false.
     * @return true  if ref is in list<br>false if ref isn't in list or parameter was null.
     */
    public boolean containsReferenz(T reference) {
        return indexOf(reference) >= 0;
    }

    /**
     * Gibt die Position des ersten Vorkommens der übergebenen Referenz zurück.
     * @param reference <b>null</b> liefert -1 zurück.
     * @return Position der Referenz oder -1  falls die Referenz nicht vorhanden ist. (.equals(Object) wird benutzt um auf Gleichheit zu prüfen.)
     */
    public int indexOf(T reference) {
        if (reference == null) {
            return -1;
        }

        for (int i = 0; i < referenzes.size(); i++) {
            if (reference.equals(referenzes.get(i))) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Gibt das ausgewählte Bild zurück.
     * @return Selektiertes Bild
     * @see #getImage(int)
     * @see #getSelectedImageReferenz()
     * @see #getReferenz(int) 
     */
    public Image getSelectedImage() {
        if (getSelectedImageIndex() < 0 || getSelectedImageIndex() >= images.size()) {
            return null;
        }
        return images.get(getSelectedImageIndex());
    }

    /**
     * Gibt das zum ausgewählten Bild zugehörige Objekt zurück.
     * @return Bildverknüpfung
     */
    public T getSelectedImageReferenz() {
        if (getSelectedImageIndex() < 0 || getSelectedImageIndex() >= referenzes.size()) {
            return null;
        }
        return referenzes.get(getSelectedImageIndex());
    }

    /**
     * Gibt die Verknüpfung anhand des Indexes zurück.
     * @param i Index
     * @return Verknüpfung
     */
    public T getReferenz(int i) {
        if (i < 0 || i >= referenzes.size()) {
            return null;
        }
        return referenzes.get(i);

    }

    /**
     * Fügt ein Bild ohne Verknüpfung aber mit Titel hinzu.
     * @param img Bild
     * @param title Titel
     */
    @Override
    public synchronized void addImage(Image img, String title) {
        addImage(img, title, null);
    }

    /**
     * Fügt ein Bild mit Titel und mit Verknüpfung hinzu
     * @param img Bild
     * @param title Titel
     * @param ref Verknüpfung
     */
    public synchronized void addImage(Image img, String title, T ref) {
        images.add(img);
        titles.add(title == null ? "" : title);
        referenzes.add(ref);
        fireImageAdded(img);
        fireReferenceAdded(ref);
    }

    /**
     * Fügt ein Bild mit zugehöriger Verknüpfung hinzu.
     * @param img Bild
     * @param ref Verknüpfung
     */
    public synchronized void addImage(Image img, T ref) {
        addImage(img, null, ref);
    }

    /**
     * Entfernt ein Bild und seine zugehörige Verknüpfung anhand eines Indexes.
     * @param pos Index
     * @return Entferntes Bild
     */
    @Override
    public synchronized Image removeImage(int pos) {
        Image i = images.remove(pos);
        titles.remove(pos);
        T ref = referenzes.remove(pos);
        fireImageRemoved(i);
        fireReferenceRemoved(ref);
        return i;
    }

    /**
     * Ersetzt das Bild an der angegebenen Position mit einem neuen.
     * @param pos zu ersetzendes Bild
     * @param newImage Neues Bild
     * @return Das vorherige Bild oder null wenn eine ungültige Position angegeben wird.
     */
    @Override
    public synchronized Image replaceImage(int pos, Image newImage) {
        if (pos >= 0 && pos < images.size()) {
            Image i = images.set(pos, newImage);
            repaint();
            fireImageReplaced(i, newImage, pos);
            return i;
        }

        return null;
    }

    /**
     * Ersetzt die Referenz an der angegebenen Position mit einer neuen.<br>
     * Das Event imageReplaced wird nur gefeuert, wenn eine gültige Position angegeben wird.
     * @param pos zu ersetzende Referenz
     * @param newImage Neue Referenz
     * @return Die vorherige Referenz oder null wenn eine ungültige Position angegeben wird.
     */
    public synchronized T replaceReferenz(int pos, T newImage) {
        if (pos >= 0 && pos < referenzes.size()) {
            T r = referenzes.set(pos, newImage);
            fireReferenzReplaced(r, newImage, pos);
            return r;
        }

        return null;
    }

    /**
     * <h3>Wichtig um die Verknüpfung eines hinzugefügten oder entfernten Bildes herauszufinden</h3>
     * Hier kann ein Listener eingefügt werden der beim adden bzw. removen die Referenz auf das Verknüpfte Objekt liefert.
     * @param l ImageListListener
     */
    public void addReferenceImageListListener(ImageListListener<T> l) {
        if (l != null) {
            refL.add(l);
        }
    }

    public void removeReferenceImageListListener(ImageListListener<T> l) {
        refL.remove(l);
    }

    /**
     * Gibt das Bild anhand seines Indexes zurück.
     * @param i Index
     * @return Bild
     */
    @Override
    public Image getImage(int i) {
        if (i < 0 || i >= images.size()) {
            return null;
        }
        return images.get(i);
    }

    /**
     * Entfernt alle Bilder und zugehörige Referenzen aus der Liste.
     */
    @Override
    public synchronized void clear() {
        super.clear();
        referenzes.clear();
        repaint();
    }

    /**
     * Sortiert die Liste anhand des übergebenen Comparators.<br/>
     * @param comperator Vergleichskriterium
     * @param animated Gibt an ob die Sortierung animiert werden soll
     * @param speed Geschwindigkeit der Sortieranimation
     * @param aufsteigend Gibt an ob die Liste aufsteigend oder absteigend sortiert wird
     * @param sortAlgorithm Der zu verwendete Sortieralgorithmus
     * @see SortAlgorithm
     */
    public synchronized void sort(Comparator<T> comperator, boolean animated, int speed, boolean aufsteigend, SortAlgorithm sortAlgorithm) {

        if (comperator == null) {
            throw new IllegalArgumentException("Kein Comperator übergeben (comperator==null)");
        }

        if (sortAlgorithm == null) {
            throw new IllegalArgumentException("Kein Sortieralgorithmus übergeben (sortAlgorithm==null)");
        }
        

        if (sortAlgorithm == SortAlgorithm.BubbleSort) {
            boolean change = true;

            while (change) {
                change = false;
                for (int i = 0; i < referenzes.size() - 1; i++) {
                    if (comperator.compare(referenzes.get(i), referenzes.get(i + 1)) > 0 && aufsteigend) {
                        swap(i, i + 1, animated, speed);
                        change = true;
                    } else if (comperator.compare(referenzes.get(i), referenzes.get(i + 1)) < 0 && !aufsteigend) {
                        swap(i, i + 1, animated, speed);
                        change = true;
                    }
                }
            }
        } else {
            boolean change = false;
            for (int i = 0; i < referenzes.size() - 1; i++) {
                if (comperator.compare(referenzes.get(i), referenzes.get(i + 1)) > 0 && aufsteigend) {
                    change = true;
                } else if (comperator.compare(referenzes.get(i), referenzes.get(i + 1)) < 0 && !aufsteigend) {
                    change = true;
                }
            }

            if(change){
                qSort(0, images.size() - 1, comperator, animated, speed, aufsteigend);
            }
        }
    }

    private void qSort(int left, int right, Comparator<T> comperator, boolean animated, int speed, boolean aufsteigend) {
        T pivot = referenzes.get((right + left)/2);
        int i = left;
        int j = right;

        while (i < j) {
            if (aufsteigend) {
                while (comperator.compare(referenzes.get(i), pivot) < 0) {
                    i++;
                }
                
                while (comperator.compare(referenzes.get(j), pivot) > 0) {
                    j--;
                }
                
                if (i < j) {
                    swap(i, j, animated, speed);
                    i++;
                    j--;
                }

            } else {
                while (comperator.compare(referenzes.get(i), pivot) > 0) {
                    i++;
                }

                while (comperator.compare(referenzes.get(j), pivot) < 0) {
                    j--;
                }

                if (i < j) {
                    swap(i, j, animated, speed);
                    i++;
                    j--;
                }
            }
            
        }


        if (j+1 < right) {
            qSort(j+1, right, comperator, animated, speed, aufsteigend);
        }
        if (left < j) {
            qSort(left, j, comperator, animated, speed, aufsteigend);
        }
        
    }

    /**
     * Vertauscht die Positionen von 2 Bildern und natürlich die der Referenzen
     * @param pos1 Erstes Bild
     * @param pos2 Zweites Bild
     * @param animated Annimierte Vertauschung
     * @param speed Geschwindigkeit der Vertauschung 1-99
     * * @return Gestarteter Thread der das Vertauschen durchführt.<br>Falls die Vertauschung nicht durchgeführt werden kann, wird null zurückgegeben.
     */
    @Override
    public synchronized Thread swap(int pos1, int pos2, boolean animated, int speed) {
        if (pos1 >= 0 && pos1 < referenzes.size() && pos2 >= 0 && pos2 < referenzes.size()) {
            Thread th = super.swap(pos1, pos2, animated, speed);
            T t = referenzes.get(pos2);
            referenzes.set(pos2, referenzes.get(pos1));
            referenzes.set(pos1, t);
            return th;
        }

        return null;
    }

    protected void fireReferenceAdded(T ref) {
        for (ImageListListener<T> l : refL) {
            l.imageAdded(ref);
        }
    }

    protected void fireReferenceRemoved(T ref) {
        for (ImageListListener<T> l : refL) {
            l.imageRemoved(ref);
        }
    }

    protected void fireReferenzReplaced(T oldRed, T newRef, int pos) {
        for (ImageListListener<T> l : refL) {
            l.imageReplaced(oldRed, newRef, pos);
        }
    }

    @Override
    protected void fireImageHovered(int pos) {
        super.fireImageHovered(pos);
        for (ImageListListener<T> l : refL) {
            l.imageHovered(pos);
        }

    }

    @Override
    protected void fireImageSelected(int pos) {
        super.fireImageSelected(pos);
        for (ImageListListener<T> l : refL) {
            l.imageSelected(pos);
        }
    }

    /**
     * Sortieralgorithmus der zum Sortieren einer ReferenzImageList verwendet werden kann.
     * @see SortAlgorithm#BubbleSort
     * @see SortAlgorithm#Quicksort
     */
    public static enum SortAlgorithm {

        /**
         * <u>Bubblesort Algorithmus:</u><br/><br/>
         * Das Sortieren durch Aufsteigen (englisch Bubble sort, "Blasensortierung") bezeichnet einen einfachen, stabilen Sortieralgorithmus, der eine Reihe zufällig angeordneter Elemente (etwa Zahlen) der Größe nach ordnet.<br/><br/>
         * Bubblesort wird von Donald E. Knuth als vergleichsbasierter Sortieralgorithmus bezeichnet [1]. Das bedeutet, dass der Sortieralgorithmus sämtliche Entscheidungen alleine auf Basis des Größenvergleichs je zweier Elemente fällt, nicht etwa aufgrund der Inspektion der Binärdarstellung eines Elements. Bubblesort ist der einfachste solcher Algorithmen. Einzige Anforderung an den Vergleichsoperator ist, dass er eine Totalordnung der Liste ermöglicht.<br/><br/>
         * Bubblesort gehört zur Klasse der In-place-Verfahren, was bedeutet, dass der Algorithmus zum Sortieren keinen zusätzlichen Arbeitsspeicher außer den lokalen Laufvariablen der Prozedur benötigt. Dadurch, dass grundsätzlich nur aneinandergrenzende Elemente miteinander vertauscht werden, eignet sich dieses Verfahren auch zum Sortieren von Listen mit unterschiedlich großen Elementen.<br/><br/>
         */
        BubbleSort,
        /**
         * <u>Quicksort Algorithmus:</u><br/><br/>
         * Quicksort (von engl. quick‚schnell‘ und to sort ‚sortieren‘) ist ein schneller, rekursiver, nicht-stabiler Sortieralgorithmus, der nach dem Prinzip Teile und herrsche (lat. Divide et impera!, engl. Divide and conquer) arbeitet. <br/><br/>
         * Er wurde ca. 1960 von C. Antony R. Hoare in seiner Grundform entwickelt[1] und seitdem von vielen Forschern verbessert. <br/><br/>
         * Der Algorithmus hat den Vorteil, dass er über eine sehr kurze innere Schleife verfügt (was die Ausführungsgeschwindigkeit stark erhöht) und ohne zusätzlichen Speicherplatz auskommt (abgesehen von dem für die Rekursion zusätzlichen benötigten Platz auf dem Aufruf-Stack).
         */
        Quicksort
    }
}
