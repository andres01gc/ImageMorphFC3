package pantallas.morphing;

import processing.core.PImage;
import setup.Pantalla;

public class Inicio extends Pantalla {


    // Two images
    PImage a;
    PImage b;

    // A Morphing object
	Morpher morph;


    public void iniciar() {
        a = app.loadImage("../data/a.jpg");
        b = app.loadImage("../data/b.jpg");

        morph= new Morpher(a,b);
    }

    public void pintar() {
        //app.image(a, 0, 0);
       // app.image(b, 600, 0);
        morph.drawMorph(.5f);

    }

    public void finalizar() {

    }
}
