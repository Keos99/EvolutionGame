package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.evolution.game.units.Cell;
import com.evolution.game.units.Enemy;
import com.evolution.game.units.Hero;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private Map map;
    private ConsumableEmitter consumableEmitter;
    private ParticleEmitter particleEmitter;
    private EnemyEmitter enemyEmitter;
    private Hero hero;
    private List<Cell> cellCollisionList;
    private Viewport viewport;
    private Camera camera;
    private Camera windowCamera;
    private Music music;
    private Sound consumeSound;
    private MiniMap miniMap;
    private boolean paused;
    private int level;

    public Viewport getViewport() {
        return viewport;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public ConsumableEmitter getConsumableEmitter() {
        return consumableEmitter;
    }

    public EnemyEmitter getEnemyEmitter() {
        return enemyEmitter;
    }

    public Hero getHero() {
        return hero;
    }

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void show() {
        consumableEmitter = new ConsumableEmitter(this);
        hero = new Hero(this);
        enemyEmitter = new EnemyEmitter(this);
        particleEmitter = new ParticleEmitter();
        cellCollisionList = new ArrayList<Cell>();
        font = Assets.getInstance().getAssetManager().get("gomarice48.ttf", BitmapFont.class);
        map = new Map(this);
        camera = new OrthographicCamera(1280, 720);
        viewport = new FitViewport(1280, 720, camera);
        miniMap = new MiniMap(this);
        music = Assets.getInstance().getAssetManager().get("music.wav", Music.class);
        music.setLooping(true);
        music.setVolume(0.05f);
        music.play();
        consumeSound = Assets.getInstance().getAssetManager().get("laser.wav", Sound.class);
        paused = false;
        windowCamera = new OrthographicCamera(1280, 720);
        windowCamera.position.set(640, 360, 0);
        windowCamera.update();
        level = 1;
    }

    public void levelUp() {
        level++;
        consumableEmitter.setBadFoodChance(10 + level * 2);
        float deltaScale = hero.getScale() - 1.0f;
        hero.setScale(1.0f);
        for (int i = enemyEmitter.activeList.size() - 1; i >= 0; i--) {
            Enemy e = enemyEmitter.getActiveList().get(i);
            e.setScale(e.getScale() - deltaScale);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        map.render(batch);
        consumableEmitter.render(batch);
        particleEmitter.render(batch);
        hero.render(batch);
        enemyEmitter.render(batch);
        batch.end();
        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        hero.renderGUI(batch, font);
        miniMap.render(batch);
        batch.end();
    }

    public void checkCollisions() {
        // Проверка столкновений персонажей и еды
        cellCollisionList.clear();
        cellCollisionList.add(hero);
        cellCollisionList.addAll(enemyEmitter.getActiveList());
        for (int i = 0; i < cellCollisionList.size(); i++) {
            for (int j = 0; j < consumableEmitter.getActiveList().size(); j++) {
                if (cellCollisionList.get(i).getPosition().dst(consumableEmitter.getActiveList().get(j).getPosition()) < 30) {
                    cellCollisionList.get(i).eatConsumable(consumableEmitter.getActiveList().get(j).getType());
                    consumableEmitter.getActiveList().get(j).consumed();
                    consumeSound.play();
                }
            }
        }
        // Проверка столкновений персонажей между собой
        for (int i = 0; i < cellCollisionList.size() - 1; i++) {
            for (int j = i + 1; j < cellCollisionList.size(); j++) {
                if (cellCollisionList.get(i).checkCollision(cellCollisionList.get(j))) {
                    if (cellCollisionList.get(i).getScale() > cellCollisionList.get(j).getScale()) {
                        cellCollisionList.get(i).grow();
                        cellCollisionList.get(j).consumed();
                    } else {
                        cellCollisionList.get(i).consumed();
                        cellCollisionList.get(j).grow();
                    }
                }
            }
        }
    }

    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
        if (!paused) {
            map.update(dt);
            hero.update(dt);
            if (hero.getScale() > 2.0f) {
                levelUp();
            }
            camera.position.set(hero.getPosition().x - 32, hero.getPosition().y - 32, 0);
            if (camera.position.x < Rules.WORLD_WIDTH / 2) {
                camera.position.x = Rules.WORLD_WIDTH / 2;
            }
            if (camera.position.x > Rules.GLOBAL_WIDTH - Rules.WORLD_WIDTH / 2) {
                camera.position.x = Rules.GLOBAL_WIDTH - Rules.WORLD_WIDTH / 2;
            }
            if (camera.position.y < Rules.WORLD_HEIGHT / 2) {
                camera.position.y = Rules.WORLD_HEIGHT / 2;
            }
            if (camera.position.y > Rules.GLOBAL_HEIGHT - Rules.WORLD_HEIGHT / 2) {
                camera.position.y = Rules.GLOBAL_HEIGHT - Rules.WORLD_HEIGHT / 2;
            }
            camera.update();
            enemyEmitter.update(dt);
            consumableEmitter.update(dt);
            particleEmitter.update(dt);
            checkCollisions();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Assets.getInstance().clear();
    }
}
