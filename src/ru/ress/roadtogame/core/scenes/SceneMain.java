package ru.ress.roadtogame.core.scenes;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import ru.ress.roadtogame.core.Scene;

/**
 * Created by ress on 02.10.17.
 */
public class SceneMain extends Scene {
    private int x, y;
    private long time;

    public SceneMain(RenderWindow app) {
        super(app);
        x = y = 0;
        time = System.currentTimeMillis();
    }

    @Override
    public void draw() {
        long curTime = System.currentTimeMillis();
        if (curTime > time + 16) {
            if (Keyboard.isKeyPressed(Keyboard.Key.D)) x+=8;
            if (Keyboard.isKeyPressed(Keyboard.Key.A)) x-=8;
            if (Keyboard.isKeyPressed(Keyboard.Key.W)) y-=8;
            if (Keyboard.isKeyPressed(Keyboard.Key.S)) y+=8;
            time = curTime;
        }

        view.setCenter(x,y);
        app.setView(view);

        if (!isInit) return;
        defaultLayer.draw(new Vector2i(x-scrWidth/2,y-scrHeight/2), new Vector2i(scrWidth,scrHeight));
        return;
    }

}
