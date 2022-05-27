package gameObjects.weapon;

public class FireBombThrower extends Weapon {

	public FireBombThrower() {
		super(100);
	}

	int reloadTime = 50; // 25
	int throwPower = 5; // 5
	
	int reload = 0;
	
	@Override
	public void fixupdate() {
		if(reload > 0) reload--;
		
		if(game.isMousePressed) {
			if(reload <= 0) {
				FireBomb bomb = new FireBomb();
				bomb.setPosition(game.getPlayerBlockX(), game.getPlayerBlockY());
				bomb.setVx(throwPower*Math.cos(game.mouseAlign));
				bomb.setVy(throwPower*Math.sin(game.mouseAlign));
				game.addGameObject(bomb);
				reload = reloadTime;
			}
		}
	}
}
