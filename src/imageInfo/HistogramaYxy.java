package imageInfo;

import java.awt.Image;

import gc.L;
import processing.core.PApplet;
import processing.core.PImage;
import setup.Pantalla;

public class HistogramaYxy {

	/*
	 * puedo tener un arreglo del tamaño de la cantidad de pixeles cada pixel
	 * tiene RGB, HSB,XYZ, Lab o Yxy,
	 * 
	 * además debo hacer una distribución sobre la cantidad de valores que ese
	 * repiten en cada uno. así como con los RGB
	 * 
	 */

	private PImage image;
	private int x;
	private int y;
	private int w;
	private int h;
	private int valorMasAlto;

	private int[][] distribucionYxy = new int[3][256];
	private int[][] distribucionYxyAcumulada = new int[3][256];;
	private float[][] PxYxy;
	private PApplet app;
	private int totalPixels;

	public HistogramaYxy(PImage image) {
		this.app = Pantalla.app;
		this.image = image;
		refrescarYxy();
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
			buffer = distribucionYxy[0][i];
			if (buffer > valorMasAlto) {
				valorMasAlto = (int) buffer;
			}
			buffer = distribucionYxy[1][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = (int) buffer;
			}
			buffer = distribucionYxy[2][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = (int) buffer;
			}
		}
		return valorMasAlto;
	}

	public void refrescarYxy() {
		image.loadPixels();
		PxYxy = new float[image.pixels.length][];
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

			PxYxy[i] = L.getYxy(XYZ);

		}
		distribuirXxy();
	}

	public void distribuirXxy() {
		// para los valores xy al ser tan pequeños los multiplicaré por 100
		// PxXYZ

		for (int i = 0; i < PxYxy.length; i++) {
			int color = image.pixels[i];
			distribucionYxy[0][(int) PxYxy[i][0]]++;
			distribucionYxy[1][(int) (PxYxy[i][1] * 100)]++;
			distribucionYxy[2][(int) (PxYxy[i][2] * 100)]++;
		}

		int aY = 0, ax = 0, ay = 0;

		for (int i = 0; i < 256; i++) {
			aY += distribucionYxy[0][i];
			ax += distribucionYxy[1][i];
			ay += distribucionYxy[2][i];

			distribucionYxyAcumulada[0][i] = aY;
			distribucionYxyAcumulada[1][i] = ax;
			distribucionYxyAcumulada[2][i] = ay;
			// System.out.println(aY + " " + ax + " " + ay);
		}

		valorMasAlto = valorMasAlto();

	}

	public void cambiarImagen(PImage image) {
		this.image = image;
		refrescarYxy();
	}

	public void distribucionY(int x, int y, int w, int h, boolean acumulativo) {
		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		// app.rect(x, y, w, h);// espacio donde se está pintando
		int yHs = y + h;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 255f);

		app.beginShape();
		app.vertex(x, y + h);

		float acumulado = 0;
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYB = app.map(distribucionYxy[0][i], 0, totalPixels, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYB = app.map(distribucionYxy[0][i], 0, valorMasAlto, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - valorYB);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void distribucionx(int x, int y, int w, int h, boolean acumulativo) {
		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		// app.rect(x, y, w, h);// espacio donde se está pintando
		int yHs = y + h;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 255f);
		app.beginShape();
		app.vertex(x, y + h);

		float acumulado = 0;
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYB = app.map(distribucionYxy[1][i], 0, totalPixels, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYB = app.map(distribucionYxy[1][i], 0, valorMasAlto, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - valorYB);
			}
		}
		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void distribuciony(int x, int y, int w, int h, boolean acumulativo) {
		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(0, 0, 0);
		app.fill(0, 100);

		// app.rect(x, y, w, h);// espacio donde se está pintando
		int yHs = y + h;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 255f);
		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYB = app.map(distribucionYxy[2][i], 0, totalPixels, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYB = app.map(distribucionYxy[2][i], 0, valorMasAlto, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - valorYB);
			}
		}
		app.vertex(x + w, y + h);
		app.endShape();
	}

	public int[][] getDistribucionYxy() {
		return distribucionYxy;
	}

	public void setDistribucionYxy(int[][] distribucionYxy) {
		this.distribucionYxy = distribucionYxy;
	}

	public int[][] getDistribucionYxyAcumulada() {
		return distribucionYxyAcumulada;
	}

	public void setDistribucionYxyAcumulada(int[][] distribucionYxyAcumulada) {
		this.distribucionYxyAcumulada = distribucionYxyAcumulada;
	}

	public float[][] getPxYxy() {
		return PxYxy;
	}

	public void setPxYxy(float[][] pxYxy) {
		PxYxy = pxYxy;
	}
}
