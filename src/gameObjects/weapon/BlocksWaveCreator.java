package gameObjects.weapon;

public class BlocksWaveCreator extends Weapon {

	public BlocksWaveCreator() {
		super(0);
	}
	

	int reloadTime = 25;
	int reload = 0;

	@Override
	public void fixupdate() {

		if(reload > 0) reload--;

		if(game.isMousePressed) {
			if(reload <= 0) {
				int vx = Math.cos(game.mouseAlign) > 0 ? 1 : -1;
				
				BlocksWave wave = new BlocksWave(vx);
				wave.setPosition(game.getPlayerBlockX(), game.getPlayerBlockY());
				game.addGameObject(wave);
				reload = reloadTime;
			}
		}
	}
}
