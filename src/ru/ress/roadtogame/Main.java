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
        System.out.println(Math.pow(0.5,3.5)/3.5 - Math.pow(0.5,4)/8. + Math.pow(0.5,4.5)/13.5 - Math.pow(0.5,5)/16.);
        while(wnd.isAlive());
    }
}
