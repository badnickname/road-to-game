package ru.ress.roadtogame;

import ru.ress.roadtogame.core.Tilemap;
import ru.ress.roadtogame.core.Window;

import java.util.Scanner;

/**
 * Created by ress on 30.09.17.
 */
public class Main {
    public static void main(String[] args) {
        Window wnd = new Window(640,480,"road-to-game");
        while(wnd.isAlive());
        return;
    }
}
