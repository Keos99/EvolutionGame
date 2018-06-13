package com.evolution.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Consumable;

public class ConsumableEmitter extends ObjectPool<Consumable> {
    private GameScreen gs;
    private TextureRegion[] regions;
    private float time;

    public ConsumableEmitter(GameScreen gs) {
        this.gs = gs;
        this.regions = new TextureRegion[2];
        this.regions[Consumable.Type.FOOD.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("Food");
        this.regions[Consumable.Type.BAD_FOOD.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("BadFood");
        this.generateConsumable(10);
    }

    @Override
    protected Consumable newObject() {
        return new Consumable(gs, regions);
    }


    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void generateConsumable(int count) {
        for (int i = 0; i < count; i++) {
            generateConsumable();
        }
    }

    public void generateConsumable() {
        Consumable.Type type = Consumable.Type.FOOD;
        if (MathUtils.random(0, 100) < 10) {
            type = Consumable.Type.BAD_FOOD;
        }
        getActiveElement().init(type);
    }

    public void update(float dt) {
        time += dt;
        if (time >= 0.4f) {
            generateConsumable();
            time = 0.0f;
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
