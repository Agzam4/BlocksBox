package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import blocks.Block;
import game.Game;
import render.Render;

public class Player extends GameObject {

	double vx, vy;
	int w, h;

	double speed = 3;
	double jump = 10;

	public Player() {
		w = 10;
		h = 10;
		x = 15;
		y = 15;
	}

	boolean isGrounded = false;

	@Override
	public void fixupdate() {

		vx = _vx;
		if(isGrounded) vy += _vy;

		_vy = 0;

		vy += Game.GRAVITY;
		isGrounded = false;

		if(vy > 0) {
			for (int yy = 0; yy < vy; yy++) {
				for (int xx = 0; xx < w; xx++) {
					if(game.isHardBlock((int) (x+xx), (int) (y+yy+h))) {
						y += yy;
						vy = 0;
						isGrounded = true;
						break;
					}
				}
			}
			y += vy;
		}
		if(vy < 0) {
			for (int yy = 0; yy > vy; yy--) {
				for (int xx = 0; xx < w; xx++) {
					if(game.isHardBlock((int) (x+xx), (int) (y+yy+1))) {
						y += yy;
						vy = 0;
						break;
					}
				}
			}
			y += vy;
		}

		if(vx > 0) {
			for (int xx = 0; xx < vx; xx++) {
				for (int yy = 0; yy < h; yy++) {
					if(game.isHardBlock((int) (x+xx+w), (int) (y+yy))) {
						x += xx;
						vx = 0;
						if(isGrounded && !game.isHardBlock((int) (x+w+1), (int) (y+h-2))) {
							vx = 0;
							vy = -2;
//							isGrounded = false;
						}
						break;
					}
				}
			}
			x += vx;

			vx -= 1;
			if(vx < 0) vx = 0;
		}

		if(vx < 0) {
			for (int xx = 0; xx > vx; xx--) {
				for (int yy = 0; yy < h; yy++) {
					if(game.isHardBlock((int) (x+xx-1), (int) (y+yy))) {
						x += xx;
						vx = 0;
						if(isGrounded && !game.isHardBlock((int) (x-1), (int) (y+h-2))) {
							vy = -2;
//							isGrounded = false;
						}
						break;
					}
				}
			}
			x += vx;

			vx += 1;
			if(vx < 0) vx = 0;
		}
		//		if(vx < 0) {
		//			System.out.println("UP");
		//			for (int yy = -1; yy > vy; yy--) {
		//				for (int xx = 0; xx < w; xx++) {
		//					if(game.isHardBlock((int) (x+xx), (int) (y+yy+h))) {
		//						y += yy;
		//						vy = 0;
		//						isGrounded = true;
		//						break;
		//					}
		//				}
		//			}
		//			y += vy;
		//		}

		for (int yy = (int) y + 1; yy < y+h-2; yy++) {
			for (int xx = (int) x + 1; xx < x+w-2; xx++) {
				if(game.isHardBlock(xx, yy) && game.isInArray(xx, yy)) {
					game.map[xx][yy].damage(100);
				}
			}
		}

	}

	@Override
	public void update() {
		game.tcamX = x + w/2;
		game.tcamY = y + h/2;

	}

	@Override
	public void draw(Render render) {
		Graphics2D g = render.getGraphics2D();

//		g.setColor(isGrounded ? Color.WHITE : Color.LIGHT_GRAY);
		g.setColor(Color.WHITE);
		g.fillRect((int) ((x- render.getCamIX())*Block.BLOCKSIZE),
				(int) ((y - render.getCamIY())*Block.BLOCKSIZE), w*Block.BLOCKSIZE, h*Block.BLOCKSIZE);
	}

	private boolean isWall(int x, int y) {
		for (int yy = y; yy < y+h; yy++) {
			for (int xx = x; xx < x+w; xx++) {
				if(game.isHardBlock(xx, yy)) return true;
			}
		}
		return false;
	}

	double _vx, _vy;

	public void setVx(double vx) {
		_vx = vx*speed;
	}

	public void setVy(double vy) {
		if(vy < 0) _vy = vy*jump;
	}

	public double getVx() {
		return _vx;
	}

	public double getVy() {
		return _vy;
	}
	
	public double getPX() {
		return x;
	}
	
	public double getPY() {
		return y;
	}
	
	public int getW() {
		return w;
	}
	public int getH() {
		return h;
	}
}
