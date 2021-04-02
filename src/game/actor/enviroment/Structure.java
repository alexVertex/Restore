package game.actor.enviroment;

import engine.Actor;
import engine.Mathp;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.player.Player;
import game.actor.actorsHyper.EnviromentDataBase;
import org.newdawn.slick.Color;

import java.util.HashMap;

public class Structure extends Actor{
    public Structure(int i, int i1, String ID) {
        setVariable("locX",i);
        setVariable("locY",i1);
        if (ID != null) {
            HashMap<String, Object> itemVariables = EnviromentDataBase.getData(ID);
            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
    }

    @Override
    public int logic() {
        return 0;
    }

    public double[] getTileLoc(){
        return new double[]{(int)getVariable("locX"),(int)getVariable("locY")};
    }

    @Override
    public void draw() {
        String texture = "objects/"+getVariable("texture");
        double sizeY = (int)getVariable("sizeY");
        double sizeYSolid = (int)getVariable("sizeYSolid");
        double locX = (int)getVariable("locX") + Camera.locX;
        double locY = (int)getVariable("locY") + Camera.locY + (sizeY-sizeYSolid)/2;
        double sizeX = (int)getVariable("sizeX");
        double cutLeft = (double)getVariable("partLeftSolid");
        double cutUp = (double)getVariable("partTopSolid");
        double cutRight = (double)getVariable("partRightSolid");
        double cutDown = (double)getVariable("partBotSolid");
        Render2D.angleCutDraw("structure",texture,locX,locY,sizeX,sizeYSolid,0,cutLeft,cutRight,cutUp,cutDown);
    }

    public void drawTransperent() {
        String texture = "objects/"+getVariable("texture");
        double sizeY = (int)getVariable("sizeY");
        double sizeYSolid = (int)getVariable("sizeYSolid");
        double locX = (int)getVariable("locX") + Camera.locX;
        double locY = (int)getVariable("locY") + Camera.locY - (sizeYSolid)/2;
        double sizeX = (int)getVariable("sizeX");
        double alpha = 1;
        for(Player el : StartGame.game.controlGroup){
            double[] loc = el.getTileLoc();
            if(Mathp.inRectangle(locX-Camera.locX-sizeX/2,locY-Camera.locY-(sizeY-sizeYSolid)/2,locX-Camera.locX+sizeX/2,locY-Camera.locY+(sizeY-sizeYSolid)/2,loc[0],loc[1])){
                alpha = 0.25;
            }
        }
        double cutLeft = (double)getVariable("partLeftSolid");
        double cutUp = (double)getVariable("toptransperent");
        double cutRight = (double)getVariable("partRightSolid");
        double cutDown = (double)getVariable("bottransperent");
        Render2D.angleCutColorDraw(texture,locX,locY,sizeX,sizeY-sizeYSolid,0,cutLeft,cutRight,cutUp,cutDown,1,1,1,alpha);
    }

    @Override
    public void draw(double x, double y, double s) {
        String texture = "objects/"+getVariable("texture");
        double cutLeft = (double)getVariable("partLeftSolid");
        double cutUp = (double)getVariable("toptransperent");
        double cutRight = (double)getVariable("partRightSolid");
        double cutDown = (double)getVariable("partBotSolid");
        Render2D.angleCutDraw("structure"+x+""+y+""+s,texture,x,y,s,s,0,cutLeft,cutRight,cutUp,cutDown);
    }

    public String[][] info() {
        return new String[][]{
                {"ID:", getVariable("ID") + ""},
        };
    }

    @Override
    public void drawInfo(boolean onlyName) {
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

    @Override
    public boolean isUndercursor(int cursorX, int cursorY) {
        double[] location = new double[]{((int)(getVariable("locX"))),((int)(getVariable("locY")))};
        double[] size = new double[]{((int)(getVariable("sizeX"))),((int)(getVariable("sizeY")))};
        return Math.abs(cursorX - Camera.locX - location[0]) < size[0] / 2 && Math.abs(cursorY - Camera.locY - location[1]) < size[1] / 2;
    }
}
