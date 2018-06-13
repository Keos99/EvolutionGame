package com.evolution.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EvolutionGame extends Game {
    private SpriteBatch batch;
    private GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameScreen = new GameScreen(batch);
        Assets.getInstance().loadAssets();
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
