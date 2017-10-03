package ru.ress.roadtogame.core;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/**
 * Created by ress on 30.09.17.
 */
public class Scene {
    protected RenderWindow app;
    protected Tilemap defaultLayer;
    protected boolean isInit;
    protected String ftile, fmap;
    protected int scrWidth, scrHeight;
    protected View view;

    public Scene(RenderWindow app) {
        this.app = app;
        isInit = false;
        ftile = "tile.png";
        fmap = "map";
        scrWidth = app.getSize().x;
        scrHeight = app.getSize().y;
        view = new View(new Vector2f(0,0), new Vector2f(scrWidth, scrHeight));
    }

    public void init() {
        defaultLayer = new Tilemap(app);
        isInit = defaultLayer.build(fmap, ftile, "defaultLayer");
        return;
    }

    public void resize(int w, int h) {
        scrWidth = w;
        scrHeight = h;
        view.setSize(w, h);
        return;
    }

    public void draw() {
        if (!isInit) return;
        defaultLayer.draw(new Vector2i(0,0), new Vector2i(scrWidth,scrHeight));
        return;
    }

}
