package root;

import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.event.MouseEvent;
import setup.Pantalla;
import setup.ProcessingEvent;

public class Main extends PApplet {
	Logica logica;
	public static PApplet app;

	public static List<ProcessingEvent> processingEvents = Collections
			.synchronizedList(new ArrayList<ProcessingEvent>());

	// metodo para poder exportarse como una aplicaci�n, NO TOCAR
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "root.Main" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

	/**
	 * Configuraciones directas de processing con su lienzo.
	 */

	public void setup() {
		Ani.init(this);
		Pantalla.app = this;
		app = this;
		logica = new Logica();
	}

	public void settings() {
		size(Settings.WIDTH, Settings.HEIGHT, P2D);
		smooth();
	}

	public void draw() {
		logica.pintar();
	}

	public void mousePressed() {
		for (ProcessingEvent p : processingEvents) {
			p.mousePressed();
		}
		logica.mousePressed();
	}

	public void mouseReleased() {
		for (ProcessingEvent p : processingEvents) {
			p.mouseReleased();
		}
		logica.mouseReleased();
	}

	public void mouseDragged() {
		for (ProcessingEvent p : processingEvents) {
			p.mouseDragged();
		}
		logica.mouseDragged();
	}

	public void keyPressed() {
		for (ProcessingEvent p : processingEvents) {
			p.keyPressed();
		}
		logica.keyPressed();
	}

	public static PApplet getApp() {
		return app;
	}
}
