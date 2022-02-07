package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player extends BaseActor {
    int speed;
    int score;

    public Player() {
        Pos = new Vector2();
        Pos.x = (int)Gdx.graphics.getWidth() / 2;
        Pos.y = (int)Gdx.graphics.getHeight() / 2;
        speed = 90;
        score = 0;
        sr = new ShapeRenderer();
        Tag = "Player";
    }


    public void Move() {
        float sprint = 1;
        // Check sprinting
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            sprint *= 5;
        // Move Left
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Pos.x > 4)
            Pos.x -= (speed * sprint) * Gdx.graphics.getDeltaTime();
        // Move Right
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Pos.x < (Gdx.graphics.getWidth()-18))
            Pos.x += (speed * sprint) * Gdx.graphics.getDeltaTime();
        // Move Up
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Pos.y < (Gdx.graphics.getHeight()-32))
            Pos.y += (speed * sprint) * Gdx.graphics.getDeltaTime();
        // Move Down
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Pos.y > 4)
            Pos.y -= (speed * sprint) * Gdx.graphics.getDeltaTime();

        Vector3 color = new Vector3(0.25f, 0.75f, 0.45f);

        DarwPlayer(Pos.x, Pos.y, 26, color);
    }


    public void DarwPlayer(float x, float y, float radius, Vector3 color) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color.x, color.y, color.z, 1);
        sr.ellipse(x, y, radius/2, radius);
        sr.end();
    }
}



