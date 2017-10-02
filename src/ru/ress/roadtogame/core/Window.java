package ru.ress.roadtogame.core;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ress on 30.09.17.
 */
public class Window extends Thread {
    private RenderWindow app;
    private int w,h;
    private String title;

    public Window(int w, int h, String title) {
        this.w = w;
        this.h = h;
        this.title = title;
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        app = new RenderWindow();
        app.create(new VideoMode(w,h), title, RenderWindow.CLOSE);
        app.clear(Color.WHITE);
        Tilemap tilemap = new Tilemap(app);
        tilemap.build("map", "tile.png", "[layer]");

        long time = System.currentTimeMillis();
        long keytime = time;
        int frame = 0;

        int x = 0, y = 0;
        while(app.isOpen()) {
            frame += 1;
            long curTime = System.currentTimeMillis();
            if (curTime > time + 1000) {
                app.setTitle("FPS: "+Integer.toString(frame)+" Sprites: ~10000");
                frame = 0;
                time = curTime;
            }

            if (curTime > keytime + 16) {
                if (Keyboard.isKeyPressed(Keyboard.Key.D)) x+=8;
                if (Keyboard.isKeyPressed(Keyboard.Key.A)) x-=8;
                if (Keyboard.isKeyPressed(Keyboard.Key.W)) y-=8;
                if (Keyboard.isKeyPressed(Keyboard.Key.S)) y+=8;
                keytime = curTime;
            }

            //app.draw(tilemap.getSprite(new Vector2f(x,y)));
            tilemap.draw(new Vector2i(x,y),new Vector2i(640,470));
            app.display();

            for(Event event : app.pollEvents()) {
                if(event.type == Event.Type.CLOSED) app.close();
            }
        }
    }
}
