package imageInfo;

import java.awt.Image;
import java.util.ArrayList;

import javax.xml.bind.annotation.W3CDomHandler;

import gc.L;
import processing.core.PApplet;
import processing.core.PImage;
import setup.Pantalla;

public class HistogramaHSB {

	private int[][] distribucionHSB = new int[3][361];
	private int[][] distribucionHSBAcumulada = new int[3][361];
	private int[][] totalPxHSB = new int[3][];

	private PApplet app;
	private int valorMasAlto;
	private int totalPixels;
	private PImage image;

	private int valorMasAlto() {
		int valorMasAlto = 0;

		for (int i = 0; i < 361; i++) {
			int buffer = 0;

			buffer = distribucionHSB[0][i];
			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}

			buffer = distribucionHSB[1][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
			buffer = distribucionHSB[2][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
		}
		// System.out.println("el valos mas alto es:" + valorMasAlto);
		return valorMasAlto;
	}

	public HistogramaHSB(PImage image) {
		this.app = Pantalla.app;

		this.image = image;
		refrescar();
	}

	public void refrescar() {

		image.loadPixels();

		totalPixels = image.pixels.length;

		app.colorMode(app.HSB, 360, 360, 360);

		for (int i = 0; i < totalPixels; i++) {
			int color = image.pixels[i];
			// canal B
			int valorB = (int) app.brightness(color);
			distribucionHSB[2][valorB]++;
			// canal S
			int valorS = (int) app.saturation(color);
			distribucionHSB[1][valorS]++;
			// canal H
			int valorH = (int) app.hue(color);
			distribucionHSB[0][valorH]++;
		}
		valorMasAlto = valorMasAlto();
	}

	public void cambiarImagen(PImage image) {
		this.image = image;
		refrescar();
	}

	public String[] analisis() {
		float oscuros = 0;
		float medios = 0;
		float altos = 0;
		float masAlto = 0;
		int valorAlto = 0;
		int valorBajo = 0;
		float masBajo = 10000000;
		String exposicion = " ";
		String contraste = "   ";

		oscuros = app.round(oscuros * 100 / totalPixels);
		medios = app.round(medios * 100 / totalPixels);
		altos = app.round(altos * 100 / totalPixels);

		if (oscuros > medios && oscuros > altos) {
			exposicion = "baja";
		} else if (medios > oscuros && medios > altos) {
			exposicion = "normal";

		} else {
			exposicion = "alta";
		}

		float difContraste = valorBajo - valorAlto;
		/// System.out.println(valorAlto + " " + valorBajo);
		if (difContraste < 51) {
			contraste = "muy bajo";

		} else if (difContraste < 51 * 2) {
			contraste = "bajo";

		} else if (difContraste < 51 * 3) {
			contraste = "normal";

		} else if (difContraste < 51 * 4) {
			contraste = "alta";
		} else {
			contraste = "muy Alta";
		}

		String[] resp = { oscuros + "%", medios + "%", altos + "%", masBajo + "/255", masAlto + "/255", exposicion,
				contraste };
		return resp;
	}

	public void curvaH(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando
		int yHs = y + h;

		float acumulado = 0;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 361);
		for (int i = 0; i < 360; i++) {
			// apariencia de las bolas
			if (acumulativo) {
				float valorA = app.map(distribucionHSB[0][i], 0, totalPixels, 0, h);
				float valorB = app.map(distribucionHSB[0][i + 1], 0, totalPixels, 0, h);

				acumulado += valorA;
				app.colorMode(app.HSB, 360, 360, 360);
				L.gradientLine(x + (espacioX * i), yHs - acumulado, x + (espacioX * i), yHs - acumulado - valorB,
						app.color(i, 300, 300), app.color(i + 1, 300, 300));

			} else {
				float valorA = app.map(distribucionHSB[0][i], 0, valorMasAlto, 0, h);
				float valorB = app.map(distribucionHSB[0][i + 1], 0, valorMasAlto, 0, h);
				app.colorMode(app.HSB, 360, 360, 360);
				L.gradientLine(x + (espacioX * i), yHs - valorA, x + (espacioX * i), yHs - valorB,
						app.color(i, 300, 300), app.color(i + 1, 300, 300));
			}
		}
	}

	public void curvaS(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando
		app.colorMode(app.RGB, 255, 255, 255);

		app.stroke(0, 0, 0);
		app.fill(0, 100);

		int yHs = y + h;
		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 361);
		for (int i = 0; i < 361; i++) {
			// apariencia de las bolas
			if (acumulativo) {
				float valorYR = app.map(distribucionHSB[1][i], 0, totalPixels, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYR = app.map(distribucionHSB[1][i], 0, valorMasAlto, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - valorYR);
			}
		}
		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void curvaB(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando

		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		int yHs = y + h;
		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 361);
		for (int i = 0; i < 361; i++) {
			// apariencia de las bolas
			if (acumulativo) {
				float valorYR = app.map(distribucionHSB[2][i], 0, totalPixels, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYR = app.map(distribucionHSB[2][i], 0, valorMasAlto, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - valorYR);
			}
		}
		app.vertex(x + w, y + h);
		app.endShape();
	}
}
