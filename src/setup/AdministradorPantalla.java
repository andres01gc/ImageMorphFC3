package setup;

public class AdministradorPantalla {
	private static Pantalla pantallaVisible;

	// esta clase se encarga de administrar la pantalla actual.
	public static void cambiarPantalla(Pantalla pantalla) {
		if (pantallaVisible != null)
			pantallaVisible.finalizar();// mata la anterior.

		pantallaVisible = pantalla;
		pantallaVisible.iniciar();// inicia la nueva
	}

	public static Pantalla getCurrentScreen() {
		return pantallaVisible;
	}
}
