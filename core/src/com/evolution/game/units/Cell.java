package com.evolution.game.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Cell extends GamePoint {
    float angle;
    float acceleration;
    Vector2 tmp;

    public Cell(float x, float y, float acceleration) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.tmp = new Vector2(0.0f, 0.0f);
        this.angle = 0.0f;
        this.acceleration = acceleration;
        this.scale = 1.0f;
    }

    public abstract void consumed();

    public void eatConsumable(Consumable.Type type) {
        switch (type) {
            case FOOD:
                grow();
                break;
            case BAD_FOOD:
                decrease();
                break;
        }
    }

    public void grow() {
        scale += 0.05f;
        if (scale > 5.0f) {
            scale = 5.0f;
        }
    }

    public void decrease() {
        scale -= 0.1f;
        if (scale < 0.2f) {
            scale = 0.2f;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, angle);
    }

    public void update(float dt) {
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        if (angle > 360.0f) {
            angle -= 360.0f;
        }
        velocity.scl(0.98f);
        position.mulAdd(velocity, dt);
    }
}
