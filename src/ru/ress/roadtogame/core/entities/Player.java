package ru.ress.roadtogame.core.entities;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import ru.ress.roadtogame.core.Entity;

import java.util.ArrayList;

/**
 * Created by ress on 22.10.17.
 */
public class Player extends Entity {
    private int mspeed;
    private double gravity;
    private float vspeed, hspeed;
    private final int TRUN = 0;
    private final int TSTAY = 1;
    private final int TJUMP = 2;
    private final int TLEAP = 3;
    private boolean moved;
    private float vspeedReserve;
    private float leap;

    public Player(Texture texture, int w, int h) {
        super(texture, w, h);
        originY = originX = 16;
        initAnimation();
        mspeed = 5;
        imageSpeed = 0.2;
        type = TPLAYER;
        gravity = 0.6;
        hspeed = vspeed = 0;
        moved = false;
        vspeedReserve = 1;
        leap = 0;
    }

    private boolean isPlaceFree(double x, double y) {
        for (Entity object : objects) {
            if (object == this) continue;
            if (object.isLocated(x,y)) return false;
        }
        return true;
    }

    @Override
    public Vector2f getPosition() {
        return new Vector2f((float)x-4,(float)y-14);
    }

    @Override
    public Vector2f getSize() {
        return new Vector2f((float)8,(float)28);
    }

    private void moveContactX(float dx) {
        Vector2f pos = null;
        for (Entity object : objects) {
            if (object == this) continue;
            if ((pos = object.isLocatedX(x+dx,y)) != null) break;
        }
        if (pos != null) {
            if (dx>0) x = pos.x; else x = pos.y;
        }
    }

    public void step(Keyboard.Key keyDown, Keyboard.Key keyUp) {
        boolean isStay = !isPlaceFree(x,y+height+1);

        if (leap>0) {
            imageIndex = 3;
            setAnimType(TLEAP);
            playAnimation(0);

            float dx = (float)8.2;
            if (keyDown == Keyboard.Key.W) {
                vspeedReserve = (float)0.8;
                hspeed = -xScale*dx;
                leap = 0;
                xScale = -xScale;
                setAnimType(TJUMP);
                return;
            }

            if (vspeed+gravity < 5) vspeed += gravity/2; else vspeed = 5;

            float dl = (float)0.1;
            if (xScale>0) {
                if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
                    if (leap - dl > 0) leap -= dl; else {
                        leap = 0;
                        hspeed = 0;
                        x -= mspeed;
                    }
                }
            }
            if (xScale<0) {
                if (Keyboard.isKeyPressed(Keyboard.Key.D)) {
                    if (leap - dl > 0) leap -= dl; else  {
                        leap = 0;
                        hspeed = 0;
                        x += mspeed;
                    }
                }
            }
            if (leap == 0) {
                xScale = -xScale;
                leap = 0;
                setAnimType(TJUMP);
                return;
            }

            float dy = height/2;
            if (isPlaceFree(x, y+vspeed+dy)) y+=vspeed; else {
                xScale = -1;
                leap = 0;
                setAnimType(TJUMP);
                return;
            }

            if ( isPlaceFree(x-mspeed,y) && isPlaceFree(x+mspeed,y) ) {
                xScale = -1;
                leap = 0;
                setAnimType(TJUMP);
                return;
            }

            return;
        }

        if (!Keyboard.isKeyPressed(Keyboard.Key.W)) vspeedReserve = 0;

        if (isStay) {
            vspeedReserve = 1;

            if (getAnimType() == TJUMP) {
                setAnimType(TSTAY);
                imageIndex = 0;
                playAnimation(0);
            }
        }

        if (vspeed+gravity < 9) vspeed += gravity; else vspeed = 9;

        float dx = (float)0.5;
        boolean move = false;
        if (Keyboard.isKeyPressed(Keyboard.Key.D)) {
            if (hspeed<mspeed) hspeed += dx;
            if (isStay) {
                xScale = 1;
                setAnimType(TRUN);
                moved = move = true;
                playAnimation(imageSpeed);
            }
        }
        if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
            if (hspeed>-mspeed) hspeed -= dx;
            if (isStay) {
                xScale = -1;
                setAnimType(TRUN);
                moved = move = true;
                playAnimation(imageSpeed);
            }
        }

        if (!Keyboard.isKeyPressed(Keyboard.Key.A) &&  !Keyboard.isKeyPressed(Keyboard.Key.D)) {
            if (!isStay) dx = (float)0.25;

            if (hspeed>0) {
                if (hspeed - dx > 0) hspeed-=dx; else hspeed = 0;
            }
            if (hspeed<0) {
                if (hspeed + dx < 0) hspeed+=dx; else hspeed = 0;
            }
        }

        float correct = 1;
        if (hspeed<0) correct = 0;
        if (hspeed != 0) {
            if (isPlaceFree( x + hspeed + correct, y + height / 2)) x += hspeed; else {
                if (hspeed>mspeed) hspeed = mspeed; else if (hspeed<-mspeed) hspeed = -mspeed;
                if (isPlaceFree( x + hspeed + correct, y + height / 2)) x += hspeed;
            }
        }

        if (!isStay) {
            if (!(isPlaceFree(x + mspeed, y + height / 2))) {
                leap = 1;
                xScale = 1;
            }
            if (!(isPlaceFree(x - mspeed, y + height / 2))) {
                leap = 1;
                xScale = -1;
            }
        }

        boolean jumped = false;
        if (keyDown == Keyboard.Key.W) {
            if ( isPlaceFree((int)x, (int)y-height/2) ) {
                if (isStay) {
                    jumped = true;
                }
            }
        }

        if (Keyboard.isKeyPressed(Keyboard.Key.W) && isPlaceFree(x,y-height/2)) {
            if (vspeedReserve>0) {
                vspeedReserve-=0.1;
                vspeed=(float)-9.5;
            }
        }

        if (keyUp == Keyboard.Key.W && vspeedReserve > 0) {
            vspeedReserve = 0;
            vspeed = 0;
        }

        if (!move && isStay ) {
            if (moved) {
                imageIndex = 0;
                moved = false;
            }
            if (imageIndex<framesInAnimate-imageSpeed) {
                setAnimType(TSTAY);
                playAnimation(imageSpeed);
            }
        }

        if (!isStay || jumped) {
            if (jumped) {
                imageIndex = 0;
            }
            if (imageIndex<framesInAnimate-imageSpeed*2) {
                setAnimType(TJUMP);
                playAnimation(imageSpeed*2);
            }
        }

        int dy = 0;
        if (vspeed>0) dy = width/2;

        if (isPlaceFree(x, y+vspeed+dy)) y+=vspeed; else {
            vspeedReserve = 0;
            vspeed = 0;
        }
    }
}
