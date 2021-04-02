package screens.editor.mainScreen;


import engine.Actor;
import engine.Screen;
import engine.Start;
import engine.control.InputMain;
import engine.control.interfaces.Button;

import engine.render.Camera;
import engine.render.Render2D;
import game.StartGame;
import game.actor.enviroment.*;
import game.actor.game.Tile;
import game.actor.player.Player;
import org.lwjgl.input.Keyboard;
import screens.editor.creationWindows.createWindowEnviroment;
import screens.editor.creationWindows.createWindowItems;
import screens.editor.creationWindows.createWindowMagic;
import screens.editor.creationWindows.createWindowNPC;
import screens.editor.storyEditor.QuestEditor;
import screens.editor.storyEditor.editDialog;
import screens.editor.storyEditor.editRegion;
import screens.editor.submenus;
import screens.ingame.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class editorScreen extends Screen {
    static int editorMode = 0;



    double setPassment = -1;
    private boolean drawPassment;

    Button subNew = new Button(20+32*0,Render2D.getWindowHeight()-280-32,32,32,"editor/subNewMap");
    Button subOpen = new Button(20+32*1,Render2D.getWindowHeight()-280-32,32,32,"editor/subOpenMap");
    Button subSave = new Button(20+32*2,Render2D.getWindowHeight()-280-32,32,32,"editor/subSaveMap");
    Button subSets = new Button(20+32*3,Render2D.getWindowHeight()-280-32,32,32,"editor/subSetsMap");
    Button[] subButtons = {subNew,subOpen,subSave,subSets};


    Button modeTiles = new Button(20, Render2D.getWindowHeight()-280,32,32,"editor/modeTiles");
    Button modeItems = new Button(20+32,Render2D.getWindowHeight()-280,32,32,"editor/modeItems");
    Button modeEnviroment = new Button(20+32*2,Render2D.getWindowHeight()-280,32,32,"editor/modeEnviroment");
    Button modeMagic= new Button(20+32*3,Render2D.getWindowHeight()-280,32,32,"editor/modeMagic");
    Button modeNPC= new Button(20+32*4,Render2D.getWindowHeight()-280,32,32,"editor/modeNPC");
    Button modeRessurects= new Button(20+32*5,Render2D.getWindowHeight()-280,32,32,"editor/modeMap");


    public static Button addItem = new Button(20,20,32,32,"interface/butPlus");





     public static boolean justCreated = true;
    static Actor workWithActor = null;


    static boolean chestContentMode = false;

    static boolean choosePackageMode = false;
    public static boolean playSounds;

    public editorScreen() {

    }

    public static void makeMap(int sx, int sy,String name, String minimapTexture, String mapID) {
        StartGame.game.sizeX = sx*32;
        StartGame.game.sizeY = sy*32;
        StartGame.game.passment = new double[ StartGame.game.sizeX][StartGame.game.sizeY];
        StartGame.game.tileMaterial = new int[ StartGame.game.sizeX][StartGame.game.sizeY];
        StartGame.game.clear();
        StartGame.game.mapName = name;
        StartGame.game.minimapTexture = minimapTexture;
        StartGame.game.mapID = mapID;
        for (int i = 0; i <  StartGame.game.sizeX; i++) {
            for (int j = 0; j < StartGame.game.sizeY; j++) {
                Tile tile = new Tile("A0", 0, 0);
                StartGame.game.tiles.add(tile);
                StartGame.game.passment[i][j] = 1;
                StartGame.game.tileMaterial[i][j] = 0;

            }
        }
    }

    private static final int CAMERA_SPEED_KEYBOARD = 1;
    private static final int CAMERA_SPEED_MOUSE = 1;
    private static final int CAMERA_OFFSET_MOUSE_BORDER = 5;
    private static final int CAMERA_SPEED_WHEEL = 1;

    public static int subMenu = -1;

    public static void changeMapSets(int x, int y, String text, String mapID,String ninimap, String region) {
        x *= 32; y*= 32;
        int oldX = StartGame.game.sizeX;
        int oldY = StartGame.game.sizeY;
        double[][] newpassment = new double[x][y];
        List<Tile> newtiles = new ArrayList<>();
        for (int i = 0; i <  x; i++) {
            for (int j = 0; j < y; j++) {
                if(i < oldX && j < oldY){
                    newpassment[i][j] = StartGame.game.passment[i][j];
                    Tile tile = StartGame.game.tiles.get(i*oldY+j);
                    newtiles.add(tile);
                } else {
                    newpassment[i][j] = 1;
                    Tile tile = new Tile("A0", 0, 0);
                    newtiles.add(tile);
                }
            }
        }
        StartGame.game.sizeX = x;
        StartGame.game.sizeY = y;
        StartGame.game.mapName = text;
        StartGame.game.mapID = mapID;
        StartGame.game.passment = newpassment;
        StartGame.game.tiles = newtiles;
        StartGame.game.minimapTexture = ninimap;
        StartGame.game.regionName = region;

    }

    @Override
    public void input() {
        if(InputMain.isKeyJustReleased(Keyboard.KEY_RBRACKET)){
            Start.setScreen(new QuestEditor());
            return;
        }
        if(InputMain.isKeyJustReleased(Keyboard.KEY_LBRACKET)){
            Start.setScreen(new editDialog());
            return;
        }
        if(InputMain.isKeyJustReleased(Keyboard.KEY_APOSTROPHE)){
            Start.setScreen(new editRegion());
            return;
        }
        for(int i = 0; i < subButtons.length;i++){
            subButtons[i].work();
            if(subButtons[i].isReleased()){
                subMenu = i;
                submenus.firstOpen = true;
                return;
            }
        }
        if(subMenu > -1) {
            return;
        }
        modeTiles.work(); if(modeTiles.isReleased()) {editorMode = 0; mustRecreateShowItems = true;}
        modeItems.work(); if(modeItems.isReleased()) {editorMode = 1; mustRecreateShowItems = true;}
        modeEnviroment.work(); if(modeEnviroment.isReleased()) {editorMode = 2; mustRecreateShowItems = true;}
        modeMagic.work(); if(modeMagic.isReleased()) {editorMode = 3; mustRecreateShowItems = true;}
        modeNPC.work(); if(modeNPC.isReleased()) {editorMode = 4; mustRecreateShowItems = true;}
        modeRessurects.work(); if(modeRessurects.isReleased()) {editorMode = 5; mustRecreateShowItems = true;}

        if(mustRecreateShowItems) return;

        if(createWindowItems.drawMe){
            if (InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE)) {
                createWindowItems.drawMe = false;
            }
            return;
        }
        if(createWindowEnviroment.drawMe){
            if (InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE)) {
                createWindowEnviroment.drawMe = false;
                createWindowEnviroment.stopPlaySound();
            }
            return;
        }
        if(createWindowMagic.drawMe){
            if (InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE)) {
                createWindowMagic.drawMe = false;
            }
            return;
        }
        if(createWindowNPC.drawMe){
            if (InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE)) {
                createWindowNPC.drawMe = false;
            }
            return;
        }
        if (InputMain.isKeyJustReleased(Keyboard.KEY_P)) {
            Start.setScreen(new GameScreen());
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_LEFT)) {
            Camera.changeLocation(CAMERA_SPEED_KEYBOARD, 0);
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_Q)) {
            editorMode = 0; mustRecreateShowItems = true; return;
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_W)) {
            editorMode = 1; mustRecreateShowItems = true; return;
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_E)) {
            editorMode = 2; mustRecreateShowItems = true; return;
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_R)) {
            editorMode = 3; mustRecreateShowItems = true; return;
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_T)) {
            editorMode = 4; mustRecreateShowItems = true; return;
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_RIGHT)) {
            Camera.changeLocation(-CAMERA_SPEED_KEYBOARD, 0);
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_UP)) {
            Camera.changeLocation(0, CAMERA_SPEED_KEYBOARD);
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_DOWN)) {
            Camera.changeLocation(0, -CAMERA_SPEED_KEYBOARD);
        }
        if (InputMain.getCursorX() < CAMERA_OFFSET_MOUSE_BORDER) {
            Camera.changeLocation(CAMERA_SPEED_MOUSE, 0);
        }
        if (InputMain.getCursorX() > Render2D.getWindowWidth() - CAMERA_OFFSET_MOUSE_BORDER) {
            Camera.changeLocation(-CAMERA_SPEED_MOUSE, 0);
        }
        if (InputMain.getCursorY() < CAMERA_OFFSET_MOUSE_BORDER) {
            Camera.changeLocation(0, CAMERA_SPEED_MOUSE);
        }
        if (InputMain.getCursorY() > Render2D.getWindowHeight() - CAMERA_OFFSET_MOUSE_BORDER) {
            Camera.changeLocation(0, -CAMERA_SPEED_MOUSE);
        }
        if (InputMain.isKeyPressed(InputMain.WMB)) {
            Camera.changeLocation(InputMain.getCursorDX() * CAMERA_SPEED_WHEEL, InputMain.getCursorDY() * CAMERA_SPEED_WHEEL);
        }
        if (InputMain.isKeyPressed(Keyboard.KEY_LMENU)) {
            Camera.changeLocation(InputMain.getCursorDX() * CAMERA_SPEED_WHEEL, InputMain.getCursorDY() * CAMERA_SPEED_WHEEL);
        }
        if (InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE)) {
            if(chestContentMode || choosePackageMode){
                chestContentMode = false;
                choosePackageMode = false;
                workWithActor = null;
                editorMode = 2;
                mustRecreateShowItems = true;
                chosen = null;
            } else {
                System.exit(0);
            }
        }
        if (InputMain.isKeyJustReleased(Keyboard.KEY_TAB)) {
            if(chestContentMode || choosePackageMode){
                chestContentMode = false;
                choosePackageMode = false;
                workWithActor = null;
                editorMode = 2;
                mustRecreateShowItems = true;
                chosen = null;
                return;
            }
        }
        if (InputMain.isKeyJustReleased(Keyboard.KEY_Z)) {
            drawPassment = !drawPassment;
        }
        if (InputMain.isKeyJustReleased(Keyboard.KEY_X)) {
            playSounds = !playSounds;
            if(!playSounds){
                enviromentMode.stopSound();
            }
        }
        if(editorMode == 0) {
            tileMode.control();
        } else if(editorMode == 1){
            itemMode.control();
        } else if(editorMode == 2){
            enviromentMode.control();
        } else if(editorMode == 3){
            magicMode.control();
        } else if(editorMode == 4){
            NPCmode.control();
        } else if(editorMode == 5){
            resurrectMode.control();
        }
        if(addItem.isReleased()){
            mustRecreateShowItems = true;
        }
    }
    static Actor undercursor = null;
    public static Actor chosen = null;

    static List<Actor> showItems = new ArrayList<>();
    public static boolean mustRecreateShowItems = false;
    @Override
    public void logic() {
        if(playSounds){
            enviromentMode.playSound();
        }
        if(mustRecreateShowItems) {
            if (editorMode == 1) {
                itemMode.logic();
            }
            if (editorMode == 2) {
                enviromentMode.logic();
            } else if(editorMode == 3){
                magicMode.logic();
            }
            else if(editorMode == 4){
                NPCmode.logic();
            }
            mustRecreateShowItems = false;
        }
    }


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
        for(RessurectPoint el : StartGame.game.ressurections){
            el.draw();
        }
        if(drawPassment) {
            for(int i = 0; i < StartGame.game.sizeX;i++){
                for(int j = 0; j < StartGame.game.sizeY;j++){
                    double locX = i*32+Camera.locX;
                    double locY = j*32+Camera.locY;
                    if(locX < 0-32) continue;
                    if(locX > Render2D.getWindowWidth()+32) continue;
                    if(locY < 0-32) continue;
                    if(locY > Render2D.getWindowHeight()+32) continue;
                    double passmentCur = StartGame.game.passment[i][j];
                    double red = 0;
                    double green = 0;
                    if(passmentCur == -1) red = 1;
                    if(passmentCur == 0) {red = 1;green = 1;}
                    if(passmentCur > 0) {green = passmentCur;}
                    Render2D.angleColorDraw("passment"+i+""+j,"interface/white",locX,locY,32,32,0,red,green,0,0.25);
                }
            }
        }

        Render2D.angleColorDraw("leftBackEditor","interface/white",128,Render2D.getWindowHeight()/2,256,Render2D.getWindowHeight(),0,0.15,0.15,0.15,1);
        modeTiles.draw();
        modeItems.draw();
        modeEnviroment.draw();
        modeMagic.draw();
        modeNPC.draw();
        modeRessurects.draw();
        for(int i = 0; i < subButtons.length;i++){
            subButtons[i].draw();
        }
        if(subMenu != -1){
            submenus.mainSubmenu(subMenu);
        }
        if(editorMode == 0) {
            tileMode.draw();
        } else if (editorMode == 1){
            itemMode.draw();
        }else if(editorMode == 2){
            enviromentMode.draw();
        } else if(editorMode == 3){
            magicMode.draw();
        }else if(editorMode == 4){
            NPCmode.draw();
        } else if(editorMode == 5){
            resurrectMode.draw();
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

    public void draw(String texture, double locX,double locY, double sizeX, double sizeY,double partx, double party,double tilesInLine,double tilesInColomn) {
        double cutLeft = partx/tilesInLine;
        double cutRight = 1-(partx+1)/tilesInLine;
        double cutUp = party/tilesInColomn;
        double cutDown = 1-(party+1)/tilesInColomn;
        Render2D.angleCutDraw("editorChoosen",texture,locX,locY,sizeX,sizeY,0,cutLeft,cutRight,cutUp,cutDown);
    }
}
