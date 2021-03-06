package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.fwumdesoft.phys.actors.Fixed;
import com.fwumdesoft.phys.actors.HitboxActor;
import com.fwumdesoft.phys.actors.Receiver;
import com.fwumdesoft.phys.actors.Reflector;
import com.fwumdesoft.phys.actors.Refractor;
import com.fwumdesoft.phys.actors.Transmitter;
import com.fwumdesoft.phys.actors.Wall;

/**
 * Contains all actors in a level and contains all of their meta data to
 * describe how they can be used such as whether they are fixed or not.
 */
public class Level {
	/** The name of the level file. */
	private String name, hint;
	private Array<Reflector> reflectors;
	private Array<Refractor> refractors;
	private Array<Wall> walls;
	private Array<Receiver> receivers;
	private Array<Transmitter> transmitters;
	
	public Level() {
		reflectors = new Array<>();
		refractors = new Array<>();
		walls = new Array<>();
		receivers = new Array<>();
		transmitters = new Array<>();
	}

	public String getHint() {
		return hint;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @return An Array of all actors in this level that have fixed positions.
	 */
	public Array<HitboxActor> getFixedPositionActors() {
		Array<HitboxActor> fixedActors = new Array<>();
		for(HitboxActor actor : getAllActors()) {
			if((actor.getFixed() & Fixed.positionFixed) == Fixed.positionFixed) {
				fixedActors.add(actor);
			}
		}
		return fixedActors;
	}
	
	/**
	 * @return An Array of all actors that are movable.
	 */
	public Array<HitboxActor> getNotFixedPositionActors() {
		Array<HitboxActor> fixedActors = new Array<>();
		for(HitboxActor actor : getAllActors()) {
			if((actor.getFixed() & Fixed.positionFixed) != Fixed.positionFixed) {
				fixedActors.add(actor);
			}
		}
		return fixedActors;
	}
	
	/**
	 * @return An Array of all actors in this level.
	 */
	public Array<HitboxActor> getAllActors() {
		Array<HitboxActor> actors = new Array<>();
		for(HitboxActor actor : reflectors) {
			actors.add(actor);
		}
		for(HitboxActor actor : refractors) {
			actors.add(actor);
		}
		for(HitboxActor actor : walls) {
			actors.add(actor);
		}
		for(HitboxActor actor : receivers) {
			actors.add(actor);
		}
		for(HitboxActor actor : transmitters) {
			actors.add(actor);
		}
		return actors;
	}
	
	/**
	 * Adds the specified Actor to this level.
	 * @param a Actor to be added.
	 */
	public void add(Actor a) {
		if(a instanceof Reflector) {
			reflectors.add((Reflector)a);
		} else if(a instanceof Refractor) {
			refractors.add((Refractor)a);
		} else if(a instanceof Wall) {
			walls.add((Wall)a);
		} else if(a instanceof Receiver) {
			receivers.add((Receiver)a);
		} else if(a instanceof Transmitter) {
			transmitters.add((Transmitter)a);
		} else {
			throw new IllegalArgumentException("Invalid Actor");
		}
	}
	
	public Array<Reflector> getReflectors() {
		return reflectors;
	}
	
	public Array<Refractor> getRefractors() {
		return refractors;
	}
	
	public Array<Wall> getWalls() {
		return walls;
	}
	
	public Array<Receiver> getReceivers() {
		return receivers;
	}
	
	public Array<Transmitter> getTransmitters() {
		return transmitters;
	}
	
	public void removeReflector(Reflector a) {
		reflectors.removeValue(a, true);
	}
	
	public void removeRefractor(Refractor a) {
		refractors.removeValue(a, true);
	}
	
	public void removeWall(Wall a) {
		walls.removeValue(a, true);
	}
	
	public void removeReceiver(Receiver a) {
		receivers.removeValue(a, true);
	}
	
	public void removeTransmitter(Transmitter a) {
		transmitters.removeValue(a, true);
	}
	
	/**
	 * Loads a level from a file.
	 * @param lvlName name of the level file to load
	 * @return A Level object loaded from the specified json file.
	 */
	public static Level loadFromFile(String lvlName) {
		Json json = new Json();
		return json.fromJson(Level.class, Gdx.files.local("levels/" + lvlName));
	}
	
	/**
	 * Saves this level to 'levels/{@link #name}'
	 */
	public void writeLevel() {
		Json json = new Json();
		String jsonText = json.prettyPrint(this);
		FileHandle lvlFile = Gdx.files.local("levels/" + name);
		lvlFile.delete();
		lvlFile.writeString(jsonText, false);
	}
}
