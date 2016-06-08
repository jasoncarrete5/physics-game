package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.fwumdesoft.phys.Main;

/**
 * This Actor represents an anechoic wall.
 */
public class Wall extends HitboxActor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.WALL));
	}
	
	public Wall() {
		setSize(5, 50);
		setOrigin(Align.center);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public String observerString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Wall: {");
		sb.append("\nPosition: (" + MathUtils.round(getX()) + ", " + MathUtils.round(getY()) + ")");
		sb.append("\nRotation: " + MathUtils.round(getRotation() % 360));
		sb.append("\nRotatable: " + isRotatable());
		sb.append("\nMovable: " + isMovable());
		sb.append("\n}");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Wall: { ");
		sb.append(" Position: (" + MathUtils.round(getX()) + ", " + MathUtils.round(getY()) + ")");
		sb.append(" Rotation: " + MathUtils.round(getRotation() % 360));
		sb.append(" }");
		return sb.toString();
	}
}
