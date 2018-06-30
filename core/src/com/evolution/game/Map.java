package com.evolution.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Map {
    private GameScreen gs;
    private TextureRegion grassRegion;
    private TextureRegion wallRegion;
    private byte[][] data;
    private int sizeX, sizeY;

    public static final int CELL_SIZE = 40;

    public Map(GameScreen gs) {
        this.gs = gs;
        this.grassRegion = Assets.getInstance().getAtlas().findRegion("grass");
        this.wallRegion = Assets.getInstance().getAtlas().findRegion("wall");
        this.sizeX = Rules.GLOBAL_WIDTH / CELL_SIZE;
        this.sizeY = Rules.GLOBAL_HEIGHT / CELL_SIZE;
        this.data = new byte[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (MathUtils.random(0, 100) < 2) {
                    data[i][j] = 1;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                batch.draw(grassRegion, i * 40, j * 40);
                if (data[i][j] == 1) {
                    batch.draw(wallRegion, i * 40, j * 40);
                }
            }
        }
    }

    public boolean isPointEmpty(float x, float y) {
        int cellX = (int) (x / CELL_SIZE);
        int cellY = (int) (y / CELL_SIZE);
        return data[cellX][cellY] == 0;
    }

    public boolean isPointEmpty(float x, float y, float radius) {
        for (int i = 0; i < 12; i++) {
            float tmpX = x + radius * (float) Math.cos(6.28f / 12f * i);
            float tmpY = y + radius * (float) Math.sin(6.28f / 12f * i);
            int cellX = (int) (tmpX / CELL_SIZE);
            int cellY = (int) (tmpY / CELL_SIZE);
            if (cellX < 0 || cellY < 0 || cellX > sizeX - 1 || cellY > sizeY - 1) {
                return false;
            }
            if (data[cellX][cellY] == 1) {
                return false;
            }
        }
        return true;
    }

    public void update(float dt) {

    }
}
