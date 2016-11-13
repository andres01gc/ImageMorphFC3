package setup;

public class ScreenManager {

	private static Pantalla currentScreen;
//esta clase se encarga de administrar la pantalla actual.
	public static void setScreen(Pantalla screen) {
		if (currentScreen != null)
			currentScreen.dispose();//mata la anterior.

		currentScreen = screen;//inicia la nueva
		currentScreen.create();
	}
	
	public static Pantalla getCurrentScreen() {
		return currentScreen;
	}
}
