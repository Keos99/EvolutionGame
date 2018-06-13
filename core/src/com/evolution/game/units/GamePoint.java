package com.evolution.game.units;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.evolution.game.GameScreen;
import com.evolution.game.Poolable;

public abstract class GamePoint implements Poolable {
    GameScreen gs;
    TextureRegion texture;
    Vector2 position;
    Vector2 velocity;
    boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    float scale;

    public Vector2 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public boolean checkCollision(GamePoint another) {
        return this.position.dst(another.position) < (this.getScale() * 32.0f + another.getScale() * 32.0f) * 0.8f;
    }
}
