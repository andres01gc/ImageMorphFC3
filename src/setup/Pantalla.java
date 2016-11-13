package setup;

import java.util.Observer;

import processing.core.PApplet;

/**
 * Esta clase se encarga de tneer todos los metdoso necesarios para una
 * pantalla. cualquiera que herede de esta, se podrá visualizar. todas, puenden
 * observar y ser hilos.
 * 
 * @author Personal
 *
 */

public abstract class Pantalla extends Thread {
	public static PApplet app;
	
	protected boolean vivo = true;

	public abstract void iniciar();

	public abstract void pintar();

	public abstract void finalizar();

	public void mousePressed() {};

	public void mouseDragged() {};

	public void mouseReleased() {};

	public void KeyPressed() {};

}
