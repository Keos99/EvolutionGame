package com.evolution.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.Assets;
import com.evolution.game.GameScreen;
import com.evolution.game.Rules;

public class Hero extends Cell {
    public Hero(GameScreen gs) {
        super(640.0f, 360.0f, 300.0f);
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("Char");
    }

    @Override
    public void consumed() {
        position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        scale = 1.0f;
    }

    public void update(float dt) {
        super.update(dt);
        if (Gdx.input.isTouched()) {
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
            gs.getViewport().unproject(tmp);
            float angleToTarget = tmp.sub(position).angle();
            if (angle > angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle -= 180.0f * dt;
                } else {
                    angle += 180.0f * dt;
                }
            }
            if (angle < angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle += 180.0f * dt;
                } else {
                    angle -= 180.0f * dt;
                }
            }
            velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        }
    }
}