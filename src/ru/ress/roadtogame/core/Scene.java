package ru.ress.roadtogame.core;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import ru.ress.roadtogame.core.entities.Player;
import ru.ress.roadtogame.core.entities.Wall;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    // OBJECTS FACTORY
    // load( Entities list )
    protected class Entities {
        private String filename;
        private Texture texturePlayer;

        private void loadTextures() {
            try {
                texturePlayer = new Texture();
                texturePlayer.loadFromFile(Paths.get("player.png"));
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        public Entities(String filename) {
            this.filename = filename;
        }

        // ЗАГРУЗКА КАРТЫ ОБЪЕКТОВ
        // list - список объектов
        public boolean load(ArrayList<Entity> list) {
            loadTextures();
            try {
                FileInputStream fin = new FileInputStream(filename);
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(fin));

                String str;
                while ((str = bufReader.readLine()) != null){
                    if (str.equals("[Objects]")) {
                        Entity entity;
                        str = bufReader.readLine();
                        if (str.startsWith("type=")) {
                            str = str.replaceAll("type=","");
                            String entityType = str;
                            switch (entityType) {
                                case "Wall" : {
                                    entity = new Wall();
                                    System.out.println("Created Wall");
                                    break;
                                }
                                case "Player" : {
                                    entity = new Player(texturePlayer,32,32);
                                    System.out.println("Created Player");
                                    break;
                                }
                                default: {
                                    entity = new Entity();
                                }
                            }

                            str = bufReader.readLine();
                            if (str.startsWith("location=")) {
                                str = str.replaceAll("location=", "");
                                String substr = "";
                                int[] digits = new int[4];
                                int count = 0;
                                for(int i=0;i<str.length()+1;i++) {
                                    if (i==str.length() || str.charAt(i) == ',') {
                                        digits[count++] = Integer.valueOf(substr);
                                        substr = "";
                                        continue;
                                    }
                                    substr += str.charAt(i);
                                }
                                switch (entityType) {
                                    case "Wall" : {
                                        entity.setCoords(digits[0] << 5,digits[1]<< 5,digits[2]<< 5,digits[3]<< 5);
                                        break;
                                    }
                                    case "Player" : {
                                        entity.setPosition(digits[0]<< 5,digits[1]<< 5);
                                        break;
                                    }
                                    default: {
                                        entity = new Entity();
                                    }
                                }
                                list.add(entity);
                            }
                        }
                    }
                }
            }catch (IOException e){
                System.out.println("Ошибка при загрузке объектов");
                return false;
            }

            return true;
        }
    }

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
        isInit = defaultLayer.build(fmap, ftile, "layer");
    }

    public void resize(int w, int h) {
        scrWidth = w;
        scrHeight = h;
        view.setSize(w, h);
    }

    public void draw() {
        if (!isInit) return;
        defaultLayer.draw(new Vector2i(0,0), new Vector2i(scrWidth,scrHeight));
    }

}
