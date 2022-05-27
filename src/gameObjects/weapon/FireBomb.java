package gameObjects.weapon;

import java.awt.Color;
import java.awt.Graphics2D;

import blocks.Block;
import render.Render;

public class FireBomb extends Weapon {

	public FireBomb() {
		super(400);
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
					if(game.isInArray((int) xx, (int) yy)) {
						if(game.isHardBlock((int) xx, (int) yy) && !game.map[xx][yy].isBurned()) {
							y -= vy;
							vy = 0;
							radialFireBoom((int) x, (int) y, 25, 10);
							needDestroy = true;
						}
					}
				}
			}
		}
		
		vy += .1d;
	}
	
	@Override
	public void draw(Render render) {
		Graphics2D g = render.getGraphics2D();
		g.setColor(Color.RED);
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
