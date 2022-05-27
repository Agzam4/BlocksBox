package generator;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import blocks.Block;
import game.Game;

public class Generator {

	public Generator(int w, int h) {
		boxes = new ArrayList<>();
		boxes.add(new Box(0, 0, w, h));
	}
	
	int roomS = 1000;
	int wallSize = 1;
	int randomSize = 25;
	int minW = 25;
	int minH = 50;
	ArrayList<Box> boxes;
	
	public void generate2(Game game) {
		int scale = 10;
		
		boolean[][] bs = new boolean[game.w/scale][game.h/scale];
		int[][] is = new int[game.w/scale][game.h/scale];

		for (int y = 0; y < game.h/scale; y++) {
			for (int x = 0; x < game.w/scale; x++) {
				bs[x][y] = Math.random() < .2;
			}
		}
		final Point points[] = new Point[] {
				new Point(1, 0),
				new Point(-1, 0),
				new Point(0, 1),
				new Point(0, -1)
		};
		for (int i = 0; i < 10; i++) {
			for (int y = 0; y < game.h/scale; y++) {
				for (int x = 0; x < game.w/scale; x++) {
					if(bs[x][y]) {
						is[x][y] = 225;
						continue;
					}
					int count = 1;
					int value = is[x][y];
					for (Point p : points) {
						if(x + p.x >= 0 && y + p.y >= 0 && x + p.x < game.w/scale && y + p.y < game.h/scale) {
							count++;
							value += is[x+p.x][y+p.y]; //bs[x+p.x][y+p.y] ? 255 : 
						}
					}
					is[x][y] = value/count;
//					is[x][y] = (int) (is[x][y]/1.05);
				}
			}
		}
		
		//
		// No scale
		//

		boolean[][] map = new boolean[game.w][game.h];
		int[][] mapcolors = new int[game.w][game.h];
		
		for (int y = 0; y < game.h/scale; y++) {
			for (int x = 0; x < game.w/scale; x++) {
				
				for (int yy = 0; yy < scale; yy++) {
					for (int xx = 0; xx < scale; xx++) {
						map[x*scale+xx][y*scale+yy] = is[x][y] < Math.random()*50 + 150;
						mapcolors[x*scale+xx][y*scale+yy] = is[x][y];
					}
				}
			}
		}
		
		final Point points8[] = new Point[] {
				new Point(1, 0),
				new Point(-1, 0),
				new Point(0, 1),
				new Point(0, -1),

				new Point(1, 1),
				new Point(1, -1),
				
				new Point(-1, 1),
				new Point(-1, -1),
		};
		
		for (int i = 0; i < 5; i++) {
			boolean[][] newmap = new boolean[game.w][game.h];
			for (int y = 0; y < game.h; y++) {
				for (int x = 0; x < game.w; x++) {
					int count = 0;
					int color = mapcolors[x][y]/2;
					int ccolor = 1;
					for (Point p : points8) {
						if(game.isInArray(x+p.x, y+p.y)) {
							if(map[x + p.x][y + p.y]) {
								count++;
								color += mapcolors[x + p.x][y + p.y];
								ccolor++;
							}
						}else {
							count++;
						}
					}
					mapcolors[x][y] = color/(count+1);
					
					if(map[x][y]) {
						if(count >= 4) { // 4
							newmap[x][y] = true;
						}else {
							newmap[x][y] = false;
						}
					}else {
						if(count >= 5) { // 5
							newmap[x][y] = true;
						}else {
							newmap[x][y] = false;
						}
					}
				}
			}
			map = newmap;
		}
		
		// AIR
		
		int startY = game.h/5;
		
		int grassPoints[] = new int[game.w];
		for (int x = 0; x < game.w; x++) {
			startY += Math.random()*8-4 + Math.cos(x/10d);
			if(startY > game.h/4) startY = game.h/4;
			if(startY < game.h/7) startY = game.h/7;
			for (int y = 0; y < game.h; y++) {
				grassPoints[x] = startY;
//				if(map[x][y] || map[x+1][y]) {
//					grassPoints[x] = y + (int) (10 + 10 * Math.cos(x/10d) + 5 * Math.cos(x/5d));
//					break;
//				}
			}
		}
		for (int i = 0; i < 3; i++) {
			grassPoints[0] = (grassPoints[0]+grassPoints[1])/2;
			int grassPointsNew[] = new int[game.w];
			for (int j = 1; j < grassPointsNew.length - 1; j++) {
				grassPointsNew[j] = (grassPoints[j-1] + grassPoints[j] + grassPoints[j+1])/3;
//				grassPointsNew[j] = (grassPoints[j-1] + grassPoints[j] + grassPoints[j+1])/2;
			}
			grassPoints[grassPointsNew.length - 1] = (grassPoints[grassPointsNew.length - 1]+grassPoints[grassPointsNew.length - 2])/2;
			grassPoints = grassPointsNew;
		}
		grassPoints[0] = grassPoints[1];
		
		int[] endGrassY  = new int[game.w];
		for (int x = 0; x < game.w; x++) {
			System.out.print(grassPoints[x] + " ");
			for (int y = 0; y < grassPoints[x]+50; y++) {
				map[x][y] = true;
			}
			endGrassY[x] = (int) (grassPoints[x]+80 + 5*Math.cos(x/10d));
			for (int y = grassPoints[x]+50; y < grassPoints[x]+80 + 5*Math.cos(x/10d); y++) {
				map[x][y] = false;
				mapcolors[x][y] = 200;
			}
		}
		System.out.println();
		
		System.out.println("Creating blocks...");
		for (int y = 0; y < game.h; y++) {
			for (int x = 0; x < game.w; x++) {
				if(map[x][y]) continue;
				Boolean isStone = y > game.h/3 + endGrassY[x]/2;
				Boolean isGrass = y < endGrassY[x];
				Block block = new Block(Block.TYPE_FIXED_BLOCK, isGrass ? 25 : (isStone ? (mapcolors[x][y] + y*10) : mapcolors[x][y] + y));
				block.setFixhp((int) ((isStone ? mapcolors[x][y] * 10 : mapcolors[x][y]) + Math.random()*250));
				if(isGrass) block.setFixhp((int) (Math.random()*25 + 10));
				int gray = (int) ((mapcolors[x][y])/2)*10 - 10;
				if(gray < 0) gray = 0;
				if(gray > 100) gray = 100;
				
				boolean hasAir = false;
				for (Point p : points8) {
					if(game.isInArray(x+p.x, y+p.y)) {
						if(map[x + p.x][y + p.y]) {
							hasAir = true;
							break;
						}
					}
				}
				if(hasAir) {
					gray = 0;
				}
//				gray = 255 - gray;
				if(isGrass) {
					if(hasAir) block.setColor(new Color(128,204,95).darker());
					else block.setColor(Math.random() < .5 ? new Color(100,158,75).darker() : new Color(81,127,60).darker());
				}else if(isStone) {
					block.setColor(new Color(gray,gray,gray));
				}else if(y == endGrassY[x]) {
					block.setColor(new Color(62, 96, 45).darker());
				}else {
					block.setColor(new Color(gray,gray*2/3,gray/2));
				}
				game.map[x][y] = block;
			}
		}
	}
	
	public void generate(Game game) {
			
		for (int i = 0; i < 25; i++) {
			cutBox();
		}

		boolean[][] map = new boolean[game.w][game.h];
		int[][] mapcolors = new int[game.w][game.h];
		for (int y = 0; y < game.h; y++) {
			for (int x = 0; x < game.w; x++) {
				map[x][y] = true;
			}
		}
		
		for (Box box : boxes) {
			for (int y = box.y+wallSize; y < box.y + box.h - wallSize; y++) {
				for (int x = box.x+wallSize; x < box.x + box.w - wallSize; x++) {
					map[x][y] = Math.random() < .5 + (y/game.h)/2d;
				}
			}
			
			for (int y = box.y+wallSize + randomSize; y < box.y + box.h - wallSize - randomSize; y++) {
				for (int x = box.x+wallSize + randomSize; x < box.x + box.w - wallSize - randomSize; x++) {
					map[x][y] = false;;
				}
			}

//			for (int y = box.y + wallSize; y < box.y + box.h - wallSize; y++) {
//				for (int x = box.x + wallSize; x < box.x + box.w - wallSize; x++) {
//					game.map[x][y] = null;
//				}
//			}
		}
		
		final Point points[] = new Point[] {
				new Point(1, 0),
				new Point(-1, 0),
				new Point(0, 1),
				new Point(0, -1),

				new Point(1, 1),
				new Point(1, -1),
				
				new Point(-1, 1),
				new Point(-1, -1),
		};
		
		int scale = 2;
		boolean[][] smap = new boolean[game.w/scale][game.h/scale];
		for (int y = 0; y < game.h/scale; y++) {
			for (int x = 0; x < game.w/scale; x++) {
				int count = 0;
				for (int yy = 0; yy < scale; yy++) {
					for (int xx = 0; xx < scale; xx++) {
						if(map[x*scale+xx][y*scale+yy]) {
							count++;
						}
					}
				}
				smap[x][y] = count >= scale*scale/2;
			}
		}
//		map = smap;

		
		for (Box b1 : boxes) {
			for (Box b2 : boxes) {
				if(!b1.equals(b2)) {
					int sx = Math.min(b1.getCX(), b2.getCX());
					int ex = Math.max(b1.getCX(), b2.getCX());
					int sy = Math.min(b1.getCY(), b2.getCY());
					int ey = Math.max(b1.getCY(), b2.getCY());
					double hypot = Math.hypot(sx-ex, sy-ey);
					double hypot2 = Math.min(Math.hypot(b1.w, b1.h), Math.hypot(b2.w, b2.h))/2;
					if(hypot > hypot2) continue;
					for (int y = sy; y < ey; y++) {
						for (int x = sx; x < ex; x++) {
							smap[x/scale][y/scale] = false;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < 5; i++) {
			boolean[][] newmap = new boolean[game.w/scale][game.h/scale];
			for (int y = 0; y < game.h/scale; y++) {
				for (int x = 0; x < game.w/scale; x++) {
					int count = 0;
					for (Point p : points) {
						if(x + p.x >= 0 && y + p.y >= 0 && x + p.x < game.w/scale && y + p.y < game.h/scale) {
							if(smap[x + p.x][y + p.y]) {
								count++;
							}
						}else {
							count++;
						}
					}
					if(smap[x][y]) {
						if(count >= 4) {
							newmap[x][y] = true;
							for (int yy = 0; yy < scale; yy++) {
								for (int xx = 0; xx < scale; xx++) {
									if(mapcolors[x*scale+xx][y*scale+yy] == 0)
									mapcolors[x*scale+xx][y*scale+yy] = count;
								}
							}
						}else {
							newmap[x][y] = false;
						}
					}else {
						if(count >= 5) {
							newmap[x][y] = true;
							for (int yy = 0; yy < scale; yy++) {
								for (int xx = 0; xx < scale; xx++) {
									if(mapcolors[x*scale+xx][y*scale+yy] == 0)
									mapcolors[x*scale+xx][y*scale+yy] = count;
								}
							}
						}else {
							newmap[x][y] = false;
						}
					}
				}
			}
			smap = newmap;
		}
		
		for (int y = 0; y < game.h/scale; y++) {
			for (int x = 0; x < game.w/scale; x++) {
				
				for (int yy = 0; yy < scale; yy++) {
					for (int xx = 0; xx < scale; xx++) {
						map[x*scale+xx][y*scale+yy] = smap[x][y];
					}
				}
			}
		}
		
		for (int i = 0; i < 3; i++) {
			boolean[][] newmap = new boolean[game.w][game.h];
			for (int y = 0; y < game.h; y++) {
				for (int x = 0; x < game.w; x++) {
					int count = 0;
					for (Point p : points) {
						if(game.isInArray(x+p.x, y+p.y)) {
							if(map[x + p.x][y + p.y]) {
								count++;
							}
						}else {
							count++;
						}
					}
					mapcolors[x][y]+=count;
					if(map[x][y]) {
						if(count >= 4) {
							newmap[x][y] = true;
							mapcolors[x][y]+=8;
						}else {
							newmap[x][y] = false;
						}
					}else {
						if(count >= 5) {
							mapcolors[x][y]+=8;
//							mapcolors[x][y]++;
							newmap[x][y] = true;
						}else {
							newmap[x][y] = false;
						}
					}
				}
			}
			map = newmap;
		}
		
		System.out.println("Creating blocks...");
		for (int y = 0; y < game.h; y++) {
			for (int x = 0; x < game.w; x++) {
				if(!map[x][y]) continue;
				Block block = new Block(Block.TYPE_FIXED_BLOCK, 1);
				block.setFixhp(1);
				int gray = (int) ((mapcolors[x][y] - Math.random()*1)/1)*1;
				if(gray > 255) gray = 255;
				block.setColor(new Color(gray,gray,gray));
				game.map[x][y] = block;
			}
		}
	}
	
	private void cutBox() {
		System.out.println("UnCut: " + boxes.size());
		ArrayList<Box> newboxes = new ArrayList<>();
		for (Box box : boxes) {
			if(box.getS() > roomS*2 && box.w > minW*2 && box.h > minH*2) {
				if(box.w > box.h) { // |||||||
					int minX = roomS/box.h;
					int x = minX + (int) (Math.random()*(box.w-minX*2));
					newboxes.add(new Box(box.x, box.y, x, box.h));
					newboxes.add(new Box(box.x + x, box.y, box.w - x, box.h));
				}else {					 // -------
					int minY = roomS/box.w;
					int y = minY + (int) (Math.random()*(box.h-minY*2));
					newboxes.add(new Box(box.x, box.y, box.w, y));
					newboxes.add(new Box(box.x, box.y + y, box.w, box.h - y));
				}
			}else {
				newboxes.add(box);
			}
		}
		boxes = newboxes;
		System.out.println("Cut: " + boxes.size());
	}
}
