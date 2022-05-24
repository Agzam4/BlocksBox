package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import blocks.Block;
import gameObjects.GameObject;
import gameObjects.Player;
import gameObjects.weapon.AcidGun;
import gameObjects.weapon.Bomb;
import gameObjects.weapon.BombThrower;
import gameObjects.weapon.Drill;
import gameObjects.weapon.FireDrill;
import gameObjects.weapon.LavaGun;
import gameObjects.weapon.Weapon;
import gui.Main;
import render.Render;

public class Game extends Stage {

	public static final double GRAVITY = 1;
	public static final double SLOW_X = .5;
	public static final int TEMPERATURE = 25;
	
	public Block[][] map;
	public int w;
	public int h;
	
	ArrayList<GameObject> gameObjects;
	

	Weapon[] weapons;
	int selectedWeapon = 0;
	
	public Game(int w, int h) {
		this.w = w;
		this.h = h;
		
		gameObjects = new ArrayList<>();
		player.setGame(this);
		
		map = new Block[w][h];
		for (int y = h*3/5; y < h; y++) {
			for (int x = 0; x < w; x++) {
					Block block = new Block(Block.TYPE_FIXED_BLOCK, 1000);
					int fixhp = 50 + (int) (Math.random()*100);
					block.setFixhp(fixhp);
					block.setColor(new Color(fixhp,fixhp,fixhp));
					map[x][y] = block;
			}
		}

		int startY = 10;
		int startY2 = 20;
		
		for (int x = 0; x < w; x++) {
			if(x%2==0)
				startY += Math.random()*4-2;
			if(x%3==0)
			startY2 += Math.random()*6-3;

			if(startY > h/5) startY = h/5;
			if(startY < -h/5) startY = -h/5;
			
			if(startY2 > h/5) startY2 = h/5;
			if(startY2 < -h/2) startY2 = -h/2;
			
			for (int y = h*2/5 + startY; y < h*4/5+startY; y++) {
				Block block = new Block(Block.TYPE_FIXED_BLOCK, 100);
				block.setFixhp((int) (Math.random()*50));
				block.setAcidResistance(90);
				block.setColor(Math.random() < .5 ? new Color(99,73,42).darker() : new Color(99,73,42));
				map[x][y] = block;
			}
			
			for (int y = h*3/10 + startY + startY2; y < h*2/5+startY; y++) {
				Block grass = new Block(Block.TYPE_FIXED_BLOCK, 50);
				grass.setFixhp((int) (Math.random()*10));
				grass.setAcidResistance(99);
				grass.setFireResistance(1);
				grass.setColor(Math.random() < .5 ? new Color(92,175,75).darker() : new Color(92,175,75));
//				System.out.println(x + " " + y);
				if(isInArray(x, y))
				map[x][y] = grass;
			}
			for (int y = h*3/10; y < h*2/5; y++) {
				if(map[x][y] == null) {
					map[x][y] = createWater();
					if(map[x][y+1] != null)
					for (int yy = 0; yy < startY2; yy++) {
						map[x][y+yy] = createSand();
					}
				}
			}
			
		}
		
//		for (int x = w/2-5; x < w/2+5; x++) {
//			for (int y = 0; y < 10; y++) {
//				map[x][y] = createLava();
//			}
//		}
//		map[w/2][0] = createLava();
		
//		for (int y = 0; y < 10; y++) {
//			for (int x = 0; x < w; x+=2) {
//				map[x][y] = createSand();
//				map[x][y+15] = createWater();
//			}
//		}
		
		/// TODO: Weapon
		
		weapons = new Weapon[5];
		weapons[0] = new Drill();
		weapons[1] = new FireDrill();
		weapons[2] = new BombThrower();
		weapons[3] = new AcidGun();
		weapons[4] = new LavaGun();
		selectedWeapon = weapons.length-1;
		
		for (int i = 0; i < weapons.length; i++) {
			if(weapons[i] != null)
			weapons[i].setGame(this);
		}
	}
	
	
	
	Player player = new Player();
	
	public void addGameObject(GameObject object) {
		object.setGame(this);
		gameObjects.add(object);
	}

	private Block createSand() {
		Block block = new Block(Block.TYPE_FALL_BLOCK, 100);
		block.setColor(Color.ORANGE.darker());
		block.setAcidResistance(100);
		return block;
	}

	private Block createWater() {
		Block block = new Block(Block.TYPE_FLUID_BLOCK, 1000);
		block.setColor(Color.BLUE.brighter());
		block.setAcidResistance(50);
		return block;
	}
	private Block createAcid() {
		Block block = new Block(Block.TYPE_FLUID_BLOCK, 1000);
		block.setColor(new Color(0, 0, 255));
		block.setAcid(255);
		return block;
	}
	
	private Block createLava() {
		Block block = new Block(Block.TYPE_FLUID_BLOCK, 1000);
		block.setColor(Color.DARK_GRAY);
		block.setFireResistance(100);
		block.setFire(1000);
		block.setFireBlock(true);
//		block.setTemperature(1000);
//		block.setFlameTemperature(10000);
		return block;
	}
	
	int updateID = 0;
	
	@Override
	public void fixupdate() {
		Block[][] map2 = new Block[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				map2[x][y] = map[x][y];
			}
		}

		for (int i = 0; i <= 1; i++) {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					Block block = map[x][y];
					if(block == null) continue;
					if(i == 0) block.update(map, map2, x, y, w, h, updateID);
					if((x + y)%2 == i) {
						block.physicsUpdate(map, map2, x, y, w, h, updateID);
					}
				}
			}
		}
		map = map2;
		
//		for (int y = 0; y < h; y++) {
//			for (int x = 0; x < w; x++) {
//				map[x][y] = map2[x][y];
//			}
//		}
		player.fixupdate();
		weapons[selectedWeapon].fixupdate();
		
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject go = gameObjects.get(i);
			go.fixupdate();
			if(go.isNeedDestroy()) gameObjects.remove(i);
		}
		
		updateID++;
//		map[w/2][0] = createAcid();
//		map[w/2][1] = createAcid();

//		map[w/2-1][2] = createLava();
//		map[w/2][2] = createLava();
//		map[w/2+1][2] = createLava();
//		
//		map[w-1][2] = createSand();
//		
//		int ay = updateID%h;
//		if(h - ay - 1 > 0) {
//			for (int x = 0; x < w; x++) {
//				if(Math.random() < .0001)
//					map[x][h - ay - 1] = createLava();
//			}
//		}
	}

	@Override
	public void update() {
		player.update();
		
		int drawW = Main.screen.width/Block.BLOCKSIZE;
		int drawH = Main.screen.height/Block.BLOCKSIZE;

		double targetCX = tcamX - drawW/2;
		double targetCY = tcamY - drawH/2;
		camX = (camX - targetCX)/1.2d + targetCX; 
		camY = (camY - targetCY)/1.2d + targetCY;

//		if(camX < 0) camX = 0;
//		if(camX > w-drawW) camX = w-drawW;
//		
//		if(camY < 0) camY = 0;
//		if(camY > h-drawH) camY = h-drawH;
		
		for (GameObject gameObject : gameObjects) {
			gameObject.update();
		}

		weapons[selectedWeapon].update();
//		camX += v_camX;
//		camY += v_camY;
	}

	public double tcamX;
	public double tcamY;
	
	public double camX;
	public double camY;
	private double v_camX;
	private double v_camY;
	
	@Override
	public void draw(Render render) {
		Graphics2D g = render.getGraphics2D();
		
		int drawW = Main.screen.width/Block.BLOCKSIZE;
		int drawH = Main.screen.height/Block.BLOCKSIZE;

		render.setCamPosition(camX, camY);
		weapons[selectedWeapon].draw(render);
		player.draw(render);

		int ax = (int) camX;
		int ay = (int) camY;
		for (int y = 0; y < drawH; y++) {
			for (int x = 0; x < drawW; x++) {
				if(x+ax >= 0 && x+ax < w && y+ay >= 0 && y+ay < h) {
					Block block = map[x+ax][y+ay];
					if(block == null) {
//						g.setColor(new Color(15,15,15));
//						g.fillRect(x*Block.BLOCKSIZE, y*Block.BLOCKSIZE, Block.BLOCKSIZE, Block.BLOCKSIZE);
						continue;
					}
					g.setColor(block.getColor());
					g.fillRect(
							(int) ((x-(int)camX%1)*Block.BLOCKSIZE),
							(int) ((y-(int)camY%1)*Block.BLOCKSIZE),
							Block.BLOCKSIZE, Block.BLOCKSIZE);
				}
			}
		}
		
		for (int i = 0; i < gameObjects.size(); i++) {
			gameObjects.get(i).draw(render);
		}
		

		g.setColor(Color.WHITE);
		g.drawString("Selected: " + weapons[selectedWeapon].getClass().getSimpleName(), 16, 16);
		g.setColor(Color.BLACK);
		g.drawString("Selected: " + weapons[selectedWeapon].getClass().getSimpleName(), 15, 15);
		
		g.dispose();
	}

	double cam_speed = 5;

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.setVx(1);
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.setVx(-1);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			player.setVy(-1);
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.setVy(1);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(player.getVx() > 0 && e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.setVx(0);
		}
		if(player.getVx() < 0 && e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.setVx(0);
		}
		
		if(player.getVy() < 0 && e.getKeyCode() == KeyEvent.VK_UP) {
			player.setVy(0);
		}
		if(player.getVy() > 0 && e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.setVy(0);
		}
	}
	
	public double mouseAlign;
	

	@Override
	public void mouseClicked(MouseEvent e) {
		updateMouse(e);
		
//		int cx = (int) (e.getX()/Block.BLOCKSIZE + camX);
//		int cy = (int) (e.getY()/Block.BLOCKSIZE + camY);
//		
//		Bomb bomb = new Bomb();
//		bomb.setPosition(cx, cy);
		
//		addGameObject(bomb);
		
		
		
		/*
		int size = 100;
		int dmg = 250;
		
		for (int y = cy-size; y < cy+size; y++) {
			for (int x = cx-size; x < cx+size; x++) {
				if(x >= 0 && x < w && y >= 0 && y < h) {
					if(map[x][y] == null) continue;
					double hypot = Math.hypot(cx-x, cy-y);
					if(hypot < size) {
						map[x][y].damage((int) (dmg*(size-hypot)/size));
					}
				}
			}
		}
		//*/
//		map[e.getX()/Block.BLOCKSIZE][e.getY()/Block.BLOCKSIZE] = null;
	}

	private void updateMouse(MouseEvent e) {
		mouseAlign = Math.atan2(e.getY() - getHalfHeight(), e.getX() - getHalfWidth());		
	}

	private int getHalfWidth() {
		return Main.screen.width/2;
	}
	
	private int getHalfHeight() {
		return Main.screen.height/2;
	}
	
	public boolean isHardBlock(int x, int y) {
		if(isInArray(x, y)) {
			if(map[x][y] == null) return false;
			else {
				int type = map[x][y].getType();
				if(map[x][y].isMove()) return false;
				return type == Block.TYPE_FIXED_BLOCK || type == Block.TYPE_FALL_BLOCK;
			}
		}else {
			return true;
		}
	}
	
	public boolean isInArray(int x, int y) {
		return x >= 0 && x < w && y >= 0 && y < h;
	}
	

	public int getPlayerBlockX() {
		return (int) (player.getPX() + player.getW()/2);
	}
	public int getPlayerBlockY() {
		return (int) (player.getPY() + player.getH()/2);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updateMouse(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateMouse(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int value = e.getWheelRotation();
		int index = selectedWeapon;
		index += value;
		if(index < 0) index = 0;
		if(index >= weapons.length) index = weapons.length-1;
		selectedWeapon = index;
	}
	
	
}
