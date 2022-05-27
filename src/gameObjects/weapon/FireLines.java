package gameObjects.weapon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import blocks.Block;
import render.Render;

public class FireLines extends Weapon {

	public FireLines() {
		super(1000);
	}

	int reloadTime = 100;
	int reload = 0;

	int useTime = 10;
	int use = 0;
	
	@Override
	public void fixupdate() {

		if(reload > 0) reload--;
		setPosition(game.getPlayerBlockX(), game.getPlayerBlockY());

		if(game.isMousePressed) {
			if(reload <= 0) {
				for (int i = 0; i < 3; i++) {
					double d1 = Math.random()*360;
					a1 = Math.toRadians(d1);
					a2 = Math.toRadians(d1 + 180 + Math.random()*15);
					fireLine();
					reload = reloadTime;
				}
			}
		}
		
//		System.out.println(use);
//		if(use > 0) {
//			reload = reloadTime;
//			use--;
//		}
	}

	int distanse1 = 25;
	int distanse = 100;
	
	double a1, a2;

	int sx, sy;
	double ex, ey;

	private void fireLine() {
		double deg = use*360/useTime;
		sx = (int) (x + distanse1*Math.cos(a1 + Math.toRadians(deg)));
		sy = (int) (y + distanse1*Math.sin(a1 + Math.toRadians(deg)));

		for (int i = 0; i < (useTime-use)*distanse/useTime; i++) {
			ex = sx + i*Math.cos(a2 + Math.toRadians(deg));
			ey = sy + i*Math.sin(a2 + Math.toRadians(deg));
			int x = (int) (ex);
			int y = (int) (ey);
			if(game.isInArray(x, y)) {
				if(game.isHardBlock(x, y)) {
					game.map[x][y].fire(damage);
					if(game.map[x][y].isBurned()) game.map[x][y].destroy();
				}
			}
		}
	}
	
	@Override
	public void draw(Render render) {
//		Graphics2D g = render.getGraphics2D();
//		g.setColor(Color.RED);
//		g.drawLine(getStartX(render), getStartY(render), getEndX(render), getEndY(render));
	}
	
	private int getStartX(Render render) {
		return (int) (sx - render.getCamX())*Block.BLOCKSIZE;
	}
	private int getStartY(Render render) {
		return (int) (sy - render.getCamY())*Block.BLOCKSIZE;
	}

	private int getEndX(Render render) {
		return (int) (ex - render.getCamX())*Block.BLOCKSIZE;
	}
	private int getEndY(Render render) {
		return (int) (ey - render.getCamY())*Block.BLOCKSIZE;
	}

}
