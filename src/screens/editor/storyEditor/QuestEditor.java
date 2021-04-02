package screens.editor.storyEditor;

import engine.Screen;
import engine.Start;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Textbox;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.QuestDataBase;
import game.actor.enviroment.*;
import game.actor.game.Tile;
import game.actor.player.Player;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.editor.mainScreen.editorScreen;
import screens.editor.submenus;

import java.util.HashMap;

import static screens.editor.mainScreen.editorScreen.makeMap;

public class QuestEditor extends Screen {

    boolean addMode = false;
    public static Button addItem = new Button(20,20,32,32,"interface/butPlus");

    Button subNew = new Button(20+32*0,Render2D.getWindowHeight()-280-32,32,32,"editor/subNewMap");
    Button subOpen = new Button(20+32*1,Render2D.getWindowHeight()-280-32,32,32,"editor/subOpenMap");
    Button subSave = new Button(20+32*2,Render2D.getWindowHeight()-280-32,32,32,"editor/subSaveMap");
    Button subSets = new Button(20+32*3,Render2D.getWindowHeight()-280-32,32,32,"editor/subSetsMap");
    Button[] subButtons = {subNew,subOpen,subSave,subSets};

    public QuestEditor() {
        if(editorScreen.justCreated) {
            makeMap(5,5,"Лимб","Limbo","limb");
            editorScreen.justCreated = false;
        }
    }

    @Override
    public void input() {
        if(InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE) || InputMain.isKeyJustReleased(Keyboard.KEY_RBRACKET)){
            Start.setScreen(new editorScreen());
            return;
        }
        if(!addMode && currentRow == -1) {
            for (int i = 0; i < subButtons.length; i++) {
                subButtons[i].work();
                if (subButtons[i].isReleased()) {
                    editorScreen.subMenu = i;
                    submenus.firstOpen = true;
                    return;
                }
            }
        }
        if(editorScreen.subMenu > -1) {
            return;
        }
        addItem.work();
        if(addItem.isReleased()){
            addMode = true;
        }
        if(addMode){
            return;
        }
        Controls.cameraControl();

    }



    @Override
    public void logic() {

    }

    public static HashMap<String, Object> currentQuest = null;
    public static int currentRow = -1;

    @Override
    public void render() {
        Render2D.clearScreen();
        Tile.draws();
        for(Structure el : StartGame.game.structuresList){
            el.draw();
        }
        for(Item el : StartGame.game.onMapItems){
            el.draw();
        }
        for(AnimTile el : StartGame.game.animtiles){
            el.logic();
            el.draw();
        }
        for(Activator el : StartGame.game.activators){
            el.draw();
        }
        for(Door el : StartGame.game.doorsList){
            el.draw();
        }
        for(Chest el : StartGame.game.chests){
            el.draw();
        }
        for(AudioSource el : StartGame.game.audioSources){
            el.draw();
        }
        for(Structure el : StartGame.game.structuresList){
            el.drawTransperent();
        }
        for(Player el : StartGame.game.getNpcList()){
            el.draw();
        }
        if(currentRow != -1){
            editQuestScreen.work();
        }
        if(addMode){
            addModeWorks();
        }
        Render2D.angleColorDraw("leftBackEditor","interface/white",128,Render2D.getWindowHeight()/2,256,Render2D.getWindowHeight(),0,0.15,0.15,0.15,1);
        addItem.draw();
        for(int i = 0; i < subButtons.length;i++){
            subButtons[i].draw();
        }
        if(editorScreen.subMenu != -1){
            submenus.mainSubmenu(editorScreen.subMenu);
        }
        int row = 0;
        for(String questID : QuestDataBase.dataBase.keySet()){
            HashMap<String,Object> data = QuestDataBase.getData(questID);
            Text.drawString(data.get("questName")+"",2,40+row*16,Text.CAMBRIA_14,Color.white);
            if(InputMain.getCursorX() < 256 && InputMain.getCursorY() > 40+row*16-8+10  && InputMain.getCursorY() < 40+row*16+8+10){
                Render2D.angleColorDraw("chooser","interface/white",128,40+row*16+10,256,16,0,1,1,1,0.5);
                if(InputMain.isKeyJustReleased(InputMain.LMB) && !addMode && currentRow == -1){
                    currentQuest = data;
                    currentRow = row;
                    submenus.firstOpen = true;
                }
            }
            if(row == currentRow){
                Render2D.angleColorDraw("chooser","interface/white",128,40+row*16+10,256,16,0,1,1,0,0.5);
            }
            row++;
        }

        double mapSize = 256;
        if (InputMain.isKeyPressed(InputMain.LMB) && InputMain.getCursorX() < mapSize && InputMain.getCursorY() > Render2D.getWindowHeight() - mapSize) {
            double localY = -(InputMain.getCursorY() - (Render2D.getWindowHeight() - mapSize)) / mapSize;
            double localX = -InputMain.getCursorX() / mapSize;
            Camera.locX = (int) (localX * StartGame.game.sizeX * 32.0 + Render2D.getWindowWidth() / 2);
            Camera.locY = (int) (localY * StartGame.game.sizeY * 32.0 + Render2D.getWindowHeight() / 2);
        }
        Render2D.simpleDraw("minimap", "minimaps/" + StartGame.game.minimapTexture,  mapSize/2, Render2D.getWindowHeight()-mapSize/2, mapSize, mapSize);
        double otnX = (Camera.locX - Render2D.getWindowWidth() / 2) / (StartGame.game.sizeX * 32);
        double otnY = (Camera.locY - Render2D.getWindowHeight() / 2) / (StartGame.game.sizeY * 32);
        Render2D.simpleDraw("minimap1", "interface/minimapChooser",    (int) (-mapSize * otnX), (Render2D.getWindowHeight()-mapSize) + (int) (-mapSize * otnY), 16, 16);
        Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }

    private static double[][] size = {{400,200},{400,400},{400,400},{400,350}};
    private static String[] name = {"Создать задание","Открыть карту","Сохранить карту","Настройки карты"};

    private static Textbox textBoxMapName = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30,256,30,"interface/text","название задания", Text.CAMBRIA_14,4);
    private static Textbox textBoxMapID = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*2,256,30,"interface/text","номер задания",Text.CAMBRIA_14,4);

    static Button OK = new Button(Render2D.getWindowWidth() / 2+size[0][0]/2 -18-32, Render2D.getWindowHeight() / 2+size[0][1]/2-18,32,32,"interface/butOK");
    static Button Cancel = new Button(Render2D.getWindowWidth() / 2+size[0][0]/2 - 18, Render2D.getWindowHeight() / 2+size[0][1]/2-18,32,32,"interface/butCancel");

    private void addModeWorks(){

        Render2D.angleColorDraw("windowCreatorBack","interface/white", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, size[0][0], size[0][1], 0, 0, 0, 0, 1);
        Render2D.angleDraw("creatorBorder1","interface/Border", Render2D.getWindowWidth() / 2-size[0][0]/2, Render2D.getWindowHeight() / 2, size[0][1], 4, 90);
        Render2D.angleDraw("creatorBorder2","interface/Border", Render2D.getWindowWidth() / 2+size[0][0]/2, Render2D.getWindowHeight() / 2, size[0][1], 4, 90);
        Render2D.angleDraw("creatorBorder3","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2-size[0][1]/2, size[0][0], 4, 0);
        Render2D.angleDraw("creatorBorder4","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2+size[0][1]/2, size[0][0], 4, 0);
        Text.drawStringCenter(name[0],Render2D.getWindowWidth() / 2,Render2D.getWindowHeight() / 2-size[0][1]/2+10,Text.CAMBRIA_14, Color.white);

        textBoxMapName.draw();
        textBoxMapName.work();
        textBoxMapID.draw();
        textBoxMapID.work();
        OK.draw();
        OK.work();
        Cancel.draw();
        Cancel.work();
        Text.drawString("Название",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30,Text.CAMBRIA_14, Color.white);
        Text.drawString("ID",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*2,Text.CAMBRIA_14,Color.white);

        if(Cancel.isReleased()){
            addMode = false;
        }
        if(OK.isReleased()){
            addMode = false;
            HashMap<String, Object> data = new HashMap<>();
            data.put("ID",textBoxMapID.getText());
            data.put("questName",textBoxMapName.getText());
            data.put("journalTargetX",16);
            data.put("journalTargetY",16);
            data.put("questAbout","");
            data.put("questMap",StartGame.game.mapID);
            data.put("questTargetsData","");

            QuestDataBase.addData(textBoxMapID.getText(),data);
        }
    }
}
