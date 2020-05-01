/** Mandelbrot Fractal Visulaizer by Amogh Rijal 2020 May **/

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

public class FractalExplorer extends JFrame {

  static final int WIDTH = 600;
  static final int HEIGHT = 600;
  static final int MAX_ITERATIONS = 50;
  static final double DEFAULT_ZOOM = 100.0;
  static final double DEFAULT_TOP_LEFT_X = -3.0;
  static final double DEFAULT_TOP_LEFT_Y = +3.0;

  double zoomFactor = DEFAULT_ZOOM;
  double topLeftX = DEFAULT_TOP_LEFT_X;
  double topLeftY = DEFAULT_TOP_LEFT_Y;


  Canvas canvas;
  BufferedImage fractalImage;

  public FractalExplorer() {
    setInitialGUIProperties();
    addCanvas();
    this.setVisible(true);
    updateFractal();
  }

  public void updateFractal() {
    for (int x=0; x < WIDTH; x++) {
      for (int y=0; y < HEIGHT; y++){
        double c_r = getXPos(x); // real values on x-axis
        double c_i = getYPos(y); // imaginary values on y-axis

        int iterCount = computeIterations(c_r, c_i);
        int pixelColor = putColor(iterCount);
        fractalImage.setRGB(x,y, pixelColor);
      }
    }

    canvas.repaint();
  }

  private int putColor(int iterCount) {
    if (iterCount == MAX_ITERATIONS) {
      return Color.BLACK.getRGB();
    }

    return Color.BLUE.getRGB();
  }

  public double getXPos(double x) {
    return x/zoomFactor + topLeftX;
  }

  public double getYPos(double y) {
    return y/zoomFactor - topLeftY;
  }

  private void addCanvas() {
    canvas = new Canvas();
    fractalImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    canvas.setVisible(true);
    this.add(canvas, BorderLayout.CENTER);
  }

  private int computeIterations(double c_r, double c_i) {
    /*

		Let c = c_r + c_i
		Let z = z_r + z_i

		z' = z*z + c
		   = (z_r + z_i)(z_r + z_i) + c_r + c_i
			 = z_r² + 2*z_r*z_i - z_i² + c_r + c_i

			 z_r' = z_r² - z_i² + c_r
			 z_i' = 2*z_i*z_r + c_i


    formula => (√(z_r² + z_i²) <= 2.0 for N iterations) =>   if less than two for N iterations the point lies in Mandelbrot set

		*/

    double z_r = 0.0;
    double z_i = 0.0;
    int count = 0;

    while ((count < MAX_ITERATIONS) & (z_r*z_r + z_i*z_i <= 4)) {
      double z_r_tmp = z_r;
      z_r = z_r * z_r - z_i * z_i + c_r;
      z_i = 2*z_i*z_r_tmp + c_i;
      count++;
    }

    return count;
}

  public void setInitialGUIProperties() {
    this.setTitle("Fractal Explorer");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(WIDTH, HEIGHT);
    this.setResizable(false);
    this.setLocationRelativeTo(null);

  }

  public static void main (String[] args) {
    new FractalExplorer();
  }

  private class Canvas extends JPanel {
    @Override public Dimension getPreferredSize() {
			return new Dimension(WIDTH, HEIGHT);
		}
    @Override public void paintComponent(Graphics drawingObj) {
      drawingObj.drawImage(fractalImage, 0, 0, null);
    }
  }
}
