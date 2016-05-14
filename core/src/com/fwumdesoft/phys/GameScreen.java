package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.fwumdesoft.phys.actors.AirMolecule;
import com.fwumdesoft.phys.actors.Reflector;
import com.fwumdesoft.phys.actors.Wave;

public class GameScreen extends ScreenAdapter {
	private Stage stage;
	
	@Override
	public void show() {
		stage = new Stage(new FillViewport(1000f, 1000f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		
		generateAir(0.1f);
		Wave wave = new Wave(10, 30, 70);
		stage.addActor(wave);
		wave.propagate(0, 0, 20);
		
		Vector2 v = Vector2.X.cpy().rotate(wave.getRotation());
		Reflector refl = new Reflector();
		refl.setPosition(v.x * 400, v.y * 400, Align.center);
		refl.setSize(7, 50);
		refl.setRotation(10);
		stage.addActor(refl);
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
	}
	
	@Override
	public void hide() {
		dispose();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
