package gameObjects;

import game.Game;
import render.Render;

public abstract class GameObject {

	public abstract void fixupdate();
	public abstract void update();
	public abstract void draw(Render render);
	
	protected double x, y;
	public boolean needDestroy;
	
	protected Game game;
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isNeedDestroy() {
		return needDestroy;
	}
}
