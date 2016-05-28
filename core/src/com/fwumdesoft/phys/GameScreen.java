package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.fwumdesoft.phys.actors.AirMolecule;
import com.fwumdesoft.phys.actors.HitboxActor;
import com.fwumdesoft.phys.actors.Transmitter;
import com.fwumdesoft.phys.actors.Wave;

public class GameScreen extends ScreenAdapter {
	private int curLevel = 0;
	private Stage stage;
	private Level level;
	private Array<HitboxActor> movableActors = new Array<>();
	
	/** {@code true} if transmitters have already been fired on this level.
	 * Gets reset by a call to resetLevel(). */
	private boolean transmitted;
	
	@Override
	public void show() {
		stage = new Stage(new FillViewport(1000f, 1000f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		
		loadNextLevel();
	}
	
	private void loadNextLevel() {
		curLevel++;
		resetLevel();
	}
	
	private void resetLevel() {
		//reset instance vars
		stage.clear();
		stage.addListener(new InputManager());
		transmitted = false;
		movableActors.clear();
		level = Level.loadFromFile(Integer.toString(curLevel));
		
		//setup level with static objects and air
		generateAir(0.03f);
		level.getAllActors().forEach(actor -> {
			stage.addActor(actor);
			if(actor.isMovable()) {
				actor.setVisible(false);
				movableActors.add(actor);
			}
		});
		Gdx.app.debug("GameScreen", "Movable Actors: " + movableActors);
		
		//setup ui windows
		Window wndActors = new Window("Objects", Main.uiskin);
		//Assume that the only movable actors will be reflectors and refractors
		//Also assume that they can be rotated
		Label lblReflectors = new Label(Integer.toString(level.getReflectors().size), Main.uiskin);
		Label lblRefractors = new Label(Integer.toString(level.getRefractors().size), Main.uiskin);
		TextButton btnCreateReflector = new TextButton("Create", Main.uiskin);
		btnCreateReflector.addListener(new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//TODO create a reflector
			}
		});
		TextButton btnCreateRefractor = new TextButton("Create", Main.uiskin);
		btnCreateRefractor.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//TODO create a refractor
			}
		}); 
		wndActors.pack();
		wndActors.setPosition(0, stage.getHeight(), Align.topLeft);
		stage.addActor(wndActors);
	}
	
	/**
	 * Generates AirMolecule objects on the screen with a specified density and adds them
	 * to the stage.
	 * @param density Density of AirMolecules in molecules/unit^2
	 */
	private void generateAir(float density) {
		float worldWidth = stage.getWidth(), worldHeight = stage.getHeight();
		float area = worldWidth * worldHeight;
		int totalAir = MathUtils.ceil(area * density);
		int cellLength = MathUtils.ceil((float)Math.sqrt(1f / density));
		
		for(int minY = 0; minY < worldHeight; minY += cellLength) {
			for(int minX = 0; minX < worldWidth; minX += cellLength) {
				float randX = MathUtils.random(minX, minX + cellLength);
				float randY = MathUtils.random(minY, minY + cellLength);
				AirMolecule airMolecule = Pools.get(AirMolecule.class, totalAir).obtain();
				airMolecule.setPosition(randX, randY, Align.center);
				airMolecule.markPosition(new Vector2(airMolecule.getX(), airMolecule.getY())); //marks original position
				stage.addActor(airMolecule);
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
//		***** check victory conditions *****
		int numWaves = level.getTransmitters().size;
		boolean reset = false;
		for(Actor actor : stage.getActors()) {
			if(actor instanceof Wave) {
				Wave wave = (Wave)actor;
				if(wave.wasSuccessful()) {
					numWaves--;
				} else if(!wave.isAlive()) {
					reset = true;
					break;
				}
			}
		}
		if(reset) {
			resetLevel();
			Gdx.app.log("GameScreen", "Reset current level");
		} else if(numWaves == 0) {
			loadNextLevel();
			Gdx.app.log("GameScreen", "Loaded next level");
		}
	}
	
	@Override
	public void hide() {
		dispose();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
	
	private class InputManager extends InputListener {
		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			switch(keycode) {
			case Keys.SPACE: //tells all transmitters to transmit a wave
				if(transmitted) break; //only allow transmitters to be transmitted once per level
				for(int i = 0; i < stage.getActors().size; i++) {
					Actor actor = stage.getActors().get(i);
					if(actor instanceof Transmitter) {
						Transmitter trans = (Transmitter)actor;
						trans.transmit();
					}
				}
				transmitted = true;
				Gdx.app.log("GameScreen.InputManager", "Transmitters fired");
				return true;
			case Keys.FORWARD_DEL:
			case Keys.DEL:
				Actor focusActor = stage.getKeyboardFocus();
				if(focusActor instanceof HitboxActor) {
					HitboxActor hActor = (HitboxActor)focusActor;
					if(hActor.isMovable()) {
						//TODO add actor back to list of actors that can be added
					}
				}
				return true;
			}
			return false;
		}
	}
}
