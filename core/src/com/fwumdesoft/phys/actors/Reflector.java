package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.fwumdesoft.phys.Main;
import com.fwumdesoft.phys.TransformType;

/**
 * Can reflect waves. All reflectors should have more height than width when
 * {@link #getRotation()} = 0.
 */
public class Reflector extends HitboxActor implements TransformType {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.REFLECTOR));
	}
	
	private byte fixed;
	
	public Reflector() {
		this((byte)0);
	}
	
	/**
	 * Instantiates a new Reflector with the specified fixation.
	 * @param fixedType A bit mask value setting the fixation of the Reflector. Used
	 * to determine what kind of modifications the user can make to this Reflector.
	 */
	public Reflector(byte fixedType) {
		setSize(3, 20);
		setOrigin(Align.center);
		fixed = fixedType;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	/**
	 * Calculates this Reflectors normal based on its current rotation.
	 * @return The line perpendicular
	 */
	public Vector2 getNormal() {
		return Vector2.X.cpy().rotate(getRotation());
	}

	@Override
	public byte getFixed() {
		return fixed;
	}

	@Override
	public void setFixed(byte fixed) {
		this.fixed = fixed;
	}
}
