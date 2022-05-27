package blocks;

import java.awt.Color;

import game.Game;

public class Block {
	
	public static int BLOCKSIZE = 7;
	
	public static final int TYPE_FIXED_BLOCK = 0; 		// Always fixed
//	public static final int TYPE_STATIC_BLOCK = 1; 		// Fixed until some action occurs
	public static final int TYPE_FALL_BLOCK = 1; 		// Falls when possible
	public static final int TYPE_FLUID_BLOCK = 2; 		// Like water
	public static final int TYPE_VOLATILE_BLOCK = 3; 	// Fly up

	private int type; // Type of block
	private int hp; // Hardness of block
	private int maxhp; // Max Hardness of block
	private int firehp; // 
	private int fixhp; // Hardness of block
	private int maxfixhp; // Hardness of block
	private int lifeTime; // Time of life

	private Color defColor = Color.BLACK;
	private Color color = Color.BLACK;
	
	private int directionX = 0;

	private int acidResistance = 0;
	private int acid = 0;

	private int temperature = 15;
	private int fireResistance = 200;
	private int fire = 0;
	
	private int random10 = (int) (Math.random()*10);
	
	private boolean isFireBlock = false;
	
	private boolean isBurned;
	
	private int lightR, lightG, lightB;
	private boolean isLightBlock;
	
	public Block(int type, int hp) {
		this.type = type;
		this.hp = hp;
		maxfixhp = hp;
		firehp = hp;
		maxhp = hp;
		directionX = Math.random() < .5 ? 1 : -1;
	}
	
	public void setLightBlock(int r, int g, int b) {
		lightR = r;
		lightG = g;
		lightB = b;
	}
	
	public int getLightR() {
		return lightR;
	}
	public int getLightG() {
		return lightG;
	}
	public int getLightB() {
		return lightB;
	}
	
	public boolean isLightBlock() {
		if(lightR + lightG + lightB > 0) return true; //  fireResistance < fire && (type == TYPE_FIXED_BLOCK || type == TYPE_FALL_BLOCK) && !isFireBlock
		return isLightBlock;
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	public void setFireBlock(boolean isFireBlock) {
		this.isFireBlock = isFireBlock;
	}
	
	public boolean isFireBlock() {
		return isFireBlock;
	}
	
//	public void setFlameTemperature(int flameTemperature) {
//		this.flameTemperature = flameTemperature;
//	}
	
	public void setFireResistance(int fireResistance) {
		this.fireResistance = fireResistance;
	}
	
	public int getFireResistance() {
		return fireResistance;
	}
	
	public void setFire(int fire) {
		this.fire = fire;
	}
	
	public int getTemperature() {
		return temperature;
	}
	
	public void setFixhp(int fixhp) {
		this.fixhp = fixhp;
		maxfixhp = fixhp;
	}
	
	public void setAcidResistance(int acidResistance) {
		this.acidResistance = acidResistance;
	}
	
	public int getAcid() {
		return acid;
	}
	
	public void addAcid(int acid) {
		this.acid += acid;
	}
	
	public void setAcid(int acid) {
		this.acid = acid;
	}
	
	public void damage(int dmg) {
//		color = color.darker();
		fixhp -= dmg;
		if(fixhp <= 0) {
			hp += fixhp;
			fixhp = 0;
			if(hp <= 0) needDestroy = true;
		}
	}


	public void fixDamage(int dmg) {
		fixhp -= dmg;
		if(fixhp <= 0) {
			fixhp = 0;
		}
	}
	
	
	public int getFire() {
		return fire;
	}
	
	public void fire(int fire) {
//		if(type == TYPE_FLUID_BLOCK) return;
//		if(this.fire > fire) return;
		if(fire < 0) return;
		this.fire = fire;
		if(fire > fireResistance) {
			this.fire = fire;
		}
	}
	
	public void addfire(int fire) {
		if(type == TYPE_FLUID_BLOCK) return;
		if(this.fire > fire) return;
		if(fire < 0) return;
		if(fire > fireResistance) {
			this.fire += fire;
		}
	}
	
	public int acidDamage(int dmg) {
		if(acidResistance < dmg) {
			int admg = (dmg-acidResistance)/100;
			damage(admg);
			return admg;
		}
		return 0;
	}
	
	boolean needDestroy = false;
	
	private boolean isFallingFixedBlock() {
		return type == TYPE_FIXED_BLOCK && fixhp <= 0 && type != TYPE_VOLATILE_BLOCK;
	}
	
	int unmovetime = 0;
	
	private double vx, vy;
	
	public void setMoveVector(double vx, double vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	int updateID = 0;
	
	public boolean isMove() {
		return isMove;
	}
	
	boolean isMove;
	
	public void update(Block[][] map, Block[][] newmap, int x, int y, int w, int h, int uid) {  // TODO
		if(needDestroy) {
			newmap[x][y] = null;
			unmovetime = 0;
			return;
		}
		
		if(vy < 0 || (y < h-1 && map[x][y+1] == null) && type != TYPE_VOLATILE_BLOCK)
			vy += Game.GRAVITY;
		
		if(type == TYPE_FIXED_BLOCK && fixhp > 0) {
			vx = 0;
			vy = 0;
		}
		
		
		if(vy != 0) {
			if(vy < 0) {
				int targetY = y;
				for (int yy = -1; yy > vy; yy--) {
					if(y + yy  > 0) {
						if(map[x][y+yy] == null && newmap[x][y+yy] == null) {
							targetY = y+yy;
						}else {
//							break;
						}
					}else {
						vy = 0;
//						break;
					}
				}
				if(map[x][targetY] == null && newmap[x][targetY] == null) {
					newmap[x][targetY] = this;
					newmap[x][y] = null;
					
					y = targetY;
				}
				
			}
			if(vy > 0) {
				int targetY = y;
				for (int yy = 1; yy < vy; yy++) {
					if(y + yy  < h-1) {
						if(map[x][y+yy] == null && newmap[x][y+yy] == null) {
							targetY = y+yy;
						}else {
//							break;
						}
					}else {
//						break;
					}
				}
				if(map[x][targetY] == null && newmap[x][targetY] == null) {
					newmap[x][targetY] = this;
					newmap[x][y] = null;
					
					y = targetY;
				}
			}
		}
		
		if(vx != 0) {
			if(vx < 0) {
				int targetX = x;
				for (int xx = -1; xx > vx; xx--) {
					if(x + xx  > 0) {
						if(map[x+xx][y] == null && newmap[x+xx][y] == null) {
							targetX= x+xx;
						}else {
							break;
						}
					}else {
						break;
					}
				}
				if(map[targetX][y] == null && newmap[targetX][y] == null) {
					newmap[targetX][y] = this;
					newmap[x][y] = null;
				}
				vx += Game.SLOW_X;
				if(vx > 0) vx = 0;
			}
			if(vx > 0) {
				int targetX = x;
				for (int xx = 1; xx < vx; xx++) {
					if(x + xx  < w-1) {
						if(map[x+xx][y] == null && newmap[x+xx][y] == null) {
							targetX= x+xx;
						}else {
							break;
						}
					}else {
						break;
					}
				}
				if(map[targetX][y] == null && newmap[targetX][y] == null) {
					newmap[targetX][y] = this;
					newmap[x][y] = null;
				}
				vx -= Game.SLOW_X;
				if(vx < 0) vx = 0;
			}
			isMove = vx != 0 || vy != 0;
			return;
		}
		
		isMove = false;
		
//		if(type == TYPE_FIXED_BLOCK && fixhp > 0) return;
		
//		if(vx != 0) {
//			if(vx > 0) {
//				for (int xx = 0; xx < vx; xx++) {
//					if(x + xx + 2 < w) {
//						if(map[x+xx+1][y] == null) continue;
//					}
//					vx = 0;
//					newmap[x+xx][y] = this;
//					newmap[x][y] = null;
//				}
//			}
//		}
		
		if(canFallDown() && y < h-1) {
			Block block = map[x][y+1];
			if(block == null && newmap[x][y+1] == null) {
				newmap[x][y+1] = this;
				newmap[x][y] = null;
				unmovetime = 0;
				return;
			}
			if(block != null && (type == TYPE_FALL_BLOCK || isFallingFixedBlock()) && block.getType() == TYPE_FLUID_BLOCK) {
				newmap[x][y+1] = this;
				newmap[x][y] = block;
				map[x][y] = null;
				map[x][y+1] = null;
				unmovetime = 0;
				return;
			}
		}
		if(type == TYPE_VOLATILE_BLOCK) {
			color = new Color(
					Math.min(255, defColor.getRed()+125),
					Math.min(255, defColor.getGreen()+125),
					Math.min(255, defColor.getBlue()+125)
					);
			fire = 0;
			vy = 0;
			vx = 0;
			if(uid%2 == 0) {
				if(y > 0) {
					if(map[x][y-1] == null && newmap[x][y-1] == null) {
						newmap[x][y-1] = this;
						newmap[x][y] = null;
						
						map[x][y-1] = this;
						map[x][y] = null;
						unmovetime = 0;
						return;
					}
				}
			}else {
				if(Math.random() < .5) {
					if(x > 1) {
						if(map[x-1][y] == null && newmap[x-1][y] == null) {
							newmap[x-1][y] = this;
							newmap[x][y] = null;
							
							map[x-1][y] = this;
							map[x][y] = null;
							unmovetime = 0;
							return;
						}
					}
				}else {
					if(x < w-1) {
						if(map[x+1][y] == null && newmap[x+1][y] == null) {
							newmap[x+1][y] = this;
							newmap[x][y] = null;
							
							map[x+1][y] = this;
							map[x][y] = null;
							unmovetime = 0;
							return;
						}
					}
					
				}
			}
		}
		
		if(type == TYPE_FALL_BLOCK || isFallingFixedBlock()) {
			if(y < h-1) {
				if(directionX > 0) {
					if(x < w - 1) {
						if(map[x+1][y] == null && map[x+1][y+1] == null && newmap[x+1][y] == null && newmap[x+1][y+1] == null) {
							newmap[x+1][y+1] = this;
							newmap[x][y] = null;
							unmovetime = 0;
							return;
						}else {
							directionX = -1;
						}
					}else {
						directionX = -1;
					}
				}else if(directionX < 0) {
					if(x > 0) {
						if(map[x-1][y] == null && map[x-1][y+1] == null && newmap[x-1][y] == null && newmap[x-1][y+1] == null) {
							newmap[x-1][y] = this;
							newmap[x][y] = null;
							unmovetime = 0;
							return;
						}else {
							directionX = 1;
						}
					}else {
						directionX = 1;
					}
				}
			}
		}
		
		if(type == TYPE_FLUID_BLOCK) {
			if(directionX > 0) {
				if(x < w - 1) {
					if(map[x+1][y] == null && newmap[x+1][y] == null) {
						newmap[x+1][y] = this;
						newmap[x][y] = null;
						unmovetime = 0;
						return;
					}else {
						directionX = -1;
					}
				}else {
					directionX = -1;
				}
			}else if(directionX < 0) {
				if(x > 0) {
					if(map[x-1][y] == null && newmap[x-1][y] == null) {
						newmap[x-1][y] = this;
						newmap[x][y] = null;
						unmovetime = 0;
						return;
					}else {
						directionX = 1;
					}
				}else {
					directionX = 1;
				}
			}
		}
		

		unmovetime++;
		if(type == TYPE_VOLATILE_BLOCK && unmovetime > 100) {
			color = defColor;
			type = TYPE_FLUID_BLOCK;
			unmovetime = 0;
			return;
		}
		if(unmovetime > 1000 && vy >= 0) {
			if(unmovetime%10 == 0) {
				if(fixhp < maxfixhp/2)
				fixhp++;
			}
		}
		
	}
	
	public int getUnmovetime() {
		return unmovetime;
	}
	
	public void physicsUpdate(Block[][] map, Block[][] newmap, int x, int y, int w, int h, int uid) { // TODO
		if(isFireBlock) {

			int r = (int) (fire);
			if(r < 0) r = 0;
			lightR = r*2;
			lightG = r;
			lightB = r/5;
		}
		
		if(type == TYPE_FLUID_BLOCK && acid > 0) {
			
			boolean canDamage = true; // updateID%10 == 0;
				if(y < h - 1) {
					if(map[x][y+1] != null && newmap[x][y+1] != null) {
						int type = map[x][y+1].getType();
						if(type == TYPE_FLUID_BLOCK && map[x][y+1].getAcid() < acid) { //  
							int a = acid + map[x][y+1].getAcid();
							map[x][y+1].setAcid(a/2);
							acid = a - a/2;
						} else if(canDamage && type == TYPE_FIXED_BLOCK || type == TYPE_FALL_BLOCK) {
							acid -= map[x][y+1].acidDamage(acid);
						}
					}
				}

				if(y > 0) {
					if(map[x][y-1] != null && newmap[x][y-1] != null) {
						int type = map[x][y-1].getType();
						if(type == TYPE_FLUID_BLOCK && map[x][y-1].getAcid() < acid) {
							int a = acid + map[x][y-1].getAcid();
							map[x][y-1].setAcid(a/2);
							acid = a - a/2;
						}
					}
				}

				if(x < w - 1) {
					if(map[x+1][y] != null && newmap[x+1][y] != null) {
						int type = map[x+1][y].getType();
						if(type == TYPE_FLUID_BLOCK && map[x+1][y].getAcid() < acid) { 
							int a = acid + map[x+1][y].getAcid();
							map[x+1][y].setAcid(a/2);
							acid = a - a/2;
						} else if(canDamage && type == TYPE_FIXED_BLOCK || type == TYPE_FALL_BLOCK) {
							acid -= map[x+1][y].acidDamage(acid);
						}
					}
				}

				if(x > 0) {
					if(map[x-1][y] != null && newmap[x-1][y] != null) {
						int type = map[x-1][y].getType();
						if(type == TYPE_FLUID_BLOCK && map[x-1][y].getAcid() < acid) { 
							int a = acid + map[x-1][y].getAcid();
							map[x-1][y].setAcid(a/2);
							acid = a - a/2;
						} else if(canDamage && type == TYPE_FIXED_BLOCK || type == TYPE_FALL_BLOCK) {
							acid -= map[x-1][y].acidDamage(acid);
						}
					}
				}
			
			
			if(acid < 0) acid = 0;
			color = new Color(
					Math.max(0, defColor.getRed()-acid),
					Math.min(255,defColor.getGreen() + acid),
					Math.max(0, defColor.getBlue()-acid));
			
			lightG = acid*3;
		}
		if(updateID != uid) {
			if(isFireBlock) {
				if(y > 0) {
					Block b = newmap[x][y-1];
					if(b != null) {
						if(b.isBurned()) {
							b.destroy();
						}
					}
				}
			}
			if(fire > 0) {
				if(x > 0) {
					Block b = newmap[x-1][y];
					if(b != null) {
						if(b.getType() == TYPE_FLUID_BLOCK && !b.isFireBlock()) {
							if(isFireBlock || b.fireResistance < fire) b.type = TYPE_VOLATILE_BLOCK;
							fire = 0;
							if(isFireBlock) {
								extinguish();
							}
						}else {
							blendFire(b);
						}
//						b.updateID = uid;
					}
				}
				if(x < w-1) {
					Block b = newmap[x+1][y];
					if(b != null) {
						if(b.getType() == TYPE_FLUID_BLOCK && !b.isFireBlock()) {
							if(isFireBlock || b.fireResistance < fire) b.type = TYPE_VOLATILE_BLOCK;
							fire = 0;
							if(isFireBlock) {
								extinguish();
							}
						}else {
							blendFire(b);
						}
//						b.updateID = uid;
					}
				}
				if(y < h-1) {
					Block b = newmap[x][y+1];
					if(b != null) {
						if(b.getType() == TYPE_FLUID_BLOCK && !b.isFireBlock()) {
							if(isFireBlock || b.fireResistance < fire) b.type = TYPE_VOLATILE_BLOCK;
							fire = 0;
							if(isFireBlock) {
								extinguish();
							}
						}else {
							blendFire(b);
						}
//						b.updateID = uid;
					}
				}
				if(y > 0) {
					Block b = newmap[x][y-1];
					if(b != null) {
						if(b.getType() == TYPE_FLUID_BLOCK && !b.isFireBlock()) {
							if(isFireBlock || b.fireResistance < fire) b.type = TYPE_VOLATILE_BLOCK;
							fire = 0;
							if(isFireBlock) {
								extinguish();
							}
						}else {
							blendFire(b);
						}
//						b.updateID = uid;
					}
				}
				if(fireResistance < fire && (type == TYPE_FIXED_BLOCK || type == TYPE_FALL_BLOCK) && !isFireBlock) {
					int dmg = fire-fireResistance;
					hp -= dmg;
					if(!isFireBlock) fire -= dmg;
					if(fire < 0) fire = 0;
					
					if(hp <= 0) {
//						if(isBurned) needDestroy = true;
						if(maxhp < (fire-fireResistance/1.5)) needDestroy = true;
//						maxhp = hp = fireResistance/2;
						fixhp = 0;
						maxfixhp = 0;
						
//						fireResistance = 2*fire;
//						fireResistance *= 2;
						fire = 0;

						type = TYPE_FALL_BLOCK;
						int gray = 10 * random10;
						defColor = color = new Color(gray, gray, gray);
						
						isBurned = true;
					}
					
					int r = (int) (fire + maxhp - hp - 255 - Math.random()*100);
					if(r > 255*5) r = 255*5;
					if(r < 0) r = 0;
					
//					color = new Color(
//					Math.min(255, Math.max(0, Math.max(defColor.getRed(), r*2))),
//					Math.min(255, Math.max(0, Math.max(defColor.getGreen(), r))),
//					Math.min(255, Math.max(0, Math.max(defColor.getBlue(), r/5))));
					lightR = r*2;
					lightG = r;
					lightB = r/5;
//					fire /= 2;
				}else {
					if(acid <= 0 && !isFireBlock) {
						color = defColor;
						lightR = 0;
						lightG = 0;
						lightB = 0;
					}
				}
				
//				if(fire > fireResistance) {
//					int r = (int) (fire- fireResistance + Math.random()*100);
//					if(r < 0) r = 0;
//					color = new Color(
//					Math.min(255, Math.max(0, Math.max(defColor.getRed(), r*2))),
//					Math.min(255, Math.max(0, Math.max(defColor.getGreen(), r))),
//					Math.min(255, Math.max(0, Math.max(defColor.getBlue(), r/5))));
//				}
//				
			}
		}
		
		updateID = uid;
		
	}
	
	private void extinguish() {
		type = TYPE_FIXED_BLOCK;
		maxfixhp = fixhp = hp = maxhp = hp/2;
		isFireBlock = false;
		lightR = 0;
		lightG = 0;
		lightB = 0;
		color = defColor;
		vx = 0;
		vy = 0;
		isMove = false;
	}

	private void blendFire(Block b) {
		if(type == TYPE_FIXED_BLOCK || type == TYPE_FALL_BLOCK || type == TYPE_FLUID_BLOCK || isFireBlock) {
			int f = b.getFire();
			int add = (fire - f)/2;
			if(add < 0) return;
			b.fire(f + add);
		}
//		if(isFireBlock) add = fire;
//		fire -= add; 
		
//		if(add > 0 && fire > f && f + add > b.getFireResistance()) {
//			if(isFireBlock) {
//				b.fire(f/2);
//			}else {
			
//			if(fire < 0) {
//				fire = 0;
//				color = defColor;
//			}
			// if(!isFireBlock) 
//			}
//		}
	}

	private boolean canFallDown() {
		return type == TYPE_FALL_BLOCK || type == TYPE_FLUID_BLOCK || isFallingFixedBlock();
	}
	
	public void setColor(Color color) {
		this.color = color;
		defColor = color;
	}
	
	public Color getColor() {
		if(isFireBlock) {
			int r = (int) (fire + Math.random()*100);
			if(r < 0) r = 0;
			
			return new Color(
			Math.min(255, Math.max(0, Math.max(defColor.getRed(), r*2))),
			Math.min(255, Math.max(0, Math.max(defColor.getGreen(), r))),
			Math.min(255, Math.max(0, Math.max(defColor.getBlue(), r/5))));
		}
		return color;
	}
	
	public int getHp() {
		return hp;
	}
	
	public int getType() {
		return type;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}

	public void destroy() {
		needDestroy = true;
	}
	
	public boolean isBurned() {
		return isBurned;
	}


}
