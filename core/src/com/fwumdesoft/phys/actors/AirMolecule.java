package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.fwumdesoft.phys.Main;

/**
 * Represents an air molecule that can be moved by a Wave.
 */
public class AirMolecule extends HitboxActor implements Poolable {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.AIR));
	}
	
	private Vector2 markedPos;
	
	public AirMolecule() {
		setSize(3f, 3f);
		setOrigin(Align.center);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public void positionChanged() {
		super.positionChanged();
		
		//Check if molecule has collided with a reflector
		if(getStage() == null) return;
		for(Actor actor : getStage().getActors()) {
			if(actor instanceof Reflector) {
				Reflector refl = (Reflector)actor;
				//air molecule should reset position when they hit a reflector so the Wave can cause them to displace
				if(Intersector.overlapConvexPolygons(hitbox(), refl.hitbox())) { 
					clearActions();
					MoveToAction moveAction = Actions.moveTo(markedPos.x, markedPos.y, 1f, Interpolation.sineOut);
					addAction(moveAction);
				}
			} else if(actor instanceof Refractor) {
				Refractor refr = (Refractor)actor;
				if(Intersector.overlapConvexPolygons(hitbox(), refr.hitbox())) { 
					clearActions();
					MoveToAction moveAction = Actions.moveTo(markedPos.x, markedPos.y, 1f, Interpolation.sineOut);
					addAction(moveAction);
				}
			} else if(actor instanceof Wall) {
				Wall wall = (Wall)actor;
				if(Intersector.overlapConvexPolygons(wall.hitbox(), hitbox())) {
					clearActions();
					MoveToAction moveAction = Actions.moveTo(markedPos.x, markedPos.y, 1f, Interpolation.sineOut);
					addAction(moveAction);
				}
			}
		}
	}
	
	/**
	 * Marks an important position for this AirMolecule. (ex. original position).
	 * @param pos Position to be marked.
	 */
	public void markPosition(Vector2 pos) {
		markedPos = pos;
	}
	
	@Override
	public void reset() {
		hitbox().dirty();
	}
	
	@Override
	public String toString() {
		String result = super.toString();
		result += ": {X: " + getX() + ", Y: " + getY() + ", Rotation: " + getRotation();
		result += "}";
		return result;
	}
}
