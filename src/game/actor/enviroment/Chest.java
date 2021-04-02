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
import game.actor.story.Event;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static game.actor.enviroment.Door.DOORS_TEXTURES_PATH;

public class Chest extends Actor {
    public static final double LOCK_SIZE = 16;
    private static final double SIZE = 32;
    public static final double CHEST_IN_LINE = 4;
    private static final double STATES = 4;

    public Chest(int i, int i1, String ID,String content,String gold) {
        setVariable("locX",i);
        setVariable("locY",i1);
        setVariable("openingClosing",0);
        setVariable("lockTexture",DOORS_TEXTURES_PATH+"Lock1");
        setVariable("openX", i);
        setVariable("openY", i1+32);
        setVariable("openSide", 3);
        setVariable("animSpeed", 0.0);
        setVariable("goldIn",gold);

        List<Item> contents = new ArrayList<>();
        if(content != null) {
            String[] splits = content.split(",");
            for (String el : splits) {
                Item item = new Item("", 0, 0, 0, 0, el, 1);
                contents.add(item);
            }
        }
        setVariable("content", contents);
        if (ID != null) {
            HashMap<String, Object> itemVariables = EnviromentDataBase.getData(ID);
            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
    }

    public boolean isUndercursor(int curX, int curY){
        double[] location = getTileLoc();
        return Math.abs(curX - Camera.locX - location[0]) < SIZE / 2 && Math.abs(curY - Camera.locY - location[1]) < SIZE / 2 && !isOpened();
    }

    public void open(Player player){

        player.setVariable("partY",getVariable("openSide"));
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
            if(playerLockpicking < lockLevel && !haveKey)
            {
                PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CANT_OPEN);
                return;
            }
            else {
                setVariable("lockLevel",0);
            }
        }
        SoundSource sr = AudioManager.playSoundInterface("activators/"+getVariableString("sound"));
        player.setVariable("voiceSourceInGame",sr);
        setVariable("animSpeed", 0.05);
        for(Item el : (List<Item>)getVariable("content")){
            StartGame.game.addItem(el);
            Event.eventPickup(( el.getVariable("name")+""),el.getVariableInteger("count"));

        }
        String gold = getVariableString("goldIn");
        int takeGold = Integer.parseInt(gold);
        Event.eventAddGold(takeGold);
        StartGame.game.gold+=takeGold;
    }

    private boolean isOpened(){
        return getVariable("partY").equals((int)STATES-1);
    }

    @Override
    public int logic() {
        PlayerVoice.workVoiceInGame(this);
        double curFrame = (Double) getVariable("stateAnim");
        curFrame += (double)getVariable("animSpeed");
        if(curFrame >= STATES){
            curFrame = STATES-1;
            setVariable("animSpeed", 0.0);

        }
        setVariable("stateAnim",curFrame);
        setVariable("partY", (int)(curFrame));

        return 0;
    }

    String getTexture(){
        return (String) getVariable("texture");
    }
    private double[] getTextureLocation(){
        return new double[]{(int)getVariable("partX"),(int)getVariable("partY")};
    }
    public  double[] getTileLoc(){
        return new double[]{(int)getVariable("locX"),(int)getVariable("locY")};
    }
    public double[] getOpenLoc(){
        return new double[]{(int)getVariable("openX"),(int)getVariable("openY")};
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double[] part = getTextureLocation();
        String texture ="chests/"+ getTexture();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        double cutLeft = part[0]/CHEST_IN_LINE;
        double cutRight = 1-(part[0]+1)/CHEST_IN_LINE;
        double cutUp = part[1]/STATES;
        double cutDown = 1-(part[1]+1)/STATES;
        Render2D.angleCutDraw("chest",texture,locX,locY,SIZE,SIZE,0,cutLeft,cutRight,cutUp,cutDown);
        if(!getVariable("lockLevel").equals(0)){
            Render2D.simpleDraw("chestLock",(String) getVariable("lockTexture"),locX+getVariableInteger("lockOffX"),locY+getVariableInteger("lockOffY"),LOCK_SIZE,LOCK_SIZE);
        }
    }

    @Override
    public void draw(double x, double y, double s) {
        double[] part = getTextureLocation();
        String texture = "chests/"+getTexture();
        double cutLeft = part[0]/CHEST_IN_LINE;
        double cutRight = 1-(part[0]+1)/CHEST_IN_LINE;
        double cutUp = part[1]/STATES;
        double cutDown = 1-(part[1]+1)/STATES;
        Render2D.angleCutDraw("chest"+x+""+y+""+s,texture, x, y,s,s,0,cutLeft,cutRight,cutUp,cutDown);
        if(!getVariable("lockLevel").equals(0)){
            Render2D.simpleDraw("chestLock"+x+""+y+""+s,(String) getVariable("lockTexture"), x +getVariableInteger("lockOffX"), y +getVariableInteger("lockOffY"),LOCK_SIZE,LOCK_SIZE);
        }
    }

    public void drawSelect() {
        double[] location = getTileLoc();
        double x = location[0] + Camera.locX;
        double y = location[1] + Camera.locY;
        Render2D.simpleDraw("editorInMapSelector","editor/selector", x, y,SIZE,SIZE);
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

    public String saveContent(){
        StringBuilder ansswer = new StringBuilder();
        List<Item> content  = (List<Item>)getVariable("content");
        for(Item el : content){
            ansswer.append(el.getId());
            if(content.indexOf(el) != content.size()-1){
                ansswer.append(",");
            }
        }
        return ansswer.toString();
    }
}
