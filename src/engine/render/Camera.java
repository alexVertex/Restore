package engine.render;

import game.StartGame;

public class Camera{
    public static int locX = 0;
    public static int locY = 0;
    public static void changeLocation(int plusX, int plusY){
        locX+=plusX;
        locY+=plusY;
        moveCameraInBorders();
    }

    private static void moveCameraInBorders() {
        if(locX > Render2D.getWindowWidth()/2){
            locX = (int) (Render2D.getWindowWidth()/2);
        }
        if(locY < Render2D.getWindowHeight()/2 - StartGame.game.sizeY*32){
            locY = (int) Render2D.getWindowHeight()/2 - StartGame.game.sizeY*32;
        }
        if(locX < Render2D.getWindowWidth()/2 - StartGame.game.sizeX*32){
            locX = (int) Render2D.getWindowWidth()/2 - StartGame.game.sizeX*32;
        }
        if(locY > Render2D.getWindowHeight()/2){
            locY = (int) (Render2D.getWindowHeight()/2);
        }
    }

    public static void setLocation(int X, int Y){
        locX=X;
        locY=Y;
        moveCameraInBorders();
    }
}
