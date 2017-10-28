package ru.ress.roadtogame.core;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import ru.ress.roadtogame.core.scenes.SceneMain;

/**
 * Created by ress on 30.09.17.
 */
public class Window extends Thread {
    private RenderWindow app;
    private int w,h;
    private String title;
    private Scene scene;

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
        app.create(new VideoMode(w,h), title, RenderWindow.CLOSE | RenderWindow.RESIZE);
        app.clear(Color.WHITE);
        //app.setFramerateLimit(60);
        scene = new SceneMain(app);
        scene.init();
        loop();
    }

    private void loop() {

        long time = System.currentTimeMillis();
        int frame = 0;

        int x = 0, y = 0;
        while(app.isOpen()) {
            frame++;
            long curTime = System.currentTimeMillis();
            if (curTime > time + 1000) {
                app.setTitle("FPS: "+Integer.toString(frame));
                frame = 0;
                time = curTime;
            }

            scene.draw();
            scene.sendKeyDown(Keyboard.Key.UNKNOWN);
            scene.sendKeyUp(Keyboard.Key.UNKNOWN);
            app.display();

            for(Event event : app.pollEvents()) {
                switch (event.type) {
                    case CLOSED: {
                        app.close();
                        break;
                    }
                    case RESIZED: {
                        Vector2i size = app.getSize();
                        scene.resize(size.x, size.y);
                        break;
                    }
                    case KEY_PRESSED: {
                        scene.sendKeyDown(event.asKeyEvent().key);
                        break;
                    }
                    case KEY_RELEASED: {
                        scene.sendKeyUp(event.asKeyEvent().key);
                        break;
                    }
                    case MOUSE_BUTTON_PRESSED: {
                        Vector2i pos = event.asMouseEvent().position;
                        scene.sendMouseDown(event.asMouseButtonEvent().button, pos.x, pos.y);
                        break;
                    }
                    case MOUSE_BUTTON_RELEASED: {
                        Vector2i pos = event.asMouseEvent().position;
                        scene.sendMouseUp(event.asMouseButtonEvent().button, pos.x, pos.y);
                        break;
                    }
                }
            }
        }

    }

}
