package game.actor.enviroment;

import engine.Actor;
import engine.Mathp;
import engine.audio3.AudioManager;
import engine.audio3.SoundSource;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.player.Player;
import game.actor.actorsHyper.EnviromentDataBase;
import game.actor.player.PlayerVoice;
import org.newdawn.slick.Color;

import java.util.HashMap;

import static game.actor.game.Tile.SIZE;

public class Activator extends Actor {

    private static final Double DOOR_ANIM_SPEED = 0.04;
    private static final Double DOOR_FRAME_DELIMETER = 0.26;
    public static final double ACTIVATORS_IN_LINE = 4;
    private static final double STATES = 4;

    public Activator(int locX, int locY, String ID) {
        setVariable("locX",locX);
        setVariable("locY",locY);
        setVariable("openingClosing",0);
        if (ID != null) {
            HashMap<String, Object> itemVariables = EnviromentDataBase.getData(ID);
            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
    }

    @Override
    public int logic() {
        PlayerVoice.workVoiceInGame(this);

        int doorState = (int)getVariable("openingClosing");
        double animState = (double)getVariable("stateAnim");
        if(doorState > 0){
            animState += DOOR_ANIM_SPEED;
            if(animState > 1){
                animState = 1;
                doorState = 0;
            }
        }
        int part = (int)(animState/DOOR_FRAME_DELIMETER);
        setVariable("partY",part);
        setVariable("openingClosing",doorState);
        setVariable("stateAnim",animState);
        if(getVariable("trap").equals(1)){
            double[] loc = getTileLoc();
            for(Player el : StartGame.game.controlGroup){
                double[] elLoc = el.getTileLoc();
                double rast = Mathp.rast(loc[0],loc[1],elLoc[0],elLoc[1]);
                if(rast < SIZE){
                    setVariable("openingClosing",1);
                }
            }
        }
        return doorState;
    }

    public void useActivator(Player player){
        int lockLevel = (int)getVariable("lockLevel");
        if(lockLevel != 0){
            int playerLockpicking = player.getVariableInteger("skillLockPick");
            String key = getVariableString("keys");
            boolean haveKey = false;
            for(Item el : StartGame.game.inventory){
                if(el.getVariable("ID").equals(key));{
                    haveKey = true;
                }
            }
            if(playerLockpicking < lockLevel && !haveKey){

                PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CANT_OPEN);
                return;
            }
            else {
                setVariable("lockLevel",0);
            }
        }
        if(getVariableTrunked("stateAnim") == 0.0) {
            SoundSource sr = AudioManager.playSoundInterface("activators/" + getVariableString("sound"));
            player.setVariable("voiceSourceInGame", sr);
        }

        StartGame.game.passment[31][35] = 1;
        StartGame.game.passment[31][36] = 1;
        StartGame.game.tiles.get(35+31*64).setVariable("partX",1);
        StartGame.game.tiles.get(36+31*64).setVariable("partX",1);
        StartGame.game.tiles.get(35+31*64).setVariable("partY",0);
        StartGame.game.tiles.get(36+31*64).setVariable("partY",0);
        setVariable("openingClosing",1);
    }

    public boolean isUndercursor(int curX, int curY){
        double[] location = getTileLoc();
        return Math.abs(curX - Camera.locX - location[0]) < SIZE / 2 && Math.abs(curY - Camera.locY - location[1]) < SIZE / 2;
    }

    private double[] getTextureLocation(){
        return new double[]{(int)getVariable("partX"),(int)getVariable("partY")};
    }
    public double[] getTileLoc(){
        return new double[]{(int)getVariable("locX"),(int)getVariable("locY")};
    }
    String getTexture(){
        return "activators/"+ getVariable("texture");
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double[] part = getTextureLocation();
        String texture = getTexture();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        double cutLeft = part[0]/ACTIVATORS_IN_LINE;
        double cutRight = 1-(part[0]+1)/ACTIVATORS_IN_LINE;
        double cutUp = part[1]/STATES;
        double cutDown = 1-(part[1]+1)/STATES;
        Render2D.angleCutDraw("activator",texture,locX,locY,SIZE,SIZE,0,cutLeft,cutRight,cutUp,cutDown);
    }

    @Override
    public void draw(double x, double y, double s) {
        double[] part = getTextureLocation();
        String texture = getTexture();
        double cutLeft = part[0]/ACTIVATORS_IN_LINE;
        double cutRight = 1-(part[0]+1)/ACTIVATORS_IN_LINE;
        double cutUp = part[1]/STATES;
        double cutDown = 1-(part[1]+1)/STATES;
        Render2D.angleCutDraw("activator"+x+""+y+""+s,texture,x,y,s,s,0,cutLeft,cutRight,cutUp,cutDown);
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

    public void reset() {
        setVariable("openingClosing",0);
        setVariable("stateAnim",0.0);
    }
}
