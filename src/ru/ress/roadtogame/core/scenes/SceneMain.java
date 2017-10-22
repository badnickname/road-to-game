package ru.ress.roadtogame.core.scenes;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import ru.ress.roadtogame.core.Entity;
import ru.ress.roadtogame.core.Scene;
import ru.ress.roadtogame.core.entities.Player;

import java.util.ArrayList;

/**
 * Created by ress on 02.10.17.
 */
public class SceneMain extends Scene {
    private int x, y;
    private long time;
    private Color bgColor;
    private ArrayList<Entity> entity;

    public SceneMain(RenderWindow app) {
        super(app);
        x = y = 0;
        time = System.currentTimeMillis();
        entity = new ArrayList<>();
        bgColor = new Color(154,104,41);
        new Entities(fmap).load(entity);

        for(Entity object : entity) {
            if (object.getType() == Entity.TPLAYER) {
                Player player = (Player)object;
                player.sendObjects(entity);
            }
        }
    }

    private void drawScene() {
        view.setCenter(x,y);
        app.setView(view);

        if (!isInit) return;
        app.clear(bgColor);
        defaultLayer.draw(new Vector2i(x-scrWidth/2,y-scrHeight/2), new Vector2i(scrWidth,scrHeight));

        for(Entity object : entity) {
            switch (object.getType()) {
                case Entity.TPLAYER : {
                    object.step();

                    app.draw(object.getSprite());
                    Vector2f coords = object.getPosition();
                    x = (int)coords.x;
                    y = (int)coords.y;
                }
            }
        }
    }

    @Override
    public void draw() {
        long curTime = System.currentTimeMillis();
        if (curTime > time + 16) {
            drawScene();
            time = curTime;
        }
    }

}
