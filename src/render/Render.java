package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Render {
	
	private BufferedImage main;
	private int width, height;

	public Render(Dimension dimension) {
		width = dimension.width;
		height = dimension.height;
		main = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	

	public Image getImage() {
		return main;
	}



	public void drawDebugTiles(int[][] base) {
		
	}
	
	public void clear() {
		Graphics2D g = (Graphics2D) main.getGraphics();
		g.setColor(new Color(200,220,255));
		g.fillRect(0, 0, width, height);
		g.dispose();

	}


	public void drawImage(BufferedImage img, int x, int y, double scale) {
		Graphics2D g = (Graphics2D) main.getGraphics();
		int w = (int) (img.getWidth()*scale);
		int h = (int) (img.getHeight()*scale);
		g.setColor(Color.RED);
//		g.fillRect(x, y, w, h);
		g.drawImage(img, x, y-h, w, h, null);
		g.dispose();
	}



	public void drawRect(int x, int y, int w, int h) {
		Graphics2D g = (Graphics2D) main.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(x, y, w, h);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, w, h);
		g.dispose();
	}
	
	public Graphics2D getGraphics2D() {
		return (Graphics2D) main.getGraphics();
	}

	double camX;
	double camY;

	public void setCamPosition(double camX, double camY) {
		this.camX = camX;
		this.camY = camY;
	}
	
	public double getCamX() {
		return camX;
	}
	
	public double getCamY() {
		return camY;
	}



	public int getCamIX() {
		return (int) camX;
	}
	public int getCamIY() {
		return (int) camY;
	}
}
