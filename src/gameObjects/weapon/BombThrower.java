package gameObjects.weapon;

public class BombThrower extends Weapon {

	public BombThrower() {
		super(100);
	}

	int reloadTime = 25;
	int throwPower = 3;
	
	int reload = 0;
	
	@Override
	public void fixupdate() {
		if(reload > 0) reload--;
		
		if(game.isMousePressed) {
			if(reload <= 0) {
				Bomb bomb = new Bomb();
				bomb.setPosition(game.getPlayerBlockX(), game.getPlayerBlockY());
				bomb.setVx(throwPower*Math.cos(game.mouseAlign));
				bomb.setVy(throwPower*Math.sin(game.mouseAlign));
				game.addGameObject(bomb);
				reload = reloadTime;
			}
		}
	}
}
