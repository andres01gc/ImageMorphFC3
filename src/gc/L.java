package gc;

import processing.core.PApplet;
import processing.core.PImage;
import root.Main;

public class L {
	static PApplet app = Main.app;
	static double[][] matrizXYZ = { { 0.490, 0.310, 0.200 }, { 0.177, 0.813, 0.011 }, { 0.000, 0.010, 0.990 } };

	// metodo escrito por asimes
	// https://forum.processing.org/two/discussion/5620/how-to-draw-a-gradient-colored-line
	public static void gradientLine(float x1, float y1, float x2, float y2, int a, int b) {
		float deltaX = x2 - x1;
		float deltaY = y2 - y1;
		float tStep = (float) (1.0 / app.dist(x1, y1, x2, y2));
		for (float t = 0.0f; t < 1.0; t += tStep) {
			app.stroke(app.lerpColor(a, b, t));
			// app.stroke(0, 0);
			app.point(x1 + t * deltaX, y1 + t * deltaY);
		}
	}

	/**
	 * permite obtener los valores XYZ a partir de los canales rgb
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static double[] rgb2XYZ(float r, float g, float b) {
		/*
		 * Este metodo se debería de hacer automatico para que se pueda
		 * presentar de manera adecuada.
		 */

		double[] rgb = { r, g, b };
		double[] resultado = new double[3];
		// int[][] multiplicacion = new int[matrizXYZ.length][];
		resultado[0] = ((rgb[0] * matrizXYZ[0][0]) + (rgb[1] * matrizXYZ[0][1]) + (rgb[2] * matrizXYZ[0][2]));
		resultado[1] = ((rgb[0] * matrizXYZ[1][0]) + (rgb[1] * matrizXYZ[1][1]) + (rgb[2] * matrizXYZ[1][2]));
		resultado[2] = ((rgb[0] * matrizXYZ[2][0]) + (rgb[1] * matrizXYZ[2][1]) + (rgb[2] * matrizXYZ[2][2]));

		// System.out.println(resultado[0] + " " + resultado[1] + " " +
		// resultado[2]);

		return resultado;

	}

	/**
	 * Permite obtener los valores Yxy a partir de los valores xyz
	 * 
	 * @param xyz
	 * @return
	 */

	public static float[] getYxy(double[] xyz) {
		float Y = (float) xyz[1];// el y del xyz es el mismo que el Y de Yxy
		float x = (float) xyz[0] / (float) (xyz[0] + xyz[1] + xyz[2]);
		float y = (float) xyz[1] / (float) (xyz[0] + xyz[1] + xyz[2]);
		float[] resultado = { Y, x, y };

		// System.out.println(Y + " " + x*100 + " " + y*100);

		// System.out.println((double) 209 / (double) 660);
		return resultado;
	}

	/**
	 * permite obtener los valores Lab a partir de xyz
	 * 
	 * @param xyz
	 * @return
	 */
	public static double[] xyztoLab(double[] xyz) {
		final double E = 0.008856;
		final double K = 903.3;
		double fx, fy, fz;
		double xr = (double) xyz[0] / (double) 95.047;
		double yr = (double) xyz[0] / (double) 100;
		double zr = (double) xyz[0] / (double) 108.883;
		if (xr > E) {
			fx = (double) Math.pow(xr, 1.0 / 3.0);
		} else {
			fx = (double) (K * xr + 16) / (double) 116;
		}
		if (yr > E) {
			fy = (double) Math.pow(yr, 1.0 / 3.0);
		} else {
			fy = (double) (K * yr + 16) / (double) 116;
		}
		if (zr > E) {
			fz = (double) Math.pow(zr, (double) 1.0 / (double) 3.0);
		} else {
			fz = (double) (K * zr + 16.0) / (double) 116;
		}
		double[] lab = { fx, fy, fz };
		// System.out.println(fx + " " + fy + " " + fz);
		return lab;
	}

	/**
	 * permite cambiar los valores solo del canal Red
	 * 
	 * @param color
	 * @param valorR
	 * @return
	 */
	public static int changeOnlyRed(int color, int valorR) {
		// app.pushStyle();
		app.colorMode(app.RGB, 255, 255, 255);
		float g = app.green(color);
		float b = app.blue(color);
		int nC = app.color(valorR, g, b);
		// app.popStyle();
		return nC;
	}

	/**
	 * permite cambiar solo los valores del canal del Blue
	 * 
	 * @param color
	 * @param valorB
	 * @return
	 */
	public static int changeOnlyBlue(int color, int valorB) {
		// app.pushStyle();
		app.colorMode(app.RGB, 255, 255, 255);
		float r = app.red(color);
		float g = app.green(color);
		int nC = app.color(r, g, valorB);
		// app.popStyle();
		return nC;
	}

	/**
	 * Permite cambiar solo los valor del canal Green
	 * 
	 * @param color
	 * @param valorG
	 * @return
	 */
	public static int changeOnlyGreen(int color, int valorG) {
		app.colorMode(app.RGB, 255, 255, 255);
		float r = app.red(color);
		float b = app.blue(color);
		int nC = app.color(r, valorG, b);
		return nC;
	}

	/**
	 * Aplica filtro whitePatch a la imagen
	 * 
	 * @param original
	 * @return
	 */
	public static PImage whitePatch(PImage original) {
		PImage image = original.copy();
		app.colorMode(app.RGB, 255, 255, 255);

		float[] vMasAlto = new float[3];
		for (int i = 0; i < image.pixels.length; i++) {
			int colorPx = image.pixels[i];
			float r = app.red(colorPx);
			float g = app.green(colorPx);
			float b = app.blue(colorPx);
			float[] bufferCanales = { r, g, b };

			for (int j = 0; j < bufferCanales.length; j++) {

				/*
				 * si el valor que está en e buffer es mayor que el valor
				 * vMasAlto, entonces este sera remplazo por aque mas alto
				 */
				if (bufferCanales[j] > vMasAlto[j]) {
					vMasAlto[j] = bufferCanales[j];
				}
			}
		}

		app.colorMode(app.RGB, 255, 255, 255);

		app.loadPixels();
		for (int i = 0; i < image.pixels.length; i++) {
			int colorPx = image.pixels[i];
			float r = app.red(colorPx);
			float g = app.green(colorPx);
			float b = app.blue(colorPx);
			float nR = r * 255 / vMasAlto[0];
			float nG = g * 255 / vMasAlto[1];
			float nB = b * 255 / vMasAlto[2];
			image.pixels[i] = app.color(nR, nG, nB);
		}
		image.updatePixels();

		System.out.println("se aplicó filtro white patch");
		return image;
	}

	/**
	 * /** permite variar la luminisodidad minima y minima de la imagen a partir
	 * de los valores minimos y minimos que se ingresen
	 * 
	 * @param original
	 * @param valorMasBajo
	 * @param valorMasAlto
	 * @return
	 */
	public static PImage ModifyBrightness(PImage original, int valorMasBajo, int valorMasAlto) {
		PImage image = original.copy();
		int vMasAlto = 0;
		int vMasBajo = 255;
		for (int i = 0; i < image.pixels.length; i++) {
			app.colorMode(app.HSB, 360, 255, 255);
			int buffer = (int) app.brightness(image.pixels[i]);
			// se hará con el brillo.
			if (buffer > vMasAlto) {
				vMasAlto = buffer;
			} else {
				if (buffer < vMasBajo) {
					vMasBajo = buffer;
				}
			}
		}

		// aquí se confuguran los nuevos valores tanto de los brillos como de
		// los altos

		// valorMasAlto -= app.map(app.mouseX, 0, app.width, 0, 255);
		// valorMasBajo += app.map(app.mouseY, 0, app.height, 0, 255);

		for (int i = 0; i < image.pixels.length; i++) {
			int colorPixel = image.pixels[i];
			int newBright = (int) app.map(app.brightness(colorPixel), vMasBajo, vMasAlto, valorMasBajo, valorMasAlto);
			image.pixels[i] = app.color(app.hue(colorPixel), app.saturation(colorPixel), newBright);
		}

		image.updatePixels();
		app.colorMode(app.RGB, 255, 255, 255);
		return image;
	}

	public static double[] valoresCromaticiadadCIE(double[] xyz) {
		double x = xyz[0] / (xyz[0] + xyz[1] + xyz[2]);
		double y = xyz[1] / (xyz[0] + xyz[1] + xyz[2]);
		double z = xyz[2] / (xyz[0] + xyz[1] + xyz[2]);
		double[] resultado = { x, y, z };
		// System.out.println(x + y + z);
		return resultado;
	}

	public void printMouse() {
		System.out.println("mouseX:" + app.mouseX + "  mouseY:" + +app.mouseY);
	}

	public PImage colorTransfer(PImage Target, PImage source) {
		return null;

	}

	public static float[] YxytoXYZ(float[] Yxy) {
		float X = (float) (Yxy[1] * Yxy[0]) / (float) Yxy[2];
		float Y = Yxy[0];
		float Z = (float) ((1 - Yxy[1] - Yxy[2]) * Yxy[0]) / (float) Yxy[2];
		float[] XYZ = { X, Y, Z };
		return XYZ;
	}

	public static float[] YxytoXYZ(int[] Yxy) {
		float X = (float) (Yxy[1] * Yxy[0]) / (float) Yxy[2];
		float Y = Yxy[0];
		float Z = (float) ((1 - Yxy[1] - Yxy[2]) * Yxy[0]) / (float) Yxy[2];
		float[] XYZ = { X, Y, Z };
		return XYZ;
	}

	public static float[] XYZtoRGB(float[] XYZ) {
		float x = XYZ[0];
		float y = XYZ[1];
		float z = XYZ[2];

		double r = r = 2.5623 * x + (-1.1661) * y + (-0.3962) * z;
		double g = g = (-1.0215) * x + 1.9778 * y + 0.0437 * z;
		double b = b = 0.0752 * x + (-0.2562) * y + 1.1810 * z;

		float[] RGB = { (float) r, (float) g, (float) b };
		return RGB;
	}

	public static float[] Lab2XYZ(double[] Lab) {
		double var_Y = (double) (Lab[0] + 16) / (double) 116;
		double var_X = (double) Lab[1] / (double) (500 + var_Y);
		double var_Z = (double) var_Y - (double) (Lab[2] / 200);

		if (Math.pow(var_Y, 3) > 0.008856) {
			var_Y = (double) Math.pow(var_Y, 3);
		} else {
			var_Y = (double) ((var_Y - 16 / 116) / (double) 7.787);
		}

		if (Math.pow(var_X, 3) > 0.008856) {
			var_X = (double) Math.pow(var_X, 3);
		} else {
			var_X = (double) ((var_X - 16 / 116) / (double) 7.787);
		}

		if ((double) Math.pow(var_Z, 3) > 0.008856) {
			var_Z = (double) Math.pow(var_Z, 3);
		} else {
			var_Z = (double) ((var_Z - 16 / 116) / 7.787);
		}

		float X = (float) (95.047 * var_X); // ref_X = 95.047 Observer= 2°,
											// Illuminant= D65
		float Y = (float) (100.000 * var_Y); // ref_Y = 100.000
		float Z = (float) (108.883 * var_Z); // ref_Z = 108.883
		System.out.println(X + " " + Y + " " + Z);
		float[] XYZ = { X, Y, Z };
		return XYZ;

	}

	public static void pintarPosMouse() {
		System.out.println("mouse X: " + app.mouseX + "  mouse Y: " + app.mouseY);

	}

}
