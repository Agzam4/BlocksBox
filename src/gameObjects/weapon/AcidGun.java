package gameObjects.weapon;

import java.awt.Color;

import blocks.Block;

public class AcidGun extends BlockGun {

	public AcidGun() {
		super(300);
	}
	
	@Override
	protected Block createBlock(int damage) {
		Block acid = new Block(Block.TYPE_FLUID_BLOCK, 10000);
		acid.setAcid(damage);
		acid.setMoveVector(throwPower*Math.cos(game.mouseAlign), throwPower*Math.sin(game.mouseAlign));
		acid.setColor(Color.BLUE);
		return acid;
	}
}
