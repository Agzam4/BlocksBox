package gameObjects.weapon;

import blocks.Block;

public class BlockGun extends Weapon {
	
	public BlockGun(int damage) {
		super(damage);
	}

	int throwPower = 15;
	
	@Override
	public void fixupdate() {
		if(game.isMousePressed) {
			int x = game.getPlayerBlockX(), y = game.getPlayerBlockY();
			if(game.isInArray(x, y)) {
				for (int xx = -1; xx <= 1; xx++) {
					if(!game.isHardBlock(x+xx, y)) game.map[x+xx][y] = createBlock(damage);
				}
			}
			System.out.println();
		}
	}
	
	protected Block createBlock(int damage) {
		return null;
	}
}
