package ru.ress.roadtogame.core.entities;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import ru.ress.roadtogame.core.Entity;

/**
 * Created by ress on 27.10.17.
 */
public class Hook extends Entity {
    private double hspeed, vspeed;
    private int mspeed;
    private boolean move;
    private Player player;
    private Sprite rope;

    public Hook(Texture texture, Player player, double x, double y, double dir) {
        super(texture, 16, 16);
        shades = false;
        type = THOOK;
        mspeed = 22;
        originX = originY = 8;
        initAnimation();
        this.x = x;
        this.y = y;
        this.player = player;
        hspeed = mspeed * Math.cos(dir);
        vspeed = mspeed * Math.sin(dir);
        move = true;

        rope = new Sprite(texture);
        rope.setOrigin(8,8);
        rope.setTextureRect(new IntRect(16,0,16,16));
    }

    @Override
    public void onMouseUp(Mouse.Button but, int mouseX, int mouseY) {
        if (but != Mouse.Button.RIGHT) return;
        destroy();
    }

    public void destroy() {
        player.setHooked(false);
        objects.remove(this);
    }

    private boolean isPlaceFree(double x, double y) {
        for (Entity object : objects) {
            if (object.getType() != TWALL) continue;
            if (object.isLocated(x,y)) return false;
        }
        return true;
    }

    public void drawRope(RenderWindow app) {
        Vector2f playerPos = player.getPosition();
        double _x = playerPos.x;
        double _y = playerPos.y+16;
        int dist = (int)pointDistanceTo(_x, _y) - 16;
        double dir = pointDirectionTo(_x, _y);
        double cos = Math.cos(dir);
        double sin = Math.sin(dir);
        for (int i = 0; i<dist; i+=9) {
            rope.setPosition((int)x + (float)(i*cos), (int)y + (float)(i*sin));
            app.draw(rope);
        }
        return;
    }

    @Override
    public void step() {
        if (!move) return;

        if (pointDistanceTo(player.getPosition().x, player.getPosition().y) > 400) {
            destroy();
            return;
        }

        if (isPlaceFree(x+hspeed, y+vspeed)) {
            x+=hspeed;
            y+=vspeed;
        } else {
            player.setHooked(true);
            move = false;
        }
    }
}
