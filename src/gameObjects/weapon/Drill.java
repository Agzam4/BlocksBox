package gameObjects.weapon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import blocks.Block;
import render.Render;

public class Drill extends Weapon {

	public Drill() {
		super(1);
	}
	int size = 40;
	int d = 7;
	double align;
	
	@Override
	public void fixupdate() {
		setPosition(game.getPlayerBlockX(), game.getPlayerBlockY());
		align = game.mouseAlign;
		if(game.isMousePressed) {
			for (int deg = 0; deg < 360; deg+=5) {
				double rad = Math.toRadians(deg);
				drill(x+Math.cos(rad)*d, y+Math.sin(rad)*d, align, size);
			}
			
//			for (int deg = -5; deg <= 5; deg++) {
//				drill(align + Math.toRadians(deg), size);
//			}
//			radialBoom((int)x, (int)y, 25, 25);
			
		}
	}
	
	private void drill(double x, double y, double align, int size) {
		for (double d = 0; d < size; d+=0.5) {
			int xx = (int) (x + Math.cos(align)*d);
			int yy = (int) (y + Math.sin(align)*d);
			if(game.isInArray(xx, yy)) {
				if(game.map[xx][yy] != null) {
					damage(xx, yy);
				}
			}
		}
	}
	
	protected void damage(int xx, int yy) {
		game.map[xx][yy].damage(damage);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void draw(Render render) {
		Graphics2D g = render.getGraphics2D();
		g.setColor(Color.BLACK);
//		int radius = 25;
//		
		g.setStroke(new BasicStroke(d*Block.BLOCKSIZE*2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(getStartX(render), getStartY(render), getEndX(render), getEndY(render));
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
//		g.fillOval((int) ((x-radius  - render.getCamX())*Block.BLOCKSIZE),
//				(int) ((y-radius - render.getCamY())*Block.BLOCKSIZE), radius*2*Block.BLOCKSIZE, radius*2*Block.BLOCKSIZE);
	}

	
	private int getStartX(Render render) {
		return (int) (x - render.getCamX())*Block.BLOCKSIZE;
	}
	private int getStartY(Render render) {
		return (int) (y - render.getCamY())*Block.BLOCKSIZE;
	}

	private int getEndX(Render render) {
		return (int) (x - render.getCamX() + Math.cos(align)*size)*Block.BLOCKSIZE;
	}
	private int getEndY(Render render) {
		return (int) (y - render.getCamY() + Math.sin(align)*size)*Block.BLOCKSIZE;
	}
	
}
