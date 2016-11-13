package setup;

import java.util.Observer;

/**
 * Esta clase se encarga de tneer todos los metdoso necesarios para una pantalla. cualquiera que herede de esta, se podrá visualizar.
 *todas, puenden observar y ser hilos.
 * @author Personal
 *
 */

public abstract class Pantalla extends Thread implements Observer {

	protected boolean hilo = true;// para utilizarlo en los hilos. en caso de que los quieran usar,

	public abstract void create();

	public abstract void update();

	public abstract void pintar();

	public abstract void dispose();

	public abstract void resum();

	public abstract void mousePressed();

}
