package de.realriu.riulib.helpers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Bietet Funktionen zum Skalieren von Bildern
 * @author riu
 * @version 1.1
 */
public class ScaleImage {

    /**
     * Skaliert das übergebene Bild runter auf die angegebene Größe und gibt es zurück.
     * @param src Bild das skaliert werden soll
     * @param width Neue Breite
     * @param height Neue Höhe
     * @return Skaliertes Bild
     * @throws IOException
     */
    public static BufferedImage scale(BufferedImage src, int width, int height) {
        BufferedImage bsrc = src;
        BufferedImage bdest =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bdest.createGraphics();
        AffineTransform at =
                AffineTransform.getScaleInstance((double) width / bsrc.getWidth(),
                (double) height / bsrc.getHeight());
        g.drawRenderedImage(bsrc, at);

        return bdest;
    }

    /**
     * Nimmt die Maße des Bildes und rechnet sie so um, das es Perfekt in das übergebene Rechteck passen.
     * <b>Die X & Y Koordinaten des Ergebnisrechtecks werden so umgerechnet, dass das Bild in der Mitte des gewünschten Rechtecks liegt!</b>
     * @param preferedSize Das gewünschte Rechteck
     * @param image Bild, mit den Originalgrößen
     * @return Die neuen Dimensionen des Bildes
     */
    public static Rectangle fitToRect(Rectangle preferedSize, BufferedImage image) {
        if (image != null && preferedSize!=null) {

            int nw = image.getWidth() * preferedSize.heigth / image.getHeight();
            int nh;

            if (nw < preferedSize.width) {
                nh = preferedSize.heigth;
            } else {
                nw = preferedSize.width;
                nh = image.getHeight() * preferedSize.width / image.getWidth();
            }

            return new Rectangle(preferedSize.x+(preferedSize.width-nw)/2, preferedSize.y+(preferedSize.heigth-nh)/2, nw, nh);
        } else {
            throw new IllegalArgumentException("Ungülige Parameter: Image(" + image + ") / Recktangle(" + preferedSize + ") ");
        }
    }

    /**
     * Nimmt die Maße des Bildes und rechnet sie so um, das es Perfekt in die übergebene Breite und Höhe passen.<br/>
     * <b>Die X & Y Koordinaten des Ergebnisrechtecks werden auf 0 gesetzt!</b>
     * @param preferedWidth Gewünschte Breite
     * @param preferedHeigth Gewünschte Höhe
     * @param image Bild, mit den Originalgrößen
     * @return Die neuen Dimensionen des Bildes
     */
    public static Rectangle fitToRect(int preferedWidth, int preferedHeigth, BufferedImage image) {
        if (image != null && preferedWidth>0 && preferedHeigth>0) {

            int nw = image.getWidth() * preferedHeigth / image.getHeight();
            int nh;

            if (nw < preferedWidth) {
                nh = preferedHeigth;
            } else {
                nw = preferedWidth;
                nh = image.getHeight() * preferedWidth / image.getWidth();
            }

            return new Rectangle(0, 0, nw, nh);
        } else {
            throw new IllegalArgumentException("Ungülige Parameter: Image(" + image + ") / Breite(" + preferedWidth + ") / Höhe("+preferedHeigth+")");
        }
    }

    /**
     * Ganz einfache Klasse, die dazu dient einen Punkt auf einer Fläche zu beschreiben.
     * @see #x
     * @see #y
     */
    public static class Point {

        /**
         * X-Koordinate des Punktes
         */
        public int x;
        /**
         * Y-Koordinate des Punktes
         */
        public int y;

        /**
         * Erzeugt einen neuen Punkt mit den angegebenen Koordinaten
         * @param x X-Koordinate
         * @param y Y-Koordinate
         */
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }



        @Override
        public String toString() {
            return "X="+x+" Y="+y;
        }
    }

    /**
     * Ganz einfache Klasse, die dazu dient eine rechteckige Fläche zu beschreiben.
     * @see Point
     * @see #width
     * @see #heigth
     */
    public static class Rectangle extends Point {

        /**
         * Breite des Rechtecks
         */
        public final int width;
        /**
         * Höhe des Rechtecks
         */
        public final int heigth;

        /**
         * Erzeugt ein neues Rechteck mit den angegebenen Maßen
         * @param x X-Koordinate
         * @param y Y-Koordinate
         * @param width Breite (width=|width|)
         * @param heigth Höhe (heigth=|heigth|)
         */
        public Rectangle(int x, int y, int width, int heigth) {
            super(x, y);
            this.width = width;
            this.heigth = heigth;
        }

        @Override
        public String toString() {
            return super.toString()+" Width="+width+" Heigth="+heigth;
        }
    }
}
