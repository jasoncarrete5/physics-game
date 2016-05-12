package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.fwumdesoft.phys.Main;

public class Refractor extends HitboxActor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.REFRACTOR));
	}
	
	private float refractionIndex;
	private byte fixed;
	
	public Refractor(float refractionIndex, byte fixedMask) {
		setSize(3, 20);
		setOrigin(Align.center);
		this.refractionIndex = refractionIndex;
		fixed = fixedMask;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	/**
	 * Calculates the angle of refraction of a wave based on its angle
	 * of incidence with the refractor.
	 * @param incidence The angle of incidence.
	 * @return The angle of refraction based on the angle of incidence
	 * and the refractionIndex.
	 */
	public float getRefractionAngle(float incidence) {
		return MathUtils.radDeg * (float)Math.asin(MathUtils.sinDeg(incidence) / refractionIndex);
		
	}
	
	public Vector2 getNormal() {
		return Vector2.X.cpy().rotate(getRotation());
	}
}
