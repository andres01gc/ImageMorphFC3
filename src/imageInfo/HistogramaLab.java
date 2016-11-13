package imageInfo;

import java.awt.Image;

import gc.L;
import processing.core.PApplet;
import processing.core.PImage;
import setup.Pantalla;

public class HistogramaLab {

	/*
	 * puedo tener un arreglo del tamaño de la cantidad de pixeles cada pixel
	 * tiene RGB, HSB,XYZ, Lab o Yxy,
	 * 
	 * además debo hacer una distribución sobre la cantidad de valores que ese
	 * repiten en cada uno. así como con los RGB
	 * 
	 */

	private PImage image;

	private int valorMasAlto;

	private int[][] distribucionLab = new int[3][256];

	private double[][] PxLab;

	private PApplet app;
	private int totalPixels;

	public HistogramaLab(PImage image) {
		this.app = Pantalla.app;
		this.image = image;
		refrescarLab();
	}

	public double[] getYxy(double[] xyz) {
		double Y = xyz[1];// el y del xyz es el mismo que el Y de Yxy
		double x = xyz[0] / (xyz[0] + xyz[1] + xyz[2]);
		double y = xyz[1] / (xyz[0] + xyz[1] + xyz[2]);
		double[] resultado = { Y, x, y };
		return resultado;
	}

	public double[] valoresCromaticiadadCIE(double[] xyz) {
		double x = xyz[0] / (xyz[0] + xyz[1] + xyz[2]);
		double y = xyz[1] / (xyz[0] + xyz[1] + xyz[2]);
		double z = xyz[2] / (xyz[0] + xyz[1] + xyz[2]);
		double[] resultado = { x, y, z };
		// System.out.println(x + y + z);
		return resultado;
	}

	private int valorMasAlto() {
		int valorMasAlto = 0;
		for (int i = 0; i < 255; i++) {
			double buffer = 0;
			buffer = distribucionLab[0][i];
			if (buffer > valorMasAlto) {
				valorMasAlto = (int) buffer;
			}
			buffer = distribucionLab[1][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = (int) buffer;
			}
			buffer = distribucionLab[2][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = (int) buffer;
			}
		}

		return valorMasAlto;
	}

	public void refrescarLab() {
		image.loadPixels();
		PxLab = new double[image.pixels.length][];
		totalPixels = image.pixels.length;
		app.colorMode(app.RGB, 255, 255, 255);

		for (int i = 0; i < totalPixels; i++) {
			int color = image.pixels[i];

			// canal B
			int valorB = (int) app.blue(color);
			// canal G
			int valorG = (int) app.green(color);
			// canal R
			int valorR = (int) app.red(color);

			int[] rgb = { valorR, valorG, valorB };

			double[] XYZ = L.rgb2XYZ(valorR, valorG, valorB);
			// guardo todos los pixeles con información XYZ
			PxLab[i] = L.xyztoLab(XYZ);

		}
		distribuirLab();
	}

	public void distribuirLab() {
		// para los valores Lab al ser tan pequeños los multiplicaré por 100
		// PxXYZ
		for (int i = 0; i < PxLab.length; i++) {
			int color = image.pixels[i];
			distribucionLab[0][(int) (PxLab[i][0] * 100)]++;
			distribucionLab[1][(int) (PxLab[i][1] * 100)]++;
			distribucionLab[2][(int) (PxLab[i][2] * 100)]++;
		}
		valorMasAlto = valorMasAlto();
	}

	public void cambiarImagen(PImage image) {
		this.image = image;
		refrescarLab();
	}

	public void distribucionL(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando
		int yHs = y + h;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 255f);

		// apariencia cuerva
		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		app.beginShape();
		app.vertex(x, y + h);

		float acumulado = 0;
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYB = app.map(distribucionLab[0][i], 0, totalPixels, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYB = app.map(distribucionLab[0][i], 0, valorMasAlto, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - valorYB);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void distribuciona(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando
		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		int yHs = y + h;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 255f);

		app.beginShape();
		app.vertex(x, y + h);

		float acumulado = 0;
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYB = app.map(distribucionLab[1][i], 0, totalPixels, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYB = app.map(distribucionLab[1][i], 0, valorMasAlto, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - valorYB);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void distribucionb(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando

		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		int yHs = y + h;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 255f);

		app.beginShape();
		app.vertex(x, y + h);

		float acumulado = 0;
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYB = app.map(distribucionLab[2][i], 0, totalPixels, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYB = app.map(distribucionLab[2][i], 0, valorMasAlto, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - valorYB);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

}
