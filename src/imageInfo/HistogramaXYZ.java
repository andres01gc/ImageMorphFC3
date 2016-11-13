package imageInfo;

import java.awt.Image;

import gc.L;
import processing.core.PApplet;
import processing.core.PImage;
import setup.Pantalla;

public class HistogramaXYZ {

	/*
	 * puedo tener un arreglo del tamaño de la cantidad de pixeles cada pixel
	 * tiene RGB, HSB,XYZ, Lab o Yxy,
	 * 
	 * además debo hacer una distribución sobre la cantidad de valores que ese
	 * repiten en cada uno. así como con los RGB
	 * 
	 */

	private double[][] PxXYZ;

	private PApplet app;
	private int totalPixels;

	private PImage image;

	private float[][] totalPxRGB;
	private int valorMasAlto;

	private int[][] distribucionXYZ = new int[3][];

	public HistogramaXYZ(PImage image) {
		this.app = Pantalla.app;
		this.image = image;
		refrescarXYZ();
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
		for (int i = 0; i < 256; i++) {
			int buffer = 0;
			buffer = distribucionXYZ[0][i];
			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
			buffer = distribucionXYZ[1][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
			buffer = distribucionXYZ[2][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
		}
		// System.out.println(valorMasAlto);

		return valorMasAlto;
	}

	public double[] xyztoLab(double[] xyz) {
		final double E = 0.008856;
		final double K = 903.3;
		double fx, fy, fz;

		double xr = xyz[0] / 95.047;
		double yr = xyz[0] / 100;
		double zr = xyz[0] / 108.883;

		if (xr > E) {
			fx = Math.pow(xr, 1.0 / 3.0);
		} else {
			fx = (K * xr + 16) / 116;
		}

		if (yr > E) {
			fy = Math.pow(yr, 1.0 / 3.0);
		} else {
			fy = (K * yr + 16) / 116;
		}

		if (zr > E) {
			fz = Math.pow(zr, 1.0 / 3.0);
		} else {
			fz = (K * zr + 16) / 116;
		}

		double[] lab = { fx, fy, fz };
		return lab;
	}

	public void refrescarXYZ() {

		image.loadPixels();

		PxXYZ = new double[image.pixels.length][];

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

			double[] XYZ = L.rgb2XYZ(valorR, valorG, valorB);
			// guardo todos los pixeles con información XYZ
			PxXYZ[i] = XYZ;
		}
		image.updatePixels();

		distribuirXYZ();
		valorMasAlto = valorMasAlto();
	}

	public void distribuirXYZ() {

		// PxXYZ
		distribucionXYZ[0] = new int[256];
		distribucionXYZ[1] = new int[256];
		distribucionXYZ[2] = new int[256];

		for (int i = 0; i < PxXYZ.length; i++) {
			distribucionXYZ[0][(int) PxXYZ[i][0]]++;
			distribucionXYZ[1][(int) PxXYZ[i][1]]++;
			distribucionXYZ[2][(int) PxXYZ[i][2]]++;
			// System.out.println((int) PxXYZ[i][0] + " " + (int) PxXYZ[i][1] +
			// " " + (int) PxXYZ[i][2]);
		} //
		valorMasAlto = valorMasAlto();
	}

	public void cambiarImagen(PImage image) {
		this.image = image;
		refrescarXYZ();
	}

	public void distribucionX(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando

		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		int yHs = y + h;
		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 256);
		for (int i = 0; i < 256; i++) {
			// apariencia de las bolas
			if (acumulativo) {
				float valorYR = app.map(distribucionXYZ[0][i], 0, totalPixels, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYR = app.map(distribucionXYZ[0][i], 0, valorMasAlto, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - valorYR);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void distribucionY(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando

		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		int yHs = y + h;
		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 256);
		for (int i = 0; i < 256; i++) {
			// apariencia de las bolas
			if (acumulativo) {
				float valorYR = app.map(distribucionXYZ[1][i], 0, totalPixels, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYR = app.map(distribucionXYZ[1][i], 0, valorMasAlto, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - valorYR);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void distribucionZ(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando

		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);


		int yHs = y + h;
		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 256);
		for (int i = 0; i < 256; i++) {
			// apariencia de las bolas
			if (acumulativo) {
				float valorYR = app.map(distribucionXYZ[2][i], 0, totalPixels, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYR = app.map(distribucionXYZ[2][i], 0, valorMasAlto, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - valorYR);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

}
