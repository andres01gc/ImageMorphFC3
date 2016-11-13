package setup;

import processing.event.MouseEvent;

public interface ProcessingEvent {
	
	static boolean activar = false;

	public void mousePressed();

	public void mouseReleased();

	public void mouseDragged();

	public void keyPressed();

	public void mouseWheel(MouseEvent event);

}
