package gameObjects.weapon;

import java.awt.Color;
import java.awt.Graphics2D;

import blocks.Block;
import render.Render;

public class Bomb extends Weapon {

	public Bomb() {
		super(500);
	}

	double px, py;
	double vx, vy;
	
	int radius = 3;
	
	@Override
	public void fixupdate() {
		y += vy;
		x += vx;
		

		for (int yy = (int) (y-radius); yy < y+radius; yy++) {
			for (int xx = (int) (x-radius); xx < x+radius; xx++) {
				double hypot = Math.hypot(x-xx, y-yy);
				if(hypot < radius) {
					if(game.isHardBlock((int) xx, (int) yy)) {
						y -= vy;
						vy = 0;
						radialBoom((int) x, (int) y, 25, 10);
						needDestroy = true;
					}
				}
			}
		}
		
		vy += .1d;
	}
	
	@Override
	public void draw(Render render) {
		Graphics2D g = render.getGraphics2D();
		g.setColor(Color.WHITE);
//		render.fillOval((int) ((x-radius - render.getCamX())*Block.BLOCKSIZE),
//				(int) ((y-radius - render.getCamY())*Block.BLOCKSIZE), radius*2*Block.BLOCKSIZE, 255, 255, 255);
		
		g.fillOval((int) ((x-radius - render.getCamX())*Block.BLOCKSIZE/Render.SCALE),
				(int) ((y-radius - render.getCamY())*Block.BLOCKSIZE/Render.SCALE), radius*2*Block.BLOCKSIZE/Render.SCALE, radius*2*Block.BLOCKSIZE/Render.SCALE);
	}
	
	public void setVx(double vx) {
		this.vx = vx;
	}
	
	public void setVy(double vy) {
		this.vy = vy;
	}
}
