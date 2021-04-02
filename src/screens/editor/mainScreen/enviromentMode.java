package screens.editor.mainScreen;

import engine.Actor;
import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Droplist;
import engine.render.Camera;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.EnviromentDataBase;
import game.actor.enviroment.*;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import screens.editor.creationWindows.createWindowEnviroment;

import java.util.Comparator;
import java.util.List;


public class enviromentMode {
    static String[] filtersEnv = {"Анимированный тайл","Активатор","Дверь","Сундук","Звук окружения","Структура"};
    static Droplist filterEnv = new Droplist(128,20+32,256,32,"interface/drop",0,filtersEnv, Text.CAMBRIA_14,4);
    public static void control() {
        editorScreen.addItem.work();
        filterEnv.work();
        if(filterEnv.isReleased()){
            editorScreen.mustRecreateShowItems = true;
        }
        if(editorScreen.addItem.isReleased()){
            createWindowEnviroment.drawMe = true;
        }
        editorScreen.undercursor = null;
        for (int i=0; i < editorScreen.showItems.size();i++){
            Actor el = editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            if(Mathp.inRectangle(0+32*x,130-16+y*32,32+32*x,130+16+y*32, InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = el;
                break;
            }
        }
        List<Actor> look = null;
        if(filterEnv.getText().equals("Анимированный тайл"))
            for(Actor el : StartGame.game.animtiles){
                if(el.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                    editorScreen.undercursor = el;
                    break;
                }
            }
        if(filterEnv.getText().equals("Активатор"))
            for(Actor el : StartGame.game.activators){
                if(el.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                    editorScreen.undercursor = el;
                    break;
                }
            }
        if(filterEnv.getText().equals("Дверь"))
            for(Actor el : StartGame.game.doorsList){
                if(el.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                    editorScreen.undercursor = el;
                    break;
                }
            }
        if(filterEnv.getText().equals("Сундук"))
            for(Actor el : StartGame.game.chests){
                if(el.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                    editorScreen.undercursor = el;
                    break;
                }
            }
        if(filterEnv.getText().equals("Звук окружения"))
            for(Actor el : StartGame.game.audioSources){
                if(el.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                    editorScreen.undercursor = el;
                    break;
                }
            }
        if(filterEnv.getText().equals("Структура"))
            for(Actor el : StartGame.game.structuresList){
                if(el.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                    editorScreen.undercursor = el;
                    break;
                }
            }
        if(editorScreen.chosen != null){
            if(Mathp.inRectangle(0,120-48,32,120-16,InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = editorScreen.chosen;
            }
        }
        if(InputMain.isKeyPressed(InputMain.LMB) && filterEnv.getText().equals("Анимированный тайл") || InputMain.isKeyJustReleased(InputMain.LMB)){
            if(editorScreen.undercursor != null){
                editorScreen.chosen = editorScreen.undercursor;
            } else
            if(InputMain.getCursorX() > 256) {
                if (editorScreen.chosen != null) {
                    int pointX = (InputMain.getCursorX() - Camera.locX + 16) / 32*32;
                    int pointY = (InputMain.getCursorY() - Camera.locY + 16) / 32*32;
                    if(editorScreen.chosen.getVariable("typeName").equals("Анимированный тайл")) {

                        StartGame.game.animtiles.add(new AnimTile("", pointX, pointY, editorScreen.chosen.getVariable("ID") + ""));
                    }
                    if(editorScreen.chosen.getVariable("typeName").equals("Активатор")) {
                        Activator activator = new Activator(pointX, pointY, editorScreen.chosen.getVariable("ID") + "");
                        StartGame.game.addActivatorInGame(activator);
                    }
                    if(editorScreen.chosen.getVariable("typeName").equals("Дверь")) {
                        Door door = new Door(pointX, pointY, editorScreen.chosen.getVariable("ID") + "");
                        StartGame.game.addDoorInGame(door);
                    }
                    if(editorScreen.chosen.getVariable("typeName").equals("Сундук")) {
                        Chest chest = new Chest(pointX, pointY, editorScreen.chosen.getVariable("ID") + "", null,"0");
                        StartGame.game.addChestInGame(chest);
                    }
                    if(editorScreen.chosen.getVariable("typeName").equals("Структура"))
                        StartGame.game.structuresList.add(new Structure(pointX,pointY, editorScreen.chosen.getVariable("ID") + ""));
                    if(editorScreen.chosen.getVariable("typeName").equals("Звук окружения"))
                        StartGame.game.audioSources.add(new AudioSource(pointX,pointY, editorScreen.chosen.getVariable("ID") + ""));
                }
            }
        }
        if(InputMain.isKeyPressed(InputMain.RMB) && filterEnv.getText().equals("Анимированный тайл") ||InputMain.isKeyJustReleased(InputMain.RMB)){
            if(editorScreen.undercursor != null){
                if(InputMain.getCursorX() > 256) {
                    if(editorScreen.undercursor.getVariable("typeName").equals("Анимированный тайл"))
                        StartGame.game.animtiles.remove(editorScreen.undercursor);
                    if(editorScreen.undercursor.getVariable("typeName").equals("Активатор"))
                        StartGame.game.activators.remove(editorScreen.undercursor);
                    if(editorScreen.undercursor.getVariable("typeName").equals("Дверь"))
                        StartGame.game.doorsList.remove(editorScreen.undercursor);
                    if(editorScreen.undercursor.getVariable("typeName").equals("Сундук"))
                        StartGame.game.chests.remove(editorScreen.undercursor);
                    if(editorScreen.undercursor.getVariable("typeName").equals("Структура"))
                        StartGame.game.structuresList.remove(editorScreen.undercursor);
                    if(editorScreen.undercursor.getVariable("typeName").equals("Звук окружения")) {
                        ((AudioSource)editorScreen.undercursor).stop();
                        StartGame.game.audioSources.remove(editorScreen.undercursor);
                    }
                } else {
                    createWindowEnviroment.loadData(editorScreen.undercursor);
                }
            }
        }
        if(InputMain.isKeyJustReleased(Keyboard.KEY_TAB)){
            if(editorScreen.undercursor != null && editorScreen.undercursor.getVariable("typeName").equals("Сундук")){
                if(InputMain.getCursorX() > 256) {
                    editorScreen.editorMode = 1;
                    editorScreen.chestContentMode = true;
                    editorScreen.mustRecreateShowItems = true;
                    itemMode.goldIn.setText(editorScreen.undercursor.getVariableString("goldIn"));
                    editorScreen.workWithActor = editorScreen.undercursor;
                    editorScreen.chosen = null;
                }
            }
            if(editorScreen.undercursor != null && editorScreen.undercursor.getVariable("typeName").equals("Звук окружения")){
                if(InputMain.getCursorX() > 256) {
                    ((AudioSource)editorScreen.undercursor).changeDrawArea();
                }
            }
        }
    }

    public static void logic() {
        editorScreen.showItems.clear();
        for (String id : EnviromentDataBase.dataBase.keySet()) {
            Actor item = null;
            switch (filterEnv.getText()){
                case "Анимированный тайл": item = new AnimTile("",0,0,id); break;
                case "Активатор": item = new Activator(0,0,id); break;
                case "Дверь": item = new Door(0,0,id); break;
                case "Сундук": item = new Chest(0,0,id,null,"0"); break;
                case "Звук окружения": item = new AudioSource(0,0,id); break;
                case "Структура": item = new Structure(0,0,id); break;
            }

            if (item != null && item.getVariable("typeName").equals(filterEnv.getText())) {
                editorScreen.showItems.add(item);
            }
        }
        editorScreen.showItems.sort(Comparator.comparing(o -> (o.getVariable("ID") + "")));
    }

    public static void playSound() {
        for (AudioSource el : StartGame.game.audioSources) {
            el.logic();
        }
    }
    public static void stopSound(){
        for (AudioSource el : StartGame.game.audioSources) {
            el.stop();
        }
    }
    public static void draw() {
        editorScreen.addItem.draw();
        filterEnv.draw();
        for (int i=0; i < editorScreen.showItems.size();i++){
            Actor el = editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            if(filterEnv.getText().equals("Анимированный тайл"))
                el.logic();
            el.draw(16+32*x,130+y*32,32);
        }
        if(editorScreen.undercursor != null && !createWindowEnviroment.drawMe){
            editorScreen.undercursor.drawInfo(InputMain.getCursorX() > 256);
        }
        if(editorScreen.chosen != null){
            (editorScreen.chosen).draw(16,120-28,32);
            Text.drawString(editorScreen.chosen.getVariable("ID")+"",36,120-40,Text.CAMBRIA_14, Color.white);
        }
        createWindowEnviroment.draw();
    }
}
