package gui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import blocks.Block;
import game.Game;
import game.Stage;
import render.Debug;
import render.Render;

public class JGamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final long fixUpdadeTime = 25;
//	private static final long updadeTime = 10;
	
	Stage game;
	Render render;
	
	int targetScale = Render.SCALE;
	
	
	public void start() {
		setFocusable(true);

		int kw = 5;
		int kh = 5;
		Main.screen = getSize();
		game = new Game(Main.screen.width*kw/Block.BLOCKSIZE, Main.screen.height*kh/Block.BLOCKSIZE);
		render = new Render(this.getSize());
		
		new Thread(() -> {
			long start;
			long wait;
			while (true) {
				start = System.nanoTime();
				
				game.fixupdate();
				
				wait = fixUpdadeTime - (System.nanoTime() - start)/1_000_000;
				if(wait > 5) {
					sleep(wait);
				}else {
					if(wait < 0) Debug.fixUpdateLags = -wait;
				}
			}
		}).start();
		
		new Thread(() -> {
			long start;
			long time;

			long timer = 0;
			long updates = 0;
			
			while (true) {
				start = System.nanoTime();
				
				game.update();
				render.clear();
				game.draw(render);
				draw();
				if(targetScale != Render.SCALE) {
					System.out.println(targetScale);
					render.setScale(targetScale);
				}
				
				time = (System.nanoTime() - start);
				updates++;
				
				timer += time/1_000_000;
				if(timer > 1_000) {
					timer -= 1_000;
					Debug.fps_2 = updates;
					updates = 0;
				}
				
				Debug.fps = 1_000_000*6_000/time;
				
//				wait = updadeTime - (System.nanoTime() - start)/1_000_000;
//				if(wait > 5) {
//					sleep(wait);
//				}else {
//					if(wait < 0) Debug.drawUpdateLags = -wait;
//				}
			}
		}).start();
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				game.keyReleased(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				game.keyPressed(e);
				
				if(e.getKeyCode() == KeyEvent.VK_EQUALS) {
					if(targetScale < Block.BLOCKSIZE) {
						targetScale++;
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_MINUS) {
					if(targetScale > 1) {
						targetScale--;
					}
				}
			}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				game.isMousePressed = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				game.isMousePressed = true;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				game.mouseClicked(e);
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				game.mouseMoved(e);
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				game.mouseDragged(e);
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				game.mouseWheelMoved(e);
			}
		});
		
		setFocusable(true);
	}
	
	private void draw() {
		Graphics2D g = (Graphics2D) getGraphics();
		if(render.isShowUi()) {
			g.drawImage(render.getImage(), 0, 0, null);
		}else {
			g.drawImage(render.getImage(), 0, 0, getWidth(), getHeight(), null);
		}
	}
	
	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
