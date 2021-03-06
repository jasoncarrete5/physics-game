package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.fwumdesoft.phys.Main;

/**
 * This Actor can refract waves.
 */
public class Refractor extends HitboxActor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.REFRACTOR));
	}
	
	private float refractionIndex;
	
	public Refractor() {
		this(1, Fixed.notFixed);
	}
	
	public Refractor(float refractionIndex, byte fixedMask) {
		setSize(5, 30);
		setOrigin(Align.center);
		setFixed(fixedMask);
		this.refractionIndex = refractionIndex;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	/**
	 * Calculates the angle of refraction using Snell's Law.
	 * @param incidence The angle of incidence.
	 * @return The angle of refraction based on the angle of incidence
	 * and the refractionIndex.
	 */
	public float getRefractionAngle(float incidence) {
		return (float)Math.toDegrees(Math.asin(MathUtils.sinDeg(incidence) / refractionIndex));
	}
	
	public Vector2 getNormal() {
		return Vector2.X.cpy().rotate(getRotation());
	}
	
	public float getRefractionIndex() {
		return refractionIndex;
	}
	
	public void setRefractionIndex(float refractionIndex) {
		this.refractionIndex = refractionIndex;
	}
	
	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);
		refractionIndex = jsonData.getFloat("index");
	}
	
	@Override
	public void write(Json json) {
		super.write(json);
		json.writeValue("index", refractionIndex);
	}
	
	@Override
	public String observerString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Refractor: {");
		sb.append("\nPosition: (" + MathUtils.round(getX()) + ", " + MathUtils.round(getY()) + ")");
		sb.append("\nRotation: " + MathUtils.round(getRotation() % 360));
		sb.append("\nRefraction Index: " + refractionIndex);
		sb.append("\nRotatable: " + isRotatable());
		sb.append("\nMovable: " + isMovable());
		sb.append("\n}");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Refractor: { ");
		sb.append(" Position: (" + MathUtils.round(getX()) + ", " + MathUtils.round(getY()) + ")");
		sb.append(" Rotation: " + MathUtils.round(getRotation() % 360));
		sb.append(" Refraction Index: " + refractionIndex);
		sb.append(" }");
		return sb.toString();
	}
}
