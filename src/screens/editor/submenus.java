package screens.editor;

import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Textbox;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.RegionDataBase;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import java.io.File;

public class submenus {
    public static boolean firstOpen;
    private static double[][] size = {{400,350},{400,400},{400,400},{400,350}};
    private static String[] name = {"Создать карту","Открыть карту","Сохранить карту","Настройки карты"};
    public static void mainSubmenu(int type){
        Render2D.angleColorDraw("windowCreatorBack","interface/white", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, size[type][0], size[type][1], 0, 0, 0, 0, 1);
        Render2D.angleDraw("creatorBorder1","interface/Border", Render2D.getWindowWidth() / 2-size[type][0]/2, Render2D.getWindowHeight() / 2, size[type][1], 4, 90);
        Render2D.angleDraw("creatorBorder2","interface/Border", Render2D.getWindowWidth() / 2+size[type][0]/2, Render2D.getWindowHeight() / 2, size[type][1], 4, 90);
        Render2D.angleDraw("creatorBorder3","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2-size[type][1]/2, size[type][0], 4, 0);
        Render2D.angleDraw("creatorBorder4","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2+size[type][1]/2, size[type][0], 4, 0);
        Text.drawStringCenter(name[type],Render2D.getWindowWidth() / 2,Render2D.getWindowHeight() / 2-size[type][1]/2+10,Text.CAMBRIA_14, Color.white);
        if(InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE)){
            editorScreen.subMenu = -1;
            return;
        }
        switch (type){
            case 0:
                newMapMenu();
                break;
            case 1:
                loadMapMenu();
                break;
            case 2:
                saveMapMenu();
                break;
            case 3:
                setsMapMenu();
                break;
        }
    }
    private static Textbox textBoxMapName4 = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30,256,30,"interface/text","название карты",Text.CAMBRIA_14,4);
    private static Textbox textBoxSizeX4 = new Textbox(Render2D.getWindowWidth() / 2+70-64,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*2,128,30,"interface/text",Text.CAMBRIA_14,4,1,50);
    private static Textbox textBoxSizeY4 = new Textbox(Render2D.getWindowWidth() / 2+70+64,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*2,128,30,"interface/text",Text.CAMBRIA_14,4,1,50);
    private static Textbox textBoxMapID4 = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*3,256,30,"interface/text","номер карты",Text.CAMBRIA_14,4);
    private static Droplist dropBoxMinimap4 = new Droplist(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*4,256,30,"interface/drop",0,new String[]{"alex"},Text.CAMBRIA_14,4);
    private static Droplist dropBoxRegion4 = new Droplist(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*5,256,30,"interface/drop",0,new String[]{"alex"},Text.CAMBRIA_14,4);

    static Button OK4 = new Button(Render2D.getWindowWidth() / 2+size[3][0]/2 -18-32, Render2D.getWindowHeight() / 2+size[3][1]/2-18,32,32,"interface/butOK");
    static Button Cancel4 = new Button(Render2D.getWindowWidth() / 2+size[3][0]/2 - 18, Render2D.getWindowHeight() / 2+size[3][1]/2-18,32,32,"interface/butCancel");

    private static void setsMapMenu() {
        if(firstOpen){
            firstOpen = false;
            textBoxMapName4.setText(StartGame.game.mapName);
            textBoxSizeX4.setText(StartGame.game.sizeX/32+"");
            textBoxSizeY4.setText(StartGame.game.sizeY/32+"");
            textBoxMapID4.setText(StartGame.game.mapID);

            File folder = new File("res/tex/minimaps");
            File[] folderEntries = folder.listFiles();
            String[] fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            dropBoxMinimap4.setVals(fileNames);

            Object[] names = RegionDataBase.getRegion().keySet().toArray();
            String[] namesS = new String[names.length];
            for(int i = 0; i < names.length;i++){
                namesS[i] = names[i]+"";
            }
            dropBoxRegion4.setVals(namesS);

        }
        textBoxMapID4.draw();
        textBoxMapID4.work();
        textBoxMapName4.draw();
        textBoxMapName4.work();
        textBoxSizeX4.draw();
        textBoxSizeX4.work();
        textBoxSizeY4.draw();
        textBoxSizeY4.work();
        dropBoxMinimap4.draw();
        dropBoxMinimap4.work();
        dropBoxRegion4.draw();
        dropBoxRegion4.work();
        OK4.draw();
        OK4.work();
        Cancel4.draw();
        Cancel4.work();
        Text.drawString("Название",Render2D.getWindowWidth() / 2-size[3][0]/2 + 8,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30,Text.CAMBRIA_14,Color.white);
        Text.drawString("Размер карты",Render2D.getWindowWidth() / 2-size[3][0]/2 + 8,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*2,Text.CAMBRIA_14,Color.white);
        Text.drawString("ID карты",Render2D.getWindowWidth() / 2-size[3][0]/2 + 8,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*3,Text.CAMBRIA_14,Color.white);
        Text.drawString("Миникарта",Render2D.getWindowWidth() / 2-size[3][0]/2 + 8,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*4,Text.CAMBRIA_14,Color.white);
        Text.drawString("Регион",Render2D.getWindowWidth() / 2-size[3][0]/2 + 8,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*5,Text.CAMBRIA_14,Color.white);

        double mapSize = 256;
        Render2D.simpleDraw("minimap", "minimaps/" + dropBoxMinimap4.getText(),  mapSize/2+mapSize+10, Render2D.getWindowHeight()-mapSize/2, mapSize, mapSize);
        Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);


        if(Cancel4.isReleased()){
            editorScreen.subMenu = -1;
        }
        if(OK4.isReleased()){
            editorScreen.subMenu = -1;
            int x = Integer.parseInt(textBoxSizeX4.getText());
            int y = Integer.parseInt(textBoxSizeY4.getText());
            editorScreen.changeMapSets(x,y,textBoxMapName.getText(),textBoxMapID4.getText(),dropBoxMinimap4.getText(),dropBoxRegion4.getText());
        }
    }

    private static Textbox textBoxMapName = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30,256,30,"interface/text","название карты",Text.CAMBRIA_14,4);
    private static Textbox textBoxSizeX = new Textbox(Render2D.getWindowWidth() / 2+70-64,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*2,128,30,"interface/text",Text.CAMBRIA_14,4,1,50);
    private static Textbox textBoxSizeY = new Textbox(Render2D.getWindowWidth() / 2+70+64,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*2,128,30,"interface/text",Text.CAMBRIA_14,4,1,50);
    private static Textbox textBoxMapID = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[3][1]/2+10+30*3,256,30,"interface/text","номер карты",Text.CAMBRIA_14,4);

    static Button OK = new Button(Render2D.getWindowWidth() / 2+size[0][0]/2 -18-32, Render2D.getWindowHeight() / 2+size[0][1]/2-18,32,32,"interface/butOK");
    static Button Cancel = new Button(Render2D.getWindowWidth() / 2+size[0][0]/2 - 18, Render2D.getWindowHeight() / 2+size[0][1]/2-18,32,32,"interface/butCancel");

    private static void newMapMenu(){
        textBoxMapName.draw();
        textBoxMapName.work();
        textBoxSizeX.draw();
        textBoxSizeX.work();
        textBoxSizeY.draw();
        textBoxSizeY.work();
        textBoxMapID.draw();
        textBoxMapID.work();
        OK.draw();
        OK.work();
        Cancel.draw();
        Cancel.work();
        Text.drawString("Название",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30,Text.CAMBRIA_14,Color.white);
        Text.drawString("Размер карты",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*2,Text.CAMBRIA_14,Color.white);
        Text.drawString("ID карты",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*3,Text.CAMBRIA_14,Color.white);

        Text.drawString("Сохраните текущую карту!",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2+size[0][1]/2-28,Text.CAMBRIA_14,Color.white);

        if(Cancel.isReleased()){
            editorScreen.subMenu = -1;
        }
        if(OK.isReleased()){
            editorScreen.subMenu = -1;
            int x = Integer.parseInt(textBoxSizeX.getText());
            int y = Integer.parseInt(textBoxSizeY.getText());
            editorScreen.makeMap(x,y,textBoxMapName.getText(),"Limbo",textBoxMapID.getText());
        }
    }
    private static Textbox textBoxMapName2 = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[2][1]/2+10+30,256,30,"interface/text","название карты",Text.CAMBRIA_14,4);
    static Button OK2 = new Button(Render2D.getWindowWidth() / 2+size[2][0]/2 -18-32, Render2D.getWindowHeight() / 2+size[2][1]/2-18,32,32,"interface/butOK");
    static Button Cancel2 = new Button(Render2D.getWindowWidth() / 2+size[2][0]/2 - 18, Render2D.getWindowHeight() / 2+size[2][1]/2-18,32,32,"interface/butCancel");

    private static void saveMapMenu(){
        Text.drawString("Имя файла",Render2D.getWindowWidth() / 2-size[2][0]/2 + 8,Render2D.getWindowHeight() / 2-size[2][1]/2+10+30,Text.CAMBRIA_14,Color.white);
        Text.drawStringCenter("Доступные файлы",Render2D.getWindowWidth() / 2,Render2D.getWindowHeight() / 2-size[2][1]/2+80,Text.CAMBRIA_14, Color.white);
        textBoxMapName2.draw();
        textBoxMapName2.work();
        OK2.draw();
        OK2.work();
        Cancel2.draw();
        Cancel2.work();

        File folder = new File("res/dat/map");
        File[] folderEntries = folder.listFiles();
        int passed = 0;
        int underCursor = -1;
        String nameUnder = "";
        for(int i = 0; i < folderEntries.length;i++){
            String name = folderEntries[i].getName();
            double ly = Render2D.getWindowHeight() / 2-size[2][1]/2+10+110+20*(i-passed);
            if(name.substring(name.length()-4).equals(".map")){
                name = name.substring(0,name.length()-4);
                Text.drawString(name,Render2D.getWindowWidth() / 2-size[2][0]/2 + 8,ly,Text.CAMBRIA_14,Color.white);
                if(Mathp.inRectangle(Display.getWidth()/2-size[2][0]/2,ly,Display.getWidth()/2+size[2][0]/2,ly + 20,InputMain.getCursorX(),InputMain.getCursorY())){
                    underCursor = i; nameUnder = name;
                }
            } else {
                passed++;
            }
        }
        if(underCursor != -1){
            double ly = Render2D.getWindowHeight() / 2-size[2][1]/2+10+110+20*(underCursor-passed)+10;
            Render2D.angleColorDraw("interface/white", Render2D.getWindowWidth() / 2, ly, size[2][0], 20, 0, 1, 1, 1,0.5);
            if(InputMain.isKeyJustReleased(InputMain.LMB)){
                textBoxMapName2.setText(nameUnder);
            }
        }
        if(Cancel2.isReleased()){
            editorScreen.subMenu = -1;
        }
        if(OK2.isReleased()){
            editorScreen.subMenu = -1;
            StartGame.game.saveMap(textBoxMapName2.getText());
        }
    }

    private static Textbox textBoxMapName3 = new Textbox(Render2D.getWindowWidth() / 2+70,Render2D.getWindowHeight() / 2-size[2][1]/2+10+30,256,30,"interface/text","название карты",Text.CAMBRIA_14,4);
    static Button OK3 = new Button(Render2D.getWindowWidth() / 2+size[2][0]/2 -18-32, Render2D.getWindowHeight() / 2+size[2][1]/2-18,32,32,"interface/butOK");
    static Button Cancel3 = new Button(Render2D.getWindowWidth() / 2+size[2][0]/2 - 18, Render2D.getWindowHeight() / 2+size[2][1]/2-18,32,32,"interface/butCancel");

    private static void loadMapMenu(){
        Text.drawString("Имя файла",Render2D.getWindowWidth() / 2-size[2][0]/2 + 8,Render2D.getWindowHeight() / 2-size[2][1]/2+10+30,Text.CAMBRIA_14,Color.white);
        Text.drawStringCenter("Доступные файлы",Render2D.getWindowWidth() / 2,Render2D.getWindowHeight() / 2-size[2][1]/2+80,Text.CAMBRIA_14, Color.white);
        textBoxMapName3.draw();
        textBoxMapName3.work();
        OK3.draw();
        OK3.work();
        Cancel3.draw();
        Cancel3.work();

        File folder = new File("res/dat/map");
        File[] folderEntries = folder.listFiles();
        int passed = 0;
        int underCursor = -1;
        String nameUnder = "";
        for(int i = 0; i < folderEntries.length;i++){
            String name = folderEntries[i].getName();
            double ly = Render2D.getWindowHeight() / 2-size[2][1]/2+10+110+20*(i-passed);
            if(name.substring(name.length()-4).equals(".map")){
                name = name.substring(0,name.length()-4);
                Text.drawString(name,Render2D.getWindowWidth() / 2-size[2][0]/2 + 8,ly,Text.CAMBRIA_14,Color.white);
                if(Mathp.inRectangle(Display.getWidth()/2-size[2][0]/2,ly,Display.getWidth()/2+size[2][0]/2,ly + 20,InputMain.getCursorX(),InputMain.getCursorY())){
                    underCursor = i; nameUnder = name;
                }
            } else {
                passed++;
            }
        }
        if(underCursor != -1){
            double ly = Render2D.getWindowHeight() / 2-size[2][1]/2+10+110+20*(underCursor-passed)+10;
            Render2D.angleColorDraw("interface/white", Render2D.getWindowWidth() / 2, ly, size[2][0], 20, 0, 1, 1, 1,0.5);
            if(InputMain.isKeyJustReleased(InputMain.LMB)){
                textBoxMapName3.setText(nameUnder);
            }
        }
        if(Cancel3.isReleased()){
            editorScreen.subMenu = -1;
        }
        if(OK3.isReleased()){
            editorScreen.subMenu = -1;
            StartGame.game.loadMap(textBoxMapName3.getText());
        }
    }
}
