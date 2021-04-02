package screens.ingame;

import engine.Actor;
import engine.Mathp;
import engine.Start;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import game.StartGame;
import game.actor.enviroment.Activator;
import game.actor.enviroment.Chest;
import game.actor.enviroment.Door;
import game.actor.enviroment.Item;
import game.actor.magic.Spell;
import game.actor.player.*;
import game.actor.story.Event;
import game.actor.story.Quest;
import screens.Controls;

import java.util.List;

public class gameScreenInterface {
    private static int targetOn = -1;
    public static final int TARGET_HEAD = 0;
    public static final int TARGET_TORSO = 1;
    public static final int TARGET_ARMS = 2;
    public static final int TARGET_LEGS = 3;
    private  static Actor undercursor = null;
    private static String rememberItemName = "";
    public static Actor activeActor = null;
    private static Actor interfaceItemUndercursor = null;
    private static String interfaceItemUndercursorName = null;

    private static List[] testUnderCursorLists = {StartGame.game.getNpcList(),  StartGame.game.onMapItems, StartGame.game.doorsList, StartGame.game.chests, StartGame.game.activators};
    private static boolean lockOnMiniMap;

    static void controlInterface() {
        targetOn = Controls.getTargetOn();
        undercursor = null;
        for (int i = 0; i < StartGame.game.controlGroup.size(); i++) {
            if (Math.abs(InputMain.getCursorY() - (48 + i * 96)) < 48 && Math.abs(InputMain.getCursorX() - 58) < 58) {
                undercursor = StartGame.game.controlGroup.get(i);
            }
            if (StartGame.game.controlGroup.get(i).isUndercursor(InputMain.getCursorX(), InputMain.getCursorY())) {
                undercursor = StartGame.game.controlGroup.get(i);
            }
        }

        for(List<Actor> el : testUnderCursorLists){
            for (Actor ele : el) {
                if (ele.isUndercursor(InputMain.getCursorX(), InputMain.getCursorY())) {
                    undercursor = ele;
                }
            }
        }
        interfaceItemUndercursor = null;
        interfaceItemUndercursorName = null;
        String itemUnderCursor = PlayerDrawAndUndercursor.weaponShieldUnderCursor(StartGame.game.getControled());
        if(itemUnderCursor != null){
            interfaceItemUndercursor = (Actor) StartGame.game.getControled().getVariable(itemUnderCursor);
            interfaceItemUndercursorName=itemUnderCursor;
        }
        String spellKeyboard = Controls.getUnderkeyboardSpell();
        String itemKeyboard = Controls.getUnderkeyboardItem();
        Controls.keyboardChangeGear();
        if(spellKeyboard != null){
            Actor activeActorPre = (Spell)StartGame.game.getControled().getVariable(spellKeyboard);
            activeActor = activeActor == activeActorPre ? null : activeActorPre;
            rememberItemName = "";
        }
        if(itemKeyboard != null){
            Actor activeActorPre = (Item)StartGame.game.getControled().getVariable(itemKeyboard);
            activeActor = activeActor == activeActorPre ? null : activeActorPre;
            rememberItemName = itemKeyboard;
        }
        if (InputMain.isKeyJustReleased(InputMain.LMB)) {
            if (!onInterface()) {
                if(activeActor != null){
                    Spell in;
                    if(!(activeActor instanceof Spell)){
                        in = new Spell(activeActor.getVariable("spellIn")+"");
                        in.setVariable("itemName",rememberItemName);
                    }  else {
                        in = (Spell) activeActor;
                    }
                    if(getTargetForSpell(in)){
                        activeActor = null;
                        rememberItemName = "";
                        return;
                    }
                }
                if(StartGame.game.getControled().getVariableInteger("moveTargetType") != PlayerMovesSet.MOVE_TARGET_CAST) {
                    if (undercursor == null) {
                        PlayerMovesSet.setTarget(StartGame.game.getControled(), InputMain.getCursorX() - Camera.locX, InputMain.getCursorY() - Camera.locY);
                    } else {
                        if (undercursor instanceof NPC) {
                            PlayerMovesSet.setTargetAttack(StartGame.game.getControled(), (NPC) undercursor, targetOn);
                        } else if (undercursor instanceof Player) {
                            StartGame.game.setChoosenHero(StartGame.game.controlGroup.indexOf(undercursor));
                        } else if (undercursor instanceof Item) {
                            PlayerMovesSet.setTargetPickUp(StartGame.game.getControled(), (Item) undercursor);
                        } else if (undercursor instanceof Chest) {
                            PlayerMovesSet.setTargetOpenChest(StartGame.game.getControled(), (Chest) undercursor);
                        } else if (undercursor instanceof Door) {
                            PlayerMovesSet.setTargetOpenDoor(StartGame.game.getControled(), (Door) undercursor);
                        } else if (undercursor instanceof Activator) {
                            PlayerMovesSet.setTargetActivator(StartGame.game.getControled(), (Activator) undercursor);
                        }
                    }
                }
            }
        }
    }

    private static boolean getTargetForSpell(Spell spell){
        int activeActorTargetType = spell.getVariableInteger("targetType");
        if(activeActorTargetType == 0){
            PlayerMovesSet.setTargetMagic(StartGame.game.getControled(), StartGame.game.getControled(), spell);
            return true;
        }
        if(undercursor != null) {
            if (activeActorTargetType == 1) {
                PlayerMovesSet.setTargetMagic(StartGame.game.getControled(), (Player) undercursor, spell);
                return true;
            } else if (activeActorTargetType == 2) {
                PlayerMovesSet.setTargetMagic(StartGame.game.getControled(), undercursor.getVariableTrunked("locX"), undercursor.getVariableTrunked("locY"), spell);
                return true;
            }
        } else {
            if (activeActorTargetType == 2) {
                PlayerMovesSet.setTargetMagic(StartGame.game.getControled(), InputMain.getCursorX() - Camera.locX, InputMain.getCursorY() - Camera.locY, spell);
                return true;
            }
        }
        return false;
    }

    private static boolean onInterface(){
        if(InputMain.getCursorY() < 200 && InputMain.getCursorX() > Render2D.getWindowWidth()-200 || lockOnMiniMap){
            return true;
        }
        if(Mathp.rast(0,Render2D.getWindowHeight(),InputMain.getCursorX(),InputMain.getCursorY())<64){
            if(Mathp.rast(0,Render2D.getWindowHeight(),InputMain.getCursorX(),InputMain.getCursorY())>32){
                if(Mathp.getLine(InputMain.getCursorX()/Render2D.getSizeOtn(),InputMain.getCursorY()/Render2D.getHighOtn(),25,13,-11748)<0){
                    PlayerMovesSet.setSpeed(StartGame.game.getControled(),0);
                }
                else if(Mathp.getLine(InputMain.getCursorX()/Render2D.getSizeOtn(),InputMain.getCursorY()/Render2D.getHighOtn(),14,26,-23336)>0){
                    PlayerMovesSet.setSpeed(StartGame.game.getControled(),2);
                } else {
                    PlayerMovesSet.setSpeed(StartGame.game.getControled(),1);
                }
            } else {
                AutoControl.changeBattleMode(StartGame.game.getControled());
            }
            AudioManager.playSoundInterface("interface/Click");

            return true;
        }
        if(Mathp.rast(Render2D.getWindowWidth(),Render2D.getWindowHeight(),InputMain.getCursorX(),InputMain.getCursorY())<64){
            if(Mathp.rast(Render2D.getWindowWidth(),Render2D.getWindowHeight(),InputMain.getCursorX(),InputMain.getCursorY())>32){
                StartGame.game.pauseWorks();
                AudioManager.playSoundInterface("interface/Clock");

            } else {
                Controls.openJournal();
                AudioManager.playSoundInterface("interface/Click");

            }
            return true;
        }
        if(interfaceItemUndercursor != null){

            if(interfaceItemUndercursorName.equals( "weaponSpare") || interfaceItemUndercursorName.equals( "weaponCur")){
                interfaceItemUndercursor = (Item)StartGame.game.getControled().getVariable(interfaceItemUndercursorName);
                PlayerDrawAndUndercursor.changeWeaponShield(StartGame.game.getControled(),"weapon");

            }else if(interfaceItemUndercursorName.equals( "shieldSpare") || interfaceItemUndercursorName.equals( "shieldCur")){
                interfaceItemUndercursor = (Item)StartGame.game.getControled().getVariable(interfaceItemUndercursorName);
                PlayerDrawAndUndercursor.changeWeaponShield(StartGame.game.getControled(),"shield");

            } else if(interfaceItemUndercursorName.contains("spells")){
                Actor activeActorPre = (Spell)StartGame.game.getControled().getVariable(interfaceItemUndercursorName);
                activeActor = activeActor == activeActorPre ? null : activeActorPre;
                AudioManager.playSoundInterface("interface/Click");

            }else if(interfaceItemUndercursorName.contains("uses")){
                Actor activeActorPre = (Item)StartGame.game.getControled().getVariable(interfaceItemUndercursorName);
                activeActor = activeActor == activeActorPre ? null : activeActorPre;
                rememberItemName = interfaceItemUndercursorName;
                AudioManager.playSoundInterface("interface/Click");

            }
            return true;
        }
        return false;
    }

    static void drawInterface() {
        if(StartGame.game.playerInRessurectionPoint()){
            Render2D.simpleDraw("camp", "interface/camp", Render2D.getWindowWidth() - 16, Render2D.getWindowHeight() - 16, 36, 36);
        } else {
            Render2D.simpleDraw("journal", "interface/journal", Render2D.getWindowWidth() - 16, Render2D.getWindowHeight() - 16, 36, 36);
        }
        double rotate = (StartGame.game.time/StartGame.game.timeMax)*360+45;
        Render2D.angleDraw("timeBase","interface/time",Render2D.getWindowWidth(),Render2D.getWindowHeight(),128,128,rotate);
        rotate *= 100;
        Render2D.angleDraw("timeSpeed","interface/timeSpeed",Render2D.getWindowWidth(),Render2D.getWindowHeight(),128,128,rotate);
        Render2D.simpleDraw("comands","interface/comands",32,Render2D.getWindowHeight()-32,64,64);

        PlayerDrawAndUndercursor.controlInterfaceDraw(StartGame.game.getControled());
        if(interfaceItemUndercursor != null){
            interfaceItemUndercursor.drawInfo(false);
        }
        for(int i = 0; i < StartGame.game.controlGroup.size();i++){
            PlayerDrawAndUndercursor.drawPortret(StartGame.game.controlGroup.get(i),i,i==StartGame.game.getChoosenHero());
        }
        workMiniMap();
        Event.showEvents();
        drawCursor();
    }

    private static void workMiniMap() {
        if (InputMain.isKeyPressed(InputMain.LMB) && InputMain.getCursorY() < 200 && InputMain.getCursorX() > Render2D.getWindowWidth() - 200) {
            lockOnMiniMap = true;
        }
        if(lockOnMiniMap){
            double localX = -(InputMain.getCursorX() - (Render2D.getWindowWidth() - 200)) / 200.0;
            double localY = -InputMain.getCursorY() / 200.0;
            Camera.setLocation((int) (localX * StartGame.game.sizeX * 32.0 + Render2D.getWindowWidth() / 2),(int) (localY * StartGame.game.sizeY * 32.0 + Render2D.getWindowHeight() / 2));
            if (InputMain.isKeyReleased(InputMain.LMB) ) {
                lockOnMiniMap = false;
            }
        }
        Render2D.simpleDraw("minimap", "minimaps/" + StartGame.game.minimapTexture, Render2D.getWindowWidth() - 100, 100, 200, 200);
        double otnX = (Camera.locX - Render2D.getWindowWidth() / 2) / (StartGame.game.sizeX * 32);
        double otnY = (Camera.locY - Render2D.getWindowHeight() / 2) / (StartGame.game.sizeY * 32);
        Render2D.simpleDraw("minimap1", "interface/minimapChooser", Render2D.getWindowWidth() - 200 - (int) (200 * otnX), (int) (-200 * otnY), 16, 16);

        for (Player el : StartGame.game.controlGroup) {
            otnX = (el.getVariableTrunked("locX")) / (StartGame.game.sizeX * 32.0);
            otnY = (el.getVariableTrunked("locY")) / (StartGame.game.sizeY * 32.0);
            Render2D.angleColorDraw("controlMeta", "interface/minimapPlayer", Render2D.getWindowWidth() - 200 + (int) (200 * otnX), (int) (200 * otnY), 8, 8, 0, 0, 1, 0, 1);
        }
        for (Player el : StartGame.game.getNpcList()) {
            if (el.isVisible()) {
                otnX = (el.getVariableTrunked("locX")) / (StartGame.game.sizeX * 32.0);
                otnY = (el.getVariableTrunked("locY")) / (StartGame.game.sizeY * 32.0);
                if(el.getVariableInteger("dead") == 1)
                    Render2D.angleColorDraw("controlMeta", "interface/minimapPlayer", Render2D.getWindowWidth() - 200 + (int) (200 * otnX), (int) (200 * otnY), 8, 8, 0, 0.5, 0.5, 0.5, 1);
                else {
                    if(el.getVariableInteger("good") == 0){
                        Render2D.angleColorDraw("controlMeta", "interface/minimapPlayer", Render2D.getWindowWidth() - 200 + (int) (200 * otnX), (int) (200 * otnY), 8, 8, 0, 1, 0, 0, 1);
                    } else {
                        Render2D.angleColorDraw("controlMeta", "interface/minimapPlayer", Render2D.getWindowWidth() - 200 + (int) (200 * otnX), (int) (200 * otnY), 8, 8, 0, 1, 1, 0, 1);
                    }
                }

            }
        }
        for (Quest ele : StartGame.game.questInJournal) {
            ele.drawMiniMap();
        }
    }

    private static String[] targetOnTextures = {"cursors/attackHead","cursors/attackTorso","cursors/attackArms","cursors/attackLegs"};
    private static void drawCursor(){
        if((Start.getScreen().getClass() != GameScreen.class)){
            return;
        }
        if(activeActor != null){
            Render2D.simpleDraw("cursor","cursors/normal", InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
            activeActor.draw(InputMain.getCursorX()+36,InputMain.getCursorY()+36,32);
            return;
        }
        if(targetOn != -1){
            Render2D.simpleDraw("cursor",targetOnTextures[targetOn],InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
            return;
        }
        if(undercursor == null){
            Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
        } else if(undercursor instanceof NPC){
            if(undercursor.getVariable("dead").equals(1)){
                Render2D.simpleDraw("cursor","cursors/pickUp", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
            } else {
                if (undercursor.getVariableInteger("good") == 0) {
                    Render2D.simpleDraw("cursor", "cursors/attack", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
                } else  {
                    Render2D.simpleDraw("cursor", "cursors/talk", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
                }
            }
        } else if (undercursor instanceof Item){
            Render2D.simpleDraw("cursor","cursors/pickUp", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
        } else if (undercursor instanceof Chest){
            Render2D.simpleDraw("cursor","cursors/openChest", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
        } else if (undercursor instanceof Door){
            Render2D.simpleDraw("cursor","cursors/openDoor", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
        } else if (undercursor instanceof Activator){
            Render2D.simpleDraw("cursor","cursors/workWith", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
        } else {
            Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
        }
    }

    public static void dropActiveActor() {
        activeActor = null;
    }
}
