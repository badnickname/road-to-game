package ru.ress.roadtogame.core.particles;

import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ress on 26.10.17.
 */
public class Emitter {
    private ArrayList<Particle> list;
    private ConstTexture texture;
    private double dir0, range;
    private int alpha;
    private double x, y;
    private double speed;

    public Emitter(ConstTexture texture) {
        this.texture = texture;
        list = new ArrayList<Particle>();
    }

    public void setDirection(double dir0, double dir1) {
        this.dir0 = dir0;
        range = dir1 - dir0;
    }

    public void setAlpha(double alpha) {
        this.alpha = (int)(alpha*100);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSpeed(double speed) {
        this.speed = (float)speed;
    }

    public void burst(int count) {
        Random rnd = new Random(System.currentTimeMillis());
        for (int i=0; i<count; i++) {
            double _dir = Math.toRadians( dir0 + rnd.nextInt((int)range) );
            double dx = speed * Math.cos( _dir );
            double dy = speed * Math.sin( _dir );
            double _alpha = rnd.nextInt(alpha)/100.;
            double _dlife = rnd.nextInt(30)/100.;
            double _size = rnd.nextInt(25)/100. + 0.25;
            list.add( new Particle(texture, new Vector2f((float)x,(float)y), dx, dy, _alpha, _dlife, _size) );
        }
    }

    public void step(RenderWindow app) {

        for (int i=0; i<list.size(); i++) {
            Particle elm = list.get(i);
            app.draw(elm);
            if (!elm.move()) {
                list.remove(i);
            }
        }
    }
}
