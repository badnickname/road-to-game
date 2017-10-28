package ru.ress.roadtogame.core.entities;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.ContextSettings;
import ru.ress.roadtogame.core.Entity;

/**
 * Created by ress on 23.10.17.
 */
public class Light extends Entity {
    private RenderTexture surf;
    private RenderStates bmSub;
    private Sprite light;
    private Color clrAlpha;
    private float scretch;

    private float _w, _h;

    public Light(Texture texture, int w, int h) {
        super(texture, w, h);
        type = TLIGHT;
        shades = false;
        scretch = 4;
        surf = new RenderTexture();

        surf.setSmooth(true);
        try {
            surf.create(texture.getSize().x, texture.getSize().y );
        } catch (TextureCreationException tce) {
            tce.printStackTrace();
        }

        light = new Sprite();
        light.setPosition(0,0);
        light.setTexture(texture);

        xScale = yScale = scretch;
        sprite.setColor(new Color(255,255,255,100));

        bmSub = new RenderStates(BlendMode.NONE);
        clrAlpha = new Color(0,0,0,0);

        _w = texture.getSize().x;
        _h = texture.getSize().y;
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.x-=sprite.getTexture().getSize().x*scretch/2+16;
        this.y-=sprite.getTexture().getSize().y*scretch/2;
        sprite.setPosition((float)this.x, (float)this.y);
    }

    @Override
    public void step() {
        drawSurface();
        sprite.setTexture(surf.getTexture());
    }

    @Override
    public Vector2f getSize() {
        return new Vector2f(_w*scretch/2, _h*scretch/2);
    }

    @Override
    public Vector2f getPosition() {
        return new Vector2f((float)x+_w*scretch/2, (float)y+_h*scretch);
    }

    @Override
    public double pointDistanceTo(double _x, double _y) {
        double dx = x+_w*scretch/2-_x;
        double dy = y+_h*scretch/2-_y;
        return Math.sqrt( dx*dx + dy*dy );
    }

    private void drawSurface() {
        surf.clear( clrAlpha );
        surf.draw(light);
        int _dist = (int)(_w/4 * scretch);

        int _xpos = (int)(x+_w*scretch/2);
        int _ypos = (int)(y+_h*scretch/2);

        for (Entity object : objects) {
            if (object.getType() != TWALL && object.getType() != TPLAYER) continue;
            if (object.getMinDistance(_xpos,_ypos) > _dist) continue;

            float _x0 = (object.getPosition().x - (float)x) / scretch;
            float _y0 = (object.getPosition().y - (float)y) / scretch;
            float _x1 = (_x0 + object.getSize().x / scretch );
            float _y1 = (_y0 + object.getSize().y / scretch );
            float _x = _w/2;
            float _y = _h/2 + 2;

            ConvexShape border0 = new ConvexShape();
            border0.setFillColor( clrAlpha );
            border0.setPointCount(4);

            if (_y < _y0) {
                border0.setPoint(0, new Vector2f(_x0, _y0) );
                border0.setPoint(1, new Vector2f(_x0 + 100*(_x0-_x) , _y0 + 100*(_y0-_y)) );
                border0.setPoint(2, new Vector2f(_x1 + 100*(_x1-_x) , _y0 + 100*(_y0-_y)) );
                border0.setPoint(3, new Vector2f(_x1, _y0) );

                surf.draw(border0, bmSub);
            }

            if (_y > _y1) {
                border0.setPoint(0, new Vector2f(_x0, _y1) );
                border0.setPoint(1, new Vector2f(_x0 + 100*(_x0-_x) , _y1 + 100*(_y1-_y)) );
                border0.setPoint(2, new Vector2f(_x1 + 100*(_x1-_x) , _y1 + 100*(_y1-_y)) );
                border0.setPoint(3, new Vector2f(_x1, _y1) );

                surf.draw(border0, bmSub);
            }

            if (_x < _x0) {
                border0.setPoint(0, new Vector2f(_x0, _y0) );
                border0.setPoint(1, new Vector2f(_x0 + 100*(_x0-_x) , _y0 + 100*(_y0-_y)) );
                border0.setPoint(2, new Vector2f(_x0 + 100*(_x0-_x) , _y1 + 100*(_y1-_y)) );
                border0.setPoint(3, new Vector2f(_x0, _y1) );

                surf.draw(border0, bmSub);
            }

            if (_x > _x1) {
                border0.setPoint(0, new Vector2f(_x1, _y0) );
                border0.setPoint(1, new Vector2f(_x1 + 100*(_x1-_x) , _y0 + 100*(_y0-_y)) );
                border0.setPoint(2, new Vector2f(_x1 + 100*(_x1-_x) , _y1 + 100*(_y1-_y)) );
                border0.setPoint(3, new Vector2f(_x1, _y1) );

                surf.draw(border0, bmSub);
            }

        }

        surf.display();
    }
}
