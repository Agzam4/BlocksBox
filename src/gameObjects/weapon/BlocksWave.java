package gameObjects.weapon;

import java.awt.Color;
import java.awt.Graphics2D;

import blocks.Block;
import render.Render;

public class BlocksWave extends Weapon {

	public BlocksWave(int vx) {
		super(1);
		this.vx = vx;
	}
	
	double size = 20;
	int power = 2;
	double vx;
	
	@Override
	public void fixupdate() {
		for (int yy = (int) (y-size); yy < y+size; yy++) {
			for (int xx = (int) (x-size); xx < x+size; xx++) {
				if(xx >= 0 && xx < game.w && yy >= 0 && yy < game.h) {
					if(game.map[xx][yy] == null) continue;
					double hypot = Math.hypot(x-xx, y-yy);
					if(hypot < size) {
						game.activateChunck(xx, yy);
						game.map[xx][yy].fixDamage(damage);
						game.map[xx][yy].setMoveVector(vx*power, -power*2);
					}
				}
			}
		}
		size -= 0.1;
		if(size <= 0) {
			size = 0;
			needDestroy = true;
		}
		

//		for (int i = 0; i < size; i++) {
//			for (int xxx = 0; xxx < size; xxx++) {
//				int xx = (int) (x -size/2 + i - xxx);
//				int yy = (int) (y -size/2 + i);
//				if(xx >= 0 && xx < game.w && yy >= 0 && yy < game.h) {
//					if(game.map[xx][yy] == null) continue;
//						game.activateChunck(xx, yy);
//						game.map[xx][yy].setFixhp(0);
//						game.map[xx][yy].setMoveVector(vx*power+xxx*vx, -power*5);
//				}
//			}
//		}
		
		
		x+=vx;
		
	}
	
	@Override
	public void draw(Render render) {
		Graphics2D g = render.getGraphics2D();
		g.setColor(new Color(255,255,255,100));
		g.drawOval((int) ((x-size - render.getCamX())*Block.BLOCKSIZE/Render.SCALE),
				(int) ((y-size - render.getCamY())*Block.BLOCKSIZE/Render.SCALE), (int) (size*2*Block.BLOCKSIZE/Render.SCALE), (int) (size*2*Block.BLOCKSIZE/Render.SCALE));
	}
}
