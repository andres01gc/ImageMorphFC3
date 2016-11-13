package root;

import info.Info;
import setup.AdministradorPantalla;

public class Logica {

	Logica() {
		Info.getInstance();
		AdministradorPantalla.cambiarPantalla(Settings.inicial);
	}

	public void pintar() {
		AdministradorPantalla.getCurrentScreen().pintar();
	}

	public void mousePressed() {
		AdministradorPantalla.getCurrentScreen().mousePressed();
	}

	public void mouseDragged() {
		AdministradorPantalla.getCurrentScreen().mouseDragged();
	}

	public void mouseReleased() {
		AdministradorPantalla.getCurrentScreen().mouseReleased();
	}

	public void keyPressed() {
		AdministradorPantalla.getCurrentScreen().KeyPressed();
	}
}
