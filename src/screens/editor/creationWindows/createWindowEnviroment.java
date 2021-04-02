package screens.editor.creationWindows;

import engine.Actor;
import engine.Mathp;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;

import engine.control.interfaces.Element;
import engine.control.interfaces.Textbox;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.actorsHyper.ItemDataBase;
import game.actor.enviroment.*;
import game.actor.actorsHyper.EnviromentDataBase;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import java.io.File;
import java.util.HashMap;

import static game.actor.enviroment.Activator.ACTIVATORS_IN_LINE;
import static game.actor.enviroment.Chest.CHEST_IN_LINE;
import static game.actor.enviroment.Chest.LOCK_SIZE;

public class createWindowEnviroment {
    private static String[] itemClasses = {"Анимированный тайл","Активатор","Дверь","Сундук","Звук окружения","Структура"};
    private static Droplist choosenItemClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+12,256,30,"interface/drop",0,itemClasses, Text.CAMBRIA_14,4);

    static Button OK = new Button(Render2D.getWindowWidth() / 2+250-16-32-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butOK");
    static Button Cancel = new Button(Render2D.getWindowWidth() / 2+250-16-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butCancel");


    private static String[] infoAnimTiles = {"Номер объекта","Кадров в анимации","Скорость анимации"};
    private static String[] infoActivators = {"Номер объекта","Часть текстуры","Состояние","Уровень взлома","Нажимной","Резерв","Звук использования","Ключ"};
    private static String[] infoDoors = {"Номер объекта","Часть текстуры","Состояние","Уровень взлома","Сдвиг замка X","Сдвиг замка Y","Звук использования","Ключ"};
    private static String[] infoChest = {"Номер объекта","Часть текстуры","Состояние","Уровень взлома","Сдвиг замка X","Сдвиг замка Y","Звук использования","Ключ"};
    private static String[] infoSource= {"Номер объекта","Радиус звука"};
    private static String[] infoStructure= {"Номер объекта","Позиция Х","Позиция Y","Размер X","Размер Y","Твёрдая часть"};

    private static String[][] allInfoRus = {infoAnimTiles,infoActivators,infoDoors,infoChest,infoSource,infoStructure};

    private static Textbox textID = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+60,256,30,"interface/text","ИД предмета", Text.CAMBRIA_14,4);

    private static Textbox textBoxFrames = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*0,256,30,"interface/text",Text.CAMBRIA_14,4,1,256);
    private static Textbox textBoxAnimSpeed = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*1,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Element[] animElements = {textID,textBoxFrames,textBoxAnimSpeed};

    private static Textbox textBoxPart = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*0,256,30,"interface/text",Text.CAMBRIA_14,4,0,3);
    private static Textbox textBoxState = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*1,256,30,"interface/text",Text.CAMBRIA_14,4,0,1);
    private static Textbox textBoxLockLevel = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*2,256,30,"interface/text",Text.CAMBRIA_14,4,0,10);
    private static Textbox textBoxLockTrap = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/text",Text.CAMBRIA_14,4,0,1);
    private static Textbox textBoxLockX = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/text",Text.CAMBRIA_14,4,0,32);
    private static Textbox textBoxLockY = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*4,256,30,"interface/text",Text.CAMBRIA_14,4,0,32);
    private static Droplist dropSound = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*5,256,30,"interface/drop",0,new String[]{"",""},Text.CAMBRIA_14,4);
    private static Droplist dropKeys = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*6,256,30,"interface/drop",0,new String[]{"",""},Text.CAMBRIA_14,4);


    private static Element[] activatorElements = {textID,textBoxPart,textBoxState,textBoxLockLevel,textBoxLockTrap,dropSound,dropSound,dropKeys};

    private static Element[] doorElements = {textID,textBoxPart,textBoxState,textBoxLockLevel,textBoxLockX,textBoxLockY,dropSound,dropKeys};

    private static Element[] chestElements = {textID,textBoxPart,textBoxState,textBoxLockLevel,textBoxLockX,textBoxLockY,dropSound,dropKeys};

    private static Textbox textBoxRadius = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*0,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Element[] sourceElements = {textID,textBoxRadius};

    private static Textbox textBoxStartX = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*0,256,30,"interface/text",Text.CAMBRIA_14,4,0,7);
    private static Textbox textBoxStartY = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*1,256,30,"interface/text",Text.CAMBRIA_14,4,0,7);
    private static Textbox textBoxSizeX = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*2,256,30,"interface/text",Text.CAMBRIA_14,4,1,8);
    private static Textbox textBoxSizeY = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/text",Text.CAMBRIA_14,4,1,8);
    private static Textbox textBoxSizeSolid = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*4,256,30,"interface/text",Text.CAMBRIA_14,4,0,8);
    private static Element[] structureElements = {textID,textBoxStartX,textBoxStartY,textBoxSizeX,textBoxSizeY,textBoxSizeSolid};

    private static Element[][] allInfoInputs = {animElements,activatorElements,doorElements,chestElements,sourceElements,structureElements};

    private static Droplist droptextures = new Droplist(Render2D.getWindowWidth() / 2+130,Render2D.getWindowHeight() / 2-400+100+30*15,236,30,"interface/drop",0,new String[]{"",""},Text.CAMBRIA_14,4);


    private static String[] itemFolder = {"res/tex/animTiles/","res/tex/activators/","res/tex/doors/","res/tex/chests/","res/snd/ambient/","res/tex/objects/"};
    private static String itemFile = "smth";
    private static int choosenX = 0;
    private static int choosenY = 0;
    public static boolean drawMe = false;
    public static void loadData(Actor load){
        choosenItemClass.setValue(load.getVariable("typeName")+"");

        File folder = new File(itemFolder[choosenItemClass.getVal()]);
        File[] folderEntries = folder.listFiles();
        String[] fileNames = new String[folderEntries.length];
        for(int i = 0; i < folderEntries.length;i++){
            String name = folderEntries[i].getName().substring(0,folderEntries[i].getName().length()-4);
            fileNames[i] = name;
        }
        droptextures.setVals(fileNames);

        drawMe = true;
        textID.setText(load.getVariable("ID")+"");
        if(choosenItemClass.getText().equals("Звук окружения")){
            droptextures.setValue((load.getVariable("source") + ""));
            itemFile = (load.getVariable("source")+"");
        } else {
            droptextures.setValue((load.getVariable("texture") + ""));
            itemFile = (load.getVariable("texture")+"");
        }

        switch (choosenItemClass.getText()){
            case "Анимированный тайл":
                loadAnimTile(load);
                break;
            case "Активатор":
                loadActivator(load);
                break;
            case "Дверь":
                loadDoor(load);
                break;
            case "Сундук":
                loadChest(load);
                break;
            case "Звук окружения":
                loadSource(load);
                break;
            case "Структура":
                loadStructure(load);
                break;
        }
    }

    private static void loadAnimTile(Actor load){
        textBoxFrames.setText(load.getVariable("frames")+"");
        textBoxAnimSpeed.setText(load.getVariableTrunked("animSpeed",100)+"");
    }
    private static void loadActivator(Actor load){
        textBoxPart.setText(load.getVariable("partX")+"");
        textBoxState.setText(load.getVariableTrunked("stateAnim")+"");
        textBoxLockLevel.setText(load.getVariableInteger("lockLevel")+"");
        textBoxLockTrap.setText(load.getVariableInteger("trap")+"");
        dropSound.setValue(load.getVariableString("sound"));
        dropKeys.setValue(load.getVariableString("keys"));

    }
    private static void loadDoor(Actor load){
        textBoxPart.setText(load.getVariable("partX")+"");
        textBoxState.setText((1-load.getVariableTrunked("stateAnim"))+"");
        textBoxLockLevel.setText(load.getVariableInteger("lockLevel")+"");
        textBoxLockX.setText((load.getVariableInteger("lockOffX")+8)+"");
        textBoxLockY.setText((load.getVariableInteger("lockOffY")+8)+"");
        dropSound.setValue(load.getVariableString("sound"));
        dropKeys.setValue(load.getVariableString("keys"));

    }
    private static void loadChest(Actor load){
        textBoxPart.setText(load.getVariable("partX")+"");
        textBoxState.setText((1-load.getVariableTrunked("stateAnim"))+"");
        textBoxLockLevel.setText(load.getVariableInteger("lockLevel")+"");
        textBoxLockX.setText((load.getVariableInteger("lockOffX")+8)+"");
        textBoxLockY.setText((load.getVariableInteger("lockOffY")+8)+"");
        dropSound.setValue(load.getVariableString("sound"));
        dropKeys.setValue(load.getVariableString("keys"));


    }
    private static void loadSource(Actor load){
        textBoxRadius.setText(load.getVariableTrunked("rast")+"");
    }

    private static void loadStructure(Actor load){


        textBoxSizeX.setText(load.getVariableInteger("sizeX")/32+"");
        textBoxSizeY.setText(load.getVariableInteger("sizeY")/32+"");
        textBoxSizeSolid.setText(load.getVariableInteger("sizeYSolid")/32+"");
        textBoxStartX.setText(load.getVariableTrunked("partLeftSolid",8)+"");
        textBoxStartY.setText(load.getVariableTrunked("toptransperent",8)+"");

    }
    private static int currentItemType = 0;
    private static boolean reSearch = true;
    public static void draw(){
        if(reSearch) {
            File folder = new File(itemFolder[choosenItemClass.getVal()]);
            File[] folderEntries = folder.listFiles();
            String[] fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            droptextures.setVals(fileNames);
            itemFile = droptextures.getText();

            folder = new File("res/snd/activators");
            folderEntries = folder.listFiles();
            fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            dropSound.setVals(fileNames);

            dropKeys.setVals(ItemDataBase.getItemIdKeys());
            reSearch = false;
        }
        if(drawMe) {

            Render2D.angleColorDraw("windowCreatorBack","interface/white", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, 500, 800, 0, 0, 0, 0, 1);
            Render2D.angleDraw("creatorBorder1","interface/Border", Render2D.getWindowWidth() / 2-250, Render2D.getWindowHeight() / 2, 800, 4, 90);
            Render2D.angleDraw("creatorBorder2","interface/Border", Render2D.getWindowWidth() / 2+250, Render2D.getWindowHeight() / 2, 800, 4, 90);
            Render2D.angleDraw("creatorBorder3","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2-400, 500, 4, 0);
            Render2D.angleDraw("creatorBorder4","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2+400, 500, 4, 0);
            choosenItemClass.draw();
            choosenItemClass.work();
            currentItemType = choosenItemClass.getVal();

            if(choosenItemClass.isChanged()) {
                reSearch = true;
                File folder = new File(itemFolder[choosenItemClass.getVal()]);
                File[] folderEntries = folder.listFiles();
                String[] fileNames = new String[folderEntries.length];
                for (int i = 0; i < folderEntries.length; i++) {
                    String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                    fileNames[i] = name;
                }
                droptextures.setVals(fileNames);
                itemFile = droptextures.getText();
                reSearch = false;
            }
            for(int i = 0; i < allInfoRus[currentItemType].length;i++){
                Text.drawString(allInfoRus[currentItemType][i],Render2D.getWindowWidth() / 2-250+10,Render2D.getWindowHeight() / 2-400+50+i*30,Text.CAMBRIA_24, Color.white);
                allInfoInputs[currentItemType][i].work();
                if(dropKeys.isChanged()){
                    int y = 0;
                }
                allInfoInputs[currentItemType][i].draw();
            }
            //Render2D.simpleDraw(itemFolder+itemFile, Render2D.getWindowWidth() / 2-120, Render2D.getWindowHeight() / 2+270, 256, 256);
            //Render2D.simpleDraw("editor/selector", 8 + choosenX * 16+(Render2D.getWindowWidth() / 2-248), 8 + choosenY * 16+(Render2D.getWindowHeight() / 2+270-128), 16, 16);
            draw(Render2D.getWindowWidth() / 2-120+140, Render2D.getWindowHeight() / 2+270,32);
            if (InputMain.isKeyJustPressed(InputMain.LMB) || InputMain.isKeyJustReleased(InputMain.LMB)) {
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        if (Mathp.inRectangle(0 + i * 16+(Render2D.getWindowWidth() / 2-248), 0 + j * 16+(Render2D.getWindowHeight() / 2+270-128), 16 + i * 16+(Render2D.getWindowWidth() / 2-248), 16 + j * 16+(Render2D.getWindowHeight() / 2+270-128), InputMain.getCursorX(), InputMain.getCursorY())) {
                            choosenX = i;
                            choosenY = j;
                        }
                    }
                }
            }
            droptextures.draw();
            droptextures.work();
            itemFile = droptextures.getText();
            OK.work();
            if(OK.isReleased()){
                if(wavEffect != null){
                    AudioManager.stopAmbient(wavEffect);
                    wavEffect = null;
                }
                switch (choosenItemClass.getText()){
                    case "Анимированный тайл":
                        saveAnimTile();
                        editorScreen.chosen = new AnimTile("",0,0,textID.getText());
                        break;
                    case "Активатор":
                        saveActivator();
                        editorScreen.chosen = new Activator(0,0,textID.getText());
                        break;
                    case "Дверь":
                        saveDoor();
                        editorScreen.chosen = new Door(0,0,textID.getText());
                        break;
                    case "Сундук":
                        saveChest();
                        editorScreen.chosen = new Chest(0,0,textID.getText(),null,"0");
                        break;
                    case "Звук окружения":
                        saveSource();
                        editorScreen.chosen = new AudioSource(0,0,textID.getText());
                        break;
                    case "Структура":
                        saveStructure();
                        editorScreen.chosen = new Structure(0,0,textID.getText());
                        break;
                    default:
                        break;
                }
                drawMe = false;
                editorScreen.mustRecreateShowItems = true;
            }
            OK.draw();
            Cancel.work();
            if(Cancel.isReleased()){
                drawMe = false;
                if(wavEffect != null){
                    AudioManager.stopAmbient(wavEffect);
                    wavEffect = null;
                }
            }
            Cancel.draw();
        }

    }
    private static void saveAnimTile(){
        HashMap<String,Object> animTile = new HashMap<>();
        animTile.put("frames",Integer.parseInt(textBoxFrames.getText()));
        animTile.put("animSpeed",(double)Integer.parseInt(textBoxAnimSpeed.getText())/100);
        animTile.put("typeName","Анимированный тайл");
        animTile.put("texture",itemFile);

        String ID = textID.getText();
        animTile.put("ID",ID);
        animTile.put("SaveType",EnviromentDataBase.ANIM_TILE);
        EnviromentDataBase.addData(ID,animTile);
    }
    private static void saveActivator(){
        HashMap<String,Object> animTile = new HashMap<>();
        animTile.put("partX",Integer.parseInt(textBoxPart.getText()));
        int state = Integer.parseInt(textBoxState.getText());
        if(state == 1){
            animTile.put("partY", 0);
            animTile.put("stateAnim", 0.0);
        } else {
            animTile.put("partY", 3);
            animTile.put("stateAnim", 1.0);
        }

        animTile.put("lockLevel",Integer.parseInt(textBoxLockLevel.getText()));
        animTile.put("trap",Integer.parseInt(textBoxLockTrap.getText()));
        animTile.put("typeName","Активатор");
        animTile.put("texture",itemFile);
        animTile.put("sound",dropSound.getText());
        animTile.put("keys",dropKeys.getText());

        String ID = textID.getText();
        animTile.put("ID",ID);
        animTile.put("SaveType",EnviromentDataBase.ACTIVATOR);

        EnviromentDataBase.addData(ID,animTile);
    }
    private static void saveDoor(){
        HashMap<String,Object> animTile = new HashMap<>();
        animTile.put("partX",Integer.parseInt(textBoxPart.getText()));
        int state = Integer.parseInt(textBoxState.getText());
        if(state == 1){
            animTile.put("partY", 0);
            animTile.put("stateAnim", 0.0);
        } else {
            animTile.put("partY", 3);
            animTile.put("stateAnim", 1.0);
        }

        animTile.put("lockLevel",Integer.parseInt(textBoxLockLevel.getText()));
        animTile.put("typeName","Дверь");
        animTile.put("texture",itemFile);
        animTile.put("lockOffX",Integer.parseInt(textBoxLockX.getText())-8);
        animTile.put("lockOffY",Integer.parseInt(textBoxLockY.getText())-8);
        animTile.put("sound",dropSound.getText());
        animTile.put("keys",dropKeys.getText());

        String ID = textID.getText();
        animTile.put("ID",ID);
        animTile.put("SaveType",EnviromentDataBase.DOOR);

        EnviromentDataBase.addData(ID,animTile);
    }
    private static void saveChest(){
        HashMap<String,Object> animTile = new HashMap<>();
        animTile.put("partX",Integer.parseInt(textBoxPart.getText()));
        int state = Integer.parseInt(textBoxState.getText());
        if(state == 1){
            animTile.put("partY", 0);
            animTile.put("stateAnim", 0.0);
        } else {
            animTile.put("partY", 3);
            animTile.put("stateAnim", 1.0);
        }

        animTile.put("lockLevel",Integer.parseInt(textBoxLockLevel.getText()));
        animTile.put("lockOffX",Integer.parseInt(textBoxLockX.getText())-8);
        animTile.put("lockOffY",Integer.parseInt(textBoxLockY.getText())-8);
        animTile.put("sound",dropSound.getText());
        animTile.put("keys",dropKeys.getText());

        animTile.put("typeName","Сундук");
        animTile.put("texture",itemFile);

        String ID = textID.getText();
        animTile.put("ID",ID);
        animTile.put("SaveType",EnviromentDataBase.CHEST);

        EnviromentDataBase.addData(ID,animTile);
    }
    private static void saveSource(){
        HashMap<String,Object> animTile = new HashMap<>();
        animTile.put("rast",(double)Integer.parseInt(textBoxRadius.getText()));
        animTile.put("typeName","Звук окружения");
        animTile.put("source",itemFile);
        String ID = textID.getText();
        animTile.put("ID",ID);
        animTile.put("SaveType",EnviromentDataBase.SOURCE);

        EnviromentDataBase.addData(ID,animTile);
    }
    private static void saveStructure(){
        HashMap<String,Object> animTile = new HashMap<>();


        int startX = Integer.parseInt(textBoxStartX.getText());
        int startY = Integer.parseInt(textBoxStartY.getText());
        int sizeX = Integer.parseInt(textBoxSizeX.getText());
        int sizeY = Integer.parseInt(textBoxSizeY.getText());
        int solid = Integer.parseInt(textBoxSizeSolid.getText());

        double cutLeft = startX*0.125;
        double cutRight = (8-sizeX-startX)*0.125;
        double cutTop = startY*0.125;
        double cutBot = (8-sizeY-startY)*0.125;
        double solidTop = cutTop+(sizeY-solid)*0.125;
        double transparentBot = cutBot+(solid)*0.125;

        animTile.put("sizeX",sizeX*32);
        animTile.put("sizeY",sizeY*32);
        animTile.put("sizeYSolid",solid*32);
        animTile.put("partLeftSolid",cutLeft);
        animTile.put("partRightSolid",cutRight);
        animTile.put("partTopSolid",solidTop);
        animTile.put("partBotSolid",cutBot);
        animTile.put("toptransperent",cutTop);
        animTile.put("bottransperent",transparentBot);

        animTile.put("typeName","Структура");
        animTile.put("texture",itemFile);
        String ID = textID.getText();
        animTile.put("ID",ID);
        animTile.put("SaveType",EnviromentDataBase.STRUCTURE);

        EnviromentDataBase.addData(ID,animTile);
    }
    private static double curAnimState = 0;
    public static void draw(double lx, double ly,double s) {
        String texture = itemFile;

        if(choosenItemClass.getText().equals("Анимированный тайл")) {
            double totalFrames = 0;
            try {
                totalFrames = Integer.parseInt(textBoxFrames.getText());
                curAnimState += (double)Integer.parseInt(textBoxAnimSpeed.getText())/100.0;
            } catch (NumberFormatException ex){
                curAnimState+=0;
                totalFrames = 1;
            }
            if(curAnimState >= totalFrames){
                curAnimState -= totalFrames;
            }
            double cutLeft = (int)curAnimState/totalFrames;
            double cutRight = 1-((int)curAnimState+1)/totalFrames;
            double cutUp = 0;
            double cutDown = 0;
            Render2D.angleCutDraw("creatorItemView","animTiles/"+texture,lx,ly,32,32,0,cutLeft,cutRight,cutUp,cutDown);
        } else if(choosenItemClass.getText().equals("Активатор")){
            double part = 0;
            try {
                part = Integer.parseInt(textBoxPart.getText());
            } catch (NumberFormatException ex){
                part = 0;
            }
            double cutLeft = part/ACTIVATORS_IN_LINE;
            double cutRight = 1-(part+1)/ACTIVATORS_IN_LINE;

            Render2D.angleCutDraw("creatorItemView","activators/"+texture,lx,ly,s,s*4,0,cutLeft,cutRight,0,0);
        }else if(choosenItemClass.getText().equals("Дверь")){
            double part = 0;
            int lock = 0;
            int ofX = 0;
            int ofY = 0;
            try {
                part = Integer.parseInt(textBoxPart.getText());
                lock = Integer.parseInt(textBoxLockLevel.getText());
                ofX = Integer.parseInt(textBoxLockX.getText());
                ofY = Integer.parseInt(textBoxLockY.getText());
            } catch (NumberFormatException ex){
                part = 0;
                lock = 0;
                ofX = 0;
                ofY = 0;
            }
            double cutLeft = part/ACTIVATORS_IN_LINE;
            double cutRight = 1-(part+1)/ACTIVATORS_IN_LINE;

            Render2D.angleCutDraw("creatorItemView","doors/"+texture,lx,ly,s,s*4,0,cutLeft,cutRight,0,0);
            if(lock>0){
                for(int i = 0; i < 1;i++) {
                    Render2D.simpleDraw("CreatorLock",(String) "doors/Lock1", lx + ofX-16+LOCK_SIZE/2, ly + ofY-64+32*(i)+LOCK_SIZE/2, LOCK_SIZE, LOCK_SIZE);
                }
            }
        } else if (choosenItemClass.getText().equals("Сундук")){
            int part = 0;
            int lock = 0;
            int ofX = 0;
            int ofY = 0;
            try {
                part = Integer.parseInt(textBoxPart.getText());
                lock = Integer.parseInt(textBoxLockLevel.getText());
                ofX = Integer.parseInt(textBoxLockX.getText());
                ofY = Integer.parseInt(textBoxLockY.getText());
            } catch (NumberFormatException ex){
                part = 0;lock = 0;
                ofX = 0;
                ofY = 0;
            }
            double cutLeft = part/CHEST_IN_LINE;
            double cutRight = 1-(part+1)/CHEST_IN_LINE;
            Render2D.angleCutDraw("creatorItemView","chests/"+texture, lx, ly,s,s*4,0,cutLeft,cutRight,0,0);

            if(lock>0){
                for(int i = 0; i < 1;i++) {
                    Render2D.simpleDraw("CreatorLock",(String) "doors/Lock1", lx + ofX-16+LOCK_SIZE/2, ly + ofY-64+32*(i)+LOCK_SIZE/2, LOCK_SIZE, LOCK_SIZE);
                }
            }
        } else if (choosenItemClass.getText().equals("Структура")){
            int startX = 0;
            int startY = 0;
            int sizeX = 0;
            int sizeY = 0;
            int solid = 0;
            try {

                startX = Integer.parseInt(textBoxStartX.getText());
                startY = Integer.parseInt(textBoxStartY.getText());
                sizeX = Integer.parseInt(textBoxSizeX.getText());
                sizeY = Integer.parseInt(textBoxSizeY.getText());
                solid = Integer.parseInt(textBoxSizeSolid.getText());

            } catch (NumberFormatException ex){
                startX = 0;
                startY = 0;
                sizeX = 0;
                sizeY = 0;
                solid = 0;
            }
            double cutLeft = startX*0.125;
            double cutRight = (8-sizeX-startX)*0.125;

            double cutTop = startY*0.125;
            double cutBot = (8-sizeY-startY)*0.125;
            Render2D.angleCutDraw("creatorItemView","objects/"+texture, lx-132, ly,sizeX*32*0.75,sizeY*32*0.75,0,cutLeft,cutRight,cutTop,cutBot);

            double solidTop = cutTop+(sizeY-solid)*0.125;
            double solidY = ly +(sizeY-solid)*12;
            Render2D.angleCutDraw("creatorItemView","objects/"+texture, lx+100, solidY,sizeX*32*0.75,solid*32*0.75,0,cutLeft,cutRight,solidTop,cutBot);

            double transparentBot = cutBot+(solid)*0.125;
            double transparentY = ly -(solid)*12;
            Render2D.angleCutColorDraw("objects/"+texture, lx+100, transparentY,sizeX*32*0.75,(sizeY-solid)*32*0.75,0,cutLeft,cutRight,cutTop,transparentBot,1,1,1,0.25);

        } if (choosenItemClass.getText().equals("Звук окружения")) {
            if(droptextures.isReleased() || wavEffect == null) {
                stopPlaySound();
                wavEffect = AudioManager.playSoundAmbient("/ambient/"+itemFile);
            }
        } else {
            stopPlaySound();
        }
    }
    static String wavEffect = null;

    public static void stopPlaySound() {
        if(wavEffect != null){
            AudioManager.stopAmbient(wavEffect);
            wavEffect = null;
        }
    }
}
