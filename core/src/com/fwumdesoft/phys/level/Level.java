package com.fwumdesoft.phys.level;

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

public class Level {
	private Array<Reflector> reflectors;
	private Array<Refractor> refractors;
	private Array<Wall> walls;
	private Array<Receiver> receivers;
	private Array<Transmitter> trasnmitters;
	
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
		}
	}
	
	public Array<Refractor> getRefractors() {
		return refractors;
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
