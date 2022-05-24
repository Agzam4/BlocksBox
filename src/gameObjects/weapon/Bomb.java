package gameObjects.weapon;

import java.awt.Color;
import java.awt.Graphics2D;

import blocks.Block;
import render.Render;

public class Bomb extends Weapon {

	public Bomb() {
		super(100);
	}

	double px, py;
	double vx, vy;
	
	int radius = 3;
	
	@Override
	public void fixupdate() {
		y += vy;
		x += vx;
		
		if(game.isHardBlock((int) x, (int) y)) {
			y -= vy;
			vy = 0;
			radialBoom((int) x, (int) y, 25, 10);
			needDestroy = true;
		}
		
		vy += .1d;
	}
	
	@Override
	public void draw(Render render) {
		Graphics2D g = render.getGraphics2D();
		g.setColor(Color.WHITE);
		g.fillOval((int) ((x-radius - render.getCamX())*Block.BLOCKSIZE),
				(int) ((y-radius - render.getCamY())*Block.BLOCKSIZE), radius*2*Block.BLOCKSIZE, radius*2*Block.BLOCKSIZE);
	}
	
	public void setVx(double vx) {
		this.vx = vx;
	}
	
	public void setVy(double vy) {
		this.vy = vy;
	}
}
