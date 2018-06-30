package com.evolution.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Cell;
import com.evolution.game.units.Consumable;
import com.evolution.game.units.Enemy;
import com.evolution.game.units.Hero;

public class EvolutionGame extends Game {
    private SpriteBatch batch;

    // План:
    // 1. Сделать местность вместо белого поля + разные локации +
    // 2. Служебные классы(Система экранов, экраны, интерфейс)+
    // 3. Анимация (частично+)
    // 4. Всякие камни, объекты которые нельзя съесть +
    // 5. PowerUps
    // 6. Джойстик
    // -------------------------------
    // 100% местность, джойстик, порт на андроид
    // подумать про систему боев ботов

    // Домашнее задание:
    // 1. Разбор кода
    // 2. Сохранение игры, как бы Вы сохраняли состояние игры, идея на словах
    // * 3. Реализация в коде

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
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
