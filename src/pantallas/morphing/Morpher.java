package pantallas.morphing;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import root.Main;
import triangulate.Pair;
import triangulate.Triangle;
import triangulate.TrianglePair;
import triangulate.Triangulate;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by andre on 11/12/2016.
 */
public class Morpher {
PApplet app= Main.getApp();


    // A list of edges (each one is a pair of PVectors, one on each image)
    ArrayList<Pair> pairs = new ArrayList<Pair>();
    // A list of triangles (each one is a pair of Triangles, one on each image)
    ArrayList<TrianglePair> tiles;

    // The two images
    PImage imgA;
    PImage imgB;

    Morpher(PImage imgA_, PImage imgB_) {
        imgA = imgA_;
        imgB = imgB_;

        // Start off with some boring default points along the edges of the image
        pairs.add(new Pair(0, 0));
        pairs.add(new Pair(imgA.width/2, 0));
        pairs.add(new Pair(imgA.width, 0));
        pairs.add(new Pair(0, imgA.height/2));
        pairs.add(new Pair(imgA.width, imgA.height/2));
        pairs.add(new Pair(0, imgA.height));
        pairs.add(new Pair(imgA.width/2, imgA.height));
        pairs.add(new Pair(imgA.width, imgA.height));
        makeTriangles();
    }

    // Add a pair of points, one on each image
    void addPair(PVector a, PVector b) {
        pairs.add(new Pair(a.copy(), b.copy()));
        makeTriangles();
    }

    // Create an array of triangle pairs
    void makeTriangles() {
        tiles = Triangulate.triangulatePairs(pairs);
    }

    // Here's how we draw an image
    void displayImageA() {
        // Look at every triangle
        for (TrianglePair t : tiles) {
            app.noTint();
            app.  noStroke();
            app. noFill();
            app. textureMode(app.IMAGE);
            // Draw as a shape with three points and textured from the image
            app. beginShape();
            app. texture(imgA);
            // This is awkard, but getting point "a" from each point of the triangle
            PVector a = t.p1.a;
            PVector b = t.p2.a;
            PVector c = t.p3.a;
            app. vertex(a.x, a.y, a.x, a.y);
            app.vertex(b.x, b.y, b.x, b.y);
            app.vertex(c.x, c.y, c.x, c.y);
            app. endShape();
        }
    }

    // Same method for drawing image B
    void displayImageB() {
        for (TrianglePair t : tiles) {
            app.noTint();
            app. noStroke();
            app. noFill();
            app. textureMode(app.IMAGE);
            app. beginShape();
            app. texture(imgB);
            PVector a = t.p1.b;
            PVector b = t.p2.b;
            PVector c = t.p3.b;
            app.vertex(a.x, a.y, a.x, a.y);
            app.vertex(b.x, b.y, b.x, b.y);
            app.vertex(c.x, c.y, c.x, c.y);
            app.endShape();
        }
    }


    // In case we want to see the triangles themselves
    void displayTrianglesA() {
        for (TrianglePair t : tiles) {
            app.stroke(255);
            app.strokeWeight(1);
            app.noFill();
            app.beginShape();
            PVector a = t.p1.a;
            PVector b = t.p2.a;
            PVector c = t.p3.a;
            app.vertex(a.x, a.y);
            app.vertex(b.x, b.y);
            app.vertex(c.x, c.y);
            app.endShape(app.CLOSE);
        }
    }

    void displayTrianglesB() {
        for (TrianglePair t : tiles) {
            app.stroke(255);
            app.strokeWeight(1);
            app.noFill();
            app.beginShape();
            PVector a = t.p1.b;
            PVector b = t.p2.b;
            PVector c = t.p3.b;
            app.vertex(a.x, a.y);
            app.vertex(b.x, b.y);
            app.vertex(c.x, c.y);
            app.endShape(app.CLOSE);
        }
    }


    // Ok here is hte harder one, we're going to display the morphed image
    void drawMorph(float amt) {

        // For every triangle
        for (int i = 0; i < tiles.size(); i++) {

            // Let's get the pair
            TrianglePair tp = tiles.get(i);
            // We make a new triangle which interpolates
            Triangle t = tp.mix(amt);

            // Draw the first image
            app.tint(255);
            // stroke(255); // show triangles
            app.noStroke();
            app.noFill();
            app.textureMode(app.IMAGE);
            app.beginShape();
            app.texture(imgA);
            // Use morphed triangle with corresponding texture points on original triangle
           app.vertex(t.p1.x, t.p1.y, tp.p1.a.x, tp.p1.a.y);
           app.vertex(t.p2.x, t.p2.y, tp.p2.a.x, tp.p2.a.y);
           app.vertex(t.p3.x, t.p3.y, tp.p3.a.x, tp.p3.a.y);
           app.endShape();

            // Draw the second image blended with first
            app.noStroke();
            app.noFill();
            app.tint(255, amt*255);
            //tint(255,255); // Try showing ImageB always
            //stroke(255);   // Try showing show triangles
            app.textureMode(app.IMAGE);
            app.beginShape();
            app.texture(imgB);
            // Use morphed triangle with corresponding texture points on original triangle
            app.vertex(t.p1.x, t.p1.y, tp.p1.b.x, tp.p1.b.y);
            app.vertex(t.p2.x, t.p2.y, tp.p2.b.x, tp.p2.b.y);
            app.vertex(t.p3.x, t.p3.y, tp.p3.b.x, tp.p3.b.y);
            app.endShape();
        }
    }


    void savePoints() {
        PrintWriter pw = app.createWriter("data/points.txt");
        for (Pair p : pairs) {
            String s = p.a.x + "," + p.a.y + "," + p.b.x + "," + p.b.y;
            pw.println(s);
        }
        pw.flush();
        pw.close();
    }

    void loadPoints() {
        pairs.clear();
        String[] lines = app.loadStrings("data/points.txt");
        for (int i = 0; i < lines.length; i++) {
            float[] vals = PApplet.parseFloat( lines[i].split(","));
            addPair(new PVector(vals[0], vals[1]), new PVector(vals[2], vals[3]));
        }
    }
}
