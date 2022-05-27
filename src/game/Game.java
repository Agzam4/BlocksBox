package game;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;

import blocks.Block;
import gameObjects.GameObject;
import gameObjects.Player;
import gameObjects.weapon.AcidGun;
import gameObjects.weapon.BlocksWaveCreator;
import gameObjects.weapon.Bomb;
import gameObjects.weapon.BombThrower;
import gameObjects.weapon.Drill;
import gameObjects.weapon.FireBombThrower;
import gameObjects.weapon.FireDrill;
import gameObjects.weapon.FireLines;
import gameObjects.weapon.LavaGun;
import gameObjects.weapon.Weapon;
import generator.Generator;
import gui.Main;
import render.Debug;
import render.LightComposite;
import render.Render;

public class Game extends Stage {

	public static final double GRAVITY = 1;
	public static final double SLOW_X = .5;
	public static final int TEMPERATURE = 25;
	
	public Block[][] map;
	private int[][] lightR, lightG, lightB;
	
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
		lightR = new int[w][h];
		lightG = new int[w][h];
		lightB = new int[w][h];
		
//		for (int y = h*3/5; y < h; y++) {
//			for (int x = 0; x < w; x++) {
//					Block block = new Block(Block.TYPE_FIXED_BLOCK, 1000);
//					int fixhp = 50 + (int) (Math.random()*100);
//					block.setFixhp(fixhp);
//					block.setColor(new Color(fixhp,fixhp,fixhp));
//					map[x][y] = block;
//			}
//		}
//
//		int startY = 10;
//		int startY2 = 20;
//		
//		for (int x = 0; x < w; x++) {
//			if(x%2==0)
//				startY += Math.random()*4-2;
//			if(x%3==0)
//			startY2 += Math.random()*6-3;
//
//			if(startY > h/5) startY = h/5;
//			if(startY < -h/5) startY = -h/5;
//			
//			if(startY2 > h/5) startY2 = h/5;
//			if(startY2 < -h/2) startY2 = -h/2;
//			
//			for (int y = h*2/5 + startY; y < h*4/5+startY; y++) {
//				Block block = new Block(Block.TYPE_FIXED_BLOCK, 100);
//				block.setFixhp((int) (Math.random()*50));
//				block.setAcidResistance(90);
//				block.setColor(Math.random() < .5 ? new Color(99,73,42).darker() : new Color(99,73,42));
//				map[x][y] = block;
//			}
//			
//			for (int y = h*3/10 + startY + startY2; y < h*2/5+startY; y++) {
//				Block grass = new Block(Block.TYPE_FIXED_BLOCK, 50);
//				grass.setFixhp((int) (Math.random()*10));
//				grass.setAcidResistance(99);
//				grass.setColor(Math.random() < .5 ? new Color(92,175,75).darker() : new Color(92,175,75));
//				if(isInArray(x, y))
//				map[x][y] = grass;
//			}
//			for (int y = h*3/10; y < h*2/5; y++) {
//				if(map[x][y] == null) {
//					map[x][y] = createWater();
//					if(map[x][y+1] != null)
//					for (int yy = 0; yy < startY2; yy++) {
//						map[x][y+yy] = createSand();
//					}
//				}
//			}
//		}
//		
		
		
		Generator generator = new Generator(w, h);
		generator.generate2(this);
		
		
		/// TODO: Weapon
		
		weapons = new Weapon[8];
		weapons[0] = new Drill();
		weapons[1] = new FireDrill();
		weapons[2] = new BombThrower();
		weapons[3] = new AcidGun();
		weapons[4] = new LavaGun();
		weapons[5] = new BlocksWaveCreator();
		weapons[6] = new FireLines();
		weapons[7] = new FireBombThrower();
		
		selectedWeapon = 1;// weapons.length-1;
		
		for (int i = 0; i < weapons.length; i++) {
			if(weapons[i] != null)
			weapons[i].setGame(this);
		}

		chunksCountW = (int) Math.ceil(w/(double)chunkSizeW);
		chunksCountH = (int) Math.ceil(h/(double)chunkSizeH);
		chunksSleepTime = new int[chunksCountW][chunksCountH];
		
		for (int y = 0; y < chunksCountH; y++) {
			for (int x = 0; x < chunksCountW; x++) {
				chunksSleepTime[x][y] = chunkOffTime;
			}
		}
		
//		createTower(w/5, h*9/10, 15);
		
//		Block.BLOCKSIZE /= 2;
	}
	
	int roomW = 100;
	int roomH = 40;
	int wallsSize = 10;
	
	private void createTower(int x, int y, int n) {
		createRoom(x, y);
		
		if(n > 0) {
			createTower(x, y-roomH, n-1);
			if(n%3 == 0) {
				createRoom(x+roomW, y);
				createTower(x+roomW*2, y, n-2);
			}
		}
	}
	
	private void createRoom(int x, int y) {
		for (int yy = y-roomH/2; yy < y+roomH/2; yy++) {
			for (int xx = x-roomW/2; xx < x+roomW/2; xx++) {
				if(isInArray(xx, yy)) {
					Block block = new Block(Block.TYPE_FIXED_BLOCK, 500);
					block.setFixhp((int) (Math.random()*50) + 50);
					block.setColor(Color.DARK_GRAY);
					block.setFireResistance((int) (450 + Math.random()*30));
					map[xx][yy] = block;
				}
			}
		}

		for (int yy = y-roomH/2; yy < y+roomH/2 - wallsSize; yy++) {
			for (int xx = x-roomW/2 + wallsSize; xx < x+roomW/2 - wallsSize; xx++) {
				if(isInArray(xx, yy)) {
					map[xx][yy] = null;
				}
			}
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

	int chunksSleepTime[][];
	int chunksCountW;
	int chunksCountH;
	int chunkSizeW = 5;
	int chunkSizeH = 5;
	int chunkOffTime = 5;
	
	
	@Override
	public void fixupdate() {

		for (int yy = 0; yy < chunksCountH; yy++) {
			for (int xx = 0; xx < chunksCountW; xx++) {
				if(chunksSleepTime[xx][yy] < chunkOffTime) {
					chunksSleepTime[xx][yy]++;
				}
			}
		}
		
		Block[][] map2 = new Block[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				map2[x][y] = map[x][y];
			}
		}

		for (int i = 0; i <= 1; i++) {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					int chunkX = x/chunkSizeW;
					int chunkY = y/chunkSizeH;
					if(chunksSleepTime[chunkX][chunkY] >= chunkOffTime) continue;
					
					Block block = map[x][y];
					if(block == null) {
						updateLight(x, y);
						continue;
					}
					if(i == 0) {
						block.update(map, map2, x, y, w, h, updateID);
						if(block.getUnmovetime() <= 0 || block.isMove()) {
							activateChunck(x, y);
							activateChunck(x+1, y);
							activateChunck(x-1, y);
							activateChunck(x, y+1);
							activateChunck(x, y-1);
						}
					}
					if((x + y)%2 == i) {
						block.physicsUpdate(map, map2, x, y, w, h, updateID);
						updateLight(x, y);
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
	
	private void updateLight(int x, int y) { // TODO
		if(map[x][y] != null) {
			if(map[x][y].isLightBlock()) {
				lightR[x][y] = Math.max(lightR[x][y], map[x][y].getLightR());
				lightG[x][y] = Math.max(lightG[x][y], map[x][y].getLightG());
				lightB[x][y] = Math.max(lightB[x][y],  map[x][y].getLightB());
				return;
			}
		}
		updateLightArray(lightR, x, y); 
		updateLightArray(lightG, x, y); 
		updateLightArray(lightB, x, y); 

		
	}

	private void updateLightArray(int[][] light, int x, int y) {
		int color = light[x][y];
		int count = 1;
		if(!isHardBlock(x+1, y)) {
			color += light[x+1][y];
			count++;
		}
		if(!isHardBlock(x-1, y)) {
			color += light[x-1][y];
			count++;
		}
		if(!isHardBlock(x, y+1)) {
			color += light[x][y+1];
			count++;
		}
		if(!isHardBlock(x, y-1)) {
			color += light[x][y-1];
			count++;
		}
		light[x][y] = (int) (color/count/1.05);
	}
	

	public void activateChunck(int x, int y) {
		int chunkX = x/chunkSizeW;
		int chunkY = y/chunkSizeH;
		if(chunkX >= 0 && chunkX < chunksCountW && chunkY >= 0 && chunkY < chunksCountH) {
			chunksSleepTime[chunkX][chunkY] = 0;
		}
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
	public void draw(Render render) { // TODO
		
		render.clearUI();
		
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
					int chunkX = (x+ax)/chunkSizeW;
					int chunkY = (y+ay)/chunkSizeH;
					if(chunkX >= 0 && chunkX < chunksCountW && chunkY >= 0 && chunkY < chunksCountH) {
						chunksSleepTime[chunkX][chunkY] = 0;
					}
					
					Block block = map[x+ax][y+ay];
					if(block == null) {
						
						render.addfillRect(
							(int) (x*Block.BLOCKSIZE/Render.SCALE),
							(int) (y*Block.BLOCKSIZE/Render.SCALE),
							(int) Math.ceil(Block.BLOCKSIZE/(double)Render.SCALE), (int) Math.ceil(Block.BLOCKSIZE/(double)Render.SCALE),
							lightR[x+ax][y+ay], lightG[x+ax][y+ay], lightB[x+ax][y+ay]);
						continue;
					}
					int cr = block.getColor().getRed();
					int cg = block.getColor().getGreen();
					int cb = block.getColor().getBlue();
					cr += lightR[x+ax][y+ay];
					cg += lightG[x+ax][y+ay];
					cb += lightB[x+ax][y+ay];
					
					cr = fixColor(cr);
					cg = fixColor(cg);
					cb = fixColor(cb);
					
					render.fillRect(
							(int) (x*Block.BLOCKSIZE/Render.SCALE),
							(int) (y*Block.BLOCKSIZE/Render.SCALE),
							(int) Math.ceil(Block.BLOCKSIZE/(double)Render.SCALE), (int) Math.ceil(Block.BLOCKSIZE/(double)Render.SCALE),
							cr, cg, cb
						);
				}
			}
		}
		
		
		
		for (int i = 0; i < gameObjects.size(); i++) {
			gameObjects.get(i).draw(render);
		}

//		int drawChunckSize = 1;

//		for (int yy = 0; yy < chunksCountH; yy++) {
//			for (int xx = 0; xx < chunksCountW; xx++) {
//				int cst = chunksSleepTime[xx][yy];
//				int color = 255*cst/chunkOffTime;
//				if(color < 0) color = 0;
//				if(color > 255) color = 255;
//					render.fillRect(
//							render.getWidth() - chunksCountW*drawChunckSize + xx*drawChunckSize,
//							yy*drawChunckSize,
//							drawChunckSize, drawChunckSize,
//							0, 255-color, 0
//							);
//				g.setColor(new Color(0, 255-color, 0));
//				g.fillRect(Main.screen.width-xx*drawChunckSize, yy*drawChunckSize, drawChunckSize, drawChunckSize);
//			}
//		}
		

//		Graphics2D g = render.getUIGraphics2D();
		
		render.drawString("Selected: " + weapons[selectedWeapon].getClass().getSimpleName(), 1);
		render.drawString("Update Lags: " + Debug.fixUpdateLags + "ms", 2);
		render.drawString("FPS: " + Debug.fps_2 + " (" + Debug.fps + ")", 3);
		
		render.getGraphics2D().dispose();
		
	}
	

	private int fixColor(int i) {
		if(i < 0) return 0;
		if(i > 255) return 255;
		return i;
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
