package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.fwumdesoft.phys.Main;

public class Reflector extends HitboxActor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.REFLECTOR));
	}
	
	private byte fixed;
	
	/**
	 * Instantiates a new Reflector with the specified fixation.
	 * @param fixedType A bit mask value setting the fixation of the Reflector. Used
	 * to determine what kind of modifications the user can make to this Reflector.
	 */
	public Reflector(byte fixedType) {
		setSize(5, 20);
		setOrigin(Align.center);
		fixed = fixedType;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	public byte getFixedMask() {
		return fixed;
	}
}
