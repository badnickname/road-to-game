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
    private Surface surface;
    private int width, height;
    private int tilewidth, tileheight;
    private Tile[][] map;
    private Texture tileimage;

    public Sprite getSprite() {
        draw();
        return surface.getSurf();
    }

    public void build(String filename, String fileimage, int w, int h) {
        loadTexture(fileimage);
        surface = new Surface(w,h);
        map = new Tile[1][1];
        try{
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

                if (str.equals("[layer]")) {
                    int i=0;
                    byte digitMax = 0;
                    while ((str = bufReader.readLine()) != null && !str.equals("")) {
                        byte digit = getLine(i++,str);
                        if (digit>digitMax) digitMax = digit;
                    }
                }
            }
        }catch (IOException e){
            System.out.println("Ошибка при загрузке тайловой карты");
        }
        return;
    }

    private void draw() {
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                if (map[i][j] != null) surface.draw(map[i][j]);
            }
        }
    }

    private byte getLine(int line, String str) {
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
        Tile tile;
        Vector2i size = tileimage.getSize();
        int hLen = size.x/tilewidth;
        int vLen = size.y/tileheight;
        digit -= 1;
        if (digit<0) return null;
        int i = digit / hLen;
        int j = digit % hLen;
        tile = new Tile(xIndex*tilewidth, yIndex*tileheight, j*tilewidth, i*tileheight, tilewidth, tileheight);
        return tile;
    }

    // SURFACE

    private class Surface {
        private RenderTexture renderTexture;
        private Sprite spr;

        Surface(int w, int h) {
            spr = new Sprite();
            try {
                renderTexture = new RenderTexture();
                renderTexture.create(w,h);
            } catch (TextureCreationException ex) {
                ex.printStackTrace();
            }
        }

        void draw(Tile tile) {
            renderTexture.draw(tile.getSprite());
            return;
        }

        Sprite getSurf() {
            renderTexture.display();
            spr.setTexture(renderTexture.getTexture());
            return spr;
        }
    }

    // TILE

    private class Tile {
        private Sprite spr;
        private IntRect rect;
        Tile(int xpos, int ypos, int x, int y, int w, int h) {
            spr = new Sprite();
            rect = new IntRect(x,y,w,h);
            spr.setTexture(tileimage);
            spr.setTextureRect(rect);
            spr.setPosition(xpos,ypos);
        }

        Sprite getSprite() {
            return spr;
        }
    }
}
