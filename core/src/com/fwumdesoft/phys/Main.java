package com.fwumdesoft.phys;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
	/** Contains AssetDescriptor constants for easy access of assets. */
	public static class Assets {
		public static final AssetDescriptor<Texture> AIR = new AssetDescriptor<>("textures/air.png", Texture.class);
		public static final AssetDescriptor<Texture> REFLECTOR = new AssetDescriptor<>("textures/reflector.png", Texture.class);
		public static final AssetDescriptor<Texture> REFRACTOR = new AssetDescriptor<>("textures/refractor.png", Texture.class);
		public static final AssetDescriptor<Texture> WALL = new AssetDescriptor<>("textures/wall.png", Texture.class);
		public static final AssetDescriptor<Texture> TRANSMITTER = new AssetDescriptor<>("textures/transmitter.png", Texture.class);
		public static final AssetDescriptor<Texture> RECEIVER = new AssetDescriptor<>("textures/receiver.png", Texture.class);
		public static final AssetDescriptor<Sound> BEAT_LEVEL_SOUND = new AssetDescriptor<>("sounds/beatlevel.mp3", Sound.class);
		public static final AssetDescriptor<Sound> FAIL_LEVEL_SOUND = new AssetDescriptor<>("sounds/faillevel.mp3", Sound.class);
		public static final AssetDescriptor<Sound> COLLISION_SOUND = new AssetDescriptor<>("sounds/collision.mp3", Sound.class);
	}
	
	public static AssetManager asset;
	public static Skin uiskin;
	public static Game game;
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		game = this;
		uiskin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		asset = new AssetManager();
		asset.load(Assets.AIR);
		asset.load(Assets.REFLECTOR);
		asset.load(Assets.REFRACTOR);
		asset.load(Assets.WALL);
		asset.load(Assets.TRANSMITTER);
		asset.load(Assets.RECEIVER);
		asset.load(Assets.BEAT_LEVEL_SOUND);
		asset.load(Assets.FAIL_LEVEL_SOUND);
		asset.load(Assets.COLLISION_SOUND);
		asset.finishLoading();
		
		setScreen(new MainMenuScreen());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		uiskin.dispose();
		asset.dispose();
		Gdx.app.log("Main", "Game Disposed");
	}
}
