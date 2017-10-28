package ru.ress.roadtogame.core.entities;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import ru.ress.roadtogame.core.Entity;
import ru.ress.roadtogame.core.particles.Emitter;

import java.io.IOException;
import java.nio.file.Paths;
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
    private Emitter emitter;
    private Texture textureHook;
    private boolean hooked;
    private Hook hook;
    private double rspeed, maxrspeed;
    private int rdir;

    public Player(Texture texture, int w, int h) {
        super(texture, w, h);
        originY = originX = 16;
        initAnimation();
        mspeed = 5;
        imageSpeed = 0.2;
        type = TPLAYER;
        gravity = 0.6;
        hspeed = vspeed = rdir = 0;
        moved = false;
        hooked = false;
        vspeedReserve = 1;
        leap = 0;
        maxrspeed = rspeed = 0;

        Texture particleWall;
        particleWall = new Texture();
        try {
            particleWall.loadFromFile(Paths.get("particle_wall.png"));
        } catch (IOException io) {
            io.printStackTrace();
        }
        emitter = new Emitter(particleWall);
        emitter.setAlpha(1);

        textureHook = new Texture();
        try {
            textureHook.loadFromFile(Paths.get("hook.png"));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private boolean isPlaceFree(double x, double y) {
        for (Entity object : objects) {
            if (object.getType() != TWALL) continue;
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
        int _dx = (int)dx;
        int _dy = height/2;
        int _move = 0;
        if (_dx>0) {
            for (int i=_dx; i>0; i--) {
                if (isPlaceFree(x+i,y+_dy)) _move = i;
            }
        } else if (_dx<0) {
            for (int i=_dx; i<0; i++) {
                if (isPlaceFree(x+i,y+_dy)) _move = i;
            }
        }
        x+=_move;
    }

    private void moveContactY(float dy) {
        int _dy = (int)dy;
        int _dy1 = height/2;
        int _move = 0;
        if (_dy>0) {
            emitter.setPosition(x,y+_dy1);
            emitter.setDirection(180,360);
            emitter.setSpeed(6);
            emitter.burst(_dy*3);
            for (int i=_dy; i>0; i--) {
                if (isPlaceFree(x,y+_dy1+i)) _move = i;
            }
        } else if (_dy<0) {
            for (int i=_dy; i<0; i++) {
                if (isPlaceFree(x,y-_dy1+i)) _move = i;
            }
        }
        y+=_move;
    }

    public Emitter getEmitter() {
        return emitter;
    }

    @Override
    public void onMouseDown(Mouse.Button but, int mouseX, int mouseY) {
        if (but != Mouse.Button.RIGHT) return;
        hook = new Hook(textureHook, this, x, y, pointDirectionTo(mouseX, mouseY));
        hook.sendObjects(objects);
        objects.add(hook);
    }

    @Override
    public void onMouseUp(Mouse.Button but, int mouseX, int mouseY) {

    }

    public void setHooked(boolean hook) {
        if (hook) {
            if (this.hook.getPosition().x < x) rdir = -1; else rdir = 1;
            rspeed = Math.sqrt(hspeed*hspeed + vspeed*vspeed);
            maxrspeed = rspeed;
            rspeed *= rdir;
        }
        hooked = hook;
    }

    public void step(Keyboard.Key keyDown, Keyboard.Key keyUp) {
        boolean isStay = !isPlaceFree(x,y+height+1);

        if (hooked) {
            imageIndex = 3;
            setAnimType(TJUMP);
            playAnimation(0);

            leap = 0;
            if (isStay) {
                hook.destroy();
            }

            double dir = pointDirectionTo(hook.getPosition().x+8, hook.getPosition().y+8) + Math.PI/2;
            hspeed = (float)(rspeed * Math.cos(dir));
            vspeed = (float)(rspeed * Math.sin(dir));

            if (isPlaceFree(x+hspeed, y+vspeed+width/2) && isPlaceFree(x+hspeed, y+vspeed)) {
                x+=hspeed;
                y+=vspeed;
            } else {
                hook.destroy();
            }
            return;
        }

        if (leap>0) {
            emitter.setPosition(x,y);
            emitter.setDirection(210,300);
            emitter.setSpeed(1);
            emitter.burst(1);

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

                if (hspeed>0) emitter.setDirection(270,450);
                else emitter.setDirection(90,270);
                emitter.setPosition(x,y);
                emitter.setSpeed(5);
                emitter.burst(20);
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

            if ( isPlaceFree(x-mspeed,y+dy) && isPlaceFree(x+mspeed,y+dy) ) {
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
                moveContactX(hspeed);
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
            moveContactY(vspeed);
            vspeedReserve = 0;
            vspeed = 0;
        }
    }
}
