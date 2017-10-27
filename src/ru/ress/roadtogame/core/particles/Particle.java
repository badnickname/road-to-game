package ru.ress.roadtogame.core.particles;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

/**
 * Created by ress on 26.10.17.
 */
public class Particle extends Sprite {
    private float hspeed, vspeed;
    private float alpha, dlife;

    Particle(ConstTexture text, double hspeed, double vspeed, double alpha, double dlife, double stretch) {
        super(text);
        this.hspeed = (float)hspeed;
        this.vspeed = (float)vspeed;
        this.alpha = (float)alpha;
        this.dlife = (float)dlife;
        setScale((float)stretch, (float)stretch);
    }

    boolean move() {
        if (alpha - dlife > 0) alpha -= dlife; else return false;
        setColor(new Color(255,255,255, (int)(255*alpha) ));

        Vector2f pos = getPosition();
        float _x = pos.x + hspeed;
        float _y = pos.y + vspeed;
        setPosition(_x, _y);
        return true;
    }
}
