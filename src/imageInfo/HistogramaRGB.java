package imageInfo;

import java.awt.Image;

import processing.core.PApplet;
import processing.core.PImage;
import setup.Pantalla;

public class HistogramaRGB {

	private int[][] distribucionRGB = new int[3][256];
	private int[][] distribucionRGBAcumulada = new int[3][256];

	private PApplet app;
	private int valorMasAlto;
	private int totalPixels;
	private PImage image;

	private int valorMasAlto() {
		for (int i = 0; i < 256; i++) {
			int buffer = 0;

			buffer = distribucionRGB[0][i];
			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
			buffer = distribucionRGB[1][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
			buffer = distribucionRGB[2][i];

			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			}
		}
		return valorMasAlto;
	}

	public HistogramaRGB(PImage image) {
		this.app = Pantalla.app;
		// this.x = x;
		// this.y = y;
		// this.w = w;
		// this.h = h;
		this.image = image;
		refrescarV2();
	}

	public void refrescarV2() {

		image.loadPixels();
		totalPixels = image.pixels.length;
		app.colorMode(app.RGB, 255, 255, 255);

		// se distribuye el histograma normal
		for (int i = 0; i < totalPixels; i++) {
			int color = image.pixels[i];
			// canal R
			int valorR = (int) app.red(color);
			distribucionRGB[0][valorR]++;
			// canal G
			int valorG = (int) app.green(color);
			distribucionRGB[1][valorG]++;
			// canal B
			int valorB = (int) app.blue(color);
			distribucionRGB[2][valorB]++;
		}
		// se distribuye el histograma acumulado
		// se distribuye el histograma normal

		distribucionRGBAcumulada[0][0] += distribucionRGB[0][0];
		distribucionRGBAcumulada[1][0] += distribucionRGB[1][0];
		distribucionRGBAcumulada[2][0] += distribucionRGB[2][0];
		//
		// System.out.println(distribucionRGBAcumulada[0][0] + " " +
		// distribucionRGBAcumulada[1][0] + " "
		// + distribucionRGBAcumulada[2][0]);

		int aR = 0, aG = 0, aB = 0;
		for (int i = 0; i < 256; i++) {
			aR += distribucionRGB[0][i];
			aG += distribucionRGB[1][i];
			aB += distribucionRGB[2][i];
			distribucionRGBAcumulada[0][i] = aR;
			distribucionRGBAcumulada[1][i] = aG;
			distribucionRGBAcumulada[2][i] = aB;
		}
		// System.out.println(totalPixels);
		// se distribuye el histograma acumulado

		valorMasAlto = valorMasAlto();
	}

	public void cambiarImagen(PImage image) {
		this.image = image;
		refrescarV2();
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

	public void curvaR(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando
		app.colorMode(app.RGB, 255, 255, 255);
		app.stroke(255, 0, 0);
		app.fill(255, 0, 0, 100);

		int yHs = y + h;
		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		// hallar el valor a separar en el eje x
		float espacioX = ((float) w / (float) 256);
		for (int i = 0; i < 256; i++) {
			// apariencia de las bolas
			if (acumulativo) {
				float valorYR = app.map(distribucionRGB[0][i], 0, totalPixels, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYR = app.map(distribucionRGB[0][i], 0, valorMasAlto, 0, h);
				acumulado += valorYR;
				app.vertex(x + (espacioX * i), yHs - valorYR);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void curvaG(int x, int y, int w, int h, boolean acumulativo) {
		int yHs = y + h;

		app.colorMode(app.RGB, 255, 255, 255);
		app.fill(0, 255, 0, 100);
		app.stroke(0, 255, 0);

		app.beginShape();
		app.vertex(x, y + h);
		float acumulado = 0;
		float espacioX = ((float) w / (float) 255);
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYG = app.map(distribucionRGB[1][i], 0, totalPixels, 0, h);
				acumulado += valorYG;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYG = app.map(distribucionRGB[1][i], 0, valorMasAlto, 0, h);
				acumulado += valorYG;
				app.vertex(x + (espacioX * i), yHs - valorYG);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public void curvaB(int x, int y, int w, int h, boolean acumulativo) {
		// app.rect(x, y, w, h);// espacio donde se está pintando
		int yHs = y + h;
		// hallar el valor a separar en el eje x
		app.colorMode(app.RGB, 255, 255, 255);

		app.fill(0, 0, 255, 100);
		app.stroke(0, 0, 255);

		app.beginShape();
		app.vertex(x, y + h);

		float acumulado = 0;
		float espacioX = ((float) w / (float) 255);
		for (int i = 0; i < 256; i++) {
			if (acumulativo) {
				float valorYB = app.map(distribucionRGB[2][i], 0, totalPixels, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - acumulado);
			} else {
				float valorYB = app.map(distribucionRGB[2][i], 0, valorMasAlto, 0, h);
				acumulado += valorYB;
				app.vertex(x + (espacioX * i), yHs - valorYB);
			}
		}

		app.vertex(x + w, y + h);
		app.endShape();
	}

	public int[][] getDistribucionRGB() {
		return distribucionRGB;
	}

	public void setDistribucionRGB(int[][] distribucionRGB) {
		this.distribucionRGB = distribucionRGB;
	}

	public int[][] getDistribucionRGBAcumulada() {
		return distribucionRGBAcumulada;
	}

	public void setDistribucionRGBAcumulada(int[][] distribucionRGBAcumulada) {
		this.distribucionRGBAcumulada = distribucionRGBAcumulada;
	}

}
