package game.actor.game;

import engine.Actor;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import org.newdawn.slick.Color;

import java.io.Serializable;

public class Tile extends Actor implements Serializable {

    public static final double SIZE = 32;
    private static final double  TILES_IN_LINE = 16;

    public Tile(String texture,int partX, int partY){
        setVariable("texture", texture);
        setVariable("partX", partX);
        setVariable("partY", partY);
    }

    public static void draws() {
        int startX = -(int)(Camera.locX/SIZE);
        int startY = -(int)(Camera.locY/SIZE);
        int finishX = startX + (int)(Render2D.getWindowWidth()/SIZE)+2;
        int finishY = startY + (int)(Render2D.getWindowHeight()/SIZE)+2;
        if(startX < 0) startX=0;
        if(startY < 0) startY=0;
        if(finishX > StartGame.game.sizeX-1) finishX = StartGame.game.sizeX-1;
        if(finishY > StartGame.game.sizeX-1) finishY = StartGame.game.sizeY-1;
        for(int i = startX; i <= finishX;i++){
            for(int j = startY; j <= finishY;j++){
                int index = j + i * StartGame.game.sizeX;
                StartGame.game.tiles.get(index).draw(index);
            }
        }
    }

    String getTexture(){
        return "tiles/"+ getVariable("texture");
    }

    String getTextur1(){
        return (String) getVariable("texture");
    }
    double[] getTextureLocation(){
        return new double[]{(int)getVariable("partX"),(int)getVariable("partY")};
    }

    private double[] getTileLoc(int index){
        int x = index/StartGame.game.sizeX;
        int y = index%StartGame.game.sizeX;
        return new double[]{x*32,y*32};
    }

    @Override
    public int logic() {
        return 0;
    }

    @Override
    public void draw() {

    }

    @Override
    public void draw(double x, double y, double s) {

    }

    public void draw(int index) {
        double[] location = getTileLoc(index);
        double[] part = getTextureLocation();
        String texture = getTexture();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        if(locX < 0-32) return;
        if(locX > Render2D.getWindowWidth()+32) return;
        if(locY < 0-32) return;
        if(locY > Render2D.getWindowHeight()+32) return;
        double cutLeft = part[0]/TILES_IN_LINE;
        double cutRight = 1-(part[0]+1)/TILES_IN_LINE;
        double cutUp = part[1]/TILES_IN_LINE;
        double cutDown = 1-(part[1]+1)/TILES_IN_LINE;
        Render2D.angleCutDraw(texture,locX,locY,SIZE,SIZE,0,cutLeft,cutRight,cutUp,cutDown);
    }

    public String[][] info() {
        return new String[][]{
                {"ID:", getVariable("ID") + ""},
        };
    }

    @Override
    public void drawInfo(boolean onlyName){
        String[][] info = info();
        double sizeX = 212;
        double sizeY = info.length*20+10;
        if(onlyName) sizeY=30;
        float locX = InputMain.getCursorX()+28;
        float locY = InputMain.getCursorY()+24;
        if(locY + sizeY > Render2D.getWindowHeight()){
            locY -= sizeY+24;
        }
        Render2D.angleColorDraw("infoBack","interface/white",locX+sizeX/2,locY+sizeY/2,sizeX,sizeY,0,0,0,0,1);
        Render2D.angleDraw("infoBorder1","interface/borderBack",locX+sizeX/2,locY,sizeX,4,0);
        Render2D.angleDraw("infoBorder2","interface/borderBack",locX+sizeX/2,locY+sizeY,sizeX,4,0);
        Render2D.angleDraw("infoBorder3","interface/borderBack",locX,locY+sizeY/2,sizeY,4,90);
        Render2D.angleDraw("infoBorder4","interface/borderBack",locX+sizeX,locY+sizeY/2,sizeY,4,90);
        for(int i =0; i < info.length;i++){
            Text.drawString(info[i][0],locX+4, locY+8+i*20,Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(info[i][1],locX+4+200, locY+8+i*20,Text.CAMBRIA_14, Color.white);
            if(onlyName) break;
        }
    }
    public boolean isUndercursor(int curX, int curY){
        return false;
    }
}
