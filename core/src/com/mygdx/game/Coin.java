package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.random;

public class Coin {
    ShapeRenderer sr;

    float radius;
    Vector2 Pos;
    Vector3 Color;

    public Coin() {
        sr = new ShapeRenderer();
        Pos = new Vector2();
        Color = new Vector3();
        radius = 12f;
    }

    public boolean PickUp(BaseActor actor){
        if(Vector2.dst(actor.Pos.x, actor.Pos.y, Pos.x, Pos.y) < radius-2 && actor.Tag == "Player")
            return true;
        else
            return false;
    }

    public void PlaceCoin() {
        Vector3 color =  new Vector3(.7f, .7f, .1f);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(color.x, color.y, color.z, 1);
        sr.circle(Pos.x, Pos.y, radius);
        sr.end();
    }

    public ShapeRenderer OnTriggerEnter(){
        return null;
    }
}
