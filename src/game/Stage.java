package game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import render.Render;

public abstract class Stage {

	public boolean isMousePressed;
	public int mouseX, mouseY;

	public abstract void fixupdate();
	
	public abstract void update();

	public abstract void draw(Render render);

	public abstract void keyReleased(KeyEvent e);
	public abstract void keyPressed(KeyEvent e);
	public abstract void mouseClicked(MouseEvent e);

	public abstract void mouseMoved(MouseEvent e);
	public abstract void mouseDragged(MouseEvent e);

	public abstract void mouseWheelMoved(MouseWheelEvent e);
}
