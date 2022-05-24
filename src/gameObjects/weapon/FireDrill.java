package gameObjects.weapon;

public class FireDrill extends Drill {
	
	public FireDrill() {
		damage = 500;
		size = 30;
		d = 4;
	}

	@Override
	protected void damage(int xx, int yy) {
		int fr = game.map[xx][yy].getFireResistance();
//		if(damage < fr) return;
		game.map[xx][yy].fire(damage);
		if(game.map[xx][yy].isBurned()) game.map[xx][yy].destroy();
	}
}
