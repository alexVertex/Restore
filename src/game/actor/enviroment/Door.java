package game.actor.enviroment;

import engine.Actor;
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

public class Door extends Actor {
    static final String DOORS_TEXTURES_PATH = "doors/";
    private static final Double FRAMES_IN_DOOR = 4.0;
    private static final Double DOOR_ANIM_SPEED = 0.04;
    private static final Double DOOR_FRAME_DELIMETER = 0.26;
    private static final double LOCK_SIZE = 16;

    public Door(int i, int i1, String ID) {
        setVariable("locX",i);
        setVariable("locY",i1);
        setVariable("openingClosing",0);
        setVariable("lockTexture",DOORS_TEXTURES_PATH+"Lock1");
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
            }
        } else if (doorState < 0){
            animState -= DOOR_ANIM_SPEED;
            if(animState < 0){
                animState = 0;
            }
        }
        int part = (int)(animState/DOOR_FRAME_DELIMETER);
        setVariable("partY",part);
        setVariable("openingClosing",doorState);
        setVariable("stateAnim",animState);
        return doorState;
    }

    public void useDoor(Player player){
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
        SoundSource sr = AudioManager.playSoundInterface("activators/"+getVariableString("sound"));
        player.setVariable("voiceSourceInGame",sr);
        StartGame.game.passment[(int)getTileLoc()[0]/32][(int)getTileLoc()[1]/32] = 1;
        int doorState = (int)getVariable("openingClosing");
        double animState = (double)getVariable("stateAnim");
        if(doorState < 0 || animState == 0){
            doorState = 1;
        } else if(doorState > 0 || animState == 1){
           // doorState = -1;
        }
        setVariable("openingClosing",doorState);
    }

    public boolean isUndercursor(int curX, int curY){
        if(!getVariable("openingClosing").equals(0)) return false;
        double[] location = getTileLoc();
        return Math.abs(curX - Camera.locX - location[0]) < SIZE / 2 && Math.abs(curY - Camera.locY - location[1]) < SIZE / 2;
    }
    private double[] getTextureLocation(){
        return new double[]{(int)getVariable("partX"),(int)getVariable("partY")};
    }
    public double[] getTileLoc(){
        return new double[]{(int)getVariable("locX"),(int)getVariable("locY")};
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        double[] part = getTextureLocation();
        double cutUp = part[1]/FRAMES_IN_DOOR;
        double cutDown = 1-(part[1]+1)/FRAMES_IN_DOOR;
        double cutLeft = part[0]/FRAMES_IN_DOOR;
        double cutRight = 1-(part[0]+1)/FRAMES_IN_DOOR;
        Render2D.angleCutDraw("door", "doors/"+getVariable("texture"),locX,locY,SIZE,SIZE,0,cutLeft,cutRight,cutUp,cutDown);
        if(!getVariable("lockLevel").equals(0)){
            Render2D.simpleDraw("chestLock",(String) getVariable("lockTexture"),locX+getVariableInteger("lockOffX"),locY+getVariableInteger("lockOffY"),LOCK_SIZE,LOCK_SIZE);
        }
    }

    @Override
    public void draw(double x, double y, double s) {
        double[] part = getTextureLocation();
        double cutUp = part[1]/FRAMES_IN_DOOR;
        double cutDown = 1-(part[1]+1)/FRAMES_IN_DOOR;
        double cutLeft = part[0]/FRAMES_IN_DOOR;
        double cutRight = 1-(part[0]+1)/FRAMES_IN_DOOR;
        Render2D.angleCutDraw("door"+x+""+y+""+s,"doors/"+ getVariable("texture"), x, y,SIZE,SIZE,0,cutLeft,cutRight,cutUp,cutDown);
        if(!getVariable("lockLevel").equals(0)){
            Render2D.simpleDraw("chestLock",(String) getVariable("lockTexture"),x+getVariableInteger("lockOffX"),y+getVariableInteger("lockOffY"),LOCK_SIZE,LOCK_SIZE);
        }
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
        StartGame.game.passment[(int)getTileLoc()[0]/32][(int)getTileLoc()[1]/32] = -1;

    }
}
