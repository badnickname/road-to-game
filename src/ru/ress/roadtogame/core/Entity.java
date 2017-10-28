package ru.ress.roadtogame.core;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;

import java.util.ArrayList;

/**
 * Created by ress on 02.10.17.
 */
public class Entity {
    public final static int TDEFAULT = 0;
    public final static int TWALL = 1;
    public final static int TPLAYER = 2;
    public final static int TLIGHT = 3;
    public final static int THOOK = 4;

    protected double x,y;
    protected int width, height;
    protected double imageSpeed;
    protected double imageIndex;
    protected int framesInAnimate;
    protected Sprite sprite;
    protected int originX, originY;
    protected float xScale, yScale;
    protected int animType;
    protected int type;
    protected boolean shades;

    private int textureSize;

    protected ArrayList<Entity> objects;

    public void sendObjects(ArrayList<Entity> list) {
        objects = list;
    }

    public Entity() {
        type = TDEFAULT;
        shades = true;
    }

    public Entity(Texture texture, int w, int h) {
        type = TDEFAULT;
        shades = true;

        sprite = new Sprite();
        sprite.setTexture(texture);
        sprite.setTextureRect(new IntRect(0,0,texture.getSize().x,texture.getSize().y));
        sprite.setPosition(0,0);
        textureSize = texture.getSize().x;
        originY = originX = 0;
        xScale = yScale = 1;

        width = w;
        height = h;
        x = y = 0;

        animType = 0;
    }

    public boolean isDrawShades() {
        return shades;
    }

    public void onMouseDown(Mouse.Button but, int mouseX, int mouseY) {

    }

    public void onMouseUp(Mouse.Button but, int mouseX, int mouseY) {

    }

    public void step() {}

    public boolean isLocated(double _x, double _y) {
        if (_x >= x && _x <= x + width &&
                _y >= y && _y <= y + height) return true;
        return false;
    }

    public Vector2f isLocatedX(double _x, double _y) {
        if (_x >= x && _x <= x + width &&
                _y >= y && _y <= y + height) return new Vector2f((float)x,(float)x+width);
        return null;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setAnimType(int anim) {
        animType = anim;
    }

    public int getAnimType() {
        return animType;
    }

    private void updateAnimation() {
        sprite.setScale(xScale, yScale);
        sprite.setPosition((int)x,(int)y);
    }

    public void setCoords(int x, int y, int x1, int y1) {
        this.x = x;
        this.y = y;
        width = x1;
        height = y1;
    }

    public Vector2f getPosition() {
        return new Vector2f((float)x-originX,(float)y-originY);
    }

    public Vector2f getSize() {
        return new Vector2f((float)width,(float)height);
    }

    public int getType() {
        return type;
    }

    public Sprite getSprite() {
        if (sprite != null) updateAnimation();
        return sprite;
    }

    protected void initAnimation() {
        framesInAnimate = textureSize / width;
        imageSpeed = imageIndex = 0;
        if (sprite != null) {
            sprite.setTextureRect(new IntRect(0,0,width,height));
            sprite.setOrigin(originX,originY);
        }
    }

    protected void playAnimation(double imgSpeed) {
        imageIndex+=imgSpeed;
        if (imageIndex > framesInAnimate) imageIndex = 0;

        sprite.setTextureRect(new IntRect((int)(imageIndex)*width,animType*height,width,height));
    }

    public double pointDistanceTo(double _x, double _y) {
        double dx = x-_x;
        double dy = y-_y;
        return Math.sqrt( dx*dx + dy*dy );
    }

    public int getMinDistance(double _x, double _y) {
        int dist0 = (int) pointDistanceTo(_x,_y);
        int dist1 = (int) pointDistanceTo(_x-width,_y-height);
        if (dist0<dist1) return dist0; else return dist1;
    }

    protected double pointDirectionTo(double _x, double _y) {
        double dt = 0;
        if (_x < x) dt = Math.PI;
        double dx = _x-x;
        double dy = _y-y;
        if (dx == 0) dx = 0.0000001;
        double result = Math.atan( dy/dx ) + dt;

        while (result>2*Math.PI) {
            result -= 2*Math.PI;
        }
        return result;
    }
}
