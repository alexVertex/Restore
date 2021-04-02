package screens.editor.mainScreen;

import engine.Actor;
import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Textbox;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.NPCDataBase;
import game.actor.player.NPC;
import game.actor.player.NPCPackage;
import game.actor.player.Player;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import screens.editor.creationWindows.createWindowNPC;

import java.util.Comparator;

public class NPCmode {

    static String[] filtersNPC = {"Персонаж"};
    static Droplist filterNPC = new Droplist(128,20+32,256,32,"interface/drop",0,filtersNPC, Text.CAMBRIA_14,4);

    static int packgePointChoosen = -1;
    static NPCPackage workWithPackage = null;
    static String[] filtersPackages = {"Прогулка","Патруль"};
    static Droplist filterPackages = new Droplist(Display.getWidth()-128,20+32,256,32,"interface/drop",0,filtersPackages,Text.CAMBRIA_14,4);
    static Textbox minTimeWait = new Textbox(Display.getWidth()-226,20+64,60,20,"interface/text", Text.CAMBRIA_14,2,1,99999);
    static Textbox maxTimeWait = new Textbox(Display.getWidth()-226,20+84,60,20,"interface/text", Text.CAMBRIA_14,2,1,99999);
    static  Button patrolMinus = new Button(Display.getWidth()-226+30+10,20+84,20,20,"interface/butMinus");
    static Button patrolPlus = new Button(Display.getWidth()-226+30+10+20,20+84,20,20,"interface/butPlus");

    public static void control() {
        filterNPC.work();
        if(filterNPC.isReleased()){
            editorScreen.mustRecreateShowItems = true;
        }
        editorScreen.addItem.work();
        if(editorScreen.addItem.isReleased()){
            createWindowNPC.drawMe = !createWindowNPC.drawMe;
        }
        editorScreen.undercursor = null;
        for(Player el : StartGame.game.getNpcList()){
            if(el.isUndercursorAll(InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = el;
                break;
            }
        }
        for (int i=0; i < editorScreen.showItems.size();i++){
            Actor el = editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            if(Mathp.inRectangle(0+32*x,130-16+y*32,32+32*x,130+16+y*32,InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = el;
                break;
            }
        }
        if(editorScreen.chosen != null){
            if(Mathp.inRectangle(0,120-48,32,120-16,InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = editorScreen.chosen;
            }
        }
        if(editorScreen.choosePackageMode && InputMain.isKeyJustPressed(InputMain.LMB)){
            packgePointChoosen = workWithPackage.pointUndercursor();
        }
        if(InputMain.isKeyJustReleased(InputMain.LMB)) {
            if(editorScreen.choosePackageMode){
                packgePointChoosen = -1;
            } else {
                if (editorScreen.undercursor != null) {
                    editorScreen.chosen = editorScreen.undercursor;
                }
                if (editorScreen.chosen != null && editorScreen.undercursor == null && InputMain.getCursorX() > 256) {
                    int pointX = (InputMain.getCursorX() - Camera.locX + 16) / 32 * 32;
                    int pointY = (InputMain.getCursorY() - Camera.locY + 16) / 32 * 32;
                    NPC npc = new NPC(editorScreen.chosen.getVariable("ID") + "", pointX, pointY);
                    StartGame.game.addNPCInGame(npc);
                    NPCPackage def = new NPCPackage(npc.getVariableTrunked("locX")-32*2,npc.getVariableTrunked("locX")+32*2,
                            npc.getVariableTrunked("locY")-32*2,npc.getVariableTrunked("locY")+32*2,100,500);
                    npc.setVariable("package",def);
                }
            }
        }
        if(InputMain.isKeyJustReleased(InputMain.RMB)) {
            if (editorScreen.undercursor != null && InputMain.getCursorX() < 256) {
                createWindowNPC.loadData(editorScreen.undercursor);
            }
            if (editorScreen.undercursor != null && InputMain.getCursorX() > 256) {
                StartGame.game.getNpcList().remove(editorScreen.undercursor);
            }
        }
        if(InputMain.isKeyJustReleased(Keyboard.KEY_TAB)){
            if(editorScreen.undercursor != null){
                if(InputMain.getCursorX() > 256) {
                    editorScreen.choosePackageMode = true;
                    editorScreen.workWithActor = editorScreen.undercursor;
                    if(editorScreen.workWithActor.getVariable("package") == null){
                        workWithPackage = new NPCPackage(editorScreen.workWithActor.getVariableTrunked("locX")-32*2,editorScreen.workWithActor.getVariableTrunked("locX")+32*2,
                                editorScreen.workWithActor.getVariableTrunked("locY")-32*2,editorScreen.workWithActor.getVariableTrunked("locY")+32*2,100,500);
                        filterPackages.setPos(0);
                    } else {
                        workWithPackage = (NPCPackage)editorScreen.workWithActor.getVariable("package");
                        int[] timeBordersWander = workWithPackage.getTimeWaitWander();
                        minTimeWait.setText(timeBordersWander[0]+"");
                        maxTimeWait.setText(timeBordersWander[1]+"");
                        filterPackages.setPos(workWithPackage.getType());
                    }
                    editorScreen.chosen = null;
                }
            }
        }
        if(editorScreen.choosePackageMode  ){
            if(packgePointChoosen != -1) {
                int pointX = (InputMain.getCursorX() - Camera.locX + 16) / 32 * 32;
                int pointY = (InputMain.getCursorY() - Camera.locY + 16) / 32 * 32;
                if(filterPackages.getVal()==0) {
                    workWithPackage.setPoint(packgePointChoosen, pointX, pointY,0);
                } else if(filterPackages.getVal()==1) {
                    workWithPackage.setPoint(packgePointChoosen, pointX, pointY,Integer.parseInt(minTimeWait.getText()));
                }
            }
            minTimeWait.work();
            maxTimeWait.work();
            filterPackages.work();
            if(filterPackages.isReleased()){
                if(filterPackages.getVal()==0){
                    workWithPackage = new NPCPackage(editorScreen.workWithActor.getVariableTrunked("locX")-32*2,editorScreen.workWithActor.getVariableTrunked("locX")+32*2,
                            editorScreen.workWithActor.getVariableTrunked("locY")-32*2,editorScreen.workWithActor.getVariableTrunked("locY")+32*2,100,500);
                } else {
                    double[][] points = {{editorScreen.workWithActor.getVariableTrunked("locX"),editorScreen.workWithActor.getVariableTrunked("locY"),0}};
                    workWithPackage = new NPCPackage(points,false);

                }
            }
            if(filterPackages.getVal()==1){
                patrolMinus.work();
                if(patrolMinus.isReleased()){
                    workWithPackage.removePatrol();
                }
                if(patrolPlus.isReleased()){
                    workWithPackage.addPatrol();
                }
                patrolPlus.work();
            }

            if(InputMain.isKeyJustPressed(Keyboard.KEY_RETURN)){
                if(filterPackages.getVal()==0) {
                    workWithPackage.setPoint(4, Integer.parseInt(minTimeWait.getText()), Integer.parseInt(maxTimeWait.getText()),0);
                }
                editorScreen.workWithActor.setVariable("package",workWithPackage);
                editorScreen.choosePackageMode = false;
                editorScreen.workWithActor = null;
            }
        }
    }

    public static void logic() {
        editorScreen.showItems.clear();
        for (String id : NPCDataBase.dataBase.keySet()) {
            NPC item = new NPC(id,0,0);
            if (item.getVariable("NPCClassName").equals(filterNPC.getText())) {
                editorScreen.showItems.add(item);
            }
        }
        editorScreen.showItems.sort(Comparator.comparing(o -> (o.getVariable("ID") + "")));
    }

    public static void draw() {
        editorScreen.addItem.draw();
        filterNPC.draw();
        for (int i=0; i < editorScreen.showItems.size();i++){
            Actor el = editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            el.draw(16+32*x,130+y*32,32);
        }
        if(editorScreen.undercursor != null && !createWindowNPC.drawMe){
            editorScreen.undercursor.drawInfo(InputMain.getCursorX() > 256);
        }
        if(editorScreen.chosen != null){
            (editorScreen.chosen).draw(16,120-28,32);
            Text.drawString(editorScreen.chosen.getVariable("ID")+"",36,120-40,Text.CAMBRIA_14, Color.white);
        }
        if(editorScreen.choosePackageMode){
            Render2D.angleColorDraw("rightBackEditor","interface/white",Render2D.getWindowWidth()-128,Render2D.getWindowHeight()/2,256,Render2D.getWindowHeight(),0,0.15,0.15,0.15,1);
            NPC work = (NPC) editorScreen.workWithActor;
            work.drawSelect();
            workWithPackage.draw();
            if(filterPackages.getVal()==0) {
                minTimeWait.draw();
                maxTimeWait.draw();
            } else  if(filterPackages.getVal()==1) {
                minTimeWait.draw();
                maxTimeWait.draw();

                patrolMinus.draw();
                patrolPlus.draw();
            }
            filterPackages.draw();
        }
        createWindowNPC.draw();

    }
}
