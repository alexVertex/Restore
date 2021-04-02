package screens.editor.storyEditor;

import engine.Screen;
import engine.Start;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Slider;
import engine.control.interfaces.Textbox;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.RegionDataBase;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import java.io.File;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public class editRegion extends Screen {

    private final Object[] names;
    Button addItem = new Button(20,20,32,32,"interface/butPlus");


    Textbox name = new Textbox(512,40,256,32,"interface/text","Название региона", Text.CAMBRIA_14,4);

    Slider[][] timeCycle = new Slider[24][3];
    Droplist[] visualMults = new Droplist[24];

    Droplist[] exploreMus = new Droplist[10];
    Droplist[] battleMus = new Droplist[10];


    public editRegion(){
        for(int i = 0; i < 24;i++){
            for(int j = 0; j < 3;j++) {
                Slider slider = new Slider(128+256+j*256, 80+i*32, 256, 26, "interface/slider", 0.5);
                timeCycle[i][j] = slider;
            }
            Droplist vivion = new Droplist(256+256+3*256,72+i*32,128,26,"interface/drop",0,new String[]{"1","0.75","0.5","0.25"},Text.CAMBRIA_14,4);
            visualMults[i] = vivion;
        }

        File folder = new File("res/mus/");
        File[] folderEntries = folder.listFiles();
        String[] fileNames = new String[folderEntries.length+1];
        fileNames[0] = "пусто";
        for (int i = 0; i < folderEntries.length; i++) {
            String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
            fileNames[i+1] = name;
        }
        for(int i = 0; i < 10;i++){
            Droplist explore = new Droplist(1600-150,72+i*32,128,26,"interface/drop",0,fileNames,Text.CAMBRIA_14,4);
            Droplist battle = new Droplist(1600-150,72+(i+12)*32,128,26,"interface/drop",0,fileNames,Text.CAMBRIA_14,4);
            exploreMus[i] = explore;
            battleMus[i] = battle;
        }
        names =  RegionDataBase.getRegion().keySet().toArray();
    }


    @Override
    public void input() {
        if(InputMain.isKeyJustReleased(Keyboard.KEY_APOSTROPHE)){
            Start.setScreen(new editorScreen());
        }
    }

    @Override
    public void logic() {

    }

    @Override
    public void render() {
        Render2D.clearScreen();
        Render2D.angleColorDraw("leftBackEditor","interface/white",128,Render2D.getWindowHeight()/2,256,Render2D.getWindowHeight(),0,0.15,0.15,0.15,1);
        for(int i = 0; i < names.length;i++){
            Text.drawString(names[i]+"",0,i*20+40,Text.CAMBRIA_14, Color.white);
            if(InputMain.getCursorX() < 256 && InputMain.getCursorY() > 40+i*20-8+10  && InputMain.getCursorY() < 40+i*20+8+10){
                Render2D.angleColorDraw("chooser","interface/white",128,40+i*20+10,256,16,0,1,0,0,0.5);
                if(InputMain.isKeyJustReleased(InputMain.LMB)){
                    openRegion(names[i]+"");
                }
            }
        }
        name.work(); name.draw();
        for (int i = 0; i < timeCycle.length;i++) {
            for (Slider slider : timeCycle[i]) {
                slider.work();
                slider.draw();
            }
            visualMults[i].work();
            visualMults[i].draw();
            int y = 80+i*32;
            Render2D.angleColorDraw("ambientLight","interface/white",128-32+4*256,y,128,26,0,1,1,1,1.0);
            glBlendFunc(GL_ZERO, GL_ONE_MINUS_SRC_COLOR);
            Render2D.angleColorDraw("ambientLight","interface/white",128-32+4*256,y,128,26,0,1-timeCycle[i][0].getSignal(),1-timeCycle[i][1].getSignal(),1-timeCycle[i][2].getSignal(),0.0);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        for (int i = 0; i < exploreMus.length;i++) {
            exploreMus[i].work();
            exploreMus[i].draw();
            battleMus[i].work();
            battleMus[i].draw();
        }
        addItem.work(); addItem.draw();
        if(addItem.isReleased()){
            saveRegion();
        }
        Render2D.simpleDraw("cursor","cursors/normal", InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }

    private void openRegion(String s) {
        String data = RegionDataBase.loadRegion(s);
        String[] dataSplit = data.split(";");
        name.setText(dataSplit[0]);
        for (int i = 0; i < 24; i++) {
            timeCycle[i][0].setSignal(Double.parseDouble(dataSplit[1 + i * 4]));
            timeCycle[i][1].setSignal(Double.parseDouble(dataSplit[1 + i * 4 + 1]));
            timeCycle[i][2].setSignal(Double.parseDouble(dataSplit[1 + i * 4 + 2]));
            visualMults[i].setValue(dataSplit[1 + i * 4 + 3]);
        }
        for (int i = 0; i < 10; i++) {
            exploreMus[i].setValue(dataSplit[97 + i]);
            battleMus[i].setValue(dataSplit[107 + i]);
        }
    }
    void saveRegion(){
        String save = name.getText();
        for (int i = 0; i < timeCycle.length;i++) {
            for (Slider slider : timeCycle[i]) {
                slider.work();
                save += ";" + slider.getSignal();
            }
            save += ";" + visualMults[i].getText();
        }
        for (int i = 0; i < exploreMus.length;i++) {
            save += ";" + exploreMus[i].getText();
        }
        for (int i = 0; i < battleMus.length;i++) {
            save += ";" + battleMus[i].getText();
        }
        RegionDataBase.addData(name.getText(),save);
    }
}
