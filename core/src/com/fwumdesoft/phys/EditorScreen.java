package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * The level editor screen.
 */
public class EditorScreen extends ScreenAdapter {
	private Stage stage;
	
	@Override
	public void show() {
		stage = new Stage(new FillViewport(400f, 400f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		Table ui = new Table();
		ui.setFillParent(true);
		stage.addActor(ui);
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
