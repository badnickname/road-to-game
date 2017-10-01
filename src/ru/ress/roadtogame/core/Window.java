package ru.ress.roadtogame.core;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

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
        Tilemap tilemap = new Tilemap();
        tilemap.build("map", "tile.png", 640, 480);

        while(app.isOpen()) {
            app.draw(tilemap.getSprite());
            app.display();

            for(Event event : app.pollEvents()) {
                if(event.type == Event.Type.CLOSED) app.close();
            }
        }
    }
}
