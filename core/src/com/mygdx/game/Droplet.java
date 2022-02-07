package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.random;

public class Droplet {
    ShapeRenderer droplet;
    int posY;
    int posX;
    Vector3 color;
    float speed;
    float size;

    public Droplet() {
        droplet = new ShapeRenderer();
        posY = Gdx.graphics.getHeight();
        color = new Vector3(0.15f, 0.35f, 0.75f);
        posX = random.nextInt(Gdx.graphics.getWidth() - 1);
        speed = -9.82f;
        size = 10;
    }


    public void DarwDroplet(float x, float y, float radius, Vector3 color) {
        droplet.begin(ShapeRenderer.ShapeType.Filled);
        droplet.setColor(color.x, color.y, color.z, 1);
        droplet.circle(x, y, radius);
        droplet.end();
    }

    public void MoveItem(float speedMod) {
        posY += ((speed * speedMod) * Gdx.graphics.getDeltaTime());
        if (posY < 0 || posY > Gdx.graphics.getHeight())
            speed = 0;
        Vector3 color = new Vector3(0.25f, 0.75f, 0.45f);

        DarwDroplet(posX, posY, size, color);
    }
}
