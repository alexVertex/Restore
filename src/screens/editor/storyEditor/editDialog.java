package screens.editor.storyEditor;

import engine.Screen;
import engine.Start;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Textbox;
import engine.render.Render2D;
import engine.render.Text;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class editDialog extends Screen {

    public static Button addItem = new Button(20,20,32,32,"interface/butPlus");



    @Override
    public void input() {

        addItem.work();
        if(addItem.isReleased()){
            saveDialog();
        }
        for(int i = 0; i < textsReasons.length;i++){
            if(textsReasons[i].getText().length() == 0){
                textsReasons[i].setText("пусто");
            }
            if(textsNexts[i].getText().length() == 0){
                textsNexts[i].setText("пусто");
            }
            if(textsNextsRes[i].getText().length() == 0){
                textsNextsRes[i].setText("пусто");
            }
        }
        if(textReplioa.getText().length() == 0){
            textReplioa.setText("пусто");
        }
    }
    private String[] fileNames;
    @Override
    public void logic() {
        File folder = new File("res/dat/dialogs");
        File[] folderEntries = folder.listFiles();
        fileNames = new String[folderEntries.length];
        for (int i = 0; i < folderEntries.length; i++) {
            String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
            fileNames[i] = name;
        }

    }
    private static Textbox textID = new Textbox(600,60,256,30,"interface/text","ИД диалога",Text.CAMBRIA_14,4);
    private static Textbox textName = new Textbox(600,60+30,256,30,"interface/text","Название диалога",Text.CAMBRIA_14,4);
    private static Textbox textSpeaker = new Textbox(600,60+60,256,30,"interface/text","Название диалога",Text.CAMBRIA_14,4);

    private static Textbox textReplioa = new Textbox(1300,300,500,200,"interface/textLarge","Название диалога",Text.CAMBRIA_14,8,true);
    Droplist reason = new Droplist(1300+250-128,300-100-15,256,30,"interface/drop",0,new String[]{"Герой","Собеседник"},Text.CAMBRIA_14,4);
    public static Button addReplica = new Button(1300-250+16,300-100-7,30,30,"interface/butPlus");
    public static Button delReplica = new Button(1300-250+16+32,300-100-7,30,30,"interface/butCancel");
    public static Button nextReplica = new Button(1300-250+16+32*3,300-100-7,30,30,"interface/right");
    public static Button prevReplica = new Button(1300-250+16+32*2,300-100-7,30,30,"interface/left");


    private static Droplist[] dropsReasons = new Droplist[8];
    private static Droplist[] dropsNexts = new Droplist[8];
    private static Textbox[] textsNexts = new Textbox[8];
    private static Textbox[] textsNextsRes = new Textbox[8];

    private static Textbox[] textsReasons = new Textbox[8];

    private static final String[] reasons = {"Пусто","Задание не получено","Задание выполнено","Золото на руках","Диалог был"};
    private static final String[] nexts = {"Пусто","Начать задание","Передать предмет","Забрать предмет","Передать заклинание","Передать золото","Забрать золото"};

    static {
        for(int i = 0; i < dropsReasons.length;i++){
            Droplist reason = new Droplist(600,60+120+i*30,256,30,"interface/drop",0,reasons,Text.CAMBRIA_14,4);
            dropsReasons[i] = reason;
            reason = new Droplist(600,60+390+i*30,256,30,"interface/drop",0,nexts,Text.CAMBRIA_14,4);
            dropsNexts[i] = reason;
            Textbox textbox = new Textbox(600+256,60+390+i*30,256,30,"interface/text","",Text.CAMBRIA_14,4);
            textsNexts[i] = textbox;
            textbox = new Textbox(600+256,60+120+i*30,256,30,"interface/text","",Text.CAMBRIA_14,4);
            textsReasons[i] = textbox;
            textbox = new Textbox(600+256+32+128,60+390+i*30,64,30,"interface/text","",Text.CAMBRIA_14,4);
            textsNextsRes[i] = textbox;
        }
    }

    @Override
    public void render() {
        Render2D.clearScreen();
        textID.work(); textName.work(); textSpeaker.work(); textReplioa.work(); reason.work(); addReplica.work(); delReplica.work(); nextReplica.work(); prevReplica.work();
        textID.draw(); textName.draw(); textSpeaker.draw(); textReplioa.draw(); reason.draw(); addReplica.draw(); delReplica.draw(); nextReplica.draw(); prevReplica.draw();
        Text.drawString("ИД", 300, 54,Text.CAMBRIA_24,Color.white);
        Text.drawString("Название", 300, 54+30,Text.CAMBRIA_24,Color.white);
        Text.drawString("Собеседник", 300, 54+60,Text.CAMBRIA_24,Color.white);
        Text.drawString("Реплика: " + (curReplica+1)+"/"+replics.size(), 1300-250+32*4+5,300-106-7,Text.CAMBRIA_14,Color.white);

        for(int i = 0; i < 8;i++){
            Text.drawString("Условие "+(i+1), 300, 54+120+i*30,Text.CAMBRIA_24,Color.white);
            Text.drawString("Последствие "+(i+1), 300, 54+90+300+i*30,Text.CAMBRIA_24,Color.white);
            dropsReasons[i].work(); dropsNexts[i].work(); textsNexts[i].work(); textsReasons[i].work(); textsNextsRes[i].work();
            dropsReasons[i].draw(); dropsNexts[i].draw(); textsNexts[i].draw(); textsReasons[i].draw(); textsNextsRes[i].draw();
        }

        addItem.draw();
        for(int i = 0; i < fileNames.length;i++){
            if(InputMain.getCursorX() < 256 && InputMain.getCursorY() > 40+i*20-8+10  && InputMain.getCursorY() < 40+i*20+8+10){
                Render2D.angleColorDraw("chooser","interface/white",128,40+i*20+10,256,16,0,1,0,0,0.5);
                if(InputMain.isKeyJustReleased(InputMain.LMB)){
                    openDialog(fileNames[i]);
                }
            }
            Text.drawString(fileNames[i],2,40+i*20,Text.CAMBRIA_14, Color.white);
        }
        Render2D.simpleDraw("cursor","cursors/normal", InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);

        if(InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE) || InputMain.isKeyJustReleased(Keyboard.KEY_LBRACKET)){
            Start.setScreen(new editorScreen());
            return;
        }
        if(addReplica.isReleased()){
            createReplica();
        }
        if(delReplica.isReleased()){
            deleteReplica();
        }
        if(nextReplica.isReleased()){
            nextReplica();
        }
        if(prevReplica.isReleased()){
            prevReplica();
        }
        if(curReplica > -1){
            replics.set(curReplica,textReplioa.getText());
            names.set(curReplica,reason.getVal()+"");
        }
    }
    int curReplica = -1;
    List<String> replics = new ArrayList<>();
    List<String> names = new ArrayList<>();

    private void saveDialog() {
        File file2 = new File("res/dat/dialogs/", textID.getText() + ".dia");
        try {
            file2.delete();
            if (file2.createNewFile()) {
                FileWriter fstream1 = new FileWriter(file2.getAbsoluteFile());// конструктор с одним параметром - для перезаписи
                BufferedWriter out1 = new BufferedWriter(fstream1); //  создаём буферезированный поток
                out1.write(""); // очищаем, перезаписав поверх пустую строку
                out1.close(); // закрываем
            }
        } catch (Exception e) {
            System.err.println("Error in file cleaning: " + e.getMessage());
        }
        List<String> saveData = new ArrayList<>();

        saveData.add(textID.getText()+";"+textName.getText()+";"+textSpeaker.getText());
        for(int i = 0; i < dropsReasons.length;i++){
            saveData.add(dropsReasons[i].getVal()+":"+textsReasons[i].getText());
        }
        for(int i = 0; i < dropsNexts.length;i++){
            saveData.add(dropsNexts[i].getVal()+":"+textsNexts[i].getText()+":"+textsNextsRes[i].getText());
        }
        for(int i = 0; i < replics.size();i++){
            saveData.add(names.get(i)+":"+replics.get(i));
        }
        try {
            for (String el : saveData) {
                Files.write(Paths.get(file2.toURI()), (el + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDialog(String text) {
        replics.clear();
        names.clear();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("res/dat/dialogs/"+text+".dia"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert lines != null;
        String[] mainData = lines.get(0).split(";");
        textID.setText(mainData[0]);
        textName.setText(mainData[1]);
        textSpeaker.setText(mainData[2]);

        for(int i = 0; i < dropsReasons.length;i++){
            mainData = lines.get(1+i).split(":");
            dropsReasons[i].setPos(Integer.parseInt(mainData[0]));
            textsReasons[i].setText((mainData[1]));
        }

        for(int i = 0; i < dropsNexts.length;i++){
            mainData = lines.get(9+i).split(":");
            dropsNexts[i].setPos(Integer.parseInt(mainData[0]));
            textsNexts[i].setText((mainData[1]));
            textsNextsRes[i].setText((mainData[2]));
        }

        for(int i = 17; i < lines.size();i++){
            mainData = lines.get(i).split(":");
            names.add(mainData[0]);
            replics.add(mainData[1]);
        }
    }

    private void prevReplica() {
        if(curReplica > 0) {
            curReplica--;
            textReplioa.setText(replics.get(curReplica));
            reason.setPos(Integer.parseInt(names.get(curReplica)));
        }
    }

    private void nextReplica() {
        if(curReplica < replics.size()-1) {
            curReplica++;
            textReplioa.setText(replics.get(curReplica));
            reason.setPos(Integer.parseInt(names.get(curReplica)));
        }
    }

    private void createReplica() {
        replics.add(curReplica+1,"Слова");
        names.add(curReplica+1,"0");
        textReplioa.setText("Слова");
        reason.setPos(0);
        curReplica++;
    }
    private void deleteReplica() {
        if(curReplica > -1) {
            replics.remove(curReplica);
            names.remove(curReplica);
            curReplica--;
            if(curReplica != -1) {
                textReplioa.setText(replics.get(curReplica));
                reason.setPos(Integer.parseInt(names.get(curReplica)));
            } else {
                textReplioa.setText("Слова");
                reason.setPos(0);
            }
        }
    }
}
