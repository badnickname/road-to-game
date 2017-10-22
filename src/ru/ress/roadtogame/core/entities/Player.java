package ru.ress.roadtogame.core.entities;

import org.jsfml.graphics.Texture;
import org.jsfml.window.Keyboard;
import ru.ress.roadtogame.core.Entity;

import java.util.ArrayList;

/**
 * Created by ress on 22.10.17.
 */
public class Player extends Entity {
    private ArrayList<Entity> objects;
    private int mspeed;
    private double gravity;
    private double vspeed;
    private boolean canJump;

    public Player(Texture texture, int w, int h) {
        super(texture, w, h);
        mspeed = 5;
        imageSpeed = 0.2;
        type = TPLAYER;
        gravity = 0.6;
        vspeed = 0;
        canJump = true;
    }

    public void sendObjects(ArrayList<Entity> list) {
        objects = list;
    }

    private boolean isPlaceFree(double x, double y) {
        for (Entity object : objects) {
            if (object == this) continue;
            if (object.isLocated(x,y)) return false;
        }
        return true;
    }

    @Override
    public void step() {
        if (vspeed+gravity < 9) vspeed += gravity; else vspeed = 9;

        if (Keyboard.isKeyPressed(Keyboard.Key.D)) {
            if (isPlaceFree((int)x+mspeed, (int)y)) x+=mspeed;
            xScale = 1;
            playAnimation(imageSpeed);
        }
        if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
            if (isPlaceFree((int)x-mspeed, (int)y)) x-=mspeed;
            xScale = -1;
            playAnimation(imageSpeed);
        }
        if (Keyboard.isKeyPressed(Keyboard.Key.W)) {
            if (canJump) {
                canJump = false;
                vspeed = -15;
            }
        }

        int dy = 0;
        if (vspeed>0) dy = width/2;

        if (isPlaceFree(x, y+vspeed+dy)) y+=vspeed; else {
            if (vspeed > 0) canJump = true;
            vspeed = 0;
        }
    }
}
