package gc;

import imageInfo.HistogramaRGB;
import imageInfo.HistogramaYxy;
import processing.core.PApplet;
import processing.core.PImage;
import root.Main;

public class Filtro {
	static PApplet app = Main.app;
	static final int AUTOMATIC = 0;

	public static PImage redPatch(PImage original) {
		app.pushStyle();

		PImage image = original.copy();

		int valorMasAlto = 0;
		int valorMasBajo = 255;

		for (int i = 0; i < image.pixels.length; i++) {
			app.colorMode(app.RGB, 255, 255, 255);
			int buffer = (int) app.red(image.pixels[i]);
			// se hará con el brillo.
			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			} else {
				if (buffer < valorMasBajo) {
					valorMasBajo = buffer;
				}
			}
		}
		
		// aquí se confuguran los nuevos valores tanto de los brillos como de
		// los altos

		// valorMasAlto = (int) app.map(app.mouseX, 0, app.width, 255, 1);
		// valorMasBajo = (int) app.map(app.mouseY, 0, app.height, 1, 255);

		// valorMasAlto = app.constrain(valorMasAlto, 0, 255);
		// valorMasBajo = app.constrain(valorMasBajo, 0, 255);

		app.colorMode(app.RGB, 255, 255, 255);

		System.out.println(valorMasBajo + " " + valorMasAlto);

		for (int i = 0; i < image.pixels.length; i++) {
			int colorPixel = image.pixels[i];
			int nR = (int) app.map(app.red(colorPixel), 0, 255, valorMasBajo, valorMasAlto);
			image.pixels[i] = L.changeOnlyRed(colorPixel, nR);
		}

		image.updatePixels();

		app.popStyle();
		return image;
	}

	public static PImage greenPatch(PImage original) {
		app.pushStyle();

		PImage image = original.copy();

		int valorMasAlto = 0;
		int valorMasBajo = 255;

		for (int i = 0; i < image.pixels.length; i++) {
			app.colorMode(app.RGB, 255, 255, 255);
			int buffer = (int) app.green(image.pixels[i]);

			// se hará con el brillo.
			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			} else {
				if (buffer < valorMasBajo) {
					valorMasBajo = buffer;
				}
			}
		}

		// aquí se confuguran los nuevos valores tanto de los brillos como de
		// los altos

		// valorMasAlto = (int) app.map(app.mouseX, 0, app.width, 255, 1);
		// valorMasBajo = (int) app.map(app.mouseY, 0, app.height, 1, 255);

		// valorMasAlto = app.constrain(valorMasAlto, 0, 255);
		// valorMasBajo = app.constrain(valorMasBajo, 0, 255);

		app.colorMode(app.RGB, 255, 255, 255);

		// System.out.println(valorMasBajo + " " + valorMasAlto);
		for (int i = 0; i < image.pixels.length; i++) {
			int colorPixel = image.pixels[i];

			int nG = (int) app.map(app.green(colorPixel), 0, 255, valorMasBajo, valorMasAlto);
			image.pixels[i] = L.changeOnlyGreen(colorPixel, nG);
		}

		image.updatePixels();

		app.popStyle();
		return image;
	}

	public static PImage umbral(PImage image, float r, float g, float b, float umbralR, float umbralG, float umbralB) {
		PImage imageB = image.copy();
		image.loadPixels();
		imageB.loadPixels();

		for (int i = 0; i < image.pixels.length; i++) {
			int colorPx = app.color(image.pixels[i]);

			if (app.red(colorPx) < r + (umbralR / 2) && app.red(colorPx) > r - (umbralR / 2)
					&& app.green(colorPx) < g + (umbralG / 2) && app.green(colorPx) > g - (umbralG / 2)
					&& app.blue(colorPx) < b + umbralB / 2 && app.blue(colorPx) > b - umbralB / 2) {

				imageB.pixels[i] = app.color(255);
			} else {
				imageB.pixels[i] = app.color(0);
			}

		}

		image.updatePixels();
		imageB.updatePixels();
		return imageB;
	}

	public static PImage coordenadasCromaticas(PImage image) {
		PImage imageB = image.copy();
		image.loadPixels();
		imageB.loadPixels();

		for (int i = 0; i < image.pixels.length; i++) {

			int colorPx = imageB.pixels[i];

			float r = app.red(colorPx);
			float g = app.green(colorPx);
			float b = app.blue(colorPx);

			float nR = app.map(r / (r + g + b), 0, 1, 0, 255);
			float nG = app.map(g / (r + g + b), 0, 1, 0, 255);
			float nB = app.map(b / (r + g + b), 0, 1, 0, 255);

			imageB.pixels[i] = app.color(nR, nG, nB);
		}

		imageB.updatePixels();
		image.updatePixels();

		return imageB;
	}

	public static PImage bluePatch(PImage original) {
		app.pushStyle();

		PImage image = original.copy();

		int valorMasAlto = 0;
		int valorMasBajo = 255;

		for (int i = 0; i < image.pixels.length; i++) {
			app.colorMode(app.RGB, 255, 255, 255);
			int buffer = (int) app.blue(image.pixels[i]);
			// se hará con el brillo.
			if (buffer > valorMasAlto) {
				valorMasAlto = buffer;
			} else {
				if (buffer < valorMasBajo) {
					valorMasBajo = buffer;
				}
			}
		}

		// aquí se confuguran los nuevos valores tanto de los brillos como de
		// los altos

		// valorMasAlto = (int) app.map(app.mouseX, 0, app.width, 255, 1);
		// valorMasBajo = (int) app.map(app.mouseY, 0, app.height, 1, 255);

		// valorMasAlto = app.constrain(valorMasAlto, 0, 255);
		// valorMasBajo = app.constrain(valorMasBajo, 0, 255);

		app.colorMode(app.RGB, 255, 255, 255);

		System.out.println(valorMasBajo + " " + valorMasAlto);

		for (int i = 0; i < image.pixels.length; i++) {
			int colorPixel = image.pixels[i];

			int nB = (int) app.map(app.blue(colorPixel), 0, 255, valorMasBajo, valorMasAlto);
			image.pixels[i] = L.changeOnlyBlue(colorPixel, nB);
		}

		image.updatePixels();
		app.popStyle();
		return image;
	}

	private static int[] sumaHistogramas1(int[] primaria, int[] objetivo) {
		int[] nuevosValoresAcumulado = new int[primaria.length];

		for (int i = 0; i < 256; i++) {
			int vP = primaria[i];
			for (int j = 0; j < 256; j++) {
				int vO = objetivo[j];
				// System.out.println(vP + " " + vO);
				if (vO == vP) {
					// System.out.println(j);
					nuevosValoresAcumulado[i] = j;
					break;
				} else if (vO > vP) {
					// System.out.println(j);
					nuevosValoresAcumulado[i] = j - 1;
					break;
				}
			}
		}
		return nuevosValoresAcumulado;
	}

	private static int[] sumaHistogramas(int[] primaria, int[] objetivo) {
		int[] nuevosValoresAcumulado = new int[primaria.length];

		for (int i = 0; i < 256; i++) {
			int vP = primaria[i];

			for (int j = 0; j < 256; j++) {
				int vO = objetivo[j];
				// System.out.println(vP + " " + vO);
				if (vO == vP) {
					// System.out.println(j);
					nuevosValoresAcumulado[i] = j;
					break;
				} else if (vO > vP) {

					// System.out.println(j);
					nuevosValoresAcumulado[i] = j - 1;
					break;
				}
			}
		}
		return nuevosValoresAcumulado;
	}

	public static PImage HistogramMatchingRgb(PImage primaria, PImage objetivo) {

		HistogramaRGB hRGBObjetivo = new HistogramaRGB(objetivo);
		HistogramaRGB hRGBPrimaria = new HistogramaRGB(primaria);

		int[][] rgbObjetivoAcumulado = hRGBObjetivo.getDistribucionRGBAcumulada();
		int[][] rgbPrimariaAcumulado = hRGBPrimaria.getDistribucionRGBAcumulada();

		int nuevosValoresR[] = sumaHistogramas(rgbPrimariaAcumulado[0], rgbObjetivoAcumulado[0]);
		int nuevosValoresg[] = sumaHistogramas(rgbPrimariaAcumulado[1], rgbObjetivoAcumulado[1]);
		int nuevosValoresb[] = sumaHistogramas(rgbPrimariaAcumulado[2], rgbObjetivoAcumulado[2]);

		PImage image = primaria.copy();
		image.loadPixels();
		app.colorMode(app.RGB, 255, 255, 255);

		for (int i = 0; i < image.pixels.length; i++) {
			int r = (int) app.red(image.pixels[i]);
			int g = (int) app.green(image.pixels[i]);
			int b = (int) app.blue(image.pixels[i]);
			// System.out.println(nuevosValoresR[r] + " " + nuevosValoresg[g] +
			// " " + nuevosValoresb[b]);
			image.pixels[i] = app.color(nuevosValoresR[r], nuevosValoresg[g], nuevosValoresb[b]);
		}

		image.updatePixels();

		return image;

	}

	public static PImage HistogramMatchingYxy(PImage primaria, PImage objetivo) {

		HistogramaYxy hYxyObjetivo = new HistogramaYxy(objetivo);
		HistogramaYxy hYxyPrimaria = new HistogramaYxy(primaria);

		int[][] YxyObjetivoAcumulado = hYxyObjetivo.getDistribucionYxyAcumulada();
		int[][] YxyPrimariaAcumulado = hYxyPrimaria.getDistribucionYxyAcumulada();
		float[][] totalPxYxy = hYxyPrimaria.getPxYxy();

		int nuevosValoresY[] = sumaHistogramas(YxyObjetivoAcumulado[0], YxyPrimariaAcumulado[0]);
		int nuevosValoresy[] = sumaHistogramas(YxyObjetivoAcumulado[1], YxyPrimariaAcumulado[1]);
		int nuevosValoresx[] = sumaHistogramas(YxyObjetivoAcumulado[2], YxyPrimariaAcumulado[2]);

		PImage image = primaria.copy();
		image.loadPixels();

		for (int i = 0; i < image.pixels.length; i++) {
			// COMPARACIÓN CANAL Y
			float Y = totalPxYxy[i][0];
			float x = totalPxYxy[i][1];
			float y = totalPxYxy[i][2];
			float nY = (float) nuevosValoresY[(int) Y];
			float nx = (float) ((float) nuevosValoresx[(int) (x * 100)] / (float) 100);
			float ny = (float) ((float) nuevosValoresy[(int) (y * 100)] / (float) 100);
			float[] nYxy = { nY, nx, ny };

			float[] valores = L.XYZtoRGB(L.YxytoXYZ(nYxy));
			// System.out.println(nY + " " + nx + " " + ny);
			image.pixels[i] = app.color(valores[0], valores[1], valores[2]);
		}
		image.updatePixels();
		return image;
	}

	public static PImage HistogramMatchingYxyYintacta(PImage primaria, PImage objetivo) {

		HistogramaYxy hYxyObjetivo = new HistogramaYxy(objetivo);
		HistogramaYxy hYxyPrimaria = new HistogramaYxy(primaria);

		int[][] YxyObjetivoAcumulado = hYxyObjetivo.getDistribucionYxyAcumulada();
		int[][] YxyPrimariaAcumulado = hYxyPrimaria.getDistribucionYxyAcumulada();
		float[][] totalPxYxy = hYxyPrimaria.getPxYxy();

		int nuevosValoresx[] = sumaHistogramas(YxyObjetivoAcumulado[1], YxyPrimariaAcumulado[1]);
		int nuevosValoresy[] = sumaHistogramas(YxyObjetivoAcumulado[2], YxyPrimariaAcumulado[2]);

		PImage image = primaria.copy();
		image.loadPixels();

		for (int i = 0; i < image.pixels.length; i++) {
			// COMPARACIÓN CANAL Y
			float Y = totalPxYxy[i][0];
			float x = totalPxYxy[i][1];
			float y = totalPxYxy[i][2];

			float nY = Y;
			float nx = (float) nuevosValoresx[(int) (x * 100)] / 100;
			float ny = (float) nuevosValoresy[(int) (y * 100)] / 100;
			float[] nYxy = { nY, nx, ny };
			float[] valores = L.XYZtoRGB(L.YxytoXYZ(nYxy));
			// System.out.println(nY + " " + nx + " " + ny);
			app.colorMode(app.RGB, 255, 255, 255);
			image.pixels[i] = app.color(valores[0], valores[1], valores[2]);
		}
		image.updatePixels();
		return image;
	}

	public static PImage HistogramMatchingYxyXYintacta(PImage primaria, PImage objetivo) {

		HistogramaYxy hYxyObjetivo = new HistogramaYxy(objetivo);
		HistogramaYxy hYxyPrimaria = new HistogramaYxy(primaria);

		int[][] YxyObjetivoAcumulado = hYxyObjetivo.getDistribucionYxyAcumulada();
		int[][] YxyPrimariaAcumulado = hYxyPrimaria.getDistribucionYxyAcumulada();
		float[][] totalPxYxy = hYxyPrimaria.getPxYxy();

		int nuevosValoresY[] = sumaHistogramas(YxyObjetivoAcumulado[0], YxyPrimariaAcumulado[0]);

		PImage image = primaria.copy();
		image.loadPixels();

		for (int i = 0; i < image.pixels.length; i++) {
			// COMPARACIÓN CANAL Y
			float Y = totalPxYxy[i][0];
			float x = totalPxYxy[i][1];
			float y = totalPxYxy[i][2];

			float nY = nuevosValoresY[(int) Y];
			float nx = x;
			float ny = y;
			float[] nYxy = { nY, nx, ny };
			float[] valores = L.XYZtoRGB(L.YxytoXYZ(nYxy));
			// System.out.println(nY + " " + nx + " " + ny);
			app.colorMode(app.RGB, 255, 255, 255);
			image.pixels[i] = app.color(valores[0], valores[1], valores[2]);
		}
		image.updatePixels();
		return image;
	}

	public PImage colorTransfer(PImage fuente, PImage objetivo) {

		return null;

	}

	public static PImage RGBequialization(PImage original) {

		PImage image = original.copy();
		HistogramaRGB hrgb = new HistogramaRGB(image);

		int[][] distribucionAcumuladaRgb = hrgb.getDistribucionRGBAcumulada();
		int totalPixels = image.pixels.length;
		int[][] nuevosValoresRGB = new int[3][256];
		for (int i = 0; i < distribucionAcumuladaRgb[0].length; i++) {

			float nR = (float) (distribucionAcumuladaRgb[0][i] / (float) totalPixels) * 255;
			float nG = (float) (distribucionAcumuladaRgb[1][i] / (float) totalPixels) * 255;
			float nB = (float) (distribucionAcumuladaRgb[2][i] / (float) totalPixels) * 255;

			nuevosValoresRGB[0][i] = (int) nR;
			nuevosValoresRGB[1][i] = (int) nG;
			nuevosValoresRGB[2][i] = (int) nB;
		}

		image.loadPixels();
		app.colorMode(app.RGB, 255, 255, 255);
		for (int i = 0; i < image.pixels.length; i++) {
			int r = (int) app.red(image.pixels[i]);
			int g = (int) app.green(image.pixels[i]);
			int b = (int) app.blue(image.pixels[i]);
			image.pixels[i] = app.color(nuevosValoresRGB[0][r], nuevosValoresRGB[1][g], nuevosValoresRGB[2][b]);
		}
		image.updatePixels();

		return image;
	}

	static public PImage filtroMatrix(PImage image, float[][] matrix, int matrixSize) {
		PImage img = image.copy();

		img.loadPixels();
		for (int x = 0; x < img.width; x++) {
			for (int y = 0; y < img.height; y++) {
				int c = convolution(x, y, matrix, matrixSize, img);
				int loc = x + y * img.width;
				img.pixels[loc] = c;
			}
		}
		img.updatePixels();
		return img;
	}

	static private int convolution(int x, int y, float[][] matrix, int matrixsize, PImage img) {
		float rtotal = 0.0f;
		float gtotal = 0.0f;
		float btotal = 0.0f;
		int offset = matrixsize / 2;

		for (int i = 0; i < matrixsize; i++) {
			for (int j = 0; j < matrixsize; j++) {
				int xloc = x + i - offset;
				int yloc = y + j - offset;
				int loc = xloc + img.width * yloc;

				// Make sure we haven't walked off the edge of the pixel array
				// It is often good when looking at neighboring pixels to make
				// sure we have not gone off the edge of the pixel array by
				// accident.
				loc = app.constrain(loc, 0, img.pixels.length - 1);

				// Calculate the convolution
				// We sum all the neighboring pixels multiplied by the values in
				// the convolution matrix.
				rtotal += (app.red(img.pixels[loc]) * matrix[i][j]);
				gtotal += (app.green(img.pixels[loc]) * matrix[i][j]);
				btotal += (app.blue(img.pixels[loc]) * matrix[i][j]);
			}
		}

		// Make sure RGB is within range
		rtotal = app.constrain(rtotal, 0, 255);
		gtotal = app.constrain(gtotal, 0, 255);
		btotal = app.constrain(btotal, 0, 255);

		// Return the resulting color
		return app.color(rtotal, gtotal, btotal);
	}

}
