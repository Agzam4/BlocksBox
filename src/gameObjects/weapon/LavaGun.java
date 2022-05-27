package gameObjects.weapon;

import java.awt.Color;

import blocks.Block;

public class LavaGun extends BlockGun {

	public LavaGun() {
		super(1000);
	}

	@Override
	protected Block createBlock(int damage) {
		Block lava = new Block(Block.TYPE_FLUID_BLOCK, 1000);
		lava.setFireBlock(true);
		lava.setFire(250);
		lava.setFireResistance(250);
		lava.setMoveVector(throwPower*Math.cos(game.mouseAlign), throwPower*Math.sin(game.mouseAlign));
//		lava.setFireBlock(true);
		int gray = (int) (Math.random()*50);
		lava.setColor(new Color(gray, gray, gray));
		return lava;
	}
}
