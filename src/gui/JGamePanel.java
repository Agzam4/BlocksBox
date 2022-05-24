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
import render.Render;

public class JGamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final long fixUpdadeTime = 25;
	private static final long updadeTime = 10;
	
	Stage game;
	Render render;
	
	public void start() {
		setFocusable(true);
		
		Main.screen = getSize();
		game = new Game(Main.screen.width*2/Block.BLOCKSIZE, Main.screen.height*2/Block.BLOCKSIZE);
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
				}
			}
		}).start();
		
		new Thread(() -> {
			long start;
			long wait;
			while (true) {
				start = System.nanoTime();
				
				game.update();
				render.clear();
				game.draw(render);
				
				draw();
				
				wait = updadeTime - (System.nanoTime() - start)/1_000_000;
				if(wait > 5) {
					sleep(wait);
				}
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
	}
	
	private void draw() {
		Graphics2D g = (Graphics2D) getGraphics();
		g.drawImage(render.getImage(), 0, 0, null);
	}
	
	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
