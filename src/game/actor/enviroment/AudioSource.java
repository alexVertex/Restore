package game.actor.enviroment;
import engine.Actor;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.actorsHyper.EnviromentDataBase;
import org.newdawn.slick.Color;

import java.util.HashMap;

public class AudioSource extends Actor{
    public AudioSource(double locX, double locY, String ID) {
        setVariable("playing",0);
        setVariable("drawArea",0);
        setVariable("locX",locX);
        setVariable("locY",locY);
        if (ID != null) {
            HashMap<String, Object> itemVariables = EnviromentDataBase.getData(ID);
            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
    }

    public void stop(){
        String sound = (String) getVariable("audio");
        if(sound != null) {
            AudioManager.stopAmbient(sound);
        }
        setVariable("playing",0);
    }

    @Override
    public int logic() {
        if(getVariableInteger("playing") == 0) {
            String audio =  AudioManager.playSoundAmbient("ambient/"+getVariable("source") + "");
            setVariable("playing",1);
            setVariable("audio",audio);
        } else {
            String sound = (String) getVariable("audio");
            AudioManager.workAmbient(sound, getVariableTrunked("locX"), getVariableTrunked("locY"), getVariableTrunked("rast"));
        }
        return 0;
    }

    public double[] getTileLoc(){
        return new double[]{(double)getVariable("locX"),(double)getVariable("locY")};
    }

    public void changeDrawArea(){
        setVariable("drawArea",1-getVariableInteger("drawArea"));
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        Render2D.simpleDraw("sound","editor/sound",locX,locY,32,32);
        double rast = getVariableTrunked("rast")*2;
        if(getVariableInteger("drawArea") == 1)
            Render2D.angleColorDraw("soundArea","editor/sound", locX, locY,rast,rast,0,1,1,1,0.25);
    }

    @Override
    public void draw(double x, double y, double s) {
        Render2D.simpleDraw("sound"+x+""+y+""+s,"editor/sound", x, y,s,s);
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
        double[] location = getTileLoc();
        return Math.abs(curX - Camera.locX - location[0]) <= 32 / 2 && Math.abs(curY - Camera.locY - location[1]) <= 32 / 2;
    }
}
