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
import com.fwumdesoft.phys.actors.Refractor;
import com.fwumdesoft.phys.actors.Wave;

public class GameScreen extends ScreenAdapter {
	private Stage stage;
	
	@Override
	public void show() {
		stage = new Stage(new FillViewport(1000f, 1000f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		
		generateAir(0.1f);
		Wave wave = new Wave();
		stage.addActor(wave);
		wave.propagate(0, 0, 20);
		
		Refractor refr = new Refractor(1.75f, TransformType.fixed);
		refr.setPosition(150, 55, Align.center);
		refr.setRotation(180);
		stage.addActor(refr);
		
		Reflector refl2 = new Reflector(TransformType.fixed);
		refl2.setPosition(100, 75, Align.center);
		refl2.setRotation(-45);
		stage.addActor(refl2);
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
