package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import blocks.Block;
import gui.JGamePanel;

public class Render {
	
	public static int SCALE = Block.BLOCKSIZE;

	private BufferedImage all;
	private BufferedImage main;
	private BufferedImage ui;
	private int width, height;
	private WritableRaster wr;

	private boolean showUi = false;
	private boolean drawUIonGame = true;
	
	Dimension dimension;

	public Render(Dimension dimension) {
		this.dimension = dimension;
		width = dimension.width/SCALE;
		height = dimension.height/SCALE;
		main = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		all = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		ui = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
		wr = main.getRaster();
		
	}
	
	public void setScale(int scale) {
		SCALE = scale;
		width = dimension.width/SCALE;
		height = dimension.height/SCALE;
		main = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		wr = main.getRaster();
	}
	
	public void setShowUi(boolean showUi) {
		this.showUi = showUi;
	}
	
	public boolean isShowUi() {
		return showUi;
	}
	
	public boolean isDrawUIonGame() {
		return drawUIonGame;
	}

	public Image getImage() {
		if(!showUi || drawUIonGame) return main;
		
		WritableRaster wr = all.getRaster();
		Raster game = main.getRaster();
		Raster ui = this.ui.getRaster();

		int[] srcPixels = new int[dimension.width];
		int[] srcGame = new int[main.getWidth()];
		int[] srcUi = new int[dimension.width];
		
		int k = dimension.width/main.getWidth();
		for (int blockY = 0; blockY < main.getHeight(); blockY++) {
			game.getDataElements(0, blockY, width/k, 1, srcGame);
			for (int blockPixelY = 0; blockPixelY < k; blockPixelY++) {
				int y = blockY*k + blockPixelY;
				wr.getDataElements(0, y, width, 1, srcPixels);
				ui.getDataElements(0, y, width, 1, srcUi);
				for (int blockX = 0; blockX < main.getWidth(); blockX++) {
					for (int blockPixelX = 0; blockPixelX < k; blockPixelX++) {
						//				for (int x = 0; x < dimension.width; x++) {
						int x = blockX*k + blockPixelX;
						
						int uiPixel = srcUi[x];
						int uiA = (uiPixel >> 24) & 0xFF;
						if(uiA == 0) {
							srcPixels[x] = srcGame[blockX];
						}else {
							srcPixels[x] = srcUi[x];
						}
					}
				}
				wr.setDataElements(0, y, dimension.width, 1, srcPixels);
			}
		}
		
		return all;
	}
	
	public BufferedImage getUi() {
		return ui;
	}

	public void drawDebugTiles(int[][] base) {
		
	}
	
	public void clear() {
		Graphics2D g = (Graphics2D) main.getGraphics();
		g.setColor(new Color(50,50,50));
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


	public void addfillRect(int sx, int sy, int w, int h, int r, int g, int b) {
		if(sx + w > width) w = width-sx;
		if(sy + h > height) h = height-sy;
		
		int[] srcPixel = new int[3];
		int[] srcPixels = new int[w];

		for (int y = 0; y < h; y++) {
			wr.getDataElements(sx, sy+y, w, 1, srcPixels);
			for (int x = 0; x < w; x++) {
				int pixel = srcPixels[x];
				srcPixel[0] = (pixel >> 16) & 0xFF;
				srcPixel[1] = (pixel >>  8) & 0xFF;
				srcPixel[2] = (pixel) & 0xFF;
//				srcPixel[3] = (pixel >> 24) & 0xFF;

				srcPixel[0] = fixColor(srcPixel[0] + r);
				srcPixel[1] = fixColor(srcPixel[1] + g);
				srcPixel[2] = fixColor(srcPixel[2] + b);

				srcPixels[x] = (0 & 0xFF) << 24 |
						(srcPixel[0] & 0xFF) << 16 |
						(srcPixel[1] & 0xFF) <<  8 |
						srcPixel[2] & 0xFF;
			}
			wr.setDataElements(sx, sy+y, w, 1, srcPixels);
		}
	}
	
	private int fixColor(int color) {
		if(color > 255) return 255;
		if(color < 0) return 0;
		return color;
	}
	
	public void fillOval(int sx, int sy, int size, int r, int g, int b) {
		if(sx > width) return;
		if(sy > height) return;
		
		int w = size;
		if(sx+size > width) w = width - sx;
		int[] srcPixels = new int[w];
		for (int y = 0; y < size; y++) {
			wr.getDataElements(sx, sy+y, w, 1, srcPixels);
			for (int x = 0; x < w; x++) {
				if((pow(size/2-x) + pow(size/2-y))*4 < size*size)
				srcPixels[x] = (0 & 0xFF) << 24 |
						(r & 0xFF) << 16 |
						(g & 0xFF) <<  8 |
						b & 0xFF;
			}
			wr.setDataElements(sx, sy+y, w, 1, srcPixels);
		}
	}
	
	private int pow(int i) {
		return i*i;
	}

	public void fillRect(int sx, int sy, int w, int h, int r, int g, int b) {
		if(sx + w > width) w = width-sx;
		if(sy + h > height) h = height-sy;
		int[] srcPixels = new int[w];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				srcPixels[x] = (0 & 0xFF) << 24 |
						(r & 0xFF) << 16 |
						(g & 0xFF) <<  8 |
						b & 0xFF;
			}
//			System.out.println(sx + " " + sy);
//			System.out.println((sx+w) + "/" + width);
//			System.out.println((sy+h) + "/" + height);
			wr.setDataElements(sx, sy+y, w, 1, srcPixels);
		}
	}

	public void clearUI() {
		WritableRaster wr = ui.getRaster();
		int[] srcPixels = new int[dimension.width];
		for (int y = 0; y < dimension.height; y++) {
			for (int x = 0; x < dimension.width; x++) {
				srcPixels[x] = (0 & 0xFF) << 24 |
						(0 & 0xFF) << 16 |
						(0 & 0xFF) <<  8 |
						0 & 0xFF;
			}
			wr.setDataElements(0, y, dimension.width, 1, srcPixels);
		}
	}

	public Graphics2D getGraphics2D() {
		return (Graphics2D) main.getGraphics();
	}
	
	public Graphics2D getUIGraphics2D() {
		return (Graphics2D) ui.getGraphics();
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
	
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}



	public void drawString(String string, int i) {
		Graphics2D g = drawUIonGame ? getGraphics2D() : getUIGraphics2D();
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		g.setColor(Color.WHITE);
		g.drawString(string, 1, 11*i);
		g.setColor(Color.BLACK);
		g.drawString(string, 2, 1 + 11*i);
	}
}
