package screens.ingame;

import de.gurkenlabs.litiengine.input.Input;
import engine.Sets;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.*;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.game.Region;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import screens.Controls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static engine.render.Render2D.getWindowHeight;
import static engine.render.Render2D.getWindowWidth;
import static org.lwjgl.opengl.GL11.*;


public class SetsScreen {
    static boolean loaded = false;
    static double wide = 450;
    static double high = 600;
    private static final double borderSize = 4;
    private static final double leftBorderX = Render2D.getWindowWidth() / 2 - wide / 2;
    private static final double rightBorderX = Render2D.getWindowWidth() / 2 + wide / 2;
    private static final double topbotBorderX = Render2D.getWindowWidth() / 2;
    private static final double locBotBorderY = Render2D.getWindowHeight() / 2 + high / 2;
    private static final double locTopBorderY = Render2D.getWindowHeight() /2 - high / 2;
    private static final double topbotBorderY = Render2D.getWindowHeight() / 2;
    private static final double textStartX = leftBorderX + 10;
    private static final double textStartY = locTopBorderY + 5;
    private static final double textPerY = 25;
    static double wideElements = 200;
    static double highElements = 25;
    static double locXElements = rightBorderX-wideElements/2;
    static double locStartYElements = locTopBorderY+highElements/2;
    static double wideElementsSmall = 25;
    static double locXElementsSmall = rightBorderX-wideElementsSmall/2;

    static double sizeXElementsReverse = wide-4;
    static double locXElementsReverse = leftBorderX+sizeXElementsReverse/2+2;



    private static final Slider music = new Slider(locXElements,locStartYElements+highElements*2+10,wideElements,highElements,"interface/slider",1);
    private static final Slider interfaces = new Slider(locXElements,locStartYElements+highElements*3+10,wideElements,highElements,"interface/slider",1);
    private static final Slider sounds = new Slider(locXElements,locStartYElements+highElements*4+10,wideElements,highElements,"interface/slider",1);
    private static final Button toControls = new Button(locXElementsReverse,locStartYElements+highElements*7+10,sizeXElementsReverse,highElements*1.2,"interface/text","interface/Click");

    private static final Button toGenerals = new Button(locXElementsReverse,locStartYElements+highElements*21+10,sizeXElementsReverse,highElements*1.2,"interface/text","interface/Click");

    static double boxSize = 32;


    private static String[] textsBase = {"","Громкости","Музыки","Интерфейса","Звуков","","Управление","Переназначить клавиши"};
    private static Element[] elementsBase = {null,null,music,interfaces,sounds,null,null,toControls};


    private static int[] keysForPlayerGetSpell = {};
    private static int[] keysForPlayerGetItem = {};

    private static void saveKeysChanges() {
        Controls.chooseControlPlayer[0] = keysControls[2];
        Controls.chooseControlPlayer[1] = keysControls[3];
        Controls.chooseControlPlayer[2] = keysControls[4];
        Controls.chooseControlPlayer[3] = keysControls[5];
        Controls.chooseControlPlayer[4] = keysControls[6];

        Controls.keysForPlayerSpeedControl[0] = keysControls[9];
        Controls.keysForPlayerSpeedControl[1] = keysControls[19];
        Controls.keysForPlayerSpeedControl[2] = keysControls[11];
        Controls.keyForStopPlayer = keysControls[12];
        Controls.keysForPlayerAutoControl[0] = keysControls[15];
        Controls.keysForPlayerAutoControl[1] = keysControls[16];
        Controls.keysForPlayerAutoControl[2] = keysControls[17];
        Controls.keysForPlayerAutoControl[3] = keysControls[18];
        Controls.keysForPlayerTargetOn[0] = keysControls[21];
        Controls.keysForPlayerTargetOn[1] = keysControls[22];
        Controls.keysForPlayerTargetOn[2] = keysControls[23];
        Controls.keysForPlayerTargetOn[3] = keysControls[24];
        Controls.keysForPlayerGetSpell[0] = keysControls[27];
        Controls.keysForPlayerGetSpell[1] = keysControls[28];
        Controls.keysForPlayerGetSpell[2] = keysControls[29];
        Controls.keysForPlayerGetSpell[3] = keysControls[30];
        Controls.keysForPlayerGetSpell[4] = keysControls[31];
        Controls.keysForPlayerGetSpell[5] = keysControls[32];
        Controls.keysForPlayerGetSpell[6] = keysControls[33];
        Controls.keysForPlayerGetSpell[7] = keysControls[34];

        Controls.keysForCameraControl[0] = keysControls1[2];
        Controls.keysForCameraControl[1] = keysControls1[3];
        Controls.keysForCameraControl[2] = keysControls1[4];
        Controls.keysForCameraControl[3] = keysControls1[5];
        Controls.keyForLockCameraOnPlayer = keysControls1[6];
        Controls.keyForCameraShift = keysControls1[7];
        Controls.keysForGameSpeedControl[0] = keysControls1[10];
        Controls.keysForGameSpeedControl[1] = keysControls1[11];
        Controls.keysForGameSpeedControl[2] = keysControls1[12];
        Controls.keyForWeaponShift = keysControls1[15];
        Controls.keyForShieldShift = keysControls1[16];
        Controls.openCampMenu = keysControls1[19];
        Controls.chooseCampScreen[0] = keysControls1[20];
        Controls.chooseCampScreen[1] = keysControls1[21];
        Controls.chooseCampScreen[2] = keysControls1[22];
        Controls.chooseCampScreen[3] = keysControls1[23];
        Controls.chooseCampScreen[4] = keysControls1[24];

        Controls.keysForPlayerGetItem[0] = keysControls1[27];
        Controls.keysForPlayerGetItem[1] = keysControls1[28];
        Controls.keysForPlayerGetItem[2] = keysControls1[29];
        Controls.keysForPlayerGetItem[3] = keysControls1[30];
        Controls.keysForPlayerGetItem[4] = keysControls1[31];
        Controls.keysForPlayerGetItem[5] = keysControls1[32];
        Controls.keysForPlayerGetItem[6] = keysControls1[33];
        Controls.keysForPlayerGetItem[7] = keysControls1[34];
    }

    private static String[] textsControls = {"","Выбор персонажа","Персонаж 1","Персонаж 2","Персонаж 3","Персонаж 4","Персонаж 5","","Команды движения","Бежать","Идти","Красться","Остановиться","",
            "Команды поведения","Защитная стойка","Не атаковать","Атаковать в ответ","Автоатака","","Боевые команды","Бить в голову","Бить в тело","Бить по рукам","Бить по ногам","",
            "Быстрый доступ - заклинания","Заклинание 1","Заклинание 2","Заклинание 3","Заклинание 4","Заклинание 5","Заклинание 6","Заклинание 7","Заклинание 8"};
    private static String[] textsControls1 = {"","Управление камерой","Влево","Вверх","Вправо","Вниз","Центрировать на герое","Зажим камеры на мышь","","Скорость игры","Пауза","Нормальная скорость","Быстрая скорость","",
            "Команды смены","Поменять оружие","Поменять щит","","Меню","Открыть меню","Инвентарь","Навыки","Заклинания","Журнал","Переходы","",
            "Быстрый доступ - предметы","Предмет 1","Предмет 2","Предмет 3","Предмет 4","Предмет 5","Предмет 6","Предмет 7","Предмет 8"};
    private static int[] keysControls = {-2,-2,Keyboard.KEY_F1,Keyboard.KEY_F2,Keyboard.KEY_F3,Keyboard.KEY_F4,Keyboard.KEY_F5,-2,-2,Keyboard.KEY_Z,Keyboard.KEY_X,Keyboard.KEY_C, Keyboard.KEY_V,-2,
            -2,Keyboard.KEY_G,Keyboard.KEY_H,Keyboard.KEY_B,Keyboard.KEY_N,-2,-2,Keyboard.KEY_A,Keyboard.KEY_S,Keyboard.KEY_D,Keyboard.KEY_F,-2,
            -2,Keyboard.KEY_Q,Keyboard.KEY_W,Keyboard.KEY_E,Keyboard.KEY_R,Keyboard.KEY_T,Keyboard.KEY_Y,Keyboard.KEY_U,Keyboard.KEY_I};
    private static int[] keysControls1 = {-2,-2,Keyboard.KEY_LEFT,Keyboard.KEY_UP,Keyboard.KEY_RIGHT,Keyboard.KEY_DOWN,Keyboard.KEY_HOME,Keyboard.KEY_LMENU,-2,-2,Keyboard.KEY_SPACE,Keyboard.KEY_SUBTRACT,Keyboard.KEY_ADD,-2,
            -2,Keyboard.KEY_J,Keyboard.KEY_K,-2,-2,Keyboard.KEY_TAB,Keyboard.KEY_1,Keyboard.KEY_2,Keyboard.KEY_3,Keyboard.KEY_4,Keyboard.KEY_5,-2,
            -2,Keyboard.KEY_1,Keyboard.KEY_2,Keyboard.KEY_3,Keyboard.KEY_4,Keyboard.KEY_5,Keyboard.KEY_6,Keyboard.KEY_7,Keyboard.KEY_8};
    private static int underKey = -1, underKey1 = -1;
    static Button OK = new Button(rightBorderX-boxSize/2-boxSize, locBotBorderY-boxSize/2,boxSize,boxSize,"interface/butOK");
    static Button Cancel = new Button(rightBorderX-boxSize/2, locBotBorderY-boxSize/2,boxSize,boxSize,"interface/butCancel");
    private static boolean generalMode = true;

    public static void work(){
        Render2D.angleColorDraw("campBack","interface/white", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight(),0,0,0,0,0.75);
        Render2D.angleDraw("campBorder1", "interface/borderBack", topbotBorderX, locBotBorderY, wide, borderSize, 0);
        Render2D.angleDraw("campBorder2", "interface/borderBack", topbotBorderX, locTopBorderY, wide, borderSize, 0);
        Render2D.angleDraw("campBorder3", "interface/borderBack", rightBorderX, topbotBorderY, high, borderSize, 90);
        Render2D.angleDraw("campBorder4", "interface/borderBack", leftBorderX, topbotBorderY, high, borderSize, 90);
        Color color = Color.white;
        Color color1 = Color.white;

        if(generalMode) {
            for (int i = 0; i < textsBase.length; i++) {
                if (textsBase[i].equals("")) {
                    color = Color.white;
                } else {
                    Text.drawString(textsBase[i], textStartX, textStartY + i * textPerY, Text.CAMBRIA_24, color);
                    color = Color.lightGray;
                }
                if (elementsBase[i] != null) {
                    elementsBase[i].work();
                    elementsBase[i].draw();
                }
            }
            if (music.isReleased()) {
                float set = (float) music.getSignal();
                AudioManager.setMusicVolumeSet(set);
            }
            if (interfaces.isReleased()) {
                float set = (float) interfaces.getSignal();
                AudioManager.setInterfaceVolumeSet(set);
            }
            if (sounds.isReleased()) {
                float set = (float) sounds.getSignal();
                AudioManager.setSoundVolumeSet(set);
            }
            if (toControls.isReleased()) {
                generalMode = false;
            }
        } else {

            for (int i = 0; i < textsControls.length; i++) {
                if(underKey == i){
                    double lx = ((textStartX+wide/2-40) + textStartX)/2;
                    double ly = textStartY + i * 15+10;
                    double sx = ((textStartX+wide/2-40) - textStartX);
                    double sy = 15;
                    Render2D.angleColorDraw("interface/white",lx,ly,sx,sy,0,1,0,0,0.5);
                }
                if(underKey1 == i){
                    double lx = ((textStartX+wide/2-40) + (textStartX+wide))/2;
                    double ly = textStartY + i * 15+10;
                    double sx = (-(textStartX+wide/2) + (textStartX+wide));
                    double sy = 15;
                    Render2D.angleColorDraw("interface/white",lx,ly,sx,sy,0,1,0,0,0.5);
                }
                if (textsControls[i].equals("")) {
                    color = Color.white;
                } else {
                    Text.drawString(textsControls[i], textStartX, textStartY + i * 15, Text.CAMBRIA_14, color);
                    color = Color.lightGray;
                }
                if (textsControls1[i].equals("")) {
                    color1 = Color.white;
                } else {
                    Text.drawString(textsControls1[i], textStartX+wide/2-20, textStartY + i * 15, Text.CAMBRIA_14, color1);
                    color1 = Color.lightGray;
                }
                if(keysControls[i] != -2){
                    Text.drawStringRigth(Keyboard.getKeyName(keysControls[i]), textStartX+wide/2-40, textStartY + i * 15, Text.CAMBRIA_14, color);
                }
                if(keysControls1[i] != -2){
                    Text.drawStringRigth(Keyboard.getKeyName(keysControls1[i]), textStartX+wide-20, textStartY + i * 15, Text.CAMBRIA_14, color1);
                }
                if(InputMain.getCursorX() < textStartX+wide/2-40 && keysControls[i] != -2){
                    if(InputMain.getCursorX()  > textStartX){
                        if(Math.abs(InputMain.getCursorY()-(textStartY + i * 15+10)) < 7){
                            double lx = ((textStartX+wide/2-40) + textStartX)/2;
                            double ly = textStartY + i * 15+10;
                            double sx = ((textStartX+wide/2-40) - textStartX);
                            double sy = 15;
                            Render2D.angleColorDraw("interface/white",lx,ly,sx,sy,0,1,1,1,0.25);
                            if(InputMain.isKeyJustReleased(InputMain.LMB)){
                                underKey = i;
                                underKey1 = -1;
                            }
                        }
                    }
                } else if(InputMain.getCursorX() > textStartX+wide/2-40 && keysControls1[i] != -2){
                    if(InputMain.getCursorX()  < textStartX+wide) {
                        if (Math.abs(InputMain.getCursorY() - (textStartY + i * 15+10)) < 7) {
                            double lx = ((textStartX+wide/2-40) + (textStartX+wide))/2;
                            double ly = textStartY + i * 15+10;
                            double sx = (-(textStartX+wide/2) + (textStartX+wide));
                            double sy = 15;
                            Render2D.angleColorDraw("interface/white",lx,ly,sx,sy,0,1,1,1,0.25);
                            if(InputMain.isKeyJustReleased(InputMain.LMB)){
                                underKey1 = i;
                                underKey = -1;
                            }
                        }
                    }
                }

            }
            toGenerals.work(); toGenerals.draw(); if(toGenerals.isReleased()) {generalMode = true;}
            Text.drawString("Назад", textStartX, textStartY + 21 * textPerY, Text.CAMBRIA_24, color);
            if(underKey != -1){
                if(InputMain.getLastPressedKey() != -1){
                    keysControls[underKey] = InputMain.getLastPressedKey();
                    underKey = -1;
                    saveKeysChanges();
                }
            }
            if(underKey1 != -1){
                if(InputMain.getLastPressedKey() != -1){
                    keysControls1[underKey1] = InputMain.getLastPressedKey();
                    underKey1 = -1;
                    saveKeysChanges();
                }
            }
        }
        OK.work();OK.draw(); if(OK.isReleased()) {generateSetsFile(); MainMenu.setsShow = false;}
        Cancel.work();Cancel.draw(); if(Cancel.isReleased()) { loadSetsFile(); MainMenu.setsShow = false;}
        if(InputMain.isKeyJustReleased(Controls.openESCMenu)){
            MainMenu.setsShow = false;
        }
        Render2D.simpleDraw("cursor","cursors/normal", InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }

    public static void generateSetsFile(){
        File file2 = new File("res/dat/sets.ini");
        try {
            file2.delete();
            if (file2.createNewFile()) {
                FileWriter fstream1 = new FileWriter(file2.getAbsoluteFile());// конструктор с одним параметром - для перезаписи
                BufferedWriter out1 = new BufferedWriter(fstream1); //  создаём буферезированный поток
                out1.write(""); // очищаем, перезаписав поверх пустую строку
                Files.write(Paths.get(file2.toURI()), (music.getSignal() + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(file2.toURI()), (interfaces.getSignal() + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(file2.toURI()), (sounds.getSignal() + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                for(Integer el : keysControls){
                    if(el >= 0){
                        Files.write(Paths.get(file2.toURI()), (el + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                    }
                }
                for(Integer el : keysControls1){
                    if(el >= 0){
                        Files.write(Paths.get(file2.toURI()), (el + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                    }
                }
                out1.close(); // закрываем
            }
        } catch (Exception e) {
            System.err.println("Error in file cleaning: " + e.getMessage());
        }
    }
    public static void loadSetsFile(){
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("res/dat/sets.ini"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return;
        }
        music.setSignal(Double.parseDouble(lines.get(0)));
        interfaces.setSignal(Double.parseDouble(lines.get(1)));
        sounds.setSignal(Double.parseDouble(lines.get(2)));
        AudioManager.setMusicVolumeSet((float) music.getSignal());
        AudioManager.setInterfaceVolumeSet((float) interfaces.getSignal());
        AudioManager.setSoundVolumeSet((float) sounds.getSignal());
        int row = 3;
        for(int i = 0; i < keysControls.length;){
            if(keysControls[i] >= 0){
                keysControls[i] = Integer.parseInt(lines.get(row));
                row++;
            }
            i++;
        }
        for(int i = 0; i < keysControls1.length;){
            if(keysControls1[i] >= 0){
                keysControls1[i] = Integer.parseInt(lines.get(row));
                row++;
            }
            i++;
        }
        saveKeysChanges();
    }
}
