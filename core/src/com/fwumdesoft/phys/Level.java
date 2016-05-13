package com.fwumdesoft.phys;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
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
	
	/**
	 * Loads a level from a file.
	 * @param lvlName name of the level file to load
	 * @return A Level object loaded from the specified json file.
	 */
	public static Level loadFromFile(String lvlName) {
		Json json = new Json();
		return json.fromJson(Level.class, Gdx.files.internal("levels/" + lvlName));
	}
	
	/**
	 * Writes this level to 'physics-game/levels/' using a {@link FileType#External}
	 * FileHandle with the specified name.
	 * @param name The name of the file.
	 */
	public void writeLevel(String name) {
		Json json = new Json();
		json.toJson(this, Gdx.files.external("physics-game/levels/" + name));
	}
}
