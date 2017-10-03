package ru.ress.roadtogame.core;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2i;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * Created by ress on 30.09.17.
 */
public class Tilemap {
    private RenderWindow app;
    private int width, height;
    private int tilewidth, tileheight;
    private Tile[][] map;
    private Sprite[][] sprMap;
    private Texture tileimage;
    private SurfaceBuilder surfaceBuilder;

    public Tilemap (RenderWindow app) {
        this.app = app;
    }

    public boolean build(String filename, String fileimage, String layer) {
        loadTexture(fileimage);
        try {
            FileInputStream fin = new FileInputStream(filename);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(fin));
            String str;
            while ((str = bufReader.readLine()) != null){
                if (str.equals("[header]")) {
                    while ((str = bufReader.readLine()) != null && !str.equals("")) {
                        if (str.startsWith("width")) width = getInt(str);
                        if (str.startsWith("height")) height = getInt(str);
                        if (str.startsWith("tilewidth")) tilewidth = getInt(str);
                        if (str.startsWith("tileheight")) tileheight = getInt(str);
                        map = new Tile[width][height];
                    }
                }

                if (str.equals("["+layer+"]")) {
                    int i=0;
                    while ((str = bufReader.readLine()) != null && !str.equals("")) {
                        procLine(i++,str);
                    }
                }
            }
        }catch (IOException e){
            System.out.println("Ошибка при загрузке тайловой карты");
            return false;
        }
        return true;
    }

    public void draw(Vector2i pos, Vector2i screen) {
        int scrx1 = (pos.x+screen.x)/tilewidth+1;
        int scrx0 = pos.x/tilewidth;
        if (scrx0<0) scrx0 = 0;
        int scry1 = (pos.y+screen.y)/tileheight+1;
        int scry0 = pos.y/tileheight;
        if (scry0<0) scry0 = 0;

        for(int i=scry0; i<scry1; i++) {
            for(int j=scrx0; j<scrx1; j++) {
                if (map[i][j] == null) continue;
                app.draw(map[i][j]);
            }
        }
    }

    private byte procLine(int line, String str) {
        int j = 0;
        byte digitMax = 0;
        String digitStr = "";
        for(int i=0;i<str.length();i++) {
            if (str.charAt(i)==',') {
                byte digit = Byte.valueOf(digitStr);
                if (digit>digitMax) digitMax = digit;
                map[line][j] = getTile(digit, j, line);
                digitStr = "";
                j++;
                continue;
            }
            digitStr += str.charAt(i);
        }
        return digitMax;
    }

    private int getInt(String str) {
        int number = 0;
        int n = 1;
        for(int i=str.length()-1;i>-1;i--) {
            char ch = str.charAt(i);
            if (ch>47 && ch < 59) {
                number += n * (ch-48);
                n*=10;
            }
        }
        return number;
    }

    private void loadTexture(String file) {
        tileimage = new Texture();
        try {
            tileimage.loadFromFile(Paths.get(file));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return;
    }

    private Tile getTile(int digit, int xIndex, int yIndex) {
        if (--digit<0) return null;

        Tile tile;
        Vector2i size = tileimage.getSize();
        int hLen = size.x/tilewidth;
        int i = digit / hLen;
        int j = digit % hLen;
        tile = new Tile(xIndex*tilewidth, yIndex*tileheight,
                j*tilewidth, i*tileheight, tilewidth, tileheight);
        return tile;
    }

    // TILE

    private class Tile extends Sprite {
        Tile(int xpos, int ypos, int x, int y, int w, int h) {
            IntRect rect = new IntRect(x,y,w,h);
            setTexture(tileimage);
            setTextureRect(rect);
            setPosition(xpos,ypos);
        }
    }

    // SURFBUILDER

    private class SurfaceBuilder extends Thread {
        SurfaceBuilder() {
            setDaemon(true);
        }

        @Override
        public void run() {

        }

    }

}
