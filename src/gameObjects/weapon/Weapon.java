package gameObjects.weapon;

import java.awt.Color;
import java.util.Iterator;

import blocks.Block;
import gameObjects.GameObject;
import render.Render;

public class Weapon extends GameObject {
	
	public int damage;
	
	public Weapon(int damage) {
		this.damage = damage;
	}
	
	@Override
	public void fixupdate() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw(Render render) {
		
	}
	
	
	protected void radialFireBoom(int cx, int cy, int size, double power) {
		for (int y = cy-size; y < cy+size; y++) {
			for (int x = cx-size; x < cx+size; x++) {
				if(x >= 0 && x < game.w && y >= 0 && y < game.h) {
					if(game.map[x][y] == null) continue;
					double hypot = Math.hypot(cx-x, cy-y);
					if(hypot < size) {
						double pp = (size-hypot)/size;
						game.map[x][y].fire(damage);
						if(game.map[x][y].isBurned()) game.map[x][y].destroy();
						double vx = 0;
						if(x > cx) vx = pp;
						if(x < cx) vx = -pp;
						game.activateChunck(x, y);
//						if(hypot < size/5) {
//							game.map[x][y].setFireBlock(true);
//							game.map[x][y].setFire((int) hypot);
//							game.map[x][y].setFireResistance(0);
//							game.map[x][y].setColor(Color.DARK_GRAY);
//						}
						game.map[x][y].setMoveVector((x-cx)*power/size, -power);
					}
				}
			}
		}
	}

	protected void radialBoom(int cx, int cy, int size, double power) {
		for (int y = cy-size; y < cy+size; y++) {
			for (int x = cx-size; x < cx+size; x++) {
				if(x >= 0 && x < game.w && y >= 0 && y < game.h) {
					if(game.map[x][y] == null) continue;
					double hypot = Math.hypot(cx-x, cy-y);
					if(hypot < size) {
						double pp = (size-hypot)/size;
						game.map[x][y].damage((int) (damage*pp));
						double vx = 0;
						if(x > cx) vx = pp;
						if(x < cx) vx = -pp;
						game.activateChunck(x, y);
//						if(hypot < size/5) {
//							game.map[x][y].setFireBlock(true);
//							game.map[x][y].setFire((int) hypot);
//							game.map[x][y].setFireResistance(0);
//							game.map[x][y].setColor(Color.DARK_GRAY);
//						}
						game.map[x][y].setMoveVector((x-cx)*power/size, -power);
					}
				}
			}
		}
//		for (int i = 0; i < 5; i++) {
//			Block fire = new Block(Block.TYPE_FALL_BLOCK, 10);
//			fire.setFireBlock(true);
//			fire.setColor(Color.DARK_GRAY);
//			fire.setFire(1000);
//			fire.setFireResistance(10000);
//			fire.setMoveVector(Math.random()*power - power*2, -5);
//			game.map[cx-2+i][cy] = fire;
//		}
	}
}
